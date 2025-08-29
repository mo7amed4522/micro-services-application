import SwiftUI

struct LoginView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var languageManager: LanguageManager
    @EnvironmentObject var authState: AuthState
    @EnvironmentObject var diContainer: DIContainer
    @Environment(\.horizontalSizeClass) var horizontalSizeClass
    @Environment(\.verticalSizeClass) var verticalSizeClass

    @StateObject private var viewModel: LoginViewModel

    init() {
        self._viewModel = StateObject(
            wrappedValue: LoginViewModel(
                authUseCase: DIContainer.shared.authUseCase
            )
        )
    }

    private var isIPad: Bool {
        horizontalSizeClass == .regular && verticalSizeClass == .regular
    }

    var body: some View {
        GeometryReader { geometry in
            VStack(spacing: 0) {
                VStack(spacing: 24) {
                    LuxorLogoView(size: isIPad ? 140 : 100)

                    VStack(spacing: 8) {
                        Text(
                            LocalizedStrings.welcomeTitle(
                                languageManager.currentLanguage
                            )
                        )
                        .font(.system(size: isIPad ? 32 : 28, weight: .bold))
                        .foregroundColor(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.textPrimary
                                : LuxorColors.Light.textPrimary
                        )

                        Text(
                            LocalizedStrings.SignInToYourAccount(
                                languageManager.currentLanguage
                            )
                        )
                        .font(.system(size: isIPad ? 18 : 16, weight: .medium))
                        .foregroundColor(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.textSecondary
                                : LuxorColors.Light.textSecondary
                        )
                    }
                }
                .padding(.top, 60)
                .padding(.bottom, 32)
            
                ScrollView {
                    VStack(spacing: 24) {
                        loginForm
                            .padding(.horizontal, isIPad ? 60 : 24)

                        Spacer(minLength: 40)
                    }
                    .padding(.top, 20)
                }
            }
        }
        .luxorThemedBackground(themeManager)
        .ignoresSafeArea(.all, edges: .bottom)
        .onAppear {
            viewModel.updateAuthState(authState)
        }
        .sheet(isPresented: $viewModel.showForgotPassword) {
            ForgotPasswordView(authUseCase: diContainer.authUseCase)
                .environmentObject(themeManager)
                .environmentObject(languageManager)
        }
    }

    @ViewBuilder
    private var loginForm: some View {
        VStack(spacing: 24) {
            VStack(spacing: 4) {
                LuxorEmailTextField(email: $viewModel.email)
                    .environmentObject(themeManager)
                    .onChange(of: viewModel.email) { _ in
                        viewModel.validateFields()
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

            VStack(spacing: 4) {
                LuxorPasswordTextField(password: $viewModel.password)
                    .environmentObject(themeManager)
                    .onChange(of: viewModel.password) { _ in
                        viewModel.validateFields()
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
            HStack {
                Spacer()
                Button(action: {
                    viewModel.forgotPassword()
                }) {
                    Text(
                        LocalizedStrings.forgotPassword(
                            languageManager.currentLanguage
                        )
                    )
                    .font(.system(size: 14, weight: .medium))
                    .foregroundColor(LuxorColors.luxorTeal)
                }
            }
            .padding(.top, -8)
            Button(action: {
                viewModel.login()
            }) {
                HStack {
                    if authState.isLoading {
                        ProgressView()
                            .progressViewStyle(
                                CircularProgressViewStyle(tint: .white)
                            )
                            .scaleEffect(0.8)
                    } else {
                        Text(
                            LocalizedStrings.SignInToYourAccount(
                                languageManager.currentLanguage
                            )
                        )
                        .font(.system(size: 18, weight: .semibold))
                    }
                }
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .frame(height: 56)
                .background(
                    RoundedRectangle(cornerRadius: 12)
                        .fill(
                            LinearGradient(
                                colors: viewModel.isFormValid
                                    ? [
                                        LuxorColors.luxorTeal,
                                        LuxorColors.luxorGold,
                                    ]
                                    : [
                                        Color.gray.opacity(0.5),
                                        Color.gray.opacity(0.3),
                                    ],
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                        )
                )
                .disabled(!viewModel.isFormValid || authState.isLoading)
                .scaleEffect(authState.isLoading ? 0.95 : 1.0)
                .animation(
                    .easeInOut(duration: 0.1),
                    value: authState.isLoading
                )
            }

            HStack {
                Rectangle()
                    .fill(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.textSecondary.opacity(0.3)
                            : LuxorColors.Light.textSecondary.opacity(0.3)
                    )
                    .frame(height: 1)

                Text(LocalizedStrings.OR(languageManager.currentLanguage))
                    .font(.system(size: 12, weight: .medium))
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.textSecondary
                            : LuxorColors.Light.textSecondary
                    )
                    .padding(.horizontal, 16)

                Rectangle()
                    .fill(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.textSecondary.opacity(0.3)
                            : LuxorColors.Light.textSecondary.opacity(0.3)
                    )
                    .frame(height: 1)
            }
            .padding(.vertical, 8)

            SocialLoginButtons()
                .environmentObject(themeManager)

            VStack(spacing: 16) {
                Rectangle()
                    .fill(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.textSecondary.opacity(0.2)
                            : LuxorColors.Light.textSecondary.opacity(0.2)
                    )
                    .frame(height: 1)
                    .padding(.horizontal, 20)

                Button(action: {
                    viewModel.createAccount()
                }) {
                    NavigationLink(
                        destination: RegisterView().environmentObject(
                            themeManager
                        )
                    ) {
                        HStack {
                            Image(systemName: "person.badge.plus")
                                .font(.system(size: 16, weight: .medium))

                            Text(
                                LocalizedStrings.CreateAccount(
                                    languageManager.currentLanguage
                                )
                            )
                            .font(.system(size: 16, weight: .semibold))
                        }
                        .foregroundColor(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.textPrimary
                                : LuxorColors.Light.textPrimary
                        )
                        .frame(maxWidth: .infinity)
                        .frame(height: 52)
                        .background(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(
                                    LinearGradient(
                                        colors: [
                                            LuxorColors.luxorTeal,
                                            LuxorColors.luxorGold,
                                        ],
                                        startPoint: .leading,
                                        endPoint: .trailing
                                    ),
                                    lineWidth: 2
                                )
                                .background(
                                    RoundedRectangle(cornerRadius: 12)
                                        .fill(
                                            themeManager.isDarkMode
                                                ? LuxorColors.Dark
                                                    .cardBackground.opacity(0.5)
                                                : LuxorColors.Light
                                                    .cardBackground.opacity(0.8)
                                        )
                                )
                        )
                    }
                }

                VStack(spacing: 4) {
                    Text(
                        LocalizedStrings.ByCreatingAccount(
                            languageManager.currentLanguage
                        )
                    )
                    .font(.system(size: 12, weight: .regular))
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.textSecondary
                            : LuxorColors.Light.textSecondary
                    )

                    HStack(spacing: 4) {
                        Button(action: {
                            viewModel.showTerms()
                        }) {
                            Text(
                                LocalizedStrings.TermsOfServices(
                                    languageManager.currentLanguage
                                )
                            )
                            .font(.system(size: 12, weight: .medium))
                            .foregroundColor(LuxorColors.luxorTeal)
                            .underline()
                        }

                        Text(
                            LocalizedStrings.And(
                                languageManager.currentLanguage
                            )
                        )
                        .font(.system(size: 12, weight: .regular))
                        .foregroundColor(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.textSecondary
                                : LuxorColors.Light.textSecondary
                        )

                        Button(action: {
                            viewModel.showPrivacyPolicy()
                        }) {
                            Text(
                                LocalizedStrings.PrivacyAndPolicy(
                                    languageManager.currentLanguage
                                )
                            )
                            .font(.system(size: 12, weight: .medium))
                            .foregroundColor(LuxorColors.luxorTeal)
                            .underline()
                        }
                    }
                }
                .padding(.top, 8)
            }
            .padding(.top, 16)
        }
        .frame(maxWidth: isIPad ? 500 : .infinity)
    }
}

#Preview {
    LoginView()
        .environmentObject(ThemeManager())
        .environmentObject(AuthState())
        .environmentObject(DIContainer.shared)
        .environmentObject(LanguageManager())
}
