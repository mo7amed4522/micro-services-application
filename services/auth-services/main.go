package main

import (
	"fmt"
	"log"
	"net"
	"os"
	"strings"

	pb "auth-services/proto"
	"auth-services/server"

	"google.golang.org/grpc"
)

func validateEnvironment() error {
	required := map[string]string{
		"FIREBASE_CREDENTIALS_PATH": os.Getenv("FIREBASE_CREDENTIALS_PATH"),
		"USER_SERVICE_ADDR":         os.Getenv("USER_SERVICE_ADDR"),
		"JWT_SECRET":                os.Getenv("JWT_SECRET"),
	}

	var missing []string
	for key, value := range required {
		if value == "" {
			missing = append(missing, key)
		}
	}

	if len(missing) > 0 {
		return fmt.Errorf("missing required environment variables: %s", strings.Join(missing, ", "))
	}

	return nil
}

func main() {
	if err := validateEnvironment(); err != nil {
		log.Fatalf("Environment validation failed: %v", err)
	}
	lis, err := net.Listen("tcp", ":50052")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}
	s := grpc.NewServer()
	firebaseCredentials := os.Getenv("FIREBASE_CREDENTIALS_PATH")
	userServiceAddr := os.Getenv("USER_SERVICE_ADDR")
	jwtSecret := os.Getenv("JWT_SECRET")

	if firebaseCredentials == "" || userServiceAddr == "" || jwtSecret == "" {
		log.Fatal("Missing required environment variables") // CRASH POINT
	}
	authServer, err := server.NewAuthServer(firebaseCredentials, userServiceAddr, jwtSecret)
	if err != nil {
		log.Fatalf("Failed to create auth server: %v", err)
	}
	defer authServer.Close()
	pb.RegisterAuthServiceServer(s, authServer)
	log.Println("Auth service is running on port 50052...")
	if err := s.Serve(lis); err != nil {
		log.Fatalf("Failed to serve: %v", err)
	}
}
