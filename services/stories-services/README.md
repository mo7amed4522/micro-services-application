# Stories Service

A gRPC-based microservice for managing user-generated stories in the Luxor educational platform. This service provides comprehensive story management capabilities including creation, publishing, categorization, and social interactions with Redis caching for optimal performance.

## Overview

The Stories Service enables users to create, share, and interact with educational stories about ancient Egypt. It supports multi-language content, categorization, tagging, and social features like reactions and view tracking. The service uses PostgreSQL for persistent storage and Redis for high-performance caching.

## Features

### 📝 Story Management
- Create and edit stories with rich content
- Multi-language support (default: English)
- Story categorization and tagging system
- Draft and published status management
- Featured stories highlighting
- Image attachment support

### 📚 Content Organization
- Category-based story filtering
- Tag-based content discovery
- User-specific story collections
- Pagination for large datasets
- Status-based filtering (draft, published)

### 🚀 Publishing & Workflow
- Draft-to-published workflow
- Author attribution and management
- Publication timestamp tracking
- Story update and versioning
- Soft delete functionality

### 📊 Social Features
- Story view count tracking
- Like/reaction system
- User engagement metrics
- Featured story promotion
- Social interaction analytics

### ⚡ Performance Optimization
- Redis caching for frequently accessed stories
- Optimized database queries with pagination
- Cache invalidation strategies
- High-performance story retrieval

## Technical Architecture

### Technology Stack
- **Language**: Go 1.21+
- **Framework**: gRPC with Protocol Buffers
- **Database**: PostgreSQL
- **Cache**: Redis
- **Dependencies**:
  - `google.golang.org/grpc` - gRPC framework
  - `github.com/lib/pq` - PostgreSQL driver
  - `github.com/go-redis/redis/v8` - Redis client
  - `github.com/google/uuid` - UUID generation

### Database Schema
The service uses PostgreSQL with the following main table structure:

