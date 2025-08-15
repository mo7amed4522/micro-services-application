package main

import (
	"log"
	"net"

	pb "upload-services/proto"
	"upload-services/server"

	"google.golang.org/grpc"
)

func main() {
	lis, err := net.Listen("tcp", ":50061")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}
	s := grpc.NewServer()
	uploadServer := server.NewUploadServer()
	pb.RegisterUploadServiceServer(s, uploadServer)
	log.Println("Upload service is running on port 50061...")
	if err := s.Serve(lis); err != nil {
		log.Fatalf("Failed to serve: %v", err)
	}
}