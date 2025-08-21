# User Service gRPC Testing with Postman

This guide explains how to test the User Service gRPC API using the provided Postman collection.

## Prerequisites

1. **Postman Desktop App** (version 9.0 or later) - gRPC support is only available in the desktop version
2. **Go** installed on your system
3. **User Service** running locally

## Setup Instructions

### 1. Start the User Service

First, ensure the User Service is running:

```bash
# Navigate to the user-services directory
cd /Users/standarduser/project/micro-services-application/services/user-services

# Run the service
go run main.go
```

The service will start on `localhost:50051`.

### 2. Import the Postman Collection

1. Open Postman Desktop App
2. Click **Import** in the top left
3. Select **File** tab
4. Choose the `UserService_Postman_Collection.json` file
5. Click **Import**

### 3. Configure gRPC Settings

After importing, you'll see the "User Service gRPC API" collection with organized folders:

- **User Management**: CRUD operations for users
- **Authentication**: Login and token validation
- **Password Management**: Password reset workflow

## Available Endpoints

### User Management

1. **Create New User**
   - Creates a new user account
   - Sample payload includes all required fields

2. **Get User By ID**
   - Retrieves user information by user ID
   - Requires valid user ID

3. **Get User By Email**
   - Retrieves user information by email address
   - Useful for checking if user exists

4. **Update User Data**
   - Updates existing user information
   - Can modify name, email, phone, role

5. **Delete User Data**
   - Removes user from the system
   - Requires user ID

### Authentication

6. **Login User**
   - Authenticates user with email/password
   - Returns access token and user data

7. **Validate Token**
   - Verifies JWT token validity
   - Returns token claims if valid

### Password Management

8. **Forget Password**
   - Initiates password reset process
   - Sends verification code

9. **Verify Code**
   - Validates the verification code
   - Required before password reset

10. **Reset Password**
    - Sets new password after verification
    - Completes the password reset flow

## Testing Workflow

### Complete User Registration and Login Flow

1. **Create New User** - Register a new account
2. **Get User By Email** - Verify user was created
3. **Login User** - Authenticate and get token
4. **Validate Token** - Verify the received token

### Password Reset Flow

1. **Forget Password** - Request password reset
2. **Verify Code** - Validate the verification code
3. **Reset Password** - Set new password
4. **Login User** - Test login with new password

## Sample Data

The collection includes realistic sample data:

- **Email**: `john.doe@example.com`
- **Password**: `SecurePass123!`
- **Full Name**: `John Doe`
- **Phone**: `+1234567890`
- **Country Code**: `+1`
- **Role**: `USER` or `ADMIN`

## Troubleshooting

### Common Issues

1. **Connection Refused**
   - Ensure the gRPC server is running on port 50051
   - Check if the service started without errors

2. **Proto File Not Found**
   - Postman should automatically detect the service methods
   - If not, you may need to import the proto file manually

3. **Invalid Request Format**
   - Ensure you're using the correct message format
   - Check that all required fields are provided

### Server Logs

Monitor the server console for request logs and error messages:

```bash
# The server will log incoming requests
User service is running on port 50051...
```

## Advanced Usage

### Environment Variables

The collection includes variables that you can modify:

- `baseUrl`: Server address (default: `localhost:50051`)
- `service`: Service name (default: `users.UserService`)

### Custom Test Scripts

You can add Postman test scripts to:
- Validate response status codes
- Extract tokens for subsequent requests
- Chain requests together

### Example Test Script

```javascript
// Add this to the "Tests" tab in Postman
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has user data", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData.statusCode).to.eql(200);
    pm.expect(jsonData.message).to.exist;
});
```

## Security Notes

- Use strong passwords in production
- Validate all input data
- Implement proper error handling
- Use HTTPS in production environments
- Store tokens securely

## Support

For issues with the gRPC service implementation, check:
- Server logs for detailed error messages
- Proto file definitions for correct message formats
- Database connectivity if using persistent storage

---

**Note**: This collection is designed for development and testing purposes. Modify the sample data and add proper authentication headers for production use.