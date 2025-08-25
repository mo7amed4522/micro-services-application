//
//  ForgotPasswordView.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import SwiftUI

struct ForgotPasswordView: View {
    @StateObject private var viewModel: ForgotPasswordViewModel
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var languageManager: LanguageManager
    @Environment(\.dismiss) private var dismiss
    
    init(authUseCase: AuthUseCase) {
        self._viewModel = StateObject(wrappedValue: ForgotPasswordViewModel(authUseCase: authUseCase))
    }
    
    var body: some View {
        GeometryReader { geometry in
            let isIPad = geometry.size.width > 768
            
            VStack(spacing: 0) {
                headerView
                    .padding(.horizontal, isIPad ? 60 : 24)
                    .padding(.top, 20)
                
                ScrollView {
                    VStack(spacing: 24) {
                        LuxorLogoView()
                            .padding(.top, 40)
                        titleSection
                        contentView
                            .padding(.horizontal, isIPad ? 60 : 24)
                        
                        Spacer(minLength: 40)
                    }
                    .padding(.top, 20)
                }
            }
        }
        .luxorThemedBackground(themeManager)
        .ignoresSafeArea(.all, edges: .bottom)
        .alert("Alert", isPresented: $viewModel.showAlert) {
            Button("OK") {
                if viewModel.currentStep == .completed {
                    dismiss()
                }
            }
        } message: {
            Text(viewModel.alertMessage)
        }
    }
    
    @ViewBuilder
    private var headerView: some View {
        HStack {
            Button(action: {
                dismiss()
            }) {
                ZStack {
                    Circle()
                        .fill(
                            themeManager.isDarkMode ?
                            LuxorColors.Dark.cardBackground :
                            Color.white
                        )
                        .frame(width: 44, height: 44)
                        .shadow(
                            color: themeManager.isDarkMode ? LuxorColors.Dark.shadow : LuxorColors.Light.shadow,
                            radius: 8,
                            x: 0,
                            y: 2
                        )
                    
                    Image(systemName: "chevron.left")
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                }
            }
            
            Spacer()
        }
    }
    
