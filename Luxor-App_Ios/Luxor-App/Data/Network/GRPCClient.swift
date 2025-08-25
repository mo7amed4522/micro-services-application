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

// Add this import to fix the "No such module 'GRPC'" error
// Note: You may need to add the GRPC package dependency to your project
// import GRPC

@available(macOS 15.0, iOS 18.0, watchOS 11.0, tvOS 18.0, visionOS 2.0, *)
class GRPCClientManager {
    
    // MARK: - Service Configuration
    private let authServiceHost = "127.0.0.1"
    private let authServicePort = 8081
    private let userServiceHost = "127.0.0.1"
    private let userServicePort = 8080
    private let uploadServiceHost = "127.0.0.1"
    private let uploadServicePort = 8082 // Upload service port
    
    func login(email: String, password: String) async throws -> Users_LoginUserResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: userServiceHost, port: userServicePort),
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
    
    // MARK: - Authentication Methods
    
    func authenticateWithApple(identityToken: String, authorizationCode: String, fullName: String?, email: String?) async throws -> Auth_SocialAuthResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: authServiceHost, port: authServicePort),
                transportSecurity: .plaintext
            )
        ) { client in
            let service = Auth_AuthService.Client(wrapping: client)
            
            let request = Auth_AppleAuthRequest.with {
                $0.identityToken = identityToken
                $0.authorizationCode = authorizationCode
                if let fullName = fullName {
                    $0.fullName = fullName
                }
                if let email = email {
                    $0.email = email
                }
            }
            
            let response = try await service.authenticateWithApple(request)
            return response
        }
    }
    
    func authenticateWithGoogle(idToken: String, accessToken: String) async throws -> Auth_SocialAuthResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: authServiceHost, port: authServicePort),
                transportSecurity: .plaintext
            )
        ) { client in
            let service = Auth_AuthService.Client(wrapping: client)
            
            let request = Auth_GoogleAuthRequest.with {
                $0.idToken = idToken
                $0.accessToken = accessToken
            }
            
            let response = try await service.authenticateWithGoogle(request)
            return response
        }
    }
    
    func authenticateWithSocial(provider: String, idToken: String, accessToken: String?, authorizationCode: String?, fullName: String?, email: String?) async throws -> Auth_SocialAuthResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: authServiceHost, port: authServicePort),
                transportSecurity: .plaintext
            )
        ) { client in
            let service = Auth_AuthService.Client(wrapping: client)
            
            let request = Auth_SocialAuthRequest.with {
                $0.provider = provider
                $0.idToken = idToken
                if let accessToken = accessToken {
                    $0.accessToken = accessToken
                }
                if let authorizationCode = authorizationCode {
                    $0.authorizationCode = authorizationCode
                }
                if let fullName = fullName {
                    $0.fullName = fullName
                }
                if let email = email {
                    $0.email = email
                }
            }
            
            let response = try await service.authenticateWithSocial(request)
            return response
        }
    }
    
    func validateToken(_ token: String) async throws -> Users_User {
        throw GRPCError.unimplemented("Token validation not implemented in proto")
    }
    func getUserByEmail(_ email: String) async throws -> User {
        throw GRPCError.unimplemented("HTTP2 transport not available. Add grpc-swift-nio-transport package dependency.")
    }
    func createUser(fullName: String, email: String, password: String, countryCode: String, phoneNumber: String) async throws -> User {
        throw GRPCError.unimplemented("HTTP2 transport not available. Add grpc-swift-nio-transport package dependency.")
    }
    func forgetPassword(email: String, type: String) async throws -> Users_ForgetPasswordResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: userServiceHost, port: userServicePort),
                transportSecurity: .plaintext
            )
        ) { client in
            let service = Users_UserService.Client(wrapping: client)
            
            let request = Users_ForgetPasswordRequest.with {
                $0.email = email
                $0.type = type
            }
            
            let response = try await service.forgetPassword(request)
            return response
        }
    }
    
    func verifyCode(email: String, verifyCode: String) async throws -> Users_VerifyCodeResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: userServiceHost, port: userServicePort),
                transportSecurity: .plaintext
            )
        ) { client in
            let service = Users_UserService.Client(wrapping: client)
            
            let request = Users_VerifyCodeRequest.with {
                $0.email = email
                $0.verifyCode = verifyCode
            }
            
            let response = try await service.verifyCode(request)
            return response
        }
    }
    
    func resetPassword(email: String, password: String) async throws -> Users_ResetPasswordResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: userServiceHost, port: userServicePort),
                transportSecurity: .plaintext
            )
        ) { client in
            let service = Users_UserService.Client(wrapping: client)
            
            let request = Users_ResetPasswordRequest.with {
                $0.email = email
                $0.password = password
            }
            
            let response = try await service.resetPassword(request)
            return response
        }
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
            accessToken: response.tokens.accessToken,
            expiresIn: 3600,
            user: convertToDomainUser(from: response.user)
        )
    }
    
    // MARK: - Auth Service Domain Model Conversion
    
    func convertToDomainUser(_ authUser: Auth_UserInfo) -> User {
        return User(
            id: authUser.id,
            fullName: authUser.fullName,
            email: authUser.email,
            countryCode: "", // Not provided in auth response
            phoneNumber: "", // Not provided in auth response
            role: authUser.role,
            verifyCode: "", // Not provided in auth response
            createAt: "", // Not provided in auth response
            updateAt: "" // Not provided in auth response
        )
    }
    
    // Fixed method signature to match the expected return type
    func convertToDomainAuthTokens(_ authTokens: Auth_AuthTokens) -> AuthTokens? {
        return AuthTokens(
            accessToken: authTokens.accessToken,
            expiresIn: Int32(authTokens.expiresIn),
            user: convertToDomainUser(authTokens.user)
        )
    }
}

