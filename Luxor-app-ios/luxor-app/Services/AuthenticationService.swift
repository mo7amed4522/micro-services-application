import Foundation
import Combine
import GRPC
import NIO

protocol AuthenticationServiceProtocol {
    func login(email: String, password: String) -> AnyPublisher<User, AuthenticationError>
    func logout() -> AnyPublisher<Void, Never>
    func forgotPassword(email: String) -> AnyPublisher<Void, AuthenticationError>
}

class AuthenticationService: AuthenticationServiceProtocol {
    private let eventLoopGroup: EventLoopGroup
    private let channel: GRPCChannel
    private let authClient: Auth_AuthServiceAsyncClient
    
    init() {
        self.eventLoopGroup = MultiThreadedEventLoopGroup(numberOfThreads: 1)
        let serverURL = GRPCConfig.authServiceURL
        
        self.channel = try! GRPCChannelPool.with(
            target: .host(serverURL.host!, port: serverURL.port!),
            transportSecurity: .plaintext,
            eventLoopGroup: eventLoopGroup
        )
        
        self.authClient = Auth_AuthServiceAsyncClient(channel: channel)
    }
    
    func login(email: String, password: String) -> AnyPublisher<User, AuthenticationError> {
        Future { [weak self] promise in
            guard let self = self else {
                promise(.failure(.unknownError))
                return
            }
            
            Task {
                do {
                    var request = Auth_LoginRequest()
                    request.email = email
                    request.password = password
                    
                    let response = try await self.authClient.login(request)
                    
                    let user = User(
                        id: UUID(uuidString: response.user.id) ?? UUID(),
                        email: response.user.email,
                        firstName: response.user.firstName,
                        lastName: response.user.lastName,
                        profileImageURL: response.user.profileImageURL.isEmpty ? nil : response.user.profileImageURL
                    )
                    
                    promise(.success(user))
                } catch {
                    promise(.failure(self.mapGRPCError(error)))
                }
            }
        }
        .eraseToAnyPublisher()
    }
    
    func logout() -> AnyPublisher<Void, Never> {
        Future { [weak self] promise in
            guard let self = self else {
                promise(.success(()))
                return
            }
            
            Task {
                do {
                    let request = Auth_LogoutRequest()
                    _ = try await self.authClient.logout(request)
                    promise(.success(()))
                } catch {
                    promise(.success(()))
                }
            }
        }
        .eraseToAnyPublisher()
    }
    
    func forgotPassword(email: String) -> AnyPublisher<Void, AuthenticationError> {
        Future { [weak self] promise in
            guard let self = self else {
                promise(.failure(.unknownError))
                return
            }
            
            Task {
                do {
                    var request = Auth_ForgotPasswordRequest()
                    request.email = email
                    
                    _ = try await self.authClient.forgotPassword(request)
                    promise(.success(()))
                } catch {
                    promise(.failure(self.mapGRPCError(error)))
                }
            }
        }
        .eraseToAnyPublisher()
    }
    
    private func mapGRPCError(_ error: Error) -> AuthenticationError {
        if let grpcError = error as? GRPCStatus {
            switch grpcError.code {
            case .unauthenticated, .permissionDenied:
                return .invalidCredentials
            case .unavailable, .deadlineExceeded:
                return .networkError
            default:
                return .unknownError
            }
        }
        return .networkError
    }
    
    deinit {
        try? channel.close().wait()
        try? eventLoopGroup.syncShutdownGracefully()
    }
}

enum AuthenticationError: Error {
    case invalidCredentials
    case networkError
    case unknownError
    
    var localizedDescription: String {
        switch self {
        case .invalidCredentials:
            return "Invalid email or password"
        case .networkError:
            return "Network connection failed"
        case .unknownError:
            return "An unknown error occurred"
        }
    }
}