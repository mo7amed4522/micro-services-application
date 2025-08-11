package server

import (
	"context"
	"log"
	"strings"

	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/metadata"
	"google.golang.org/grpc/status"
)

func AuthInterceptor(userClient UserServiceClient) grpc.UnaryServerInterceptor {
	return func(
		ctx context.Context,
		req interface{},
		info *grpc.UnaryServerInfo,
		handler grpc.UnaryHandler,
	) (interface{}, error) {
		md, ok := metadata.FromIncomingContext(ctx)
		if !ok {
			return nil, status.Errorf(codes.Unauthenticated, "Missing metadata")
		}
		auth := md.Get("authorization")
		if len(auth) == 0 {
			return nil, status.Errorf(codes.Unauthenticated, "Missing authorization header")
		}
		token := auth[0]
		if strings.HasPrefix(token, "Bearer ") {
			token = strings.TrimPrefix(token, "Bearer ")
		}
		claims, err := userClient.ValidateToken(ctx, token)
		if err != nil {
			log.Printf("Token validation failed: %v", err)
			return nil, status.Errorf(codes.Unauthenticated, "Invalid token")
		}
		ctx = context.WithValue(ctx, "userClaims", claims)
		ctx = context.WithValue(ctx, "userID", claims.UserID)
		ctx = context.WithValue(ctx, "userEmail", claims.Email)
		ctx = context.WithValue(ctx, "userRole", claims.Role)
		return handler(ctx, req)
	}
}
func GetUserIDFromContext(ctx context.Context) (string, error) {
	userID, ok := ctx.Value("userID").(string)
	if !ok || userID == "" {
		return "", status.Errorf(codes.Unauthenticated, "User ID not found in context")
	}
	return userID, nil
}
func GetUserClaimsFromContext(ctx context.Context) (*TokenClaims, error) {
	claims, ok := ctx.Value("userClaims").(*TokenClaims)
	if !ok || claims == nil {
		return nil, status.Errorf(codes.Unauthenticated, "User claims not found in context")
	}
	return claims, nil
}
