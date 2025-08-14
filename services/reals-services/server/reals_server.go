package server

import (
	"context"
	"fmt"
	"log"
	"sync"
	"time"

	pb "reals-services/proto"

	"github.com/google/uuid"
)

type RealsServer struct {
	pb.UnimplementedRealsServiceServer
	db          *Database
	redis       *RedisClient
	grpcClients *GRPCClients
	mu          sync.RWMutex
}

func NewRealsServer() *RealsServer {
	server := &RealsServer{
		db:          NewDatabase(),
		redis:       NewRedisClient(),
		grpcClients: NewGRPCClients(),
	}

	// Start background cleanup job
	go server.startCleanupJob()

	return server
}

// CreateReal handles real creation with media upload
func (s *RealsServer) CreateReal(ctx context.Context, req *pb.CreateRealRequest) (*pb.CreateRealResponse, error) {
	if req.UserId == "" {
		return s.errorResponse(400, "User ID is required"), nil
	}

	if len(req.MediaItems) == 0 {
		return s.errorResponse(400, "At least one media item is required"), nil
	}

	if len(req.MediaItems) > 10 {
		return s.errorResponse(400, "Maximum 10 media items allowed"), nil
	}

	// Upload media files via gRPC
	var mediaURLs []string
	var mediaTypes []string

	for _, item := range req.MediaItems {
		// Convert media type
		var fileTypeStr string
		switch item.MediaType {
		case pb.MediaType_IMAGE:
			fileTypeStr = "IMAGE"
			mediaTypes = append(mediaTypes, "IMAGE")
		case pb.MediaType_VIDEO:
			fileTypeStr = "VIDEO"
			mediaTypes = append(mediaTypes, "VIDEO")
		default:
			return s.errorResponse(400, "Invalid media type"), nil
		}

		// Upload file via gRPC
		fileURL, err := s.grpcClients.UploadMedia(ctx, req.UserId, item.FileData, item.FileName, item.ContentType, fileTypeStr)
		if err != nil {
			log.Printf("Failed to upload media: %v", err)
			return s.errorResponse(500, "Failed to upload media"), nil
		}

		mediaURLs = append(mediaURLs, fileURL)
	}

	// Create real record
	realID := uuid.New().String()
	expiresAt := time.Now().Add(24 * time.Hour)

	real := &Real{
		ID:         realID,
		UserID:     req.UserId,
		MediaURLs:  mediaURLs,
		MediaTypes: mediaTypes,
		ExpiresAt:  expiresAt,
		CreatedAt:  time.Now(),
	}

	if err := s.db.CreateReal(real); err != nil {
		log.Printf("Failed to create real: %v", err)
		return s.errorResponse(500, "Failed to create real"), nil
	}

	// Invalidate cache
	s.redis.InvalidateCache("reals:*")

	return &pb.CreateRealResponse{
		StatusCode: 200,
		Message:    "Real created successfully",
		Result: &pb.CreateRealResponse_Success{
			Success: &pb.RealSuccess{
				RealId:    realID,
				MediaUrls: mediaURLs,
				ExpiresAt: expiresAt.Format(time.RFC3339),
			},
		},
	}, nil
}

// GetAllActiveReals implements two-thread fetching strategy
func (s *RealsServer) GetAllActiveReals(ctx context.Context, req *pb.GetAllActiveRealsRequest) (*pb.GetAllActiveRealsResponse, error) {
	limit := int(req.Limit)
	offset := int(req.Offset)

	if limit <= 0 {
		limit = 10
	}
	if limit > 50 {
		limit = 50
	}

	switch req.Strategy {
	case pb.FetchStrategy_FAST_FETCH:
		return s.fastFetch(ctx, limit, offset)
	case pb.FetchStrategy_CACHED_FETCH:
		return s.cachedFetch(ctx, limit, offset)
	default:
		// Default to fast fetch for first 3, then cached
		if offset < 3 {
			return s.fastFetch(ctx, limit, offset)
		}
		return s.cachedFetch(ctx, limit, offset)
	}
}

