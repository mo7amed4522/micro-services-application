package main

import (
	"log"
	"net"

	pb "wallet-services/proto"
	"wallet-services/server"

	"google.golang.org/grpc"
)

func main() {
	lis, err := net.Listen("tcp", ":50055")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}
	userClient, err := server.NewUserServiceClient()
	if err != nil {
		log.Fatalf("Failed to initialize user service client: %v", err)
	}
	defer userClient.Close()
	s := grpc.NewServer(
		grpc.UnaryInterceptor(server.AuthInterceptor(userClient)),
	)
	walletServer := server.NewWalletServer()
	pb.RegisterWalletServiceServer(s, walletServer)
	log.Println("Wallet service is running on port 50055 with authentication middleware...")
	if err := s.Serve(lis); err != nil {
		log.Fatalf("Failed to serve: %v", err)
	}
}
