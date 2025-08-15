# Auth Services Makefile
# Go-based gRPC microservice for Firebase authentication

# Variables
SERVICE_NAME := auth-service
GO_MODULE := auth-services
PORT := 50052
DOCKER_IMAGE := $(SERVICE_NAME):latest
PROTO_DIR := proto
PROTO_FILES := $(wildcard $(PROTO_DIR)/*.proto)
GO_FILES := $(shell find . -name '*.go' -not -path './vendor/*')

# Go build variables
GOOS := linux
GOARCH := amd64
CGO_ENABLED := 0

# Colors for output
RED := \033[31m
GREEN := \033[32m
YELLOW := \033[33m
BLUE := \033[34m
RESET := \033[0m

.PHONY: help build run test clean docker-build docker-run proto-gen deps lint format check-env stop

# Default target
all: build

## Help
help: ## Show this help message
	@echo "$(BLUE)Auth Services - Available Commands:$(RESET)"
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "$(GREEN)%-20s$(RESET) %s\n", $$1, $$2}'

## Development
build: deps ## Build the auth service binary
	@echo "$(BLUE)Building $(SERVICE_NAME)...$(RESET)"
	@CGO_ENABLED=$(CGO_ENABLED) GOOS=$(GOOS) GOARCH=$(GOARCH) go build -ldflags "-s -w" -o $(SERVICE_NAME) .
	@echo "$(GREEN)✓ Build completed: $(SERVICE_NAME)$(RESET)"

build-local: deps ## Build for local development (current OS)
	@echo "$(BLUE)Building $(SERVICE_NAME) for local development...$(RESET)"
	@go build -o $(SERVICE_NAME) .
	@echo "$(GREEN)✓ Local build completed: $(SERVICE_NAME)$(RESET)"

run: build-local check-env ## Run the service locally
	@echo "$(BLUE)Starting $(SERVICE_NAME) on port $(PORT)...$(RESET)"
	@./$(SERVICE_NAME)

run-dev: check-env ## Run with go run for development
	@echo "$(BLUE)Running $(SERVICE_NAME) in development mode...$(RESET)"
	@go run main.go

stop: ## Stop the running service
	@echo "$(YELLOW)Stopping $(SERVICE_NAME)...$(RESET)"
	@pkill -f $(SERVICE_NAME) || true
	@echo "$(GREEN)✓ Service stopped$(RESET)"

## Dependencies
deps: ## Download and tidy dependencies
	@echo "$(BLUE)Downloading dependencies...$(RESET)"
	@go mod download
	@go mod tidy
	@echo "$(GREEN)✓ Dependencies updated$(RESET)"

deps-update: ## Update all dependencies
	@echo "$(BLUE)Updating dependencies...$(RESET)"
	@go get -u ./...
	@go mod tidy
	@echo "$(GREEN)✓ Dependencies updated$(RESET)"

## Protocol Buffers
proto-gen: ## Generate Go code from proto files
	@echo "$(BLUE)Generating protobuf code...$(RESET)"
	@protoc --go_out=. --go_opt=paths=source_relative \
		--go-grpc_out=. --go-grpc_opt=paths=source_relative \
		$(PROTO_FILES)
	@echo "$(GREEN)✓ Protobuf code generated$(RESET)"

proto-clean: ## Clean generated proto files
	@echo "$(YELLOW)Cleaning generated proto files...$(RESET)"
	@find $(PROTO_DIR) -name "*.pb.go" -delete
	@echo "$(GREEN)✓ Proto files cleaned$(RESET)"

proto-rebuild: proto-clean proto-gen ## Clean and regenerate proto files

## Testing
test: ## Run tests
	@echo "$(BLUE)Running tests...$(RESET)"
	@go test -v ./...

test-coverage: ## Run tests with coverage
	@echo "$(BLUE)Running tests with coverage...$(RESET)"
	@go test -v -coverprofile=coverage.out ./...
	@go tool cover -html=coverage.out -o coverage.html
	@echo "$(GREEN)✓ Coverage report generated: coverage.html$(RESET)"

test-race: ## Run tests with race detection
	@echo "$(BLUE)Running tests with race detection...$(RESET)"
	@go test -race -v ./...

benchmark: ## Run benchmarks
	@echo "$(BLUE)Running benchmarks...$(RESET)"
	@go test -bench=. -benchmem ./...

## Code Quality
lint: ## Run linter
	@echo "$(BLUE)Running linter...$(RESET)"
	@if command -v golangci-lint >/dev/null 2>&1; then \
		golangci-lint run; \
	else \
		echo "$(YELLOW)golangci-lint not installed. Install with: go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest$(RESET)"; \
		go vet ./...; \
	fi

format: ## Format code
	@echo "$(BLUE)Formatting code...$(RESET)"
	@go fmt ./...
	@if command -v goimports >/dev/null 2>&1; then \
		goimports -w .; \
	else \
		echo "$(YELLOW)goimports not installed. Install with: go install golang.org/x/tools/cmd/goimports@latest$(RESET)"; \
	fi
	@echo "$(GREEN)✓ Code formatted$(RESET)"

vet: ## Run go vet
	@echo "$(BLUE)Running go vet...$(RESET)"
	@go vet ./...
	@echo "$(GREEN)✓ Vet completed$(RESET)"

## Docker
docker-build: ## Build Docker image
	@echo "$(BLUE)Building Docker image: $(DOCKER_IMAGE)...$(RESET)"
	@cd ../.. && docker build -f services/auth-services/Dockerfile -t $(DOCKER_IMAGE) .
	@echo "$(GREEN)✓ Docker image built: $(DOCKER_IMAGE)$(RESET)"

docker-run: docker-build ## Run service in Docker container
	@echo "$(BLUE)Running $(SERVICE_NAME) in Docker...$(RESET)"
	@docker run --rm -p $(PORT):$(PORT) \
		--env-file .env \
		--name $(SERVICE_NAME) \
		$(DOCKER_IMAGE)

docker-run-detached: docker-build ## Run service in Docker container (detached)
	@echo "$(BLUE)Running $(SERVICE_NAME) in Docker (detached)...$(RESET)"
	@docker run -d -p $(PORT):$(PORT) \
		--env-file .env \
		--name $(SERVICE_NAME) \
		$(DOCKER_IMAGE)
	@echo "$(GREEN)✓ Container started: $(SERVICE_NAME)$(RESET)"

docker-stop: ## Stop Docker container
	@echo "$(YELLOW)Stopping Docker container...$(RESET)"
	@docker stop $(SERVICE_NAME) || true
	@docker rm $(SERVICE_NAME) || true
	@echo "$(GREEN)✓ Container stopped$(RESET)"

docker-logs: ## Show Docker container logs
	@docker logs -f $(SERVICE_NAME)

docker-shell: ## Open shell in running container
	@docker exec -it $(SERVICE_NAME) /bin/sh

docker-clean: ## Clean Docker images and containers
	@echo "$(YELLOW)Cleaning Docker resources...$(RESET)"
	@docker stop $(SERVICE_NAME) || true
	@docker rm $(SERVICE_NAME) || true
	@docker rmi $(DOCKER_IMAGE) || true
	@echo "$(GREEN)✓ Docker resources cleaned$(RESET)"

## Environment
check-env: ## Check required environment variables
	@echo "$(BLUE)Checking environment variables...$(RESET)"
	@if [ ! -f .env ]; then \
		echo "$(RED)✗ .env file not found$(RESET)"; \
		echo "$(YELLOW)Create .env file with required variables:$(RESET)"; \
		echo "  FIREBASE_CREDENTIALS_PATH=./my-app-4e7aa-firebase-adminsdk-fbsvc-38f041e31e.json"; \
		echo "  USER_SERVICE_ADDR=localhost:50051"; \
		echo "  JWT_SECRET=your_jwt_secret_here"; \
		echo "  PORT=50052"; \
		exit 1; \
	fi
	@if [ ! -f "$$(grep FIREBASE_CREDENTIALS_PATH .env | cut -d'=' -f2)" ]; then \
		echo "$(RED)✗ Firebase credentials file not found$(RESET)"; \
		echo "$(YELLOW)Ensure Firebase service account key exists$(RESET)"; \
		exit 1; \
	fi
	@echo "$(GREEN)✓ Environment check passed$(RESET)"

env-template: ## Create .env template file
	@echo "$(BLUE)Creating .env template...$(RESET)"
	@echo "# Auth Service Environment Variables" > .env.template
	@echo "FIREBASE_CREDENTIALS_PATH=./my-app-4e7aa-firebase-adminsdk-fbsvc-38f041e31e.json" >> .env.template
	@echo "USER_SERVICE_ADDR=localhost:50051" >> .env.template
	@echo "JWT_SECRET=your_jwt_secret_here" >> .env.template
	@echo "PORT=50052" >> .env.template
	@echo "$(GREEN)✓ Template created: .env.template$(RESET)"

## Utilities
clean: ## Clean build artifacts
	@echo "$(YELLOW)Cleaning build artifacts...$(RESET)"
	@rm -f $(SERVICE_NAME)
	@rm -f coverage.out coverage.html
	@go clean -cache
	@echo "$(GREEN)✓ Clean completed$(RESET)"

install-tools: ## Install development tools
	@echo "$(BLUE)Installing development tools...$(RESET)"
	@go install google.golang.org/protobuf/cmd/protoc-gen-go@latest
	@go install google.golang.org/grpc/cmd/protoc-gen-go-grpc@latest
	@go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest
	@go install golang.org/x/tools/cmd/goimports@latest
	@echo "$(GREEN)✓ Development tools installed$(RESET)"

status: ## Show service status
	@echo "$(BLUE)Service Status:$(RESET)"
	@echo "Service Name: $(SERVICE_NAME)"
	@echo "Port: $(PORT)"
	@echo "Module: $(GO_MODULE)"
	@if pgrep -f $(SERVICE_NAME) > /dev/null; then \
		echo "Status: $(GREEN)Running$(RESET)"; \
	else \
		echo "Status: $(RED)Stopped$(RESET)"; \
	fi
	@if [ -f $(SERVICE_NAME) ]; then \
		echo "Binary: $(GREEN)Built$(RESET)"; \
	else \
		echo "Binary: $(RED)Not built$(RESET)"; \
	fi

## Development Workflow
dev-setup: install-tools deps proto-gen ## Complete development setup
	@echo "$(GREEN)✓ Development environment setup completed$(RESET)"

dev-rebuild: clean proto-rebuild build ## Clean rebuild for development

ci: deps proto-gen vet lint test ## Continuous integration pipeline
	@echo "$(GREEN)✓ CI pipeline completed$(RESET)"

## Quick Commands
quick-start: build-local run ## Quick start for development

quick-test: format vet test ## Quick test pipeline

quick-docker: docker-build docker-run ## Quick Docker deployment