// fastFetch - Direct database fetch for immediate results
func (s *RealsServer) fastFetch(ctx context.Context, limit, offset int) (*pb.GetAllActiveRealsResponse, error) {
	reals, err := s.db.GetActiveReals(limit, offset)
	if err != nil {
		log.Printf("Failed to fetch reals: %v", err)
		return s.errorResponseAll(500, "Failed to fetch reals"), nil
	}

	// Convert to proto and enrich with user data
	protoReals, err := s.enrichRealsWithUserData(ctx, reals)
	if err != nil {
		log.Printf("Failed to enrich reals: %v", err)
		return s.errorResponseAll(500, "Failed to process reals"), nil
	}

	// Cache the results for future use
	go func() {
		cacheKey := fmt.Sprintf("reals:fast:%d:%d", limit, offset)
		s.redis.CacheReals(cacheKey, reals, 5*time.Minute)
	}()

	return &pb.GetAllActiveRealsResponse{
		StatusCode: 200,
		Message:    "Reals fetched successfully",
		Result: &pb.GetAllActiveRealsResponse_Success{
			Success: &pb.AllRealsSuccess{
				Reals:      protoReals,
				TotalCount: int32(len(protoReals)),
				FromCache:  false,
			},
		},
	}, nil
}

// cachedFetch - Redis cache fetch for faster subsequent requests
func (s *RealsServer) cachedFetch(ctx context.Context, limit, offset int) (*pb.GetAllActiveRealsResponse, error) {
	cacheKey := fmt.Sprintf("reals:cached:%d:%d", limit, offset)

	// Try cache first
	cachedReals, err := s.redis.GetCachedReals(cacheKey)
	if err == nil && len(cachedReals) > 0 {
		protoReals, err := s.enrichRealsWithUserData(ctx, cachedReals)
		if err == nil {
			return &pb.GetAllActiveRealsResponse{
				StatusCode: 200,
				Message:    "Reals fetched from cache",
				Result: &pb.GetAllActiveRealsResponse_Success{
					Success: &pb.AllRealsSuccess{
						Reals:      protoReals,
						TotalCount: int32(len(protoReals)),
						FromCache:  true,
					},
				},
			}, nil
		}
	}

	// Fallback to database
	reals, err := s.db.GetActiveReals(limit, offset)
	if err != nil {
		log.Printf("Failed to fetch reals: %v", err)
		return s.errorResponseAll(500, "Failed to fetch reals"), nil
	}

	protoReals, err := s.enrichRealsWithUserData(ctx, reals)
	if err != nil {
		log.Printf("Failed to enrich reals: %v", err)
		return s.errorResponseAll(500, "Failed to process reals"), nil
	}

	// Cache for next time
	go func() {
		s.redis.CacheReals(cacheKey, reals, 10*time.Minute)
	}()

	return &pb.GetAllActiveRealsResponse{
		StatusCode: 200,
		Message:    "Reals fetched successfully",
		Result: &pb.GetAllActiveRealsResponse_Success{
			Success: &pb.AllRealsSuccess{
				Reals:      protoReals,
				TotalCount: int32(len(protoReals)),
				FromCache:  false,
			},
		},
	}, nil
}

// Helper functions
func (s *RealsServer) enrichRealsWithUserData(ctx context.Context, reals []Real) ([]*pb.Real, error) {
	var protoReals []*pb.Real

	for _, real := range reals {
		// Get user info via gRPC
		userName, userAvatar, err := s.grpcClients.GetUserInfo(ctx, real.UserID)
		if err != nil {
			log.Printf("Failed to get user info: %v", err)
			userName = "Unknown User"
			userAvatar = ""
		}

		// Convert media types
		var protoMediaTypes []pb.MediaType
		for _, mediaType := range real.MediaTypes {
			switch mediaType {
			case "IMAGE":
				protoMediaTypes = append(protoMediaTypes, pb.MediaType_IMAGE)
			case "VIDEO":
				protoMediaTypes = append(protoMediaTypes, pb.MediaType_VIDEO)
			default:
				protoMediaTypes = append(protoMediaTypes, pb.MediaType_UNKNOWN)
			}
		}

		protoReal := &pb.Real{
			Id:         real.ID,
			UserId:     real.UserID,
			UserName:   userName,
			UserAvatar: userAvatar,
			MediaUrls:  real.MediaURLs,
			MediaTypes: protoMediaTypes,
			CreatedAt:  real.CreatedAt.Format(time.RFC3339),
			ExpiresAt:  real.ExpiresAt.Format(time.RFC3339),
			ViewCount:  int32(real.ViewCount),
			ViewerIds:  real.ViewerIDs,
		}

		protoReals = append(protoReals, protoReal)
	}

	return protoReals, nil
}

