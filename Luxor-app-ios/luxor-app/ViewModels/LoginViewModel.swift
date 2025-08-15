import SwiftUI
import Combine

class LoginViewModel: ObservableObject {
    @Published var email = ""
    @Published var password = ""
    @Published var isPasswordVisible = false
    @Published var emailValidation: ValidationState = .none
    @Published var passwordValidation = PasswordValidation()
    @Published var isLoading = false
    @Published var errorMessage: String?
    @Published var isLoggedIn = false
    
    enum ValidationState: Equatable {
        case none, valid, invalid
    }
    
    struct PasswordValidation {
        var hasMinLength = false
        var hasUppercase = false
        var hasLowercase = false
        var hasSpecialChar = false
        
        var isValid: Bool {
            hasMinLength && hasUppercase && hasLowercase && hasSpecialChar
        }
    }
    
    var isFormValid: Bool {
        emailValidation == .valid && passwordValidation.isValid
    }
    
    private var cancellables = Set<AnyCancellable>()
    private let authService = AuthenticationService()
    
    init() {
        setupValidation()
    }
    
    private func setupValidation() {
        $email
            .map(validateEmail)
            .assign(to: &$emailValidation)
        
        $password
            .map(validatePassword)
            .assign(to: &$passwordValidation)
    }
    
    func togglePasswordVisibility() {
        isPasswordVisible.toggle()
    }
    
    func login() {
        guard isFormValid else { return }
        
        isLoading = true
        errorMessage = nil
        
        authService.login(email: email, password: password)
            .receive(on: DispatchQueue.main)
            .sink(
                receiveCompletion: { [weak self] completion in
                    self?.isLoading = false
                    if case .failure(let error) = completion {
                        self?.errorMessage = error.localizedDescription
                    }
                },
                receiveValue: { [weak self] user in
                    self?.isLoggedIn = true
                    // Save user session or navigate to main app
                    print("Login successful for user: \(user.fullName)")
                }
            )
            .store(in: &cancellables)
    }
    
    func forgotPassword() {
        guard !email.isEmpty else { return }
        
        authService.forgotPassword(email: email)
            .receive(on: DispatchQueue.main)
            .sink(
                receiveCompletion: { [weak self] completion in
                    if case .failure(let error) = completion {
                        self?.errorMessage = error.localizedDescription
                    }
                },
                receiveValue: { [weak self] _ in
                    self?.errorMessage = "Password reset email sent!"
                }
            )
            .store(in: &cancellables)
    }
    
    func signUp() {
        // Implement sign up navigation
        print("Navigate to sign up")
    }
    
    private func validateEmail(_ email: String) -> ValidationState {
        let emailRegex = "^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        let emailPredicate = NSPredicate(format: "SELF MATCHES %@", emailRegex)
        return emailPredicate.evaluate(with: email) ? .valid : .invalid
    }
    
    private func validatePassword(_ password: String) -> PasswordValidation {
        var validation = PasswordValidation()
        validation.hasMinLength = password.count >= 8
        validation.hasUppercase = password.range(of: "[A-Z]", options: .regularExpression) != nil
        validation.hasLowercase = password.range(of: "[a-z]", options: .regularExpression) != nil
        validation.hasSpecialChar = password.range(of: "[^A-Za-z0-9]", options: .regularExpression) != nil
        return validation
    }
}