package main

import (
	"log"
	"net"

	pb "quiz-services/proto"
	"quiz-services/server"

	"google.golang.org/grpc"
)

func main() {
	lis, err := net.Listen("tcp", ":50056")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}
	s := grpc.NewServer()
	quizServer := server.NewQuizServer()
	pb.RegisterQuizServiceServer(s, quizServer)

	log.Println("Quiz service is running on port 50056...")
	if err := s.Serve(lis); err != nil {
		log.Fatalf("Failed to serve: %v", err)
	}
}
