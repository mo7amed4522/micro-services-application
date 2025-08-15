# Reals Service

A high-performance gRPC microservice for managing short-form video and image content ("reals") with automatic expiration, similar to Instagram Stories or TikTok reels. Built with Go, PostgreSQL, and Redis for optimal performance and scalability.

## 🚀 Features

### Core Functionality
- **Media Management**: Upload and manage video/image content with automatic expiration
- **Real-time Viewing**: Track view counts and viewer analytics
- **User Integration**: Seamless integration with user profiles and avatars
- **Content Discovery**: Fetch active reals with multiple strategies (fast/cached)
- **Automatic Cleanup**: Background job for expired content removal

### Performance & Scalability
- **Redis Caching**: High-performance caching for frequently accessed content
- **Dual Fetch Strategies**: Fast fetch for real-time data, cached fetch for performance
- **Background Processing**: Automatic cleanup of expired content
- **Connection Pooling**: Optimized database connections with GORM

### Security & Validation
- **User Authentication**: Secure user-based content management
- **Input Validation**: Comprehensive validation for all requests
- **Soft Deletion**: Safe content removal with audit trails
- **Media Limits**: Configurable limits on media items per real

## 🏗️ Technical Architecture

### Technology Stack
- **Language**: Go 1.21
- **Framework**: gRPC with Protocol Buffers
- **Database**: PostgreSQL with GORM ORM
- **Cache**: Redis for high-performance caching
- **File Storage**: Integration with Upload Service (S3)
- **User Data**: Integration with User Service

### Database Schema

```sql
CREATE TABLE reals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    media_urls JSONB NOT NULL,
    media_types JSONB NOT NULL,
    view_count INTEGER DEFAULT 0,
    viewer_ids JSONB DEFAULT '[]',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP NULL
);

CREATE INDEX idx_reals_user_id ON reals(user_id);
CREATE INDEX idx_reals_expires_at ON reals(expires_at);
CREATE INDEX idx_reals_deleted_at ON reals(deleted_at);
```

### Caching Strategy

- **Active Reals Cache**: Frequently accessed reals cached with TTL
- **User View Tracking**: Redis-based view tracking for performance
- **Cache Invalidation**: Smart cache invalidation on content updates
- **Fallback Mechanism**: Graceful degradation when Redis is unavailable

## 📡 API Documentation

### gRPC Service Methods

#### CreateReal
Creates a new real with media content.

```protobuf
rpc CreateReal(CreateRealRequest) returns (CreateRealResponse);

message CreateRealRequest {
    string user_id = 1;
    repeated MediaItem media_items = 2;
}

message MediaItem {
    bytes file_data = 1;
    string file_name = 2;
    string content_type = 3;
    MediaType media_type = 4;
}
```

**Features:**
- Supports up to 10 media items per real
- Automatic media upload via Upload Service
- 24-hour default expiration
- Returns media URLs and expiration time

#### GetAllActiveReals
Retrieves all active (non-expired) reals with pagination.

```protobuf
rpc GetAllActiveReals(GetAllActiveRealsRequest) returns (GetAllActiveRealsResponse);

message GetAllActiveRealsRequest {
    int32 limit = 1;
    int32 offset = 2;
    FetchStrategy strategy = 3;
}
```

**Fetch Strategies:**
- `FAST_FETCH`: Real-time data from database
- `CACHED_FETCH`: High-performance cached data

#### GetUserReals
Retrieves reals created by a specific user.

```protobuf
rpc GetUserReals(GetUserRealsRequest) returns (GetUserRealsResponse);

message GetUserRealsRequest {
    string user_id = 1;
    int32 limit = 2;
    int32 offset = 3;
}
```

#### ViewReal
Records a view for a specific real and returns enriched data.

```protobuf
rpc ViewReal(ViewRealRequest) returns (ViewRealResponse);

message ViewRealRequest {
    string real_id = 1;
    string viewer_user_id = 2;
}
```

**Features:**
- Increments view count
- Tracks unique viewers
- Returns enriched real data with user information
- Prevents duplicate view counting

#### DeleteReal
Soft deletes a real (user must own the content).

```protobuf
rpc DeleteReal(DeleteRealRequest) returns (DeleteRealResponse);

message DeleteRealRequest {
    string real_id = 1;
    string user_id = 2;
}
```

### Data Models

#### Real
```protobuf
message Real {
    string id = 1;
    string user_id = 2;
    string user_name = 3;
    string user_avatar = 4;
    repeated string media_urls = 5;
    repeated MediaType media_types = 6;
    string created_at = 7;
    string expires_at = 8;
    int32 view_count = 9;
    repeated string viewer_ids = 10;
}
```

#### Media Types
```protobuf
enum MediaType {
    UNKNOWN = 0;
    IMAGE = 1;
    VIDEO = 2;
}
```

## 🛠️ Setup and Installation

### Prerequisites

- Go 1.21 or higher
- PostgreSQL 12+
- Redis 6+
- Protocol Buffers compiler
- Access to Upload Service and User Service

### Environment Variables

Create a `.env` file in the service directory:

```env
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_USER=postgres
DB_PASSWORD=your_password
DB_NAME=userdb

# Server Configuration
PORT=50062

# Redis Configuration
REDIS_ADDR=localhost:6379
REDIS_PASSWORD=

# Service Endpoints
UPLOAD_SERVICE_ADDR=localhost:50061
USER_SERVICE_ADDR=localhost:50051
```

### Local Development

1. **Clone and navigate to the service directory:**
   ```bash
   cd services/reals-services
   ```

