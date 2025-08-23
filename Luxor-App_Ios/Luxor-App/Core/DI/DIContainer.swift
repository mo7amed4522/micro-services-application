//
//  DIContainer.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import Foundation

class DIContainer: ObservableObject {
    static let shared = DIContainer()
    
    // MARK: - State Management
    var authState: AuthState?
    
    // MARK: - Network Layer
    lazy var grpcClient: GRPCClientManager = {
        return GRPCClientManager()
    }()
    
    // MARK: - Repository Layer
    lazy var authRepository: AuthRepositoryProtocol = {
        return AuthRepository(grpcClient: grpcClient)
    }()
    
    // MARK: - Use Case Layer
    lazy var authUseCase: AuthUseCase = {
        return AuthUseCase(authRepository: authRepository)
    }()
    
    private init() {}
    
    func setAuthState(_ authState: AuthState) {
        self.authState = authState
    }
}