package server

import (
	"context"
	"database/sql"
	"fmt"
	"io/ioutil"  // Add this
	"log"
	"os"         // Add this
	"path/filepath"
	"strings"
	"time"

	pb "upload-services/proto"

	"github.com/google/uuid"
)

type UploadServer struct {
	pb.UnimplementedUploadServiceServer
	db       *Database
	s3Client *S3Client
}

func NewUploadServer() *UploadServer {
	s3Client, err := NewS3Client()
	if err != nil {
		log.Fatalf("Failed to initialize S3 client: %v", err)
	}

	return &UploadServer{
		db:       NewDatabase(),
		s3Client: s3Client,
	}
}

func (s *UploadServer) UploadFile(ctx context.Context, req *pb.UploadFileRequest) (*pb.UploadFileResponse, error) {
	if req.UserId == "" {
		return &pb.UploadFileResponse{
			StatusCode: 400,
			Message:    "User ID is required",
			Result: &pb.UploadFileResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      400,
					Message:   "User ID is required",
					Details:   []string{"User ID cannot be empty"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}

	if len(req.FileData) == 0 {
		return &pb.UploadFileResponse{
			StatusCode: 400,
			Message:    "File data is required",
			Result: &pb.UploadFileResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      400,
					Message:   "File data is required",
					Details:   []string{"File data cannot be empty"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}
	if !isValidFileType(req.ContentType, req.FileType) {
		return &pb.UploadFileResponse{
			StatusCode: 400,
			Message:    "Invalid file type",
			Result: &pb.UploadFileResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      400,
					Message:   "Invalid file type",
					Details:   []string{"Only images and videos are allowed"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}
	fileID := uuid.New().String()
	fileExt := filepath.Ext(req.FileName)
	fileKey := fmt.Sprintf("%s/%s/%s%s", req.UserId, getFileTypeString(req.FileType), fileID, fileExt)
	
	var fileURL string
	var storageLocation pb.StorageLocation
	var err error

	// Handle storage mode
	if req.StorageMode == pb.StorageMode_DEVELOPMENT {
		// Development mode - use local storage
		fileURL, err = s.saveToLocalStorage(req.FileData, fileKey, req.ContentType)
		storageLocation = pb.StorageLocation_LOCAL_FOLDER
	} else {
		// Production mode - use S3
		fileURL, err = s.s3Client.UploadFile(req.FileData, fileKey, req.ContentType)
		storageLocation = pb.StorageLocation_AWS_S3
	}

	if err != nil {
		log.Printf("Failed to upload file: %v", err)
		return &pb.UploadFileResponse{
			StatusCode: 500,
			Message:    "Failed to upload file",
			Result: &pb.UploadFileResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      500,
					Message:   "Internal server error",
					Details:   []string{"Failed to upload file to storage"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}
	query := `
		INSERT INTO uploads (id, user_id, file_key, file_name, file_type, content_type, file_size, file_url, uploaded_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`
	uploadedAt := time.Now()
	_, err = s.db.db.Exec(query, fileID, req.UserId, fileKey, req.FileName,
		getFileTypeString(req.FileType), req.ContentType, len(req.FileData), fileURL, uploadedAt)
	if err != nil {
		s.s3Client.DeleteFile(fileKey)
		return &pb.UploadFileResponse{
			StatusCode: 500,
			Message:    "Failed to save upload record",
			Result: &pb.UploadFileResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      500,
					Message:   "Internal server error",
					Details:   []string{"Failed to save upload record"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}

	return &pb.UploadFileResponse{
		StatusCode: 200,
		Message:    "File uploaded successfully",
		Result: &pb.UploadFileResponse_Success{
			Success: &pb.UploadSuccess{
				FileUrl:    fileURL,
				FileKey:    fileKey,
				FileId:     fileID,
				FileSize:   int64(len(req.FileData)),
				UploadedAt: uploadedAt.Format(time.RFC3339),
				FullUrl:    fileURL,
				Location:   storageLocation,
			},
		},
	}, nil
}

func (s *UploadServer) DeleteFile(ctx context.Context, req *pb.DeleteFileRequest) (*pb.DeleteFileResponse, error) {
	if req.UserId == "" || req.FileKey == "" {
		return &pb.DeleteFileResponse{
			StatusCode: 400,
			Message:    "User ID and file key are required",
			Result: &pb.DeleteFileResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      400,
					Message:   "User ID and file key are required",
					Details:   []string{"Both user_id and file_key must be provided"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}
	var fileExists bool
	var deletedAt sql.NullTime
	query := `SELECT EXISTS(SELECT 1 FROM uploads WHERE file_key = $1 AND user_id = $2), deleted_at FROM uploads WHERE file_key = $1 AND user_id = $2`
	err := s.db.db.QueryRow(query, req.FileKey, req.UserId).Scan(&fileExists, &deletedAt)
	if err != nil && err != sql.ErrNoRows {
		log.Printf("Database error: %v", err)
		return &pb.DeleteFileResponse{
			StatusCode: 500,
			Message:    "Internal server error",
			Result: &pb.DeleteFileResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      500,
					Message:   "Database error",
					Details:   []string{"Failed to check file existence"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}
	if !fileExists {
		return &pb.DeleteFileResponse{
			StatusCode: 404,
			Message:    "File not found",
			Result: &pb.DeleteFileResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      404,
					Message:   "File not found",
					Details:   []string{"File does not exist or does not belong to user"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}
	if deletedAt.Valid {
		return &pb.DeleteFileResponse{
			StatusCode: 400,
			Message:    "File already deleted",
			Result: &pb.DeleteFileResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      400,
					Message:   "File already deleted",
					Details:   []string{"This file has already been deleted"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}
	err = s.s3Client.DeleteFile(req.FileKey)
	if err != nil {
		log.Printf("Failed to delete file from S3: %v", err)
		return &pb.DeleteFileResponse{
			StatusCode: 500,
			Message:    "Failed to delete file from storage",
			Result: &pb.DeleteFileResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      500,
					Message:   "Internal server error",
					Details:   []string{"Failed to delete file from storage"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}
	deletedTime := time.Now()
	updateQuery := `UPDATE uploads SET deleted_at = $1 WHERE file_key = $2 AND user_id = $3`
	_, err = s.db.db.Exec(updateQuery, deletedTime, req.FileKey, req.UserId)

	if err != nil {
		log.Printf("Failed to mark file as deleted in database: %v", err)
		return &pb.DeleteFileResponse{
			StatusCode: 500,
			Message:    "Failed to update delete record",
			Result: &pb.DeleteFileResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      500,
					Message:   "Internal server error",
					Details:   []string{"Failed to update delete record"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}

	return &pb.DeleteFileResponse{
		StatusCode: 200,
		Message:    "File deleted successfully",
		Result: &pb.DeleteFileResponse_Success{
			Success: &pb.DeleteSuccess{
				FileKey:   req.FileKey,
				DeletedAt: deletedTime.Format(time.RFC3339),
			},
		},
	}, nil
}

func (s *UploadServer) GetFileURL(ctx context.Context, req *pb.GetFileURLRequest) (*pb.GetFileURLResponse, error) {
	if req.FileKey == "" {
		return &pb.GetFileURLResponse{
			StatusCode: 400,
			Message:    "File key is required",
			Result: &pb.GetFileURLResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      400,
					Message:   "File key is required",
					Details:   []string{"File key cannot be empty"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}

	expiresIn := time.Duration(req.ExpiresIn) * time.Second
	if expiresIn <= 0 {
		expiresIn = 1 * time.Hour
	}

	signedURL, err := s.s3Client.GeneratePresignedURL(req.FileKey, expiresIn)
	if err != nil {
		log.Printf("Failed to generate presigned URL: %v", err)
		return &pb.GetFileURLResponse{
			StatusCode: 500,
			Message:    "Failed to generate file URL",
			Result: &pb.GetFileURLResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      500,
					Message:   "Internal server error",
					Details:   []string{"Failed to generate signed URL"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}

	expiresAt := time.Now().Add(expiresIn)

	return &pb.GetFileURLResponse{
		StatusCode: 200,
		Message:    "File URL generated successfully",
		Result: &pb.GetFileURLResponse_Success{
			Success: &pb.URLSuccess{
				SignedUrl: signedURL,
				ExpiresAt: expiresAt.Format(time.RFC3339),
			},
		},
	}, nil
}

func isValidFileType(contentType string, fileType pb.FileType) bool {
	switch fileType {
	case pb.FileType_IMAGE:
		return strings.HasPrefix(contentType, "image/")
	case pb.FileType_VIDEO:
		return strings.HasPrefix(contentType, "video/")
	default:
		return false
	}
}

func getFileTypeString(fileType pb.FileType) string {
	switch fileType {
	case pb.FileType_IMAGE:
		return "images"
	case pb.FileType_VIDEO:
		return "videos"
	case pb.FileType_DOCUMENT:
		return "documents"
	case pb.FileType_PROFILE_PHOTO:
		return "profile_photo"
	default:
		return "unknown"
	}
}

// GetUserPhotoURL retrieves the user's profile photo URL
func (s *UploadServer) GetUserPhotoURL(ctx context.Context, req *pb.GetUserPhotoURLRequest) (*pb.GetUserPhotoURLResponse, error) {
	if req.UserId == "" {
		return &pb.GetUserPhotoURLResponse{
			StatusCode: 400,
			Message:    "User ID is required",
			Result: &pb.GetUserPhotoURLResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      400,
					Message:   "User ID is required",
					Details:   []string{"User ID cannot be empty"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}

	// Query for user's profile photo
	query := `
		SELECT file_url, file_key, uploaded_at 
		FROM uploads 
		WHERE user_id = $1 AND file_type = 'profile_photo' AND deleted_at IS NULL 
		ORDER BY uploaded_at DESC 
		LIMIT 1
	`

	var fileURL, fileKey, uploadedAt string
	err := s.db.db.QueryRow(query, req.UserId).Scan(&fileURL, &fileKey, &uploadedAt)
	if err != nil {
		if err == sql.ErrNoRows {
			return &pb.GetUserPhotoURLResponse{
				StatusCode: 404,
				Message:    "No profile photo found",
				Result: &pb.GetUserPhotoURLResponse_Error{
					Error: &pb.ErrorDetails{
						Code:      404,
						Message:   "No profile photo found",
						Details:   []string{"User has no profile photo uploaded"},
						Timestamp: time.Now().Format(time.RFC3339),
					},
				},
			}, nil
		}
		log.Printf("Database error: %v", err)
		return &pb.GetUserPhotoURLResponse{
			StatusCode: 500,
			Message:    "Internal server error",
			Result: &pb.GetUserPhotoURLResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      500,
					Message:   "Database error",
					Details:   []string{"Failed to retrieve user photo"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}

	return &pb.GetUserPhotoURLResponse{
		StatusCode: 200,
		Message:    "User photo retrieved successfully",
		Result: &pb.GetUserPhotoURLResponse_Success{
			Success: &pb.GetUserPhotoURLResponse_Success{
				Success: &pb.UserPhotoSuccess{
					PhotoUrl:  fileURL,
					FileKey:   fileKey,
					UploadedAt: uploadedAt,
					FullUrl:   fileURL, // Same as photo_url for now
				},
			},
		},
	}, nil
}

// UpdateUserPhoto updates or uploads a new profile photo for the user
func (s *UploadServer) UpdateUserPhoto(ctx context.Context, req *pb.UpdateUserPhotoRequest) (*pb.UpdateUserPhotoResponse, error) {
	if req.UserId == "" {
		return &pb.UpdateUserPhotoResponse{
			StatusCode: 400,
			Message:    "User ID is required",
			Result: &pb.UpdateUserPhotoResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      400,
					Message:   "User ID is required",
					Details:   []string{"User ID cannot be empty"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}

	if len(req.FileData) == 0 {
		return &pb.UpdateUserPhotoResponse{
			StatusCode: 400,
			Message:    "File data is required",
			Result: &pb.UpdateUserPhotoResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      400,
					Message:   "File data is required",
					Details:   []string{"File data cannot be empty"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}

	// Validate image type
	if !isValidFileType(req.ContentType, pb.FileType_IMAGE) {
		return &pb.UpdateUserPhotoResponse{
			StatusCode: 400,
			Message:    "Invalid file type",
			Result: &pb.UpdateUserPhotoResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      400,
					Message:   "Invalid file type",
					Details:   []string{"Only image files are allowed for profile photos"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}

	// Mark existing profile photo as deleted
	updateQuery := `UPDATE uploads SET deleted_at = $1 WHERE user_id = $2 AND file_type = 'profile_photo' AND deleted_at IS NULL`
	_, err := s.db.db.Exec(updateQuery, time.Now(), req.UserId)
	if err != nil {
		log.Printf("Failed to mark old profile photo as deleted: %v", err)
	}

	// Upload new profile photo
	fileID := uuid.New().String()
	fileExt := filepath.Ext(req.FileName)
	fileKey := fmt.Sprintf("%s/profile_photo/%s%s", req.UserId, fileID, fileExt)

	var fileURL string
	var storageLocation pb.StorageLocation

	// Handle storage mode
	if req.StorageMode == pb.StorageMode_DEVELOPMENT {
		// Development mode - use local storage
		fileURL, err = s.saveToLocalStorage(req.FileData, fileKey, req.ContentType)
		storageLocation = pb.StorageLocation_LOCAL_FOLDER
	} else {
		// Production mode - use S3
		fileURL, err = s.s3Client.UploadFile(req.FileData, fileKey, req.ContentType)
		storageLocation = pb.StorageLocation_AWS_S3
	}

	if err != nil {
		log.Printf("Failed to upload profile photo: %v", err)
		return &pb.UpdateUserPhotoResponse{
			StatusCode: 500,
			Message:    "Failed to upload profile photo",
			Result: &pb.UpdateUserPhotoResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      500,
					Message:   "Internal server error",
					Details:   []string{"Failed to upload file to storage"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}

	// Save to database
	insertQuery := `
		INSERT INTO uploads (id, user_id, file_key, file_name, file_type, content_type, file_size, file_url, uploaded_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`
	uploadedAt := time.Now()
	_, err = s.db.db.Exec(insertQuery, fileID, req.UserId, fileKey, req.FileName,
		"profile_photo", req.ContentType, len(req.FileData), fileURL, uploadedAt)

	if err != nil {
		// Cleanup uploaded file on database error
		if req.StorageMode == pb.StorageMode_DEVELOPMENT {
			s.deleteFromLocalStorage(fileKey)
		} else {
			s.s3Client.DeleteFile(fileKey)
		}
		return &pb.UpdateUserPhotoResponse{
			StatusCode: 500,
			Message:    "Failed to save upload record",
			Result: &pb.UpdateUserPhotoResponse_Error{
				Error: &pb.ErrorDetails{
					Code:      500,
					Message:   "Internal server error",
					Details:   []string{"Failed to save upload record"},
					Timestamp: time.Now().Format(time.RFC3339),
				},
			},
		}, nil
	}

	return &pb.UpdateUserPhotoResponse{
		StatusCode: 200,
		Message:    "Profile photo updated successfully",
		Result: &pb.UpdateUserPhotoResponse_Success{
			Success: &pb.UploadSuccess{
				FileUrl:    fileURL,
				FileKey:    fileKey,
				FileId:     fileID,
				FileSize:   int64(len(req.FileData)),
				UploadedAt: uploadedAt.Format(time.RFC3339),
				FullUrl:    fileURL,
				Location:   storageLocation,
			},
		},
	}, nil
}

// saveToLocalStorage saves file to local development storage
func (s *UploadServer) saveToLocalStorage(fileData []byte, fileKey, contentType string) (string, error) {
	// Create uploads directory if it doesn't exist
	uploadDir := "./uploads"
	if err := os.MkdirAll(uploadDir, 0755); err != nil {
		return "", fmt.Errorf("failed to create upload directory: %v", err)
	}

	// Create subdirectories based on file key
	filePath := filepath.Join(uploadDir, fileKey)
	dir := filepath.Dir(filePath)
	if err := os.MkdirAll(dir, 0755); err != nil {
		return "", fmt.Errorf("failed to create file directory: %v", err)
	}

	// Write file
	if err := ioutil.WriteFile(filePath, fileData, 0644); err != nil {
		return "", fmt.Errorf("failed to write file: %v", err)
	}

	// Return local URL
	baseURL := os.Getenv("DEV_BASE_URL")
	if baseURL == "" {
		baseURL = "http://localhost:8080"
	}
	return fmt.Sprintf("%s/uploads/%s", baseURL, fileKey), nil
}

// deleteFromLocalStorage removes file from local development storage
func (s *UploadServer) deleteFromLocalStorage(fileKey string) error {
	filePath := filepath.Join("./uploads", fileKey)
	return os.Remove(filePath)
}
