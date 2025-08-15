package server

import (
	"context"
	"log"
	"os"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

type GRPCClients struct {
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

func (g *GRPCClients) UploadMedia(ctx context.Context, userID string, fileData []byte, fileName, contentType, fileType string) (string, error) {
	conn, err := grpc.Dial(g.UploadServiceAddr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		return "", err
	}
	defer conn.Close()
	mockURL := "https://s3.amazonaws.com/bucket/" + userID + "/" + fileName
	log.Printf("Mock upload: %s", mockURL)
	return mockURL, nil
}

func (g *GRPCClients) GetUserInfo(ctx context.Context, userID string) (string, string, error) {
	conn, err := grpc.Dial(g.UserServiceAddr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		return "", "", err
	}
	defer conn.Close()
	userName := "User " + userID[:8]
	userAvatar := "https://avatar.com/" + userID
	log.Printf("Mock user info: %s", userName)
	return userName, userAvatar, nil
}
