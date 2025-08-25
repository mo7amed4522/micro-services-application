import Foundation
import SwiftUI
import Combine
import PhotosUI

@MainActor
class RegisterViewModel: ObservableObject {
    @Published var fullName = ""
    @Published var email = ""
    @Published var password = ""
    @Published var confirmPassword = ""
    @Published var countryCode = ""
    @Published var phoneNumber = ""
    @Published var selectedPhoto: PhotosPickerItem?
    @Published var profileImage: UIImage?
    @Published var isLoading = false
    @Published var showToast = false
    @Published var toastMessage = ""
    @Published var toastType: ToastType = .success
    @Published var showVerificationOptions = false
    @Published var isPhotoUploaded = false
    @Published var uploadedPhotoURL = ""
    
    // Validation states
    @Published var fullNameError = ""
    @Published var emailError = ""
    @Published var passwordError = ""
    @Published var confirmPasswordError = ""
    @Published var phoneError = ""
    @Published var photoError = ""
    
    private let grpcClient = GRPCClientManager()
    private var cancellables = Set<AnyCancellable>()
    
    init() {
        setupValidation()
    }
    
    private func setupValidation() {
        // Real-time validation
        $fullName
            .debounce(for: .milliseconds(500), scheduler: RunLoop.main)
            .sink { [weak self] value in
                self?.validateFullName(value)
            }
            .store(in: &cancellables)
        
        $email
            .debounce(for: .milliseconds(500), scheduler: RunLoop.main)
            .sink { [weak self] value in
                self?.validateEmail(value)
            }
            .store(in: &cancellables)
        
        $password
            .debounce(for: .milliseconds(500), scheduler: RunLoop.main)
            .sink { [weak self] value in
                self?.validatePassword(value)
            }
            .store(in: &cancellables)
        
        $confirmPassword
            .debounce(for: .milliseconds(500), scheduler: RunLoop.main)
            .sink { [weak self] value in
                self?.validateConfirmPassword(value)
            }
            .store(in: &cancellables)
        
        $phoneNumber
            .debounce(for: .milliseconds(500), scheduler: RunLoop.main)
            .sink { [weak self] value in
                self?.validatePhone(value)
            }
            .store(in: &cancellables)
        
        $selectedPhoto
            .sink { [weak self] item in
                Task {
                    await self?.loadSelectedPhoto(item)
                }
            }
            .store(in: &cancellables)
    }
    
    // MARK: - Validation Methods
    
    private func validateFullName(_ name: String) {
        if name.isEmpty {
            fullNameError = "Full name is required"
        } else if name.count < 2 {
            fullNameError = "Full name must be at least 2 characters"
        } else {
            fullNameError = ""
        }
    }
    
    private func validateEmail(_ email: String) {
        if email.isEmpty {
            emailError = "Email is required"
        } else if !isValidEmail(email) {
            emailError = "Please enter a valid email address"
        } else {
            emailError = ""
        }
    }
    
    private func validatePassword(_ password: String) {
        if password.isEmpty {
            passwordError = "Password is required"
        } else if password.count < 8 {
            passwordError = "Password must be at least 8 characters"
        } else if !password.contains(where: { $0.isUppercase }) {
            passwordError = "Password must contain at least one uppercase letter"
        } else if !password.contains(where: { $0.isLowercase }) {
            passwordError = "Password must contain at least one lowercase letter"
        } else if !password.contains(where: { $0.isNumber }) {
            passwordError = "Password must contain at least one number"
        } else {
            passwordError = ""
        }
    }
    
    private func validateConfirmPassword(_ confirmPassword: String) {
        if confirmPassword.isEmpty {
            confirmPasswordError = "Please confirm your password"
        } else if confirmPassword != password {
            confirmPasswordError = "Passwords do not match"
        } else {
            confirmPasswordError = ""
        }
    }
    
    private func validatePhone(_ phone: String) {
        if phone.isEmpty {
            phoneError = "Phone number is required"
        } else if phone.count < 10 {
            phoneError = "Please enter a valid phone number"
        } else {
            phoneError = ""
        }
    }
    
    private func validatePhoto() {
        if profileImage == nil {
            photoError = "Profile photo is required"
        } else {
            photoError = ""
        }
    }
    
    private func isValidEmail(_ email: String) -> Bool {
        let emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let emailPredicate = NSPredicate(format: "SELF MATCHES %@", emailRegex)
        return emailPredicate.evaluate(with: email)
    }
    
    // MARK: - Photo Handling
    
    func loadSelectedPhoto(_ item: PhotosPickerItem?) async {
        guard let item = item else { return }
        
        do {
            if let data = try await item.loadTransferable(type: Data.self),
               let image = UIImage(data: data) {
                profileImage = image
                photoError = ""
            }
        } catch {
            showErrorToast("Failed to load selected photo: \(error.localizedDescription)")
        }
    }
    
    // MARK: - Registration Process
    
