package main

import (
	"log"
	"net"
	"os"

	pb "pharaoh-services/proto"
	"pharaoh-services/server"

	"github.com/joho/godotenv"
	"google.golang.org/grpc"
)

func main() {
	if err := godotenv.Load(); err != nil {
		log.Println("No .env file found")
	}
	port := os.Getenv("PHARAOH_SERVICE_PORT")
	if port == "" {
		port = "50055"
	}
	lis, err := net.Listen("tcp", ":"+port)
	if err != nil {
		log.Fatalf("Failed to listen on port %s: %v", port, err)
	}
	grpcServer := grpc.NewServer()
	pharaohServer, err := server.NewPharaohServer()
	if err != nil {
		log.Fatalf("Failed to create pharaoh server: %v", err)
	}
	defer pharaohServer.Close()
	pb.RegisterPharaohServiceServer(grpcServer, pharaohServer)

	log.Printf("Pharaoh gRPC server listening on port %s", port)
	if err := grpcServer.Serve(lis); err != nil {
		log.Fatalf("Failed to serve gRPC server: %v", err)
	}
}