```sql
CREATE TABLE stories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    title VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'draft',
    category VARCHAR(100),
    tags TEXT[], -- Array of tags
    image_url TEXT,
    language VARCHAR(10) DEFAULT 'en',
    is_featured BOOLEAN DEFAULT false,
    view_count INTEGER DEFAULT 0,
    like_count INTEGER DEFAULT 0,
    published_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Caching Strategy
- **Story Cache**: Individual stories cached by ID
- **List Cache**: Paginated story lists cached by query parameters
- **User Cache**: User-specific story collections
- **Featured Cache**: Featured stories for quick access
- **TTL**: Configurable expiration times for different cache types

## API Documentation

### gRPC Service Methods

#### 1. CreateStory
Create a new story in draft status.

```protobuf
rpc CreateStory(CreateStoryRequest) returns (CreateStoryResponse);
```

**Request:**
- `user_id` (string, required): Story author's user ID
- `title` (string, required): Story title
- `content` (string, required): Story content/body
- `author` (string, required): Author name
- `category` (string, optional): Story category
- `tags` (repeated string, optional): Story tags
- `image_url` (string, optional): Featured image URL
- `language` (string, optional): Content language (default: "en")

**Response:**
- Complete story object with generated ID and timestamps
- Status: "draft" by default

#### 2. GetStoryById
Retrieve a specific story by its ID.

```protobuf
rpc GetStoryById(GetStoryByIdRequest) returns (GetStoryByIdResponse);
```

**Request:**
- `story_id` (string, required): Unique story identifier

**Response:**
- Complete story object with all metadata
- Automatically increments view count

#### 3. GetStoriesByUser
Retrieve paginated stories for a specific user.

```protobuf
rpc GetStoriesByUser(GetStoriesByUserRequest) returns (GetStoriesByUserResponse);
```

**Request:**
- `user_id` (string, required): User identifier
- `page` (int32, optional): Page number (default: 1)
- `limit` (int32, optional): Items per page (default: 10)
- `status` (string, optional): Filter by status ("draft", "published")

**Response:**
- Paginated list of user's stories
- Total count and pagination metadata

#### 4. GetAllStories
Retrieve paginated stories with optional filtering.

```protobuf
rpc GetAllStories(GetAllStoriesRequest) returns (GetAllStoriesResponse);
```

**Request:**
- `page` (int32, optional): Page number (default: 1)
- `limit` (int32, optional): Items per page (default: 10)
- `category` (string, optional): Filter by category
- `status` (string, optional): Filter by status
- `featured_only` (bool, optional): Show only featured stories

**Response:**
- Paginated list of stories matching criteria
- Total count and pagination metadata

#### 5. UpdateStory
Update an existing story's content and metadata.

```protobuf
rpc UpdateStory(UpdateStoryRequest) returns (UpdateStoryResponse);
```

**Request:**
- `story_id` (string, required): Story to update
- `title` (string, optional): New title
- `content` (string, optional): New content
- `category` (string, optional): New category
- `tags` (repeated string, optional): New tags
- `image_url` (string, optional): New image URL

**Response:**
- Updated story object with new timestamps
- Cache invalidation for affected entries

#### 6. DeleteStory
Soft delete a story (marks as deleted, preserves data).

```protobuf
rpc DeleteStory(DeleteStoryRequest) returns (DeleteStoryResponse);
```

**Request:**
- `story_id` (string, required): Story to delete
- `user_id` (string, required): User authorization

**Response:**
- Confirmation of deletion
- Cache cleanup for deleted story

#### 7. PublishStory
Publish a draft story, making it publicly visible.

```protobuf
rpc PublishStory(PublishStoryRequest) returns (PublishStoryResponse);
```

**Request:**
- `story_id` (string, required): Story to publish
- `user_id` (string, required): User authorization

**Response:**
- Updated story with "published" status
- Publication timestamp set

#### 8. ReactToStory
Add a reaction (like) to a story.

```protobuf
rpc ReactToStory(ReactToStoryRequest) returns (ReactToStoryResponse);
```

**Request:**
- `story_id` (string, required): Story to react to
- `user_id` (string, required): User adding reaction
- `reaction_type` (string, required): Type of reaction ("like")

**Response:**
- Confirmation of reaction
- Updated like count

## Setup and Installation

### Prerequisites
- Go 1.21 or higher
- PostgreSQL database
- Redis server
- Protocol Buffers compiler (protoc)

### Environment Variables
Create a `.env` file with the following configuration:

```env
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_USER=postgres
DB_PASSWORD=your_password
DB_NAME=userdb

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# Server Configuration
GRPC_PORT=50058
```

### Database Setup

1. **Create the stories table:**
```sql
CREATE TABLE IF NOT EXISTS stories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    title VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'draft',
    category VARCHAR(100),
    tags TEXT[],
    image_url TEXT,
    language VARCHAR(10) DEFAULT 'en',
    is_featured BOOLEAN DEFAULT false,
    view_count INTEGER DEFAULT 0,
    like_count INTEGER DEFAULT 0,
    published_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

2. **Create indexes for performance:**
```sql
CREATE INDEX idx_stories_user_id ON stories(user_id);
CREATE INDEX idx_stories_status ON stories(status);
CREATE INDEX idx_stories_category ON stories(category);
CREATE INDEX idx_stories_featured ON stories(is_featured);
CREATE INDEX idx_stories_published_at ON stories(published_at);
```

### Local Development

1. **Clone and navigate to the service directory:**
```bash
cd /Users/standarduser/project/micro-services-application/services/stories-services
```

2. **Install dependencies:**
```bash
go mod download
```

3. **Set up PostgreSQL and Redis:**
   - Ensure PostgreSQL is running on port 5432
   - Ensure Redis is running on port 6379
   - Create the database specified in your `.env` file
   - Run the database setup scripts

4. **Generate Protocol Buffer files (if needed):**
```bash
protoc --go_out=. --go-grpc_out=. proto/stories.proto
```

5. **Run the service:**
```bash
go run main.go
```

The service will start on port 50058.

### Docker Deployment

1. **Build the Docker image:**
```bash
docker build -t stories-service .
```

2. **Run with Docker:**
```bash
docker run -p 50058:50058 --env-file .env stories-service
```

### Docker Compose
The service is typically deployed as part of the larger Luxor microservices ecosystem using Docker Compose.

## Data Models

