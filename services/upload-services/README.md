# Upload Service

A gRPC-based microservice for handling file uploads, storage, and management in the Luxor educational platform. This service provides secure file upload capabilities with AWS S3 integration and PostgreSQL metadata storage.

## Overview

The Upload Service manages file operations including uploading, deleting, and generating secure URLs for files. It supports image and video uploads with validation, stores metadata in PostgreSQL, and uses AWS S3 for reliable file storage with presigned URL generation for secure access.

## Features

### 📁 File Management
- Upload files (images and videos) to AWS S3
- Delete files from both S3 and database
- Generate presigned URLs for secure file access
- File type validation and content type verification
- Automatic file key generation with UUID

### 🔒 Security & Validation
- User ID validation for all operations
- File type restrictions (images and videos only)
- Content type validation
- Private S3 bucket access with presigned URLs
- Secure file key management

### 📊 Metadata Management
- PostgreSQL database for file metadata storage
- Track file information: size, type, upload time
- User association and file ownership
- Soft delete functionality with timestamps

### ☁️ Cloud Storage
- AWS S3 integration for scalable file storage
- Configurable bucket and region settings
- Support for AWS credentials or IAM roles
- Automatic table creation and database setup

## Technical Architecture

### Technology Stack
- **Language**: Go 1.23+
- **Framework**: gRPC with Protocol Buffers
- **Database**: PostgreSQL
- **Cloud Storage**: AWS S3
- **Dependencies**:
  - `google.golang.org/grpc` - gRPC framework
  - `github.com/aws/aws-sdk-go` - AWS SDK for S3 operations
  - `github.com/lib/pq` - PostgreSQL driver
  - `github.com/google/uuid` - UUID generation

### Database Schema
The service uses PostgreSQL with the following table:

```sql
CREATE TABLE uploads (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    file_key VARCHAR(500) NOT NULL UNIQUE,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    file_url TEXT NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);
```

## API Documentation

### gRPC Service Methods

#### 1. UploadFile
Upload a file to S3 and store metadata in the database.

```protobuf
rpc UploadFile(UploadFileRequest) returns (UploadFileResponse);
```

**Request:**
- `file_data` (bytes, required): Binary file data
- `file_name` (string, required): Original filename
- `content_type` (string, required): MIME type of the file
- `user_id` (string, required): User identifier
- `file_type` (FileType, required): FILE_TYPE_IMAGE or FILE_TYPE_VIDEO

**Response:**
- `file_url` (string): S3 file URL
- `file_key` (string): Unique file identifier
- `file_id` (string): Database record ID
- `file_size` (int64): File size in bytes
- `uploaded_at` (string): Upload timestamp

**Supported File Types:**
- **Images**: JPEG, PNG, GIF, WebP
- **Videos**: MP4, AVI, MOV, WebM

#### 2. DeleteFile
Delete a file from S3 and mark as deleted in the database.

```protobuf
rpc DeleteFile(DeleteFileRequest) returns (DeleteFileResponse);
```

**Request:**
- `file_key` (string, required): File key to delete
- `user_id` (string, required): User identifier for authorization

**Response:**
- `file_key` (string): Deleted file key
- `deleted_at` (string): Deletion timestamp

#### 3. GetFileURL
Generate a presigned URL for secure file access.

```protobuf
rpc GetFileURL(GetFileURLRequest) returns (GetFileURLResponse);
```

**Request:**
- `file_key` (string, required): File key
- `expires_in` (int32, optional): URL expiration time in seconds (default: 3600)

**Response:**
- `signed_url` (string): Presigned S3 URL
- `expires_at` (string): URL expiration timestamp

## Setup and Installation

### Prerequisites
- Go 1.23 or higher
- PostgreSQL database
- AWS S3 bucket and credentials
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

# AWS S3 Configuration
AWS_REGION=us-east-1
AWS_S3_BUCKET=your-bucket-name
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key

# Server Configuration
PORT=50061

