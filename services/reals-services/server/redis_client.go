package server

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"time"

	"github.com/go-redis/redis/v8"
)

type RedisClient struct {
	client *redis.Client
	ctx    context.Context
}

func NewRedisClient() *RedisClient {
	redisAddr := os.Getenv("REDIS_ADDR")
	redisPassword := os.Getenv("REDIS_PASSWORD")
	redisDB := 0

	if redisAddr == "" {
		redisAddr = "localhost:6379"
	}

	rdb := redis.NewClient(&redis.Options{
		Addr:     redisAddr,
		Password: redisPassword,
		DB:       redisDB,
	})

	ctx := context.Background()

	// Test connection
	_, err := rdb.Ping(ctx).Result()
	if err != nil {
		log.Printf("Warning: Redis connection failed: %v", err)
		// Continue without Redis for now
	}

	log.Println("Redis client initialized")
	return &RedisClient{
		client: rdb,
		ctx:    ctx,
	}
}

// CacheReals caches reals data
func (r *RedisClient) CacheReals(key string, reals []Real, expiration time.Duration) error {
	data, err := json.Marshal(reals)
	if err != nil {
		return err
	}

	return r.client.Set(r.ctx, key, data, expiration).Err()
}

// GetCachedReals retrieves cached reals
func (r *RedisClient) GetCachedReals(key string) ([]Real, error) {
	data, err := r.client.Get(r.ctx, key).Result()
	if err != nil {
		return nil, err
	}

	var reals []Real
	err = json.Unmarshal([]byte(data), &reals)
	return reals, err
}

// InvalidateCache removes cached data
func (r *RedisClient) InvalidateCache(pattern string) error {
	keys, err := r.client.Keys(r.ctx, pattern).Result()
	if err != nil {
		return err
	}

	if len(keys) > 0 {
		return r.client.Del(r.ctx, keys...).Err()
	}
	return nil
}

// SetUserViewedReal marks that a user viewed a real
func (r *RedisClient) SetUserViewedReal(userID, realID string) error {
	key := fmt.Sprintf("user_viewed:%s:%s", userID, realID)
	return r.client.Set(r.ctx, key, "1", 24*time.Hour).Err()
}

// HasUserViewedReal checks if user has viewed a real
func (r *RedisClient) HasUserViewedReal(realID, userID string) (bool, error) {
	key := fmt.Sprintf("viewed:%s:%s", realID, userID)
	exists, err := r.client.Exists(r.ctx, key).Result()
	return exists > 0, err
}
