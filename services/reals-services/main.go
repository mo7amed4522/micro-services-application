package main

import (
    "log"
    "net"
    "os"

    "github.com/joho/godotenv"
    "google.golang.org/grpc"
    pb "reals-services/proto"
    "reals-services/server"
)

func main() {
    // Load environment variables
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

    // Create a gRPC server object
    s := grpc.NewServer()

    // Create reals server instance
    realsServer := server.NewRealsServer()

    // Register the reals service with the gRPC server
    pb.RegisterRealsServiceServer(s, realsServer)

    log.Printf("Reals service is running on port %s...", port)

    // Start the server
    if err := s.Serve(lis); err != nil {
        log.Fatalf("Failed to serve: %v", err)
    }
}
