//
//  AuthRepositoryProtocol.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import Foundation
import Combine

protocol AuthRepositoryProtocol {
    func login(credentials: LoginCredentials) -> AnyPublisher<AuthResult, Never>
    func validateToken(_ token: String) -> AnyPublisher<Bool, Never>
    func logout() -> AnyPublisher<Void, Never>
    func forgetPassword(email: String, type: String) -> AnyPublisher<ForgotPasswordResult, Never>
    func verifyCode(email: String, verifyCode: String) -> AnyPublisher<VerifyCodeResult, Never>
    func resetPassword(email: String, password: String) -> AnyPublisher<ResetPasswordResult, Never>
}