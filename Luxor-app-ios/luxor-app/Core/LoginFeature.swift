import ComposableArchitecture
import SwiftUI

@Reducer
struct LoginFeature {
    @ObservableState
    struct State: Equatable {
        var email = ""
        var password = ""
        var isPasswordVisible = false
        var emailValidation: ValidationState = .none
        var passwordValidation: PasswordValidation = PasswordValidation()
        var isLoading = false
        
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
    }
    
    enum Action {
        case emailChanged(String)
        case passwordChanged(String)
        case togglePasswordVisibility
        case loginTapped
        case forgotPasswordTapped
        case signUpTapped
    }
    
    var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case .emailChanged(let email):
                state.email = email
                state.emailValidation = validateEmail(email)
                return .none
                
            case .passwordChanged(let password):
                state.password = password
                state.passwordValidation = validatePassword(password)
                return .none
                
            case .togglePasswordVisibility:
                state.isPasswordVisible.toggle()
                return .none
                
            case .loginTapped:
                state.isLoading = true
                return .run { send in
                    try await Task.sleep(nanoseconds: 2_000_000_000)
                }
                
            case .forgotPasswordTapped:
                return .none
                
            case .signUpTapped:
                return .none
            }
        }
    }
    
    private func validateEmail(_ email: String) -> State.ValidationState {
        if email.isEmpty {
            return .none
        }
        let emailRegex = "^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        let emailPredicate = NSPredicate(format: "SELF MATCHES %@", emailRegex)
        return emailPredicate.evaluate(with: email) ? .valid : .invalid
    }
    
    private func validatePassword(_ password: String) -> State.PasswordValidation {
        var validation = State.PasswordValidation()
        validation.hasMinLength = password.count >= 8
        validation.hasUppercase = password.range(of: "[A-Z]", options: .regularExpression) != nil
        validation.hasLowercase = password.range(of: "[a-z]", options: .regularExpression) != nil
        validation.hasSpecialChar = password.range(of: "[^A-Za-z0-9]", options: .regularExpression) != nil
        return validation
    }
}