# Card Services

A gRPC-based microservice for managing payment cards and processing Stripe payments in the Luxor application ecosystem.

## Overview

The Card Service provides comprehensive card management functionality including card creation, validation, status management, and payment processing through Stripe integration. It communicates with other microservices via gRPC and uses PostgreSQL for data persistence.

## Features

- **Card Management**: Create, read, update, and delete payment cards
- **Card Validation**: Validate card data and ensure data integrity
- **Status Management**: Track and update card status (active, inactive, blocked, etc.)
- **User Association**: Link cards to specific users
- **Search Functionality**: Search cards by various criteria
- **Stripe Integration**: Process payments through Stripe API
- **Security**: Encrypted card data storage and JWT-based authentication
- **Database Integration**: PostgreSQL database with connection pooling

## Technology Stack

- **Language**: Go 1.23+
- **Framework**: gRPC
- **Database**: PostgreSQL
- **Payment Processing**: Stripe API
- **Authentication**: JWT tokens
- **Containerization**: Docker
- **Dependencies**:
  - `google.golang.org/grpc` - gRPC framework
  - `github.com/lib/pq` - PostgreSQL driver
  - `github.com/stripe/stripe-go/v79` - Stripe Go SDK
  - `github.com/google/uuid` - UUID generation

## API Endpoints

The service exposes the following gRPC methods:

### Card Operations
- `GetCardByID` - Retrieve a card by its ID
- `CreateNewCard` - Create a new payment card
- `UpdateCardData` - Update existing card information
- `DeleteCardData` - Delete a card
- `UpdateCardStatus` - Change card status

### Search & Query
- `GetCardByUserID` - Get all cards for a specific user
- `SearchCard` - Search cards by criteria
- `FindCardByStatus` - Find cards by status

### Payment Processing
- `StripePayment` - Process payments through Stripe

## Environment Variables

Create a `.env` file with the following variables:

```env
# Database Configuration
DB_HOST=postgres
DB_PORT=5432
DB_USER=postgres
DB_PASSWORD=your_password
DB_NAME=userdb

# Service Configuration
PORT=50053
USER_SERVICE_ADDR=user-service:50051

# Security
ENCRYPTION_KEY=your_encryption_key_here
JWT_SECRET=your_jwt_secret_here

# Stripe Configuration
STRIPE_SECRET_KEY=sk_test_your_stripe_secret_key_here
STRIPE_PUBLISHABLE_KEY=pk_test_your_stripe_publishable_key_here
```

## Setup & Installation

### Prerequisites
- Go 1.23 or higher
- PostgreSQL database
- Stripe account (for payment processing)

### Local Development

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd micro-services-application/services/card-services
   ```

2. **Install dependencies**:
   ```bash
   go mod download
   ```

3. **Set up environment variables**:
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

4. **Run the service**:
   ```bash
   go run main.go
   ```

The service will start on port 50053 by default.

### Docker Deployment

1. **Build the Docker image**:
   ```bash
   docker build -t card-service .
   ```

2. **Run with Docker**:
   ```bash
   docker run -p 50053:50053 --env-file .env card-service
   ```

### Docker Compose

The service is configured to work with the main application's docker-compose.yml:

```bash
# From the root directory
docker-compose up card-service
```

## Database Schema

The service uses PostgreSQL with the following card-related tables:

- **cards**: Main card information table
  - id (UUID, Primary Key)
  - userId (UUID, Foreign Key)
  - cardNumber (Encrypted)
  - cardHolderName
  - expirationDate
  - cvv (Encrypted)
  - status
  - cardType
  - balance
  - createAt
  - updateAt

## Security Features

- **Data Encryption**: Sensitive card data (card number, CVV) is encrypted at rest
- **JWT Authentication**: All requests require valid JWT tokens
- **Input Validation**: Comprehensive validation of all input data
- **Secure Communication**: gRPC with TLS support

## Integration with Other Services

- **User Service**: Validates user existence and authentication
- **Gateway Service**: Receives HTTP requests and forwards to this gRPC service
- **Transaction Service**: Processes payment transactions

## Monitoring & Logging

- Structured logging with contextual information
- gRPC reflection enabled for debugging
- Graceful shutdown handling
- Health check endpoints

## Development

### Project Structure