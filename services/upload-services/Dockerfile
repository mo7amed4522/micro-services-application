FROM golang:1.23-alpine AS builder

# Install build dependencies
RUN apk add --no-cache git

WORKDIR /app

# Copy go mod files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy project files
COPY . .

# Build the application
RUN CGO_ENABLED=0 GOOS=linux go build -o upload-service .

# Final minimal image
FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /root/

# Copy the binary
COPY --from=builder /app/upload-service .

# Copy .env file
COPY --from=builder /app/.env .

EXPOSE 50061

CMD ["./upload-service"]
