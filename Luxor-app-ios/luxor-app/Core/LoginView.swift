import SwiftUI

struct LoginView: View {
    @StateObject private var viewModel = LoginViewModel()
    
    var body: some View {
        GeometryReader { geometry in
            ScrollView {
                VStack(spacing: ResponsiveDesign.spacing(for: geometry.size) * 3) {
                    LogoSection(geometry: geometry)
                    LoginForm(viewModel: viewModel, geometry: geometry)
                    ActionButtons(viewModel: viewModel, geometry: geometry)
                    Spacer(minLength: ResponsiveDesign.spacing(for: geometry.size) * 2)
                }
                .padding(ResponsiveDesign.padding(for: geometry.size))
            }
        }
        .background(
            LinearGradient(
                colors: [
                    Color(.systemBackground),
                    LuxorTheme.primaryTeal.opacity(0.05)
                ],
                startPoint: .top,
                endPoint: .bottom
            )
        )
    }
}

// MARK: - UI Components

struct LogoSection: View {
    let geometry: GeometryProxy
    
    var body: some View {
        VStack(spacing: ResponsiveDesign.spacing(for: geometry.size)) {
            ZStack {
                Circle()
                    .fill(
                        LinearGradient(
                            colors: [LuxorTheme.primaryTeal, LuxorTheme.darkTeal],
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        )
                    )
                    .frame(
                        width: min(geometry.size.width * 0.3, 120),
                        height: min(geometry.size.width * 0.3, 120)
                    )
                    .shadow(color: LuxorTheme.primaryTeal.opacity(0.3), radius: 10, x: 0, y: 5)
                
                Image("logo-nobg")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(
                        width: min(geometry.size.width * 0.2, 80),
                        height: min(geometry.size.width * 0.2, 80)
                    )
            }
            
            Text("Welcome Back")
                .font(ResponsiveDesign.titleFont(for: geometry.size))
                .fontWeight(.bold)
                .foregroundColor(LuxorTheme.primaryTeal)
            
            Text("Sign in to your account")
                .font(ResponsiveDesign.bodyFont(for: geometry.size))
                .foregroundColor(.secondary)
        }
        .padding(.top, ResponsiveDesign.spacing(for: geometry.size) * 2)
    }
}

struct LoginForm: View {
    @ObservedObject var viewModel: LoginViewModel
    let geometry: GeometryProxy
    
    var body: some View {
        VStack(spacing: ResponsiveDesign.spacing(for: geometry.size) * 2) {
            EmailField(viewModel: viewModel, geometry: geometry)
            PasswordField(viewModel: viewModel, geometry: geometry)
            PasswordRequirements(viewModel: viewModel, geometry: geometry)
        }
    }
}

struct EmailField: View {
    @ObservedObject var viewModel: LoginViewModel
    let geometry: GeometryProxy
    
    var body: some View {
        VStack(alignment: .leading, spacing: ResponsiveDesign.spacing(for: geometry.size) / 2) {
            Text("Email")
                .font(ResponsiveDesign.headlineFont(for: geometry.size))
                .fontWeight(.medium)
                .foregroundColor(LuxorTheme.primaryTeal)
            
            HStack {
                Image(systemName: "envelope")
                    .foregroundColor(LuxorTheme.primaryTeal)
                    .frame(width: 20)
                
                TextField("Enter your email", text: $viewModel.email)
                    .textFieldStyle(PlainTextFieldStyle())
                    .keyboardType(.emailAddress)
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                
                Group {
                    switch viewModel.emailValidation {
                    case .none:
                        EmptyView()
                    case .valid:
                        Image(systemName: "checkmark.circle.fill")
                            .foregroundColor(.green)
                    case .invalid:
                        Image(systemName: "xmark.circle.fill")
                            .foregroundColor(.red)
                    }
                }
                .frame(width: 20)
            }
            .padding(ResponsiveDesign.padding(for: geometry.size))
            .background(
                RoundedRectangle(cornerRadius: ResponsiveDesign.cornerRadius(for: geometry.size))
                    .fill(Color(.systemGray6))
                    .overlay(
                        RoundedRectangle(cornerRadius: ResponsiveDesign.cornerRadius(for: geometry.size))
                            .stroke(
                                viewModel.emailValidation == .invalid ? Color.red :
                                viewModel.emailValidation == .valid ? Color.green :
                                LuxorTheme.primaryTeal.opacity(0.3),
                                lineWidth: 1
                            )
                    )
            )
        }
    }
}

struct PasswordField: View {
    @ObservedObject var viewModel: LoginViewModel
    let geometry: GeometryProxy
    
