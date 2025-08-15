import Foundation
import Combine

class LoginViewModel: ObservableObject {
    @Published var email = ""
    @Published var password = ""
    @Published var isPasswordVisible = false
    @Published var emailValidation: ValidationState = .none
    @Published var passwordValidation: PasswordValidation = PasswordValidation()
    @Published var isLoading = false
    
    enum ValidationState: Equatable {
        case none
        case valid
        case invalid
    }
    
    struct PasswordValidation: Equatable {
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
    
    init() {
        setupValidation()
    }
    
    private func setupValidation() {
        $email
            .debounce(for: .milliseconds(300), scheduler: RunLoop.main)
            .map { self.validateEmail($0) }
            .assign(to: &$emailValidation)
        
        $password
            .debounce(for: .milliseconds(300), scheduler: RunLoop.main)
            .map { self.validatePassword($0) }
            .assign(to: &$passwordValidation)
    }
    
    func togglePasswordVisibility() {
        isPasswordVisible.toggle()
    }
    
    func login() {
        guard isFormValid else { return }
        
        isLoading = true
        
        // Simulate API call
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            self.isLoading = false
            // Handle login result
        }
    }
    
    func forgotPassword() {
        // Handle forgot password
    }
    
    func signUp() {
        // Handle sign up
    }
    
    private func validateEmail(_ email: String) -> ValidationState {
        if email.isEmpty {
            return .none
        }
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