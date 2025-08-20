# 🏦 Luxor-System - Microservices Banking Platform

![Go](https://img.shields.io/badge/Go-00ADD8?style=for-the-badge&logo=go&logoColor=white)
![NestJS](https://img.shields.io/badge/NestJS-E0234E?style=for-the-badge&logo=nestjs&logoColor=white)
![GraphQL](https://img.shields.io/badge/GraphQL-E10098?style=for-the-badge&logo=graphql&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![gRPC](https://img.shields.io/badge/gRPC-4285F4?style=for-the-badge&logo=grpc&logoColor=white)
![Stripe](https://img.shields.io/badge/Stripe-008CDD?style=for-the-badge&logo=stripe&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![Swift](https://img.shields.io/badge/Swift-FA7343?style=for-the-badge&logo=swift&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Vue.js](https://img.shields.io/badge/Vue.js-42b883?style=for-the-badge&logo=vue.js&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![pgAdmin](https://img.shields.io/badge/pgAdmin-505266?style=for-the-badge&logo=pgadmin4&logoColor=white)
![Microservices](https://img.shields.io/badge/Microservices-FF6F61?style=for-the-badge&logo=microdot&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white)


## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

My-App is a modern, scalable microservices-based banking platform that provides comprehensive user management, secure card operations, and payment processing capabilities. Built with cutting-edge technologies, it offers a robust foundation for financial applications with enterprise-grade security and performance.

## 🏗️ Architecture

The application follows a microservices architecture pattern with the following components:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   GraphQL       │    │   PostgreSQL    │    │   Postman       │
│   Gateway       │◄──►│   Database      │    │   Collection    │
│   (NestJS)      │    │                 │    │                 │
└─────────┬───────┘    └─────────────────┘    └─────────────────┘
          │
          ▼
┌─────────────────┬─────────────────┬─────────────────┐
│   User Service  │  Auth Service   │  Card Service   │
│   (Go + gRPC)   │  (Go + gRPC)    │  (Go + gRPC)    │
│                 │                 │                 │
│ • User CRUD     │ • Firebase Auth │ • Card CRUD     │
│ • JWT Tokens    │ • Token Mgmt    │ • Stripe Integ  │
│ • Validation    │ • OAuth         │ • Encryption    │
└─────────────────┴─────────────────┴─────────────────┘
```

## 🛠️ Technologies Used

### Backend Services
- **Go 1.23+** - High-performance backend services
- **gRPC** - Inter-service communication
- **Protocol Buffers** - Data serialization

### API Gateway
- **NestJS** - Node.js framework for scalable applications
- **GraphQL** - Query language and runtime
- **Apollo Server** - GraphQL server implementation

### Database
- **PostgreSQL 17** - Primary database with ACID compliance
- **UUID** - Primary keys for enhanced security
- **Encryption** - AES-GCM for sensitive card data

### Authentication & Security
- **Firebase Authentication** - OAuth and social login
- **JWT Tokens** - Stateless authentication
- **bcrypt** - Password hashing
- **AES-GCM Encryption** - Card data protection

### Payment Processing
- **Stripe API** - Secure payment processing
- **Payment Intents** - Modern payment flow

### DevOps & Deployment
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration
- **Health Checks** - Service monitoring

### Development Tools
- **Postman** - API testing and documentation
- **ESLint** - Code quality for TypeScript
- **Prettier** - Code formatting

## ✨ Features

### 👤 User Management
- ✅ User registration and authentication
- ✅ Email verification with codes
- ✅ Password reset functionality
- ✅ Profile management (CRUD operations)
- ✅ Role-based access control
- ✅ JWT token management

### 🔐 Authentication
- ✅ Firebase OAuth integration
- ✅ Google/Facebook social login
- ✅ Multi-provider authentication
- ✅ Secure token refresh

### 💳 Card Management
- ✅ Secure card creation and storage
- ✅ AES-GCM encryption for sensitive data
- ✅ Card status management (Active/Inactive)
- ✅ Balance tracking
- ✅ Card search and filtering
- ✅ Pagination support

### 💰 Payment Processing
- ✅ Stripe payment integration
- ✅ Payment intent creation
- ✅ Secure payment processing
- ✅ Transaction history

### 🔒 Security Features
- ✅ End-to-end encryption
- ✅ Masked card numbers
- ✅ JWT-based authentication
- ✅ Input validation and sanitization
- ✅ SQL injection prevention

## 📋 Prerequisites

Before running this application, make sure you have the following installed:

- **Docker** (v20.0+) and **Docker Compose** (v2.0+)
- **Go** (v1.23+) - for local development
- **Node.js** (v18+) and **npm** - for gateway development
- **PostgreSQL** (v17+) - if running without Docker
- **Git** - for version control

### External Services
- **Stripe Account** - for payment processing
- **Firebase Project** - for authentication

## 🚀 Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd my-app
```

### 2. Environment Configuration

Create environment files for each service:

#### Card Service Environment
```bash
# services/card-services/.env
STRIPE_SECRET_KEY=sk_test_your_stripe_secret_key
ENCRYPTION_KEY=your_32_byte_encryption_key_here
JWT_SECRET=your_jwt_secret_key_here
```

#### Auth Service Environment
```bash
# services/auth-services/.env
FIREBASE_CREDENTIALS_PATH=./firebase-credentials.json
JWT_SECRET=your_jwt_secret_key_here
```

### 3. Firebase Setup
1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Enable Authentication with desired providers
3. Download service account credentials
4. Place `firebase-credentials.json` in `services/auth-services/`

### 4. Stripe Setup
1. Create a Stripe account at [Stripe Dashboard](https://dashboard.stripe.com/)
2. Get your test API keys
3. Add the secret key to card service environment

## ⚙️ Configuration

### Database Configuration
The application uses PostgreSQL with the following default settings:
- **Host**: localhost (postgres in Docker)
- **Port**: 5420 (external), 5432 (internal)
- **Database**: userdb
- **Username**: postgres
- **Password**: 2521

### Service Ports
- **Gateway (GraphQL)**: 3000
- **User Service**: 50051
- **Auth Service**: 50052
- **Card Service**: 50053
- **PostgreSQL**: 5420

## 🏃‍♂️ Running the Application

### Using Docker Compose (Recommended)

1. **Start all services**:
```bash
docker-compose up -d
```

2. **View logs**:
```bash
docker-compose logs -f
```

3. **Stop services**:
```bash
docker-compose down
```

### Manual Development Setup

1. **Start PostgreSQL**:
```bash
docker run -d \
  --name postgres \
  -e POSTGRES_DB=userdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=2521 \
  -p 5420:5432 \
  postgres:17-alpine
```

2. **Run User Service**:
```bash
cd services/user-services
go mod tidy
go run main.go
```

3. **Run Auth Service**:
```bash
cd services/auth-services
go mod tidy
go run main.go
```

4. **Run Card Service**:
```bash
cd services/card-services
go mod tidy
go run main.go
```

5. **Run Gateway**:
```bash
cd getway
npm install
npm run start:dev
```

## 📚 API Documentation

### GraphQL Playground
Once the application is running, access the GraphQL playground at:
```
http://localhost:3000/graphql
```

### Postman Collection
Import the provided `postman-file.json` for comprehensive API testing:

#### Available Endpoints:

**Authentication**
- Firebase Authentication
- Token validation

**User Operations**
- Get user by email
- Create new user
- Update user profile
- Delete user
- Login user
- Forget password
- Verify code
- Reset password

**Card Operations**
- Get card by ID
- Get cards by user
- Create new card
- Update card
- Update card status
- Delete card
- Search cards
- Find cards by status
- Process Stripe payment

### Sample GraphQL Queries

#### Create User
```graphql
mutation CreateUser($input: CreateUserInput!) {
  createUser(input: $input) {
    statusCode
    message
    user {
      id
      fullName
      email
      role
    }
  }
}
```

#### Create Card
```graphql
mutation CreateCard($input: CreateCardInput!) {
  createCard(input: $input) {
    statusCode
    message
    card {
      id
      cardNumber
      cardHolderName
      status
      balance
    }
  }
}
```

## 🧪 Testing

### Unit Tests
```bash
# Gateway tests
cd getway
npm test

# Go service tests
cd services/user-services
go test ./...
```

### Integration Tests
```bash
# End-to-end tests
cd getway
npm run test:e2e
```

### API Testing with Postman
1. Import `postman-file.json`
2. Set environment variables:
   - `accessToken`: JWT token from login
   - `userId`: User ID for operations
   - `cardId`: Card ID for card operations
3. Run the collection tests

## 📁 Project Structure

```
my-app/
├── 📁 db/                          # Database scripts
│   ├── db.sql                      # Database schema
│   └── password.txt                # Database credentials
├── 📁 getway/                      # NestJS GraphQL Gateway
│   ├── 📁 src/
│   │   ├── 📁 auth/               # Auth module
│   │   ├── 📁 card/               # Card module
│   │   ├── 📁 user/               # User module
│   │   └── main.ts                # Application entry point
│   ├── 📁 proto/                  # Protocol buffer definitions
│   ├── package.json               # Node.js dependencies
│   └── Dockerfile                 # Gateway container
├── 📁 proto/                       # Shared protocol buffers
│   ├── auth.proto                 # Auth service definitions
│   ├── card.proto                 # Card service definitions
│   └── user.proto                 # User service definitions
├── 📁 services/                    # Go microservices
│   ├── 📁 auth-services/          # Firebase authentication
│   ├── 📁 card-services/          # Card management & Stripe
│   └── 📁 user-services/          # User management
├── docker-compose.yml              # Multi-container orchestration
├── postman-file.json              # API testing collection
└── README.md                      # This documentation
```

## 🔧 Development

### Adding New Features
1. Define gRPC service in `.proto` files
2. Generate Go code: `protoc --go_out=. --go-grpc_out=. *.proto`
3. Implement service logic in Go
4. Add GraphQL resolvers in NestJS gateway
5. Update Postman collection for testing

### Code Quality
```bash
# Format Go code
go fmt ./...

# Lint TypeScript code
cd getway
npm run lint
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review the Postman collection for API examples

## 🚀 Deployment

### Production Deployment
1. Update environment variables for production
2. Use production Stripe keys
3. Configure production Firebase project
4. Set up SSL certificates
5. Deploy using Docker Compose or Kubernetes

### Health Checks
The application includes health checks for all services:
- Database connectivity
- Service availability
- gRPC connection status

---

**Built with ❤️ using modern microservices architecture**