func (s *RealsServer) errorResponse(code int, message string) *pb.CreateRealResponse {
	return &pb.CreateRealResponse{
		StatusCode: int32(code),
		Message:    message,
		Result: &pb.CreateRealResponse_Error{
			Error: &pb.ErrorDetails{
				Code:      int32(code),
				Message:   message,
				Timestamp: time.Now().Format(time.RFC3339),
			},
		},
	}
}

func (s *RealsServer) errorResponseAll(code int, message string) *pb.GetAllActiveRealsResponse {
	return &pb.GetAllActiveRealsResponse{
		StatusCode: int32(code),
		Message:    message,
		Result: &pb.GetAllActiveRealsResponse_Error{
			Error: &pb.ErrorDetails{
				Code:      int32(code),
				Message:   message,
				Timestamp: time.Now().Format(time.RFC3339),
			},
		},
	}
}

func (s *RealsServer) errorResponseUser(code int, message string) *pb.GetUserRealsResponse {
	return &pb.GetUserRealsResponse{
		StatusCode: int32(code),
		Message:    "false",
		Result: &pb.GetUserRealsResponse_Error{
			Error: &pb.ErrorDetails{
				Code:      int32(code),
				Message:   message,
				Timestamp: time.Now().Format(time.RFC3339),
			},
		},
	}
}

func (s *RealsServer) errorResponseView(code int, message string) *pb.ViewRealResponse {
	return &pb.ViewRealResponse{
		StatusCode: int32(code),
		Message:    "false",
		Result: &pb.ViewRealResponse_Error{
			Error: &pb.ErrorDetails{
				Code:      int32(code),
				Message:   message,
				Timestamp: time.Now().Format(time.RFC3339),
			},
		},
	}
}

func (s *RealsServer) errorResponseDelete(code int, message string) *pb.DeleteRealResponse {
	return &pb.DeleteRealResponse{
		StatusCode: int32(code),
		Message:    "false",
		Result: &pb.DeleteRealResponse_Error{
			Error: &pb.ErrorDetails{
				Code:      int32(code),
				Message:   message,
				Timestamp: time.Now().Format(time.RFC3339),
			},
		},
	}
}

// Background cleanup job
func (s *RealsServer) startCleanupJob() {
	ticker := time.NewTicker(1 * time.Hour)
	defer ticker.Stop()

	for range ticker.C {
		select {
		case <-ticker.C:
			if err := s.db.CleanupExpiredReals(); err != nil {
				log.Printf("Failed to cleanup expired reals: %v", err)
			} else {
				log.Println("Expired reals cleaned up successfully")
				// Invalidate cache after cleanup
				s.redis.InvalidateCache("reals:*")
			}
		}
	}
}
func (s *RealsServer) GetUserReals(ctx context.Context, req *pb.GetUserRealsRequest) (*pb.GetUserRealsResponse, error) {
	if req.UserId == "" {
		return s.errorResponseUser(400, "User ID is required"), nil
	}

	// Get user's reals from database
	reals, err := s.db.GetUserReals(req.UserId)
	if err != nil {
		log.Printf("Failed to get user reals: %v", err)
		return s.errorResponseUser(500, "Failed to retrieve user reals"), nil
	}

	// Enrich with user data
	enrichedReals, err := s.enrichRealsWithUserData(ctx, reals)
	if err != nil {
		log.Printf("Failed to enrich reals with user data: %v", err)
		return s.errorResponseUser(500, "Failed to process user reals"), nil
	}

	return &pb.GetUserRealsResponse{
		StatusCode: 200,
		Message:    "true",
		Result: &pb.GetUserRealsResponse_Success{
			Success: &pb.UserRealsSuccess{
				Reals:      enrichedReals,
				TotalCount: int32(len(enrichedReals)),
			},
		},
	}, nil
}

