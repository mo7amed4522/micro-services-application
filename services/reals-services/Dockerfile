FROM golang:1.21-alpine AS builder

WORKDIR /app

# Install protobuf compiler
RUN apk add --no-cache protobuf-dev

# Copy go mod files
COPY go.mod go.sum ./
RUN go mod download

# Copy proto files from main directory
COPY ../../proto ./proto

# Install protoc-gen-go
RUN go install google.golang.org/protobuf/cmd/protoc-gen-go@latest
RUN go install google.golang.org/grpc/cmd/protoc-gen-go-grpc@latest

# Generate proto files
RUN mkdir -p proto/generated
RUN protoc --go_out=proto/generated --go-grpc_out=proto/generated --proto_path=proto proto/reals.proto

# Copy source code
COPY . .

# Build the application
RUN CGO_ENABLED=0 GOOS=linux go build -o reals-service .

# Final stage
FROM alpine:latest

RUN apk --no-cache add ca-certificates
WORKDIR /root/

COPY --from=builder /app/reals-service .
COPY --from=builder /app/.env .

EXPOSE 50062

CMD ["./reals-service"]
