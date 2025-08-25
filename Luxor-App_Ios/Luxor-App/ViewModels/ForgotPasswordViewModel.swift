//
//  ForgotPasswordViewModel.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import SwiftUI
import Combine

class ForgotPasswordViewModel: ObservableObject {
    @Published var email = ""
    @Published var verifyCode = ""
    @Published var newPassword = ""
    @Published var confirmPassword = ""
    @Published var selectedVerificationType = "email"
    @Published var emailError: String?
    @Published var verifyCodeError: String?
    @Published var passwordError: String?
    @Published var confirmPasswordError: String?
    @Published var isLoading = false
    @Published var currentStep: ForgotPasswordStep = .enterEmail
    @Published var showAlert = false
    @Published var alertMessage = ""
    @EnvironmentObject var languageManager: LanguageManager
    
    private let authUseCase: AuthUseCase
    private var cancellables = Set<AnyCancellable>()
    
    enum ForgotPasswordStep {
        case enterEmail
        case verifyCode
        case resetPassword
        case completed
    }
    
    init(authUseCase: AuthUseCase) {
        self.authUseCase = authUseCase
    }
    
    func validateEmail() {
        if email.isEmpty {
            emailError = nil
        } else if !authUseCase.validateEmail(email) {
            emailError = LocalizedStrings.enterValidEmail(languageManager.currentLanguage)
        } else {
            emailError = nil
        }
    }
    
    func validateVerifyCode() {
        if verifyCode.isEmpty {
            verifyCodeError = LocalizedStrings.enterVerificationCode(languageManager.currentLanguage)
        } else if verifyCode.count < 4 {
            verifyCodeError = LocalizedStrings.verificationCodeLength(languageManager.currentLanguage)
        } else {
            verifyCodeError = nil
        }
    }
    
    func validatePasswords() {
        if newPassword.isEmpty {
            passwordError = nil
        } else if !authUseCase.validatePassword(newPassword) {
            passwordError = LocalizedStrings.passwordRequirements(languageManager.currentLanguage)
        } else {
            passwordError = nil
        }
        
        if confirmPassword.isEmpty {
            confirmPasswordError = nil
        } else if newPassword != confirmPassword {
            confirmPasswordError = LocalizedStrings.passwordsDoNotMatch(languageManager.currentLanguage)
        } else {
            confirmPasswordError = nil
        }
    }
    
    func sendVerificationCode() {
        validateEmail()
        
        guard emailError == nil && !email.isEmpty else {
            return
        }
        
        isLoading = true
        
        authUseCase.forgetPassword(email: email, type: selectedVerificationType)
            .receive(on: DispatchQueue.main)
            .sink { [weak self] result in
                self?.isLoading = false
                
                if result.isSuccess {
                    self?.currentStep = .verifyCode
                } else {
                    self?.alertMessage = result.errorMessage ?? "Failed to send verification code"
                    self?.showAlert = true
                }
            }
            .store(in: &cancellables)
    }
    
    func verifyCodeAction() {
        validateVerifyCode()
        
        guard verifyCodeError == nil && !verifyCode.isEmpty else {
            return
        }
        
        isLoading = true
        
        authUseCase.verifyCode(email: email, verifyCode: verifyCode)
            .receive(on: DispatchQueue.main)
            .sink { [weak self] result in
                self?.isLoading = false
                
                if result.isSuccess {
                    self?.currentStep = .resetPassword
                } else {
                    self?.alertMessage = result.errorMessage ?? "Invalid verification code"
                    self?.showAlert = true
                }
            }
            .store(in: &cancellables)
    }
    
    func resetPassword() {
        validatePasswords()
        
        guard passwordError == nil && confirmPasswordError == nil && 
              !newPassword.isEmpty && !confirmPassword.isEmpty else {
            return
        }
        
        isLoading = true
        
        authUseCase.resetPassword(email: email, password: newPassword)
            .receive(on: DispatchQueue.main)
            .sink { [weak self] result in
                self?.isLoading = false
                
                if result.isSuccess {
                    self?.currentStep = .completed
                    self?.alertMessage = "Password reset successfully!"
                    self?.showAlert = true
                } else {
                    self?.alertMessage = result.errorMessage ?? "Failed to reset password"
                    self?.showAlert = true
                }
            }
            .store(in: &cancellables)
    }
    
    func goBackToLogin() {
        currentStep = .enterEmail
        clearForm()
    }
    
    func goBackToPreviousStep() {
        switch currentStep {
        case .verifyCode:
            currentStep = .enterEmail
        case .resetPassword:
            currentStep = .verifyCode
        case .completed:
            currentStep = .enterEmail
            clearForm()
        case .enterEmail:
            break
        }
    }
    
    private func clearForm() {
        email = ""
        verifyCode = ""
        newPassword = ""
        confirmPassword = ""
        selectedVerificationType = "email"
        emailError = nil
        verifyCodeError = nil
        passwordError = nil
        confirmPasswordError = nil
        isLoading = false
    }
}
