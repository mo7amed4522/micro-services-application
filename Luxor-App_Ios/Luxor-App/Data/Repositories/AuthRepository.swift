//
//  AuthRepository.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import Foundation
import Combine

class AuthRepository: AuthRepositoryProtocol {
    private let grpcClient: GRPCClientManager
    private var cancellables = Set<AnyCancellable>()
    
    init(grpcClient: GRPCClientManager) {
        self.grpcClient = grpcClient
    }
    
    func login(credentials: LoginCredentials) -> AnyPublisher<AuthResult, Never> {
        return Future<AuthResult, Never> { [weak self] promise in
            guard let self = self else {
                promise(.success(AuthResult.failure(message: "Repository not available")))
                return
            }
            
            Task {
                do {
                    let response = try await self.grpcClient.login(
                        email: credentials.email,
                        password: credentials.password
                    )
                    
                    if response.statusCode == 200 {
                        let domainTokens = AuthTokens(
                            accessToken: response.accessToken,
                            expiresIn: 3600,
                            user: User(
                                id: response.user.id,
                                fullName: response.user.fullName,
                                email: response.user.email,
                                countryCode: response.user.countryCode,
                                phoneNumber: response.user.phoneNumber,
                                role: response.user.role,
                                verifyCode: response.user.verifiyCode,
                                createAt: response.user.createAt,
                                updateAt: response.user.updateAt
                            )
                        )
                        let result = AuthResult.success(user: domainTokens.user, tokens: domainTokens)
                        promise(.success(result))
                    } else {
                        let errorMessage = true ? response.error.message : response.message
                        promise(.success(AuthResult.failure(message: errorMessage)))
                    }
                } catch {
                    promise(.success(AuthResult.failure(message: "Network error: \(error.localizedDescription)")))
                }
            }
        }
        .eraseToAnyPublisher()
    }
    
    func validateToken(_ token: String) -> AnyPublisher<Bool, Never> {
        return Future<Bool, Never> { [weak self] promise in
            guard let self = self else {
                promise(.success(false))
                return
            }
            
            Task {
                do {
                    let response = try await self.grpcClient.validateToken(token)
                    promise(.success(!response.id.isEmpty))
                } catch {
                    promise(.success(false))
                }
            }
        }
        .eraseToAnyPublisher()
    }
    
    func logout() -> AnyPublisher<Void, Never> {
        return Future<Void, Never> { promise in
            UserDefaults.standard.removeObject(forKey: "access_token")
            UserDefaults.standard.removeObject(forKey: "user_data")
            promise(.success(()))
        }
        .eraseToAnyPublisher()
    }
}
