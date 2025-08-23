import SwiftUI
import Combine

class LoginViewModel: ObservableObject {
    @Published var email = ""
    @Published var password = ""
    @Published var emailError: String?
    @Published var passwordError: String?
    
    private let authUseCase: AuthUseCase
    private let authState: AuthState
    private var cancellables = Set<AnyCancellable>()
    
    init(authUseCase: AuthUseCase, authState: AuthState) {
        self.authUseCase = authUseCase
        self.authState = authState
    }
    
    var isFormValid: Bool {
        !email.isEmpty && !password.isEmpty && 
        authUseCase.validateEmail(email) && 
        authUseCase.validatePassword(password)
    }
    
    func validateFields() {
        // Validate email
        if email.isEmpty {
            emailError = nil
        } else if !authUseCase.validateEmail(email) {
            emailError = "Please enter a valid email address"
        } else {
            emailError = nil
        }
        
        // Validate password
        if password.isEmpty {
            passwordError = nil
        } else if !authUseCase.validatePassword(password) {
            passwordError = "Password must be at least 8 characters with uppercase, and special character"
        } else {
            passwordError = nil
        }
    }
    
    func login() {
        validateFields()
        
        guard isFormValid else {
            authState.showErrorToast("Please fill in all fields correctly")
            return
        }
        
        authState.setLoading(true)
        
        authUseCase.login(email: email, password: password)
            .receive(on: DispatchQueue.main)
            .sink { [weak self] result in
                guard let self = self else { return }
                
                self.authState.setLoading(false)
                
                if result.isSuccess {
                    if let user = result.user, let tokens = result.tokens {
                        self.authState.login(user: user, tokens: tokens)
                        self.authState.showSuccessToast("Welcome back, \(user.fullName)!")
                        self.clearForm()
                    }
                } else {
                    let errorMsg = result.errorMessage ?? "Login failed. Please try again."
                    self.authState.showErrorToast(errorMsg)
                }
            }
            .store(in: &cancellables)
    }
    
    private func clearForm() {
        email = ""
        password = ""
        emailError = nil
        passwordError = nil
    }
    
    func updateAuthState(_ newAuthState: AuthState) {
        // This method allows updating the authState reference from the environment
        // We'll use the injected authState for all operations
    }
    
    
    func forgotPassword() {
        print("Forgot password tapped for email: \(email)")
    }
    
    func createAccount() {
        print("Create account tapped")
    }
    
    func showTerms() {
        print("Terms of service tapped")
    }
    
    func showPrivacyPolicy() {
        print("Privacy policy tapped")
    }
}