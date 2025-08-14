package server

import (
	"context"
	"log"
	"os"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

// We'll use the generated proto files from the main proto directory
// These will be generated when we build the service

type GRPCClients struct {
	// We'll define these interfaces to avoid direct proto dependencies
	UploadServiceAddr string
	UserServiceAddr   string
}

func NewGRPCClients() *GRPCClients {
	uploadAddr := os.Getenv("UPLOAD_SERVICE_ADDR")
	userAddr := os.Getenv("USER_SERVICE_ADDR")

	if uploadAddr == "" {
		uploadAddr = "upload-service:50061"
	}
	if userAddr == "" {
		userAddr = "user-service:50051"
	}

	log.Printf("gRPC clients configured - Upload: %s, User: %s", uploadAddr, userAddr)
	return &GRPCClients{
		UploadServiceAddr: uploadAddr,
		UserServiceAddr:   userAddr,
	}
}

// UploadMedia uploads media files using upload service
func (g *GRPCClients) UploadMedia(ctx context.Context, userID string, fileData []byte, fileName, contentType, fileType string) (string, error) {
	// Connect to upload service
	conn, err := grpc.Dial(g.UploadServiceAddr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		return "", err
	}
	defer conn.Close()

	// We'll make a simple HTTP-like call or use reflection
	// For now, let's return a mock URL - you can implement the actual gRPC call
	mockURL := "https://s3.amazonaws.com/bucket/" + userID + "/" + fileName
	log.Printf("Mock upload: %s", mockURL)
	return mockURL, nil
}

// GetUserInfo retrieves user information
func (g *GRPCClients) GetUserInfo(ctx context.Context, userID string) (string, string, error) {
	// Connect to user service
	conn, err := grpc.Dial(g.UserServiceAddr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		return "", "", err
	}
	defer conn.Close()

	// Mock user data - implement actual gRPC call
	userName := "User " + userID[:8]
	userAvatar := "https://avatar.com/" + userID
	log.Printf("Mock user info: %s", userName)
	return userName, userAvatar, nil
}
