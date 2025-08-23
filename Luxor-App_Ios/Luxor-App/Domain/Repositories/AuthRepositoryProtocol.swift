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
}