    @ViewBuilder
    private var titleSection: some View {
        VStack(spacing: 12) {
            Text(titleText)
                .font(.system(size: 28, weight: .bold))
                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                .multilineTextAlignment(.center)
            
            Text(subtitleText)
                .font(.system(size: 16, weight: .regular))
                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 20)
        }
    }
    
    @ViewBuilder
    private var contentView: some View {
        VStack(spacing: 24) {
            switch viewModel.currentStep {
            case .enterEmail:
                enterEmailView
            case .verifyCode:
                verifyCodeView
            case .resetPassword:
                resetPasswordView
            case .completed:
                completedView
            }
        }
        .luxorCard(themeManager)
      
    }
    
    @ViewBuilder
    private var enterEmailView: some View {
        VStack(spacing: 20) {
            VStack(spacing: 4) {
                LuxorEmailTextField(email: $viewModel.email)
                    .environmentObject(themeManager)
                    .onChange(of: viewModel.email) { _ in
                        viewModel.validateEmail()
                    }
                
                if let emailError = viewModel.emailError {
                    HStack {
                        Text(emailError)
                            .font(.caption)
                            .foregroundColor(.red)
                        Spacer()
                    }
                }
            }
            VStack(alignment: .leading, spacing: 12) {
                Text("Choose verification method:")
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                
                HStack(spacing: 16) {
                    verificationMethodButton("email", "Email", "envelope")
                    verificationMethodButton("sms", "SMS", "message")
                }
            }
            Button(action: {
                viewModel.sendVerificationCode()
            }) {
                HStack {
                    if viewModel.isLoading {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: .white))
                            .scaleEffect(0.8)
                    } else {
                        Text("Send Verification Code")
                            .font(.system(size: 18, weight: .semibold))
                    }
                }
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .frame(height: 56)
            }
            .historicalButton(themeManager, isEnabled: !viewModel.email.isEmpty && viewModel.emailError == nil && !viewModel.isLoading)
            .disabled(viewModel.email.isEmpty || viewModel.emailError != nil || viewModel.isLoading)
        }
    }
    
    @ViewBuilder
    private var verifyCodeView: some View {
        VStack(spacing: 20) {
            VStack(spacing: 4) {
                VStack(alignment: .leading, spacing: 8) {
                    Text("Verification Code")
                        .font(.system(size: 16, weight: .medium))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                    
                    TextField("Enter code", text: $viewModel.verifyCode)
                        .keyboardType(.numberPad)
                        .onChange(of: viewModel.verifyCode) { _ in
                            viewModel.validateVerifyCode()
                        }
                }
                
                if let verifyCodeError = viewModel.verifyCodeError {
                    HStack {
                        Text(verifyCodeError)
                            .font(.caption)
                            .foregroundColor(.red)
                        Spacer()
                    }
                }
            }
            Button(action: {
                viewModel.verifyCodeAction()
            }) {
                HStack {
                    if viewModel.isLoading {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: .white))
                            .scaleEffect(0.8)
                    } else {
                        Text("Verify Code")
                            .font(.system(size: 18, weight: .semibold))
                    }
                }
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .frame(height: 56)
            }
            .historicalButton(themeManager, isEnabled: !viewModel.verifyCode.isEmpty && viewModel.verifyCodeError == nil && !viewModel.isLoading)
            .disabled(viewModel.verifyCode.isEmpty || viewModel.verifyCodeError != nil || viewModel.isLoading)
            Button(action: {
                viewModel.sendVerificationCode()
            }) {
                Text("Resend Code")
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.accent : LuxorColors.Light.accent)
            }
            .disabled(viewModel.isLoading)
        }
    }
    
    @ViewBuilder
    private var resetPasswordView: some View {
        VStack(spacing: 20) {
            VStack(spacing: 4) {
                LuxorPasswordTextField(password: $viewModel.newPassword)
                    .environmentObject(themeManager)
                    .onChange(of: viewModel.newPassword) { _ in
                        viewModel.validatePasswords()
                    }
                
                if let passwordError = viewModel.passwordError {
                    HStack {
                        Text(passwordError)
                            .font(.caption)
                            .foregroundColor(.red)
                        Spacer()
                    }
                }
            }
            
            // Confirm password input
            VStack(spacing: 4) {
                VStack(alignment: .leading, spacing: 8) {
                    Text("Confirm New Password")
                        .font(.system(size: 16, weight: .medium))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                    
                    SecureField("Confirm password", text: $viewModel.confirmPassword)
                        
                        .onChange(of: viewModel.confirmPassword) { _ in
                            viewModel.validatePasswords()
                        }
                }
                
                if let confirmPasswordError = viewModel.confirmPasswordError {
                    HStack {
                        Text(confirmPasswordError)
                            .font(.caption)
                            .foregroundColor(.red)
                        Spacer()
                    }
                }
            }
            
            // Reset password button
            Button(action: {
                viewModel.resetPassword()
            }) {
                HStack {
                    if viewModel.isLoading {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: .white))
                            .scaleEffect(0.8)
                    } else {
                        Text("Reset Password")
                            .font(.system(size: 18, weight: .semibold))
                    }
                }
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .frame(height: 56)
            }
            .historicalButton(themeManager, isEnabled: isResetPasswordEnabled && !viewModel.isLoading)
            .disabled(!isResetPasswordEnabled || viewModel.isLoading)
        }
    }
    
    @ViewBuilder
    private var completedView: some View {
        VStack(spacing: 20) {
            Image(systemName: "checkmark.circle.fill")
                .font(.system(size: 60))
                .foregroundColor(.green)
            
            Text("Password Reset Successfully!")
                .font(.system(size: 20, weight: .semibold))
                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                .multilineTextAlignment(.center)
            
            Text("You can now login with your new password.")
                .font(.system(size: 16))
                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                .multilineTextAlignment(.center)
            
            Button(action: {
                dismiss()
            }) {
                Text("Back to Login")
                    .font(.system(size: 18, weight: .semibold))
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity)
                    .frame(height: 56)
            }
            .padding(.horizontal, 16)
            .padding(.top, 8)
            .padding(.bottom, 12)
            
            .transition(.move(edge: .top).combined(with: .opacity))
        }
    }
    
    @ViewBuilder
    private func verificationMethodButton(_ type: String, _ title: String, _ iconName: String) -> some View {
        Button(action: {
            viewModel.selectedVerificationType = type
        }) {
            HStack(spacing: 8) {
                Image(systemName: iconName)
                    .font(.system(size: 16))
                Text(title)
                    .font(.system(size: 14, weight: .medium))
            }
            .foregroundColor(viewModel.selectedVerificationType == type ? .white : (themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary))
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .fill(viewModel.selectedVerificationType == type ? (themeManager.isDarkMode ? LuxorColors.Dark.accent : LuxorColors.Light.accent) : Color.clear)
                    .overlay(
                        RoundedRectangle(cornerRadius: 8)
                            .stroke(themeManager.isDarkMode ? LuxorColors.Dark.accent : LuxorColors.Light.accent, lineWidth: 1)
                    )
            )
        }
    }
    
    // MARK: - Computed Properties
    
    private var titleText: String {
        switch viewModel.currentStep {
        case .enterEmail:
            return "Forgot Password?"
        case .verifyCode:
            return "Verify Code"
        case .resetPassword:
            return "Reset Password"
        case .completed:
            return "Success!"
        }
    }
    
    private var subtitleText: String {
        switch viewModel.currentStep {
        case .enterEmail:
            return "Enter your email address and we'll send you a verification code to reset your password."
        case .verifyCode:
            return "Enter the verification code sent to \(viewModel.email)"
        case .resetPassword:
            return "Create a new password for your account."
        case .completed:
            return "Your password has been reset successfully."
        }
    }
    
    private var isResetPasswordEnabled: Bool {
        !viewModel.newPassword.isEmpty &&
        !viewModel.confirmPassword.isEmpty &&
        viewModel.passwordError == nil &&
        viewModel.confirmPasswordError == nil
    }
}


#Preview {
    ForgotPasswordView(authUseCase: AuthUseCase(authRepository: AuthRepository(grpcClient: GRPCClientManager())))
        .environmentObject(ThemeManager())
        .environmentObject(LanguageManager())
}
