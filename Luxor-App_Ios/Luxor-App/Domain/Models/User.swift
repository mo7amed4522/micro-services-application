//
//  User.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import Foundation

struct User: Codable, Identifiable {
    let id: String
    let fullName: String
    let email: String
    let countryCode: String
    let phoneNumber: String
    let role: String
    let verifyCode: String?
    let createAt: String
    let updateAt: String
    
    init(id: String = "", fullName: String = "", email: String = "", countryCode: String = "", phoneNumber: String = "", role: String = "user", verifyCode: String? = nil, createAt: String = "", updateAt: String = "") {
        self.id = id
        self.fullName = fullName
        self.email = email
        self.countryCode = countryCode
        self.phoneNumber = phoneNumber
        self.role = role
        self.verifyCode = verifyCode
        self.createAt = createAt
        self.updateAt = updateAt
    }
}

struct AuthTokens: Codable {
    let accessToken: String
    let expiresIn: Int32
    let user: User
}

struct LoginCredentials {
    let email: String
    let password: String
}

struct AuthResult {
    let isSuccess: Bool
    let user: User?
    let tokens: AuthTokens?
    let errorMessage: String?
    
    static func success(user: User, tokens: AuthTokens) -> AuthResult {
        return AuthResult(isSuccess: true, user: user, tokens: tokens, errorMessage: nil)
    }
    
    static func failure(message: String) -> AuthResult {
        return AuthResult(isSuccess: false, user: nil, tokens: nil, errorMessage: message)
    }
}

struct ForgotPasswordResult {
    let isSuccess: Bool
    let verifyCode: String?
    let errorMessage: String?
    
    static func success(verifyCode: String) -> ForgotPasswordResult {
        return ForgotPasswordResult(isSuccess: true, verifyCode: verifyCode, errorMessage: nil)
    }
    
    static func failure(message: String) -> ForgotPasswordResult {
        return ForgotPasswordResult(isSuccess: false, verifyCode: nil, errorMessage: message)
    }
}

struct VerifyCodeResult {
    let isSuccess: Bool
    let errorMessage: String?
    
    static func success() -> VerifyCodeResult {
        return VerifyCodeResult(isSuccess: true, errorMessage: nil)
    }
    
    static func failure(message: String) -> VerifyCodeResult {
        return VerifyCodeResult(isSuccess: false, errorMessage: message)
    }
}

struct ResetPasswordResult {
    let isSuccess: Bool
    let errorMessage: String?
    
    static func success() -> ResetPasswordResult {
        return ResetPasswordResult(isSuccess: true, errorMessage: nil)
    }
    
    static func failure(message: String) -> ResetPasswordResult {
        return ResetPasswordResult(isSuccess: false, errorMessage: message)
    }
}