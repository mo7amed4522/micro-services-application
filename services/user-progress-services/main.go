package main

import (
	"log"
	"net"
	"os"

	pb "user-progress-services/proto"
	"user-progress-services/server"

	"google.golang.org/grpc"
)

func main() {
	port := os.Getenv("GRPC_PORT")
	if port == "" {
		port = "50060"
	}

	lis, err := net.Listen("tcp", ":"+port)
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}
	s := grpc.NewServer()
	userProgressServer := server.NewUserProgressServer()
	pb.RegisterUserProgressServiceServer(s, userProgressServer)
	log.Printf("User Progress service is running on port %s...", port)
	if err := s.Serve(lis); err != nil {
		log.Fatalf("Failed to serve: %v", err)
	}
}
