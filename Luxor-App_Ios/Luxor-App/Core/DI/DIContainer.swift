//
//  DIContainer.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import Foundation

class DIContainer: ObservableObject {
    static let shared = DIContainer()

    var authState: AuthState?

    lazy var grpcClient: GRPCClientManager = {
        return GRPCClientManager()
    }()

    lazy var authRepository: AuthRepositoryProtocol = {
        return AuthRepository(grpcClient: grpcClient)
    }()

    lazy var authUseCase: AuthUseCase = {
        return AuthUseCase(authRepository: authRepository)
    }()

    private init() {}

    func setAuthState(_ authState: AuthState) {
        self.authState = authState
    }
}