### Story
- `id`: Unique UUID identifier
- `user_id`: Story author's user ID
- `title`: Story title (max 500 chars)
- `content`: Story content/body (unlimited text)
- `author`: Author display name
- `status`: "draft" or "published"
- `category`: Story category for organization
- `tags`: Array of searchable tags
- `image_url`: Featured image URL
- `language`: Content language code (default: "en")
- `is_featured`: Featured story flag
- `view_count`: Number of views
- `like_count`: Number of likes/reactions
- `published_at`: Publication timestamp
- `created_at`: Creation timestamp
- `updated_at`: Last modification timestamp

### Story Status Values
- **draft**: Story is being written, not publicly visible
- **published**: Story is live and publicly accessible

### Supported Languages
- **en**: English (default)
- **ar**: Arabic
- **fr**: French
- Additional languages can be added as needed

## Caching Strategy

### Cache Keys
- `story:{id}`: Individual story cache
- `stories:user:{user_id}:page:{page}:limit:{limit}`: User stories cache
- `stories:all:page:{page}:limit:{limit}:category:{category}`: All stories cache
- `stories:featured`: Featured stories cache

### Cache TTL
- Individual stories: 1 hour
- Story lists: 30 minutes
- Featured stories: 2 hours
- User-specific lists: 15 minutes

### Cache Invalidation
- Story updates invalidate individual story cache
- New stories invalidate relevant list caches
- Publishing stories invalidates public list caches
- Reactions update cached like counts

## Error Handling

The service provides comprehensive error handling with:
- HTTP-style status codes (400, 404, 500, etc.)
- Detailed error messages and context
- Validation error details
- Timestamp information for debugging

### Common Error Scenarios
- **400 Bad Request**: Missing required fields, invalid data
- **404 Not Found**: Story not found, user not found
- **403 Forbidden**: User not authorized to modify story
- **500 Internal Server Error**: Database errors, cache failures

## Performance Optimization

### Database Optimization
- Indexed queries on frequently searched fields
- Pagination to limit result sets
- Optimized JOIN operations
- Connection pooling for concurrent requests

### Caching Optimization
- Strategic cache warming for popular content
- Cache-aside pattern implementation
- Batch cache operations where possible
- Memory-efficient cache key design

### Query Optimization
- Prepared statements for repeated queries
- Selective field loading based on use case
- Efficient counting queries for pagination
- Optimized sorting and filtering

## Integration

This service integrates with:
- **User Service**: For user validation and author information
- **Upload Service**: For story image management
- **API Gateway**: For external API access and authentication
- **Notification Service**: For story publication notifications

## Monitoring and Logging

- Structured logging for all operations
- Database query performance monitoring
- Redis cache hit/miss ratio tracking
- Story creation and engagement metrics
- Error rate and response time monitoring

## Security Considerations

### Data Protection
- Input validation and sanitization
- SQL injection prevention through parameterized queries
- XSS prevention in story content
- User authorization for story modifications

### Content Moderation
- Content validation before publishing
- Inappropriate content detection
- User reporting mechanisms
- Administrative content management

## Analytics and Insights

### Story Metrics
- View count tracking with increment optimization
- Like/reaction analytics
- Category popularity analysis
- User engagement patterns

### Performance Metrics
- Story creation rates
- Publishing workflow efficiency
- Cache performance statistics
- Database query optimization insights

## Contributing

1. Follow Go coding standards and best practices
2. Add comprehensive tests for new functionality
3. Update Protocol Buffer definitions when needed
4. Maintain backward compatibility
5. Document API changes and new features
6. Test caching behavior thoroughly
7. Validate database migration scripts

## Troubleshooting

### Common Issues

**Database Connection Issues:**
```bash
# Test PostgreSQL connection
psql -h localhost -p 5432 -U postgres -d userdb
```

**Redis Connection Issues:**
```bash
# Test Redis connection
redis-cli -h localhost -p 6379 ping
```

**Cache Performance:**
- Monitor cache hit ratios
- Check Redis memory usage
- Validate cache key patterns
- Review TTL configurations

**Story Publishing Issues:**
- Verify user permissions
- Check story content validation
- Validate required fields
- Review database constraints

## License

Part of the Luxor Educational Platform - All rights reserved.