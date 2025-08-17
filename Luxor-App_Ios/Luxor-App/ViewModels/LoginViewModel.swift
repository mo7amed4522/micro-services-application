import SwiftUI
import Combine

class LoginViewModel: ObservableObject {
    @Published var email = ""
    @Published var password = ""
    @Published var isLoading = false
    @Published var showError = false
    @Published var errorMessage = ""
    
    private var cancellables = Set<AnyCancellable>()
    
    var isFormValid: Bool {
        !email.isEmpty && !password.isEmpty && isValidEmail(email) && isValidPassword(password)
    }
    
    func login() {
        guard isFormValid else {
            showError("Please fill in all fields correctly")
            return
        }
        
        isLoading = true
        
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            self.isLoading = false
        }
    }
    
    private func isValidEmail(_ email: String) -> Bool {
        let emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let emailPredicate = NSPredicate(format: "SELF MATCHES %@", emailRegex)
        return emailPredicate.evaluate(with: email)
    }
    
    private func isValidPassword(_ password: String) -> Bool {
        let hasCapital = password.range(of: "[A-Z]", options: .regularExpression) != nil
        let hasMinLength = password.count >= 8
        let hasSpecial = password.range(of: "[^A-Za-z0-9]", options: .regularExpression) != nil
        return hasCapital && hasMinLength && hasSpecial
    }
    
    private func showError(_ message: String) {
        errorMessage = message
        showError = true
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