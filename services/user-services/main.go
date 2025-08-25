package main

import (
	"context"
	"log"
	"net"
	"os"
	"os/signal"
	"syscall"

	pb "user-services/proto"
	"user-services/server"

	"google.golang.org/grpc"
)

func main() {
	// Graceful shutdown channel
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	lis, err := net.Listen("tcp", ":50051")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}

	s := grpc.NewServer()

	// Handle database initialization with retry
	userServer, err := server.NewUserServerWithRetry(ctx)
	if err != nil {
		log.Fatalf("Failed to initialize user server: %v", err)
	}
	defer userServer.Close()

	pb.RegisterUserServiceServer(s, userServer)

	// Graceful shutdown handling
	go func() {
		sigChan := make(chan os.Signal, 1)
		signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)
		<-sigChan

		log.Println("Shutting down gracefully...")
		s.GracefulStop()
		cancel()
	}()

	log.Println("User service is running on port 50051...")
	if err := s.Serve(lis); err != nil {
		log.Printf("Server stopped: %v", err)
	}
}
