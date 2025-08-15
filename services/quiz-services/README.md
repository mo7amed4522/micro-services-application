# Quiz Service

A comprehensive gRPC microservice for managing educational quizzes, quiz responses, and analytics. Built with Go and PostgreSQL, this service provides a complete quiz management system with user tracking, statistics, and performance analytics.

## 🚀 Features

### Core Functionality
- **Quiz Management**: Create, read, update, and delete quizzes
- **Section-based Organization**: Organize quizzes by educational sections
- **Multiple Choice Questions**: Support for multiple-choice questions with explanations
- **Answer Submission**: Track user responses and validate answers
- **User History**: Complete quiz history tracking per user
- **Analytics & Statistics**: Comprehensive quiz performance analytics

### Educational Features
- **Difficulty Levels**: Configurable difficulty settings for quizzes
- **Detailed Explanations**: Educational explanations for correct answers
- **Progress Tracking**: Monitor user learning progress
- **Performance Analytics**: Success rates and timing analytics
- **Pagination Support**: Efficient handling of large quiz sets

### Data Management
- **PostgreSQL Integration**: Robust data persistence with ACID compliance
- **UUID-based IDs**: Secure and scalable identifier system
- **JSON Options Storage**: Flexible multiple-choice option handling
- **Timestamp Tracking**: Complete audit trail for all operations

## 🏗️ Technical Architecture

### Technology Stack
- **Language**: Go 1.23
- **Framework**: gRPC with Protocol Buffers
- **Database**: PostgreSQL with direct SQL queries
- **UUID Generation**: Google UUID library
- **Containerization**: Docker with multi-stage builds

### Database Schema

```sql
-- Quizzes table
CREATE TABLE quizzes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    section_id VARCHAR(255) NOT NULL,
    question TEXT NOT NULL,
    options JSONB NOT NULL,
    correct_answer VARCHAR(255) NOT NULL,
    explanation TEXT,
    difficulty VARCHAR(50) DEFAULT 'medium',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Quiz responses table
CREATE TABLE quiz_responses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    quiz_id UUID NOT NULL REFERENCES quizzes(id),
    selected_option VARCHAR(255) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_quizzes_section_id ON quizzes(section_id);
CREATE INDEX idx_quiz_responses_user_id ON quiz_responses(user_id);
CREATE INDEX idx_quiz_responses_quiz_id ON quiz_responses(quiz_id);
```

## 📡 API Documentation

### gRPC Service Methods

#### GetQuizzesBySection
Retrieves all quizzes for a specific educational section.

```protobuf
rpc GetQuizzesBySection(GetQuizzesBySectionRequest) returns (GetQuizzesBySectionResponse);

message GetQuizzesBySectionRequest {
    string section_id = 1;
}
```

**Features:**
- Section-based quiz filtering
- Complete quiz list with metadata
- Error handling for invalid sections

#### GetQuizByID
Retrieves a specific quiz by its unique identifier.

```protobuf
rpc GetQuizByID(GetQuizByIDRequest) returns (GetQuizByIDResponse);

message GetQuizByIDRequest {
    string id = 1;
}
```

**Features:**
- Single quiz retrieval
- Complete quiz details including options and explanations
- UUID validation

#### CreateQuiz
Creates a new quiz with multiple-choice options.

```protobuf
rpc CreateQuiz(CreateQuizRequest) returns (CreateQuizResponse);

message CreateQuizRequest {
    Quiz quiz = 1;
}
```

**Features:**
- Automatic UUID generation
- JSON-based options storage
- Validation for required fields
- Timestamp tracking

#### UpdateQuiz
Updates an existing quiz's content and metadata.

```protobuf
rpc UpdateQuiz(UpdateQuizRequest) returns (UpdateQuizResponse);

message UpdateQuizRequest {
    string id = 1;
    Quiz quiz = 2;
}
```

**Features:**
- Partial or complete quiz updates
- Maintains creation timestamp
- Validates quiz existence

#### DeleteQuiz
Removes a quiz from the system.

```protobuf
rpc DeleteQuiz(DeleteQuizRequest) returns (DeleteQuizResponse);

message DeleteQuizRequest {
    string id = 1;
}
```

**Features:**
- Hard deletion from database
- Cascading deletion of related responses
- Confirmation response

#### SubmitQuizAnswer
Submits a user's answer to a quiz question.

```protobuf
rpc SubmitQuizAnswer(SubmitQuizAnswerRequest) returns (SubmitQuizAnswerResponse);

message SubmitQuizAnswerRequest {
    string user_id = 1;
    string quiz_id = 2;
    string selected_option = 3;
}
```