    func register() async {
        guard validateAllInputs() else { return }
        
        isLoading = true
        
        do {
            let userResponse = try await grpcClient.createUser(
                fullName: fullName,
                email: email,
                password: password,
                countryCode: countryCode,
                phoneNumber: phoneNumber
            )
            
            let userId = userResponse.user.id
            var photoURL: String? = nil
            if let profileImage = profileImage,
               let imageData = profileImage.jpegData(compressionQuality: 0.8) {
                
                let uploadResponse = try await grpcClient.uploadFile(
                    fileData: imageData,
                    fileName: "profile_\(userId).jpg",
                    contentType: "image/jpeg",
                    userId: userId,
                    fileType: .image
                )
                
                photoURL = uploadResponse.success.fileURL
            }
            
            // Step 3: Save all data to SharedPreferences
            UserDataManager.shared.saveUserData(
                userId: userId,
                fullName: fullName,
                email: email,
                phoneNumber: phoneNumber,
                countryCode: countryCode,
                photoURL: photoURL
            )
            
            // Step 4: Show success and navigate
            showToast(message: "Registration successful!", type: .success)
            
        } catch {
            showToast(message: "Registration failed: \(error.localizedDescription)", type: .error)
        }
        
        isLoading = false
    }
    
    private func uploadProfilePhoto() async throws {
        guard let image = profileImage,
              let imageData = image.jpegData(compressionQuality: 0.8) else {
            throw RegistrationError.invalidPhoto
        }
        
        let fileName = "profile_\(UUID().uuidString).jpg"
        let tempUserId = UUID().uuidString // Temporary ID for upload
        
        let response = try await grpcClient.uploadFile(
            fileData: imageData,
            fileName: fileName,
            contentType: "image/jpeg",
            userId: tempUserId,
            fileType: .image
        )
        
        if response.statusCode == 200,
           case .success(let uploadSuccess) = response.result {
            uploadedPhotoURL = uploadSuccess.fileURL
            isPhotoUploaded = true
        } else if case .error(let errorDetails) = response.result {
            throw RegistrationError.uploadFailed(errorDetails.message)
        } else {
            throw RegistrationError.uploadFailed("Unknown upload error")
        }
    }
    
    private func createUser() async throws {
        let response = try await grpcClient.createUser(
            fullName: fullName,
            email: email,
            password: password,
            countryCode: countryCode,
            phoneNumber: phoneNumber,
            
        )
        
        if response.statusCode == 200,
           case .user(let user) = response.result {
            print("User created with ID: \(user.id)")
        } else if case .error(let errorDetails) = response.result {
            throw RegistrationError.userCreationFailed(errorDetails.message)
        } else {
            throw RegistrationError.userCreationFailed("Unknown user creation error")
        }
    }
    
    private func showSuccessAndVerificationOptions() {
        showSuccessToast("Registration successful! Please verify your account.")
        showVerificationOptions = true
    }
    
    private func handleRegistrationError(_ error: Error) {
        if let registrationError = error as? RegistrationError {
            showErrorToast(registrationError.localizedDescription)
        } else {
            showErrorToast("Registration failed: \(error.localizedDescription)")
        }
    }
    
    // MARK: - Toast Methods
    
    private func showSuccessToast(_ message: String) {
        toastMessage = message
        toastType = .success
        showToast = true
    }
    
    private func showErrorToast(_ message: String) {
        toastMessage = message
        toastType = .error
        showToast = true
    }
    
    // MARK: - Verification Methods
    
    func sendVerificationCode(via method: VerificationMethod) async {
        isLoading = true
        
        do {
            let methodString = method.rawValue
            let response = try await grpcClient.forgetPassword(email: email, type: methodString)
            
            if response.statusCode == 200 {
                showSuccessToast("Verification code sent via \(method.displayName)")
            } else {
                showErrorToast("Failed to send verification code")
            }
        } catch {
            showErrorToast("Error sending verification code: \(error.localizedDescription)")
        }
        
        isLoading = false
        showVerificationOptions = false
    }
    
    // MARK: - Validation Helper
    private func validateAllInputs() -> Bool {
        validateFullName(fullName)
        validateEmail(email)
        validatePassword(password)
        validateConfirmPassword(confirmPassword)
        validatePhone(phoneNumber)
        validatePhoto()
        
        return fullNameError.isEmpty &&
               emailError.isEmpty &&
               passwordError.isEmpty &&
               confirmPasswordError.isEmpty &&
               phoneError.isEmpty &&
               photoError.isEmpty
    }
    
    // MARK: - Toast Helper
    private func showToast(message: String, type: ToastType) {
        toastMessage = message
        toastType = type
        showToast = true
    }
}

// MARK: - Supporting Types

enum RegistrationError: LocalizedError {
    case invalidPhoto
    case uploadFailed(String)
    case userCreationFailed(String)
    case invalidUserResponse
    
    var errorDescription: String? {
        switch self {
        case .invalidPhoto:
            return "Invalid photo selected"
        case .uploadFailed(let message):
            return "Photo upload failed: \(message)"
        case .userCreationFailed(let message):
            return "User creation failed: \(message)"
        case .invalidUserResponse:
            return "Invalid user response from server"
        }
    }
}

enum VerificationMethod: String, CaseIterable {
    case email = "email"
    case sms = "sms"
    case whatsapp = "whatsapp"
    
    var displayName: String {
        switch self {
        case .email: return "Email"
        case .sms: return "SMS"
        case .whatsapp: return "WhatsApp"
        }
    }
    
    var icon: String {
        switch self {
        case .email: return "envelope.fill"
        case .sms: return "message.fill"
        case .whatsapp: return "phone.fill"
        }
    }
}
