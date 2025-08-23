//
//  AuthUseCase.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import Foundation
import Combine

class AuthUseCase {
    private let authRepository: AuthRepositoryProtocol
    
    init(authRepository: AuthRepositoryProtocol) {
        self.authRepository = authRepository
    }
    
    func login(email: String, password: String) -> AnyPublisher<AuthResult, Never> {
        let credentials = LoginCredentials(email: email, password: password)
        return authRepository.login(credentials: credentials)
    }
    
    func validateToken(_ token: String) -> AnyPublisher<Bool, Never> {
        return authRepository.validateToken(token)
    }
    
    func logout() -> AnyPublisher<Void, Never> {
        return authRepository.logout()
    }
    
    func validateEmail(_ email: String) -> Bool {
        let emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let emailPredicate = NSPredicate(format: "SELF MATCHES %@", emailRegex)
        return emailPredicate.evaluate(with: email)
    }
    
    func validatePassword(_ password: String) -> Bool {
        let hasCapital = password.range(of: "[A-Z]", options: .regularExpression) != nil
        let hasMinLength = password.count >= 8
        let hasSpecial = password.range(of: "[^A-Za-z0-9]", options: .regularExpression) != nil
        return hasCapital && hasMinLength && hasSpecial
    }
}