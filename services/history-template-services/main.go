package main

import (
	"log"
	"net"

	pb "history-template-services/proto"
	"history-template-services/server"

	"google.golang.org/grpc"
)

func main() {
	lis, err := net.Listen("tcp", ":50055")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}
	s := grpc.NewServer()
	historyTemplateServer := server.NewHistoryTemplateServer()
	pb.RegisterHistoryTemplateServiceServer(s, historyTemplateServer)
	log.Println("History Template service is running on port 50055...")
	if err := s.Serve(lis); err != nil {
		log.Fatalf("Failed to serve: %v", err)
	}
}