func (s *RealsServer) ViewReal(ctx context.Context, req *pb.ViewRealRequest) (*pb.ViewRealResponse, error) {
	if req.RealId == "" {
		return s.errorResponseView(400, "Real ID is required"), nil
	}

	if req.ViewerUserId == "" {
		return s.errorResponseView(400, "Viewer ID is required"), nil
	}

	// Check if user has already viewed this real
	hasViewed, err := s.redis.HasUserViewedReal(req.RealId, req.ViewerUserId)
	if err != nil {
		log.Printf("Failed to check view status: %v", err)
	}

	// Get the real from database
	real, err := s.db.GetRealByID(req.RealId)
	if err != nil {
		log.Printf("Failed to get real: %v", err)
		return s.errorResponseView(404, "Real not found"), nil
	}

	// Check if real has expired
	if time.Now().After(real.ExpiresAt) {
		return s.errorResponseView(410, "Real has expired"), nil
	}

	// If user hasn't viewed this real before, increment view count
	if !hasViewed {
		// Update view count in database
		real.ViewCount++
		if err := s.db.UpdateReal(real); err != nil {
			log.Printf("Failed to update view count: %v", err)
		}

		// Mark user as having viewed this real in Redis
		if err := s.redis.SetUserViewedReal(req.RealId, req.ViewerUserId); err != nil {
			log.Printf("Failed to set view status: %v", err)
		}

		// Invalidate cache for this real
		s.redis.InvalidateCache(fmt.Sprintf("reals:%s", req.RealId))
	}

	// Enrich with user data
	enrichedReals, err := s.enrichRealsWithUserData(ctx, []Real{*real})
	if err != nil {
		log.Printf("Failed to enrich real with user data: %v", err)
		return s.errorResponseView(500, "Failed to process real data"), nil
	}

	if len(enrichedReals) == 0 {
		return s.errorResponseView(404, "Real not found"), nil
	}

	return &pb.ViewRealResponse{
		StatusCode: 200,
		Message:    "true",
		Result: &pb.ViewRealResponse_Real{
			Real: enrichedReals[0],
		},
	}, nil
}

func (s *RealsServer) DeleteReal(ctx context.Context, req *pb.DeleteRealRequest) (*pb.DeleteRealResponse, error) {
	if req.RealId == "" {
		return s.errorResponseDelete(400, "Real ID is required"), nil
	}

	if req.UserId == "" {
		return s.errorResponseDelete(400, "User ID is required"), nil
	}

	// Get the real to verify ownership
	real, err := s.db.GetRealByID(req.RealId)
	if err != nil {
		log.Printf("Failed to get real: %v", err)
		return s.errorResponseDelete(404, "Real not found"), nil
	}

	// Verify ownership
	if real.UserID != req.UserId {
		return s.errorResponseDelete(403, "You can only delete your own reals"), nil
	}

	// Perform soft delete
	if err := s.db.SoftDeleteReal(req.RealId); err != nil {
		log.Printf("Failed to delete real: %v", err)
		return s.errorResponseDelete(500, "Failed to delete real"), nil
	}

	// Invalidate cache
	s.redis.InvalidateCache(fmt.Sprintf("reals:%s", req.RealId))
	s.redis.InvalidateCache("reals:all:*")
	s.redis.InvalidateCache(fmt.Sprintf("reals:user:%s", req.UserId))

	return &pb.DeleteRealResponse{
		StatusCode: 200,
		Message:    "true",
		Result: &pb.DeleteRealResponse_Success{
			Success: &pb.DeleteSuccess{
				RealId:    req.RealId,
				DeletedAt: time.Now().Format(time.RFC3339),
			},
		},
	}, nil
}
