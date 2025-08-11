package main

import (
	"log"
	"net"

	pb "user-services/proto"
	"user-services/server"

	"google.golang.org/grpc"
)

func main() {
	lis, err := net.Listen("tcp", ":50051")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}
	s := grpc.NewServer()
	userServer := server.NewUserServer()
	pb.RegisterUserServiceServer(s, userServer)

	log.Println("User service is running on port 50051...")
	if err := s.Serve(lis); err != nil {
		log.Fatalf("Failed to serve: %v", err)
	}
}
