.PHONY: help build run test clean docker-build docker-run docker-stop proto deps lint security k8s-deploy k8s-delete

# Variables
SERVICE_NAME := user-services
DOCKER_IMAGE := $(SERVICE_NAME):latest
KUBE_NAMESPACE := microservices
PROTO_DIR := proto
GO_FILES := $(shell find . -name '*.go' -not -path './vendor/*')

# Default target
help: ## Show this help message
	@echo 'Usage: make [target]'
	@echo ''
	@echo 'Targets:'
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

# Development
build: ## Build the service binary
	@echo "Building $(SERVICE_NAME)..."
	go build -o bin/$(SERVICE_NAME) .

run: ## Run the service locally
	@echo "Running $(SERVICE_NAME)..."
	go run .

test: ## Run tests
	@echo "Running tests..."
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html

lint: ## Run linter
	@echo "Running linter..."
	golangci-lint run

security: ## Run security scan
	@echo "Running security scan..."
	gosec ./...

deps: ## Download dependencies
	@echo "Downloading dependencies..."
	go mod download
	go mod tidy

clean: ## Clean build artifacts
	@echo "Cleaning..."
	rm -rf bin/
	rm -f coverage.out coverage.html

# Protocol Buffers
proto: ## Generate protobuf code
	@echo "Generating protobuf code..."
	protoc --go_out=. --go_opt=paths=source_relative \
		--go-grpc_out=. --go-grpc_opt=paths=source_relative \
		$(PROTO_DIR)/*.proto

# Docker
docker-build: ## Build Docker image
	@echo "Building Docker image..."
	docker build -t $(DOCKER_IMAGE) .

docker-run: ## Run Docker container
	@echo "Running Docker container..."
	docker-compose up -d

docker-stop: ## Stop Docker container
	@echo "Stopping Docker container..."
	docker-compose down

docker-logs: ## Show Docker logs
	docker-compose logs -f $(SERVICE_NAME)

# Kubernetes
k8s-deploy: ## Deploy to Kubernetes
	@echo "Deploying to Kubernetes..."
	kubectl apply -k k8s/ -n $(KUBE_NAMESPACE)
	kubectl rollout status deployment/$(SERVICE_NAME) -n $(KUBE_NAMESPACE)

k8s-delete: ## Delete from Kubernetes
	@echo "Deleting from Kubernetes..."
	kubectl delete -k k8s/ -n $(KUBE_NAMESPACE)

k8s-logs: ## Show Kubernetes logs
	kubectl logs -f deployment/$(SERVICE_NAME) -n $(KUBE_NAMESPACE)

k8s-status: ## Show Kubernetes status
	kubectl get pods,svc,hpa -n $(KUBE_NAMESPACE) -l app=$(SERVICE_NAME)

# Environment
env-check: ## Check environment variables
	@echo "Checking environment variables..."
	@test -n "$$DB_HOST" || (echo "DB_HOST is not set" && exit 1)
	@test -n "$$DB_PORT" || (echo "DB_PORT is not set" && exit 1)
	@test -n "$$DB_USER" || (echo "DB_USER is not set" && exit 1)
	@test -n "$$DB_PASSWORD" || (echo "DB_PASSWORD is not set" && exit 1)
	@test -n "$$DB_NAME" || (echo "DB_NAME is not set" && exit 1)
	@test -n "$$JWT_SECRET" || (echo "JWT_SECRET is not set" && exit 1)
	@echo "All required environment variables are set"

# CI/CD
ci: deps lint security test ## Run CI pipeline

cd: docker-build ## Run CD pipeline