**Features:**
- Automatic answer validation
- Correctness determination
- Response tracking and storage
- Timestamp recording

#### GetUserQuizHistory
Retrieves a user's complete quiz response history.

```protobuf
rpc GetUserQuizHistory(GetUserQuizHistoryRequest) returns (GetUserQuizHistoryResponse);

message GetUserQuizHistoryRequest {
    string user_id = 1;
    int32 page = 2;
    int32 limit = 3;
}
```

**Features:**
- Paginated response history
- Chronological ordering
- Complete response details
- Performance metrics

#### GetQuizStatistics
Provides comprehensive analytics for a specific quiz.

```protobuf
rpc GetQuizStatistics(GetQuizStatisticsRequest) returns (GetQuizStatisticsResponse);

message GetQuizStatisticsRequest {
    string quiz_id = 1;
}
```

**Features:**
- Total attempt count
- Success rate calculation
- Average completion time
- Performance analytics

### Data Models

#### Quiz
```protobuf
message Quiz {
    string id = 1;
    string section_id = 2;
    string question = 3;
    map<string, string> options = 4;
    string correct_answer = 5;
    string explanation = 6;
    string difficulty = 7;
    google.protobuf.Timestamp created_at = 8;
}
```

#### QuizResponse
```protobuf
message QuizResponse {
    string id = 1;
    string user_id = 2;
    string quiz_id = 3;
    string selected_option = 4;
    bool is_correct = 5;
    google.protobuf.Timestamp answered_at = 6;
}
```

#### QuizStatistics
```protobuf
message QuizStatistics {
    string quiz_id = 1;
    int32 total_attempts = 2;
    int32 correct_attempts = 3;
    float success_rate = 4;
    float average_time = 5;
}
```

## 🛠️ Setup and Installation

### Prerequisites

