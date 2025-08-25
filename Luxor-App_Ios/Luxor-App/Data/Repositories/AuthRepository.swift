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
                            accessToken: response.tokens.accessToken,
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
    
    func register(credentials: RegisterCreditials) -> AnyPublisher<RegisterResult, Never>{
        return Future<RegisterResult, Never>{[weak self] promise in
            guard let self = self else {
                promise(.success(RegisterResult.failure(message: "Repository not available")))
                return
            }
            Task{
                do {
                    let response = try await self.grpcClient.createUser(
                        fullName: credentials.fullName, email: credentials.email, password: credentials.password, countryCode: credentials.countryCode, phoneNumber: credentials.phoneNumber
                    )
                    if response.statusCode == 200 {
                        let domainToken = RegisterationTokens(
                            
                        user:User(
                            id: response.user.id,
                            fullName: response.user.fullName,
                            email : response.user.email,
                            countryCode: response.user.countryCode,
                            phoneNumber: response.user.phoneNumber,
                            role: response.user.role,
                            verifyCode: response.user.verifiyCode,
                            createAt: response.user.createAt,
                            updateAt: response.user.updateAt
                        )
                        )
                        let result = RegisterResult.success(user: domainToken.user)
                        promise(.success(result))
                        
                    }else{
                        let errorMessage = true ? response.error.message : response.message
                        promise(.success(RegisterResult.failure(message: errorMessage)))
                    }
                }catch{
                    promise(.success(RegisterResult.failure(message: "Network error: \(error.localizedDescription)")))
                
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
    
    func forgetPassword(email: String, type: String) -> AnyPublisher<ForgotPasswordResult, Never> {
        return Future<ForgotPasswordResult, Never> { [weak self] promise in
            guard let self = self else {
                promise(.success(ForgotPasswordResult.failure(message: "Repository not available")))
                return
            }
            
            Task {
                do {
                    let response = try await self.grpcClient.forgetPassword(email: email, type: type)
                    
                    if response.statusCode == 200 {
                        let result = ForgotPasswordResult.success(verifyCode: response.verifyCode)
                        promise(.success(result))
                    } else {
                        let errorMessage = response.message.isEmpty ? "Failed to send verification code" : response.message
                        promise(.success(ForgotPasswordResult.failure(message: errorMessage)))
                    }
                } catch {
                    promise(.success(ForgotPasswordResult.failure(message: "Network error: \(error.localizedDescription)")))
                }
            }
        }
        .eraseToAnyPublisher()
    }
    
    func verifyCode(email: String, verifyCode: String) -> AnyPublisher<VerifyCodeResult, Never> {
        return Future<VerifyCodeResult, Never> { [weak self] promise in
            guard let self = self else {
                promise(.success(VerifyCodeResult.failure(message: "Repository not available")))
                return
            }
            
            Task {
                do {
                    let response = try await self.grpcClient.verifyCode(email: email, verifyCode: verifyCode)
                    
                    if response.statusCode == 200 {
                        let result = VerifyCodeResult.success()
                        promise(.success(result))
                    } else {
                        let errorMessage = response.message.isEmpty ? "Invalid verification code" : response.message
                        promise(.success(VerifyCodeResult.failure(message: errorMessage)))
                    }
                } catch {
                    promise(.success(VerifyCodeResult.failure(message: "Network error: \(error.localizedDescription)")))
                }
            }
        }
        .eraseToAnyPublisher()
    }
    
    func resetPassword(email: String, password: String) -> AnyPublisher<ResetPasswordResult, Never> {
        return Future<ResetPasswordResult, Never> { [weak self] promise in
            guard let self = self else {
                promise(.success(ResetPasswordResult.failure(message: "Repository not available")))
                return
            }
            
            Task {
                do {
                    let response = try await self.grpcClient.resetPassword(email: email, password: password)
                    
                    if response.statusCode == 200 {
                        let result = ResetPasswordResult.success()
                        promise(.success(result))
                    } else {
                        let errorMessage = response.message.isEmpty ? "Failed to reset password" : response.message
                        promise(.success(ResetPasswordResult.failure(message: errorMessage)))
                    }
                } catch {
                    promise(.success(ResetPasswordResult.failure(message: "Network error: \(error.localizedDescription)")))
                }
            }
        }
        .eraseToAnyPublisher()
    }
}
