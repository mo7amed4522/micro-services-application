package main

import (
	"log"
	"net"
	"os"

	pb "reals-services/proto"
	"reals-services/server"

	"github.com/joho/godotenv"
	"google.golang.org/grpc"
)

func main() {
	if err := godotenv.Load(); err != nil {
		log.Printf("Warning: .env file not found: %v", err)
	}
	port := os.Getenv("PORT")
	if port == "" {
		port = "50062"
	}
	lis, err := net.Listen("tcp", ":"+port)
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}
	s := grpc.NewServer()
	realsServer := server.NewRealsServer()
	pb.RegisterRealsServiceServer(s, realsServer)
	log.Printf("Reals service is running on port %s...", port)
	if err := s.Serve(lis); err != nil {
		log.Fatalf("Failed to serve: %v", err)
	}
}