- Go 1.23 or higher
- PostgreSQL 12+
- Protocol Buffers compiler
- Docker (for containerized deployment)

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
GRPC_PORT=50056
```

### Database Setup

1. **Create the database:**
   ```sql
   CREATE DATABASE userdb;
   ```

2. **Create the tables:**
   ```sql
   -- Quizzes table
   CREATE TABLE quizzes (
       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       section_id VARCHAR(255) NOT NULL,
       question TEXT NOT NULL,
       options JSONB NOT NULL,
       correct_answer VARCHAR(255) NOT NULL,
       explanation TEXT,
       difficulty VARCHAR(50) DEFAULT 'medium',
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

   -- Quiz responses table
   CREATE TABLE quiz_responses (
       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       user_id UUID NOT NULL,
       quiz_id UUID NOT NULL REFERENCES quizzes(id) ON DELETE CASCADE,
       selected_option VARCHAR(255) NOT NULL,
       is_correct BOOLEAN NOT NULL,
       answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

   -- Performance indexes
   CREATE INDEX idx_quizzes_section_id ON quizzes(section_id);
   CREATE INDEX idx_quiz_responses_user_id ON quiz_responses(user_id);
   CREATE INDEX idx_quiz_responses_quiz_id ON quiz_responses(quiz_id);
   CREATE INDEX idx_quiz_responses_answered_at ON quiz_responses(answered_at);
   ```

### Local Development

1. **Clone and navigate to the service directory:**
   ```bash
   cd services/quiz-services
   ```

2. **Install dependencies:**
   ```bash
   go mod download
   ```

3. **Generate Protocol Buffer files:**
   ```bash
   protoc --go_out=. --go-grpc_out=. proto/quiz.proto
   ```

4. **Run the service:**
   ```bash
   go run main.go
   ```

The service will start on port 50056 (or the port specified in the GRPC_PORT environment variable).

### Docker Deployment

1. **Build the Docker image:**
   ```bash
   docker build -t quiz-service .
   ```

2. **Run with Docker Compose:**
   ```yaml
   version: '3.8'
   services:
     quiz-service:
       build: .
       ports:
         - "50056:50056"
       environment:
         - DB_HOST=postgres
         - DB_PORT=5432
         - DB_USER=postgres
         - DB_PASSWORD=password
         - DB_NAME=userdb
       depends_on:
         - postgres

     postgres:
       image: postgres:13
       environment:
         - POSTGRES_DB=userdb
         - POSTGRES_USER=postgres
         - POSTGRES_PASSWORD=password
       volumes:
         - postgres_data:/var/lib/postgresql/data

   volumes:
     postgres_data:
   ```

## 🎯 Quiz Management Workflows

### Creating Educational Content
1. **Design Quiz Structure**: Define sections and difficulty levels
2. **Create Quizzes**: Use CreateQuiz with multiple-choice options
3. **Add Explanations**: Include educational explanations for answers
4. **Organize by Sections**: Group related quizzes by educational topics

### Student Assessment Flow
1. **Browse Quizzes**: Get quizzes by section or individual quiz
2. **Submit Answers**: Use SubmitQuizAnswer for user responses
3. **Track Progress**: Monitor user performance and history
4. **Analyze Performance**: Review statistics and success rates

### Analytics and Reporting
1. **Quiz Performance**: Get statistics for individual quizzes
2. **User Progress**: Track individual user quiz history
3. **Success Metrics**: Calculate success rates and timing
4. **Educational Insights**: Identify difficult questions and topics

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
- `400`: Bad Request (validation errors, missing fields)
- `404`: Quiz or response not found
- `500`: Internal server error (database issues)
- `422`: Unprocessable Entity (invalid quiz data)

### Validation Rules
- **Quiz ID**: Must be valid UUID format
- **Section ID**: Required, non-empty string
- **Question**: Required, non-empty text
- **Options**: Must contain at least 2 options
- **Correct Answer**: Must match one of the provided options
- **User ID**: Must be valid UUID format

## 📊 Performance Optimization

### Database Optimization
- **Indexing Strategy**: Optimized indexes on frequently queried fields
- **Connection Management**: Efficient database connection handling
- **Query Optimization**: Optimized SQL queries for large datasets
- **JSON Handling**: Efficient JSONB storage for quiz options

### Scalability Features
- **Pagination**: Built-in pagination for large result sets
- **Stateless Design**: Horizontally scalable service architecture
- **Connection Pooling**: Efficient database connection management
- **Lightweight Responses**: Optimized data transfer

## 🔗 Integration Points

### Educational Platform Integration
- **Learning Management Systems**: Integration with LMS platforms
- **Progress Tracking**: Coordination with user progress services
- **Content Management**: Integration with educational content systems
- **Analytics Platforms**: Data export for educational analytics

### API Gateway Integration
- **HTTP/gRPC Bridge**: REST API exposure through gateway
- **Authentication**: User authentication and authorization
- **Rate Limiting**: Request throttling and quota management
- **Monitoring**: Request tracking and performance monitoring

## 📊 Monitoring and Logging

### Metrics
- Quiz creation and update rates
- Answer submission frequency
- User engagement analytics
- Database query performance
- Error rates and response times

### Logging
- Structured logging with request context
- Quiz operation audit trails
- Performance monitoring
- Error tracking and alerting

### Health Checks
- Database connectivity monitoring
- Service availability checks
- Resource utilization tracking
- Performance threshold monitoring

## 🔒 Security Considerations

### Data Protection
- Input validation and sanitization
- SQL injection prevention
- UUID-based secure identifiers
- Audit trails for all operations

### Access Control
- User-based quiz access control
- Secure service-to-service communication
- Data privacy compliance
- Educational data protection

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
- Concurrent user simulation
- Response time validation

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
- Include educational context in quiz examples

## 🐛 Troubleshooting

### Common Issues

**Service won't start:**
- Check database connectivity
- Verify environment variables
- Ensure PostgreSQL is running
- Check port availability (50056)

**Database connection errors:**
- Verify PostgreSQL service status
- Check database credentials
- Ensure database exists
- Review connection string format

**Quiz creation failures:**
- Validate quiz data format
- Check required fields
- Verify options format
- Ensure correct answer matches options

**Performance issues:**
- Monitor database query performance
- Check index usage
- Review connection pool settings
- Analyze query execution plans

### Debug Mode
Enable debug logging:
```bash
export LOG_LEVEL=debug
go run main.go
```

## 📚 Educational Use Cases

### Learning Management
- **Course Assessments**: Create quizzes for course evaluation
- **Knowledge Checks**: Quick knowledge validation quizzes
- **Progress Tracking**: Monitor student learning progress
- **Adaptive Learning**: Difficulty-based quiz progression

### Analytics and Insights
- **Performance Analysis**: Identify learning gaps
- **Content Effectiveness**: Measure quiz effectiveness
- **Student Engagement**: Track participation metrics
- **Educational Outcomes**: Measure learning success

## 📄 License

This project is part of the micro-services-application ecosystem. Please refer to the main project license for terms and conditions.

---

**Service Port**: 50056  
**Protocol**: gRPC  
**Health Check**: `/health` (when HTTP gateway is enabled)  
**Metrics**: `/metrics` (when monitoring is enabled)