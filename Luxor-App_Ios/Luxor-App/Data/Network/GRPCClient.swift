//
//  GRPCClient.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import Foundation
import GRPCCore
import GRPCProtobuf
import GRPCNIOTransportHTTP2
import Combine

@available(macOS 15.0, iOS 18.0, watchOS 11.0, tvOS 18.0, visionOS 2.0, *)
class GRPCClientManager {
    
    func login(email: String, password: String) async throws -> Users_LoginUserResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: "127.0.0.1", port: 8080),
                transportSecurity: .plaintext
            )
        ) { client in
            let service = Users_UserService.Client(wrapping: client)
            
            let request = Users_LoginUserRequest.with {
                $0.email = email
                $0.password = password
            }
            
            let response = try await service.loginUser(request)
            return response
        }
    }
    
    func validateToken(_ token: String) async throws -> Users_User {
        // Note: Token validation would need to be implemented in the proto file
        // For now, we'll throw an error indicating this feature is not implemented
        throw GRPCError.unimplemented("Token validation not implemented in proto")
    }
    
    func getUserByEmail(_ email: String) async throws -> User {
        throw GRPCError.unimplemented("HTTP2 transport not available. Add grpc-swift-nio-transport package dependency.")
    }
    
    func createUser(fullName: String, email: String, password: String, countryCode: String, phoneNumber: String) async throws -> User {
        throw GRPCError.unimplemented("HTTP2 transport not available. Add grpc-swift-nio-transport package dependency.")
    }
    
    func forgetPassword(email: String) async throws {
        throw GRPCError.unimplemented("HTTP2 transport not available. Add grpc-swift-nio-transport package dependency.")
    }
    
    func resetPassword(email: String, password: String) async throws {
        throw GRPCError.unimplemented("HTTP2 transport not available. Add grpc-swift-nio-transport package dependency.")
    }
}

// MARK: - Error Handling
extension GRPCClientManager {
    enum GRPCError: Error {
        case connectionFailed
        case invalidResponse
        case networkError(Error)
        case unimplemented(String)
        case clientNotInitialized
        
        var localizedDescription: String {
            switch self {
            case .connectionFailed:
                return "Failed to connect to server"
            case .invalidResponse:
                return "Invalid response from server"
            case .networkError(let error):
                return "Network error: \(error.localizedDescription)"
            case .unimplemented(let message):
                return "Feature not implemented: \(message)"
            case .clientNotInitialized:
                return "gRPC client not initialized"
            }
        }
    }
}

// MARK: - Domain Model Conversion
extension GRPCClientManager {
    private func convertToDomainUser(from grpcUser: Users_User) -> User {
        return User(
            id: grpcUser.id,
            fullName: grpcUser.fullName,
            email: grpcUser.email,
            countryCode: grpcUser.countryCode,
            phoneNumber: grpcUser.phoneNumber,
            role: grpcUser.role,
            verifyCode: grpcUser.verifiyCode,
            createAt: grpcUser.createAt,
            updateAt: grpcUser.updateAt
        )
    }
    
    private func convertToDomainAuthTokens(from response: Users_LoginUserResponse) -> AuthTokens {
        return AuthTokens(
            accessToken: response.accessToken,
            expiresIn: 3600, // Default expiration time since not provided in response
            user: convertToDomainUser(from: response.user)
        )
    }
}
