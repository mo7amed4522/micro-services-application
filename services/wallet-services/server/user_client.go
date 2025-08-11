package server

import (
	"context"
	"fmt"
	"log"
	"os"
	"strings"
	"time"

	userpb "user-services/proto"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

type UserServiceClient interface {
	ValidateToken(ctx context.Context, token string) (*TokenClaims, error)
	Close() error
}

type TokenClaims struct {
	UserID string
	Email  string
	Role   string
	Exp    int64
}

type userServiceClient struct {
	conn   *grpc.ClientConn
	client userpb.UserServiceClient
}

func NewUserServiceClient() (UserServiceClient, error) {
	userServiceAddr := os.Getenv("USER_SERVICE_ADDR")
	if userServiceAddr == "" {
		userServiceAddr = "localhost:50051"
	}

	conn, err := grpc.Dial(userServiceAddr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		return nil, fmt.Errorf("failed to connect to user service: %v", err)
	}

	client := userpb.NewUserServiceClient(conn)

	return &userServiceClient{
		conn:   conn,
		client: client,
	}, nil
}

func (c *userServiceClient) ValidateToken(ctx context.Context, token string) (*TokenClaims, error) {
	log.Printf("Validating token with user service")

	if token == "" {
		return nil, fmt.Errorf("token is empty")
	}
	if strings.HasPrefix(token, "Bearer ") {
		token = strings.TrimPrefix(token, "Bearer ")
	}
	request := &userpb.ValidateTokenRequest{
		Token: token,
	}
	response, err := c.client.ValidateToken(ctx, request)
	if err != nil {
		return nil, fmt.Errorf("failed to validate token: %v", err)
	}
	if response.StatusCode != 200 {
		if response.GetError() != nil {
			return nil, fmt.Errorf("token validation failed: %s", response.GetError().Message)
		}
		return nil, fmt.Errorf("token validation failed with status code: %d", response.StatusCode)
	}
	claims := response.GetClaims()
	if claims == nil {
		return nil, fmt.Errorf("no claims found in response")
	}

	return &TokenClaims{
		UserID: claims.UserId,
		Email:  claims.Email,
		Role:   claims.Role,
		Exp:    claims.Exp,
	}, nil
}
func (c *userServiceClient) Close() error {
	if c.conn != nil {
		return c.conn.Close()
	}
	return nil
}

func ValidateTokenMiddleware(userClient UserServiceClient, token string) (*TokenClaims, error) {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	claims, err := userClient.ValidateToken(ctx, token)
	if err != nil {
		return nil, fmt.Errorf("token validation failed: %v", err)
	}

	return claims, nil
}