    var body: some View {
        VStack(alignment: .leading, spacing: ResponsiveDesign.spacing(for: geometry.size) / 2) {
            Text("Password")
                .font(ResponsiveDesign.headlineFont(for: geometry.size))
                .fontWeight(.medium)
                .foregroundColor(LuxorTheme.primaryTeal)
            
            HStack {
                Image(systemName: "lock")
                    .foregroundColor(LuxorTheme.primaryTeal)
                    .frame(width: 20)
                
                CustomSecureField(
                    placeholder: "Enter your password",
                    text: $viewModel.password,
                    isSecure: !viewModel.isPasswordVisible
                )
                .autocapitalization(.none)
                .disableAutocorrection(true)
                
                Button {
                    viewModel.togglePasswordVisibility()
                } label: {
                    Image(systemName: viewModel.isPasswordVisible ? "eye.slash" : "eye")
                        .foregroundColor(LuxorTheme.primaryTeal)
                }
                .frame(width: 20)
            }
            .padding(ResponsiveDesign.padding(for: geometry.size))
            .background(
                RoundedRectangle(cornerRadius: ResponsiveDesign.cornerRadius(for: geometry.size))
                    .fill(Color(.systemGray6))
                    .overlay(
                        RoundedRectangle(cornerRadius: ResponsiveDesign.cornerRadius(for: geometry.size))
                            .stroke(
                                viewModel.passwordValidation.isValid ? Color.green :
                                viewModel.password.isEmpty ? LuxorTheme.primaryTeal.opacity(0.3) :
                                Color.red,
                                lineWidth: 1
                            )
                    )
            )
        }
    }
}

struct PasswordRequirements: View {
    @ObservedObject var viewModel: LoginViewModel
    let geometry: GeometryProxy
    
    var body: some View {
        VStack(alignment: .leading, spacing: ResponsiveDesign.spacing(for: geometry.size) / 3) {
            Text("Password Requirements:")
                .font(ResponsiveDesign.bodyFont(for: geometry.size))
                .fontWeight(.medium)
                .foregroundColor(.secondary)
            
            HStack {
                VStack(alignment: .leading, spacing: 2) {
                    RequirementRow(
                        text: "At least 8 characters",
                        isValid: viewModel.passwordValidation.hasMinLength,
                        geometry: geometry
                    )
                    RequirementRow(
                        text: "Uppercase letter",
                        isValid: viewModel.passwordValidation.hasUppercase,
                        geometry: geometry
                    )
                }
                
                Spacer()
                
                VStack(alignment: .leading, spacing: 2) {
                    RequirementRow(
                        text: "Lowercase letter",
                        isValid: viewModel.passwordValidation.hasLowercase,
                        geometry: geometry
                    )
                    RequirementRow(
                        text: "Special character",
                        isValid: viewModel.passwordValidation.hasSpecialChar,
                        geometry: geometry
                    )
                }
            }
        }
        .padding(ResponsiveDesign.padding(for: geometry.size))
        .background(
            RoundedRectangle(cornerRadius: ResponsiveDesign.cornerRadius(for: geometry.size))
                .fill(Color(.systemGray6).opacity(0.5))
        )
    }
}

struct RequirementRow: View {
    let text: String
    let isValid: Bool
    let geometry: GeometryProxy
    
    var body: some View {
        HStack(spacing: ResponsiveDesign.spacing(for: geometry.size) / 2) {
            Image(systemName: isValid ? "checkmark.circle.fill" : "circle")
                .foregroundColor(isValid ? .green : .gray)
                .font(.caption)
            
            Text(text)
                .font(ResponsiveDesign.captionFont(for: geometry.size))
                .foregroundColor(isValid ? .green : .gray)
        }
    }
}

struct ActionButtons: View {
    @ObservedObject var viewModel: LoginViewModel
    let geometry: GeometryProxy
    
    var body: some View {
        VStack(spacing: ResponsiveDesign.spacing(for: geometry.size) * 1.5) {
            Button {
                viewModel.login()
            } label: {
                HStack {
                    if viewModel.isLoading {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: .white))
                            .scaleEffect(0.8)
                    } else {
                        Text("Sign In")
                            .fontWeight(.semibold)
                    }
                }
                .frame(maxWidth: .infinity)
                .frame(height: ResponsiveDesign.buttonHeight(for: geometry.size))
                .background(
                    LinearGradient(
                        colors: viewModel.isFormValid ? 
                            [LuxorTheme.primaryTeal, LuxorTheme.darkTeal] : 
                            [Color.gray, Color.gray.opacity(0.8)],
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                )
                .foregroundColor(.white)
                .cornerRadius(ResponsiveDesign.cornerRadius(for: geometry.size))
            }
            .disabled(!viewModel.isFormValid || viewModel.isLoading)
            Spacer()
            VStack(spacing: ResponsiveDesign.spacing(for: geometry.size)) {
                Button("Forgot Password ?") {
                    viewModel.forgotPassword()
                }
                .foregroundColor(LuxorTheme.primaryTeal)
                .font(ResponsiveDesign.bodyFont(for: geometry.size))
                
                
                
                Button("Don't have an account ?  Sign Up") {
                    viewModel.signUp()
                }
                .foregroundColor(LuxorTheme.primaryTeal)
                .font(ResponsiveDesign.bodyFont(for: geometry.size))
                .fontWeight(.medium)
            }
        }
    }
}

#Preview {
    LoginView()
}