// MARK: - Upload Service Methods

@available(macOS 15.0, iOS 18.0, watchOS 11.0, tvOS 18.0, visionOS 2.0, *)
class GRPCClientManager {
    
    // MARK: - Service Configuration
    private let authServiceHost = "127.0.0.1"
    private let authServicePort = 8081
    private let userServiceHost = "127.0.0.1"
    private let userServicePort = 8080
    private let uploadServiceHost = "127.0.0.1"
    private let uploadServicePort = 8082
    
    func login(email: String, password: String) async throws -> Users_LoginUserResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: userServiceHost, port: userServicePort),
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
    
    // MARK: - Authentication Methods
    
    func authenticateWithApple(identityToken: String, authorizationCode: String, fullName: String?, email: String?) async throws -> Auth_SocialAuthResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: authServiceHost, port: authServicePort),
                transportSecurity: .plaintext
            )
        ) { client in
            let service = Auth_AuthService.Client(wrapping: client)
            
            let request = Auth_AppleAuthRequest.with {
                $0.identityToken = identityToken
                $0.authorizationCode = authorizationCode
                if let fullName = fullName {
                    $0.fullName = fullName
                }
                if let email = email {
                    $0.email = email
                }
            }
            
            let response = try await service.authenticateWithApple(request)
            return response
        }
    }
    
    func authenticateWithGoogle(idToken: String, accessToken: String) async throws -> Auth_SocialAuthResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: authServiceHost, port: authServicePort),
                transportSecurity: .plaintext
            )
        ) { client in
            let service = Auth_AuthService.Client(wrapping: client)
            
            let request = Auth_GoogleAuthRequest.with {
                $0.idToken = idToken
                $0.accessToken = accessToken
            }
            
            let response = try await service.authenticateWithGoogle(request)
            return response
        }
    }
    
    func authenticateWithSocial(provider: String, idToken: String, accessToken: String?, authorizationCode: String?, fullName: String?, email: String?) async throws -> Auth_SocialAuthResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: authServiceHost, port: authServicePort),
                transportSecurity: .plaintext
            )
        ) { client in
            let service = Auth_AuthService.Client(wrapping: client)
            
            let request = Auth_SocialAuthRequest.with {
                $0.provider = provider
                $0.idToken = idToken
                if let accessToken = accessToken {
                    $0.accessToken = accessToken
                }
                if let authorizationCode = authorizationCode {
                    $0.authorizationCode = authorizationCode
                }
                if let fullName = fullName {
                    $0.fullName = fullName
                }
                if let email = email {
                    $0.email = email
                }
            }
            
            let response = try await service.authenticateWithSocial(request)
            return response
        }
    }
    
    func validateToken(_ token: String) async throws -> Users_User {
        throw GRPCError.unimplemented("Token validation not implemented in proto")
    }
    func getUserByEmail(_ email: String) async throws -> User {
        throw GRPCError.unimplemented("HTTP2 transport not available. Add grpc-swift-nio-transport package dependency.")
    }
    func createUser(fullName: String, email: String, password: String, countryCode: String, phoneNumber: String) async throws -> User {
        throw GRPCError.unimplemented("HTTP2 transport not available. Add grpc-swift-nio-transport package dependency.")
    }
    func forgetPassword(email: String, type: String) async throws -> Users_ForgetPasswordResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: userServiceHost, port: userServicePort),
                transportSecurity: .plaintext
            )
        ) { client in
            let service = Users_UserService.Client(wrapping: client)
            
            let request = Users_ForgetPasswordRequest.with {
                $0.email = email
                $0.type = type
            }
            
            let response = try await service.forgetPassword(request)
            return response
        }
    }
    
    func verifyCode(email: String, verifyCode: String) async throws -> Users_VerifyCodeResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: userServiceHost, port: userServicePort),
                transportSecurity: .plaintext
            )
        ) { client in
            let service = Users_UserService.Client(wrapping: client)
            
            let request = Users_VerifyCodeRequest.with {
                $0.email = email
                $0.verifyCode = verifyCode
            }
            
            let response = try await service.verifyCode(request)
            return response
        }
    }
    
    func resetPassword(email: String, password: String) async throws -> Users_ResetPasswordResponse {
        return try await withGRPCClient(
            transport: .http2NIOPosix(
                target: .dns(host: userServiceHost, port: userServicePort),
                transportSecurity: .plaintext
            )
        ) { client in
            let service = Users_UserService.Client(wrapping: client)
            
            let request = Users_ResetPasswordRequest.with {
                $0.email = email
                $0.password = password
            }
            
            let response = try await service.resetPassword(request)
            return response
        }
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
            accessToken: response.tokens.accessToken,
            expiresIn: 3600,
            user: convertToDomainUser(from: response.user)
        )
    }
    
    // MARK: - Auth Service Domain Model Conversion
    
    func convertToDomainUser(_ authUser: Auth_UserInfo) -> User {
        return User(
            id: authUser.id,
            fullName: authUser.fullName,
            email: authUser.email,
            countryCode: "", // Not provided in auth response
            phoneNumber: "", // Not provided in auth response
            role: authUser.role,
            verifyCode: "", // Not provided in auth response
            createAt: "", // Not provided in auth response
            updateAt: "" // Not provided in auth response
        )
    }
    
    // Fixed method signature to match the expected return type
    func convertToDomainAuthTokens(_ authTokens: Auth_AuthTokens) -> AuthTokens? {
        return AuthTokens(
            accessToken: authTokens.accessToken,
            expiresIn: Int32(authTokens.expiresIn),
            user: convertToDomainUser(authTokens.user)
        )
    }
}