2. **Install dependencies:**
   ```bash
   go mod download
   ```

3. **Generate Protocol Buffer files:**
   ```bash
   protoc --go_out=. --go-grpc_out=. proto/reals.proto
   ```

4. **Set up the database:**
   ```sql
   CREATE DATABASE userdb;
   -- The service will auto-migrate tables on startup
   ```

5. **Start Redis:**
   ```bash
   redis-server
   ```

6. **Run the service:**
   ```bash
   go run main.go
   ```

The service will start on port 50062 (or the port specified in the PORT environment variable).

### Docker Deployment

1. **Build the Docker image:**
   ```bash
   docker build -t reals-service .
   ```

2. **Run with Docker Compose:**
   ```yaml
   version: '3.8'
   services:
     reals-service:
       build: .
       ports:
         - "50062:50062"
       environment:
         - DB_HOST=postgres
         - REDIS_ADDR=redis:6379
         - UPLOAD_SERVICE_ADDR=upload-service:50061
         - USER_SERVICE_ADDR=user-service:50051
       depends_on:
         - postgres
         - redis
   ```

## 🔄 Content Lifecycle

### Real Creation Workflow
1. User uploads media items via CreateReal
2. Service validates user and media constraints
3. Media files uploaded to S3 via Upload Service
4. Real record created in database with 24h expiration
5. Cache invalidated for fresh content discovery

### Content Discovery
1. Client requests active reals with pagination
2. Service checks cache (if CACHED_FETCH strategy)
3. Database query for active, non-expired content
4. User data enrichment via User Service
5. Results cached for subsequent requests

### View Tracking
1. User views a real via ViewReal
2. Service checks if user already viewed (Redis)
3. View count incremented, viewer ID recorded
4. Enriched real data returned with user info

### Automatic Cleanup
- Background job runs every 30 minutes
- Removes expired reals from database
- Cleans up associated cache entries
- Maintains database performance

## 🔧 Error Handling

### Error Response Format
```protobuf
message ErrorDetails {
    int32 code = 1;
    string message = 2;
    repeated string details = 3;
    string timestamp = 4;
}
```

### Common Error Codes
- `400`: Bad Request (validation errors)
- `404`: Real not found
- `403`: Unauthorized (user doesn't own real)
- `500`: Internal server error
- `503`: Service unavailable (dependency failure)

## 🚀 Performance Optimization

### Caching Strategy
- **L1 Cache**: Redis for frequently accessed reals
- **Cache TTL**: 5 minutes for active reals list
- **Cache Warming**: Proactive cache population
- **Graceful Degradation**: Service continues without Redis

### Database Optimization
- **Indexing**: Optimized indexes on user_id, expires_at
- **Connection Pooling**: GORM connection management
- **Query Optimization**: Efficient pagination and filtering
- **Soft Deletion**: Maintains referential integrity

### Background Processing
- **Cleanup Jobs**: Automatic expired content removal
- **Batch Operations**: Efficient bulk operations
- **Resource Management**: Controlled resource usage

## 🔗 Integration Points

### Upload Service Integration
- **Media Upload**: Seamless file upload to S3
- **URL Generation**: Presigned URLs for media access
- **File Management**: Coordinated file lifecycle

### User Service Integration
- **Profile Data**: User names and avatars
- **Authentication**: User validation and authorization
- **Social Features**: User relationship data

### API Gateway Integration
- **HTTP/gRPC Bridge**: REST API exposure
- **Authentication**: JWT token validation
- **Rate Limiting**: Request throttling

## 📊 Monitoring and Logging

### Metrics
- Real creation rate
- View count analytics
- Cache hit/miss ratios
- Database query performance
- Service dependency health

### Logging
- Structured logging with context
- Request/response tracing
- Error tracking and alerting
- Performance monitoring

### Health Checks
- Database connectivity
- Redis availability
- Service dependency status
- Resource utilization

## 🔒 Security Considerations

### Data Protection
- User data validation and sanitization
- Secure media URL generation
- Audit trails for content operations
- Privacy-compliant data handling

### Access Control
- User-based content ownership
- Secure service-to-service communication
- Input validation and rate limiting
- SQL injection prevention

## 🧪 Testing

### Unit Tests
```bash
go test ./server/...
```

### Integration Tests
```bash
go test -tags=integration ./...
```

### Load Testing
- gRPC load testing with ghz
- Database performance testing
- Cache performance validation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Implement changes with tests
4. Update documentation
5. Submit a pull request

### Code Standards
- Follow Go conventions and best practices
- Maintain test coverage above 80%
- Update Protocol Buffer definitions as needed
- Document all public APIs

## 🐛 Troubleshooting

### Common Issues

**Service won't start:**
- Check database connectivity
- Verify environment variables
- Ensure Redis is running
- Check port availability

**High memory usage:**
- Monitor cache size
- Check for memory leaks
- Review background job performance
- Optimize database queries

**Slow response times:**
- Check database indexes
- Monitor Redis performance
- Review cache hit ratios
- Analyze query patterns

**Media upload failures:**
- Verify Upload Service connectivity
- Check S3 credentials and permissions
- Monitor file size limits
- Review network connectivity

### Debug Mode
Enable debug logging:
```bash
export LOG_LEVEL=debug
go run main.go
```

## 📄 License

This project is part of the micro-services-application ecosystem. Please refer to the main project license for terms and conditions.

---

**Service Port**: 50062  
**Protocol**: gRPC  
**Health Check**: `/health` (when HTTP gateway is enabled)  
**Metrics**: `/metrics` (when monitoring is enabled)