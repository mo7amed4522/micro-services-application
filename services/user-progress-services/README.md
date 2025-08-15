# User Progress Service

A gRPC-based microservice for tracking user learning progress, achievements, and learning paths in the Luxor educational platform.

## Overview

The User Progress Service manages user learning journeys by tracking progress through educational templates, unlocking achievements, and providing personalized learning paths. It's designed to gamify the learning experience and provide insights into user engagement with historical content about ancient Egypt.

## Features

### 📊 Progress Tracking
- Track user progress through educational templates and sections
- Monitor completion status and progress percentages
- Record last viewed timestamps for content
- Support for section-level granular tracking

### 🏆 Achievement System
- Unlock achievements based on learning milestones
- Badge system with customizable criteria
- Achievement history and unlocking timestamps
- Gamification elements to encourage learning

### 🛤️ Learning Paths
- Personalized learning recommendations
- Overall progress calculation across all templates
- Adaptive content suggestions based on user behavior
- Progress analytics and insights

### 📈 Analytics & Reporting
- Completed templates tracking with pagination
- User achievement analytics
- Learning progress metrics
- Historical data preservation

## Technical Architecture

### Technology Stack
- **Language**: Go 1.23+
- **Framework**: gRPC with Protocol Buffers
- **Database**: PostgreSQL
- **Dependencies**:
  - `google.golang.org/grpc` - gRPC framework
  - `github.com/lib/pq` - PostgreSQL driver
  - `github.com/google/uuid` - UUID generation
  - `google.golang.org/protobuf` - Protocol Buffers

### Database Schema
The service uses PostgreSQL with the following main tables:
- `user_progress` - Tracks user progress through templates and sections
- `achievements` - Defines available achievements and criteria
- `user_achievements` - Records unlocked user achievements

## API Documentation

### gRPC Service Methods

#### 1. GetUserProgress
Retrieve user progress for specific templates or all templates.

```protobuf
rpc GetUserProgress(GetUserProgressRequest) returns (GetUserProgressResponse);
```

**Request:**
- `user_id` (string, required): User identifier
- `template_id` (string, optional): Specific template ID to filter

**Response:**
- Progress list with completion status, percentages, and timestamps
- Total count for pagination

#### 2. UpdateProgress
Update user progress for a specific template section.

```protobuf
rpc UpdateProgress(UpdateProgressRequest) returns (UpdateProgressResponse);
```

**Request:**
- `user_id` (string, required): User identifier
- `template_id` (string, required): Template identifier
- `section_id` (string, required): Section identifier
- `progress` (float, required): Progress percentage (0-100)
- `completed` (bool, required): Completion status

#### 3. GetCompletedTemplates
Retrieve paginated list of completed templates for a user.

```protobuf
rpc GetCompletedTemplates(GetCompletedTemplatesRequest) returns (GetCompletedTemplatesResponse);
```

**Request:**
- `user_id` (string, required): User identifier
- `page` (int32, optional): Page number (default: 1)
- `limit` (int32, optional): Items per page (default: 10, max: 100)

#### 4. GetUserAchievements
Retrieve all achievements unlocked by a user.

```protobuf
rpc GetUserAchievements(GetUserAchievementsRequest) returns (GetUserAchievementsResponse);
```

#### 5. UnlockAchievement
Unlock a specific achievement for a user.

```protobuf
rpc UnlockAchievement(UnlockAchievementRequest) returns (UnlockAchievementResponse);
```

#### 6. GetLearningPath
Get personalized learning recommendations and overall progress.

```protobuf
rpc GetLearningPath(GetLearningPathRequest) returns (GetLearningPathResponse);
```

**Response includes:**
- Recommended template IDs
- Overall progress percentage
- Completed vs total templates count

## Setup and Installation

### Prerequisites
- Go 1.23 or higher
- PostgreSQL database
- Protocol Buffers compiler (protoc)

### Environment Variables
Create a `.env` file with the following configuration:

```env
DB_HOST=localhost
DB_PORT=5432
DB_USER=postgres
DB_PASSWORD=your_password
DB_NAME=userdb
GRPC_PORT=50060
```

### Local Development

1. **Clone and navigate to the service directory:**
```bash
cd /Users/standarduser/project/micro-services-application/services/user-progress-services
```

2. **Install dependencies:**
```bash
go mod download
```

3. **Set up the database:**
   - Ensure PostgreSQL is running
   - Create the database specified in your `.env` file
   - Run database migrations (if available)

4. **Generate Protocol Buffer files (if needed):**
```bash
protoc --go_out=. --go-grpc_out=. proto/user_progress.proto
```

5. **Run the service:**
```bash
go run main.go
```

The service will start on the port specified in `GRPC_PORT` (default: 50060).

### Docker Deployment

1. **Build the Docker image:**
```bash
docker build -t user-progress-service .
```

2. **Run with Docker:**
```bash
docker run -p 50060:50060 --env-file .env user-progress-service
```

### Docker Compose
The service is typically deployed as part of the larger Luxor microservices ecosystem using Docker Compose.

## Data Models

### UserProgress
- `id`: Unique identifier
- `user_id`: User identifier
- `template_id`: Educational template identifier
- `section_id`: Section within template
- `progress`: Progress percentage (0-100)
- `completed`: Boolean completion status
- `last_viewed`: Timestamp of last access
- `created_at`: Creation timestamp

### Achievement
- `id`: Unique identifier
- `code`: Achievement code
- `name`: Display name
- `description`: Achievement description
- `criteria`: Key-value criteria map
- `badge_url`: Badge image URL
- `created_at`: Creation timestamp

### LearningPath
- `user_id`: User identifier
- `recommended_template_ids`: List of recommended templates
- `overall_progress`: Overall completion percentage
- `completed_templates`: Number of completed templates
- `total_templates`: Total available templates

## Validation Rules

- **User ID**: 1-100 characters, non-empty
- **Template ID**: 1-100 characters, non-empty
- **Section ID**: 1-100 characters, non-empty
- **Progress**: Float between 0 and 100
- **UUID**: Valid UUID v4 format
- **Pagination**: Page ≥ 1, Limit 1-100 (default: 10)

## Error Handling

The service provides comprehensive error handling with:
- HTTP-style status codes (400, 500, etc.)
- Detailed error messages
- Error context and timestamps
- Validation error details

## Integration

This service integrates with:
- **User Service**: For user validation and authentication
- **History Template Service**: For educational content templates
- **Quiz Service**: For achievement unlocking based on quiz completion
- **API Gateway**: For external API access

## Monitoring and Logging

- Structured logging for all operations
- Database connection monitoring
- gRPC request/response logging
- Error tracking and reporting

## Security Considerations

- Input validation on all requests
- SQL injection prevention through parameterized queries
- User ID validation and sanitization
- Rate limiting (implemented at gateway level)

## Contributing

1. Follow Go coding standards
2. Add tests for new functionality
3. Update Protocol Buffer definitions when needed
4. Maintain backward compatibility
5. Document API changes

## License

Part of the Luxor Educational Platform - All rights reserved.