// MARK: - Upload Service Methods

func uploadFile(fileData: Data, fileName: String, contentType: String, userId: String, fileType: Upload_FileType) async throws -> Upload_UploadFileResponse {
    return try await withGRPCClient(
        transport: .http2NIOPosix(
            target: .dns(host: uploadServiceHost, port: uploadServicePort),
            transportSecurity: .plaintext
        )
    ) { client in
        let service = Upload_UploadService.Client(wrapping: client)
        
        let request = Upload_UploadFileRequest.with {
            $0.fileData = fileData
            $0.fileName = fileName
            $0.contentType = contentType
            $0.userID = userId
            $0.fileType = fileType
        }
        
        let response = try await service.uploadFile(request)
        return response
    }
}

func deleteFile(fileKey: String, userId: String) async throws -> Upload_DeleteFileResponse {
    return try await withGRPCClient(
        transport: .http2NIOPosix(
            target: .dns(host: uploadServiceHost, port: uploadServicePort),
            transportSecurity: .plaintext
        )
    ) { client in
        let service = Upload_UploadService.Client(wrapping: client)
        
        let request = Upload_DeleteFileRequest.with {
            $0.fileKey = fileKey
            $0.userID = userId
        }
        
        let response = try await service.deleteFile(request)
        return response
    }
}

func getFileURL(fileKey: String, expiresIn: Int32 = 3600) async throws -> Upload_GetFileURLResponse {
    return try await withGRPCClient(
        transport: .http2NIOPosix(
            target: .dns(host: uploadServiceHost, port: uploadServicePort),
            transportSecurity: .plaintext
        )
    ) { client in
        let service = Upload_UploadService.Client(wrapping: client)
        
        let request = Upload_GetFileURLRequest.with {
            $0.fileKey = fileKey
            $0.expiresIn = expiresIn
        }
        
        let response = try await service.getFileURL(request)
        return response
    }
}