# Service Configuration
UPLOAD_SERVICE_ADDR=localhost:50061
```

### AWS S3 Setup

1. **Create an S3 bucket:**
```bash
aws s3 mb s3://your-bucket-name --region us-east-1
```

2. **Set bucket policy for private access:**
```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Deny",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::your-bucket-name/*",
            "Condition": {
                "Bool": {
                    "aws:SecureTransport": "false"
                }
            }
        }
    ]
}
```

### Local Development

1. **Clone and navigate to the service directory:**
```bash
cd /Users/standarduser/project/micro-services-application/services/upload-services
```

2. **Install dependencies:**
```bash
go mod download
```

3. **Set up the database:**
   - Ensure PostgreSQL is running
   - Create the database specified in your `.env` file
   - The service will automatically create the required table

4. **Configure AWS credentials:**
   - Set environment variables in `.env` file, or
   - Use AWS CLI: `aws configure`, or
   - Use IAM roles (for EC2/ECS deployment)

5. **Generate Protocol Buffer files (if needed):**
```bash
protoc --go_out=. --go-grpc_out=. proto/upload.proto
```

6. **Run the service:**
```bash
go run main.go
```

The service will start on port 50061.

### Docker Deployment

1. **Build the Docker image:**
```bash
docker build -t upload-service .
```

2. **Run with Docker:**
```bash
docker run -p 50061:50061 --env-file .env upload-service
```

### Docker Compose
The service is typically deployed as part of the larger Luxor microservices ecosystem using Docker Compose.

## File Upload Workflow

1. **Client Request**: User uploads file through the API
2. **Validation**: Service validates user ID, file type, and content
3. **S3 Upload**: File is uploaded to AWS S3 with generated key
4. **Database Storage**: File metadata is stored in PostgreSQL
5. **Response**: Client receives file URL and metadata

## File Access Workflow

1. **URL Request**: Client requests file access with file key
2. **Validation**: Service validates file existence and user permissions
3. **Presigned URL**: AWS S3 presigned URL is generated
4. **Secure Access**: Client can access file using the temporary URL

## Supported File Types

### Images (FileType.IMAGE)
- `image/jpeg` - JPEG images
- `image/png` - PNG images
- `image/gif` - GIF images
- `image/webp` - WebP images

### Videos (FileType.VIDEO)
- `video/mp4` - MP4 videos
- `video/avi` - AVI videos
- `video/quicktime` - MOV videos
- `video/webm` - WebM videos

## Error Handling

The service provides comprehensive error handling with:
- HTTP-style status codes (400, 404, 500, etc.)
- Detailed error messages and context
- Validation error details
- Timestamp information for debugging

### Common Error Scenarios
- **400 Bad Request**: Missing user ID, invalid file type
- **404 Not Found**: File key not found
- **500 Internal Server Error**: S3 upload failure, database errors

## Security Considerations

### File Security
- Private S3 bucket with no public access
- Presigned URLs with configurable expiration
- File type validation to prevent malicious uploads
- User-based file ownership and access control

### Data Protection
- Secure file key generation using UUIDs
- Content type validation
- File size monitoring and limits
- Audit trail with upload and deletion timestamps

## Integration

This service integrates with:
- **User Service**: For user validation and authentication
- **API Gateway**: For external API access and rate limiting
- **Chat Service**: For file attachments in messages
- **Pharaoh/Quiz Services**: For educational content media

## Monitoring and Logging

- Structured logging for all operations
- S3 operation monitoring
- Database connection health checks
- File upload/download metrics
- Error tracking and alerting

## Performance Considerations

### Optimization
- Efficient file streaming for large uploads
- Connection pooling for database operations
- AWS SDK retry mechanisms
- Presigned URL caching strategies

### Scalability
- Stateless service design for horizontal scaling
- S3 for unlimited storage capacity
- Database indexing on file_key and user_id
- Load balancing support

## Backup and Recovery

### S3 Backup
- Enable S3 versioning for file recovery
- Cross-region replication for disaster recovery
- Lifecycle policies for cost optimization

### Database Backup
- Regular PostgreSQL backups
- Point-in-time recovery capabilities
- Metadata restoration procedures

## Contributing

1. Follow Go coding standards and best practices
2. Add tests for new functionality
3. Update Protocol Buffer definitions when needed
4. Maintain backward compatibility
5. Document API changes and new features
6. Test with actual S3 integration

## Troubleshooting

### Common Issues

**S3 Connection Issues:**
```bash
# Test AWS credentials
aws s3 ls s3://your-bucket-name
```

**Database Connection:**
```bash
# Test PostgreSQL connection
psql -h localhost -p 5432 -U postgres -d userdb
```

**File Upload Failures:**
- Check file size limits
- Verify content type mapping
- Ensure S3 bucket permissions
- Validate AWS credentials

## License

Part of the Luxor Educational Platform - All rights reserved.