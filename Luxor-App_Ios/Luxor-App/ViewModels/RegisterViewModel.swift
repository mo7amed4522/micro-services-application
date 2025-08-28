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
    @Published var showImagePicker = false
    

    @Published var fullNameError: String?
    @Published var emailError: String?
    @Published var passwordError: String?
    @Published var confirmPasswordError: String?
    @Published var phoneError: String?
    @Published var photoError: String?
    
    private let grpcClient = GRPCClientManager()
    private var languageManager: LanguageManager?
    private var cancellables = Set<AnyCancellable>()
    
    init(languageManager: LanguageManager? = nil) {
        self.languageManager = languageManager
        setupValidation()
    }
    
    func setLanguageManager(_ languageManager: LanguageManager) {
        self.languageManager = languageManager
    }
    
    private func setupValidation() {
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
    
    
    private func validateFullName(_ name: String) {
        let currentLang = languageManager?.currentLanguage ?? .english
        if name.isEmpty {
            fullNameError = LocalizedStrings.fullNameRequired(currentLang)
        } else if name.count < 2 {
            fullNameError = LocalizedStrings.fullNameMinLength(currentLang)
        } else {
            fullNameError = nil
        }
    }
    private func validateEmail(_ email: String) {
        let currentLang = languageManager?.currentLanguage ?? .english
        if email.isEmpty {
            emailError = LocalizedStrings.emailRequired(currentLang)
        } else if !isValidEmail(email) {
            emailError = LocalizedStrings.enterValidEmail(currentLang)
        } else {
            emailError = nil
        }
    }
    private func validatePassword(_ password: String) {
        let currentLang = languageManager?.currentLanguage ?? .english
        if password.isEmpty {
            passwordError = LocalizedStrings.passwordRequired(currentLang)
        } else if password.count < 8 {
            passwordError = LocalizedStrings.passwordRequirements(currentLang)
        } else if !password.contains(where: { $0.isUppercase }) {
            passwordError = LocalizedStrings.passwordUppercase(currentLang)
        } else if !password.contains(where: { $0.isLowercase }) {
            passwordError = LocalizedStrings.passwordLowercase(currentLang)
        } else if !password.contains(where: { $0.isNumber }) {
            passwordError = LocalizedStrings.passwordNumber(currentLang)
        } else {
            passwordError = nil
        }
    }
    private func validateConfirmPassword(_ confirmPassword: String) {
        let currentLang = languageManager?.currentLanguage ?? .english
        if confirmPassword.isEmpty {
            confirmPasswordError = LocalizedStrings.confirmPasswordRequired(currentLang)
        } else if confirmPassword != password {
            confirmPasswordError = LocalizedStrings.passwordsDoNotMatch(currentLang)
        } else {
            confirmPasswordError = nil
        }
    }
    private func validatePhone(_ phone: String) {
        if phone.isEmpty {
            phoneError = LocalizedStrings.phoneNumberRequired(languageManager!.currentLanguage)
        } else if phone.count < 10 {
            phoneError = LocalizedStrings.phoneNumberRequired(languageManager!.currentLanguage)
        } else {
            phoneError = nil
        }
    }
    private func validatePhoto() {
        if profileImage == nil {
            photoError = LocalizedStrings.profilePhotoRequired(languageManager!.currentLanguage )
        } else {
            photoError = nil
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
            let currentLang = languageManager?.currentLanguage ?? .english
            showErrorToast("\(LocalizedStrings.registrationFailed(currentLang)) \(error.localizedDescription)")
        }
    }
    
    func register() {
        Task {
            await performRegistration()
        }
    }
    
    private func performRegistration() async {
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
            UserDataManager.shared.saveUserData(
                userId: userId,
                fullName: fullName,
                email: email,
                phoneNumber: phoneNumber,
                countryCode: countryCode,
                photoURL: photoURL
            )
            
            let currentLang = languageManager?.currentLanguage ?? .english
            showToast(message: LocalizedStrings.registrationSuccessful(currentLang), type: .success)
            
        } catch {
            let currentLang = languageManager?.currentLanguage ?? .english
            showToast(message: "\(LocalizedStrings.registrationFailed(currentLang)) \(error.localizedDescription)", type: .error)
        }
        
        isLoading = false
    }
    
    private func uploadProfilePhoto() async throws {
        guard let image = profileImage,
              let imageData = image.jpegData(compressionQuality: 0.8) else {
            let currentLang = languageManager?.currentLanguage ?? .english
            throw RegistrationError.invalidPhoto(currentLang)
        }
        let fileName = "profile_\(UUID().uuidString).jpg"
        let tempUserId = UUID().uuidString
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
            let currentLang = languageManager?.currentLanguage ?? .english
            throw RegistrationError.uploadFailed(errorDetails.message, currentLang)
        } else {
            let currentLang = languageManager?.currentLanguage ?? .english
            throw RegistrationError.uploadFailed(LocalizedStrings.unknownUploadError(currentLang), currentLang)
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
        
        let currentLang = languageManager?.currentLanguage ?? .english
        if response.statusCode == 200,
           case .user(let user) = response.result {
            print("\(LocalizedStrings.userCreatedWithID(currentLang)) \(user.id)")
        } else if case .error(let errorDetails) = response.result {
            throw RegistrationError.userCreationFailed(errorDetails.message, currentLang)
        } else {
            throw RegistrationError.userCreationFailed(LocalizedStrings.unknownUserCreationError(currentLang), currentLang)
        }
    }
    private func showSuccessAndVerificationOptions() {
        let currentLang = languageManager?.currentLanguage ?? .english
        showSuccessToast(LocalizedStrings.registrationSuccessfulWithVerification(currentLang))
        showVerificationOptions = true
    }
    private func handleRegistrationError(_ error: Error) {
        if let registrationError = error as? RegistrationError {
            showErrorToast(registrationError.localizedDescription)
        } else {
            let currentLang = languageManager?.currentLanguage ?? .english
            showErrorToast("\(LocalizedStrings.registrationFailed(currentLang)) \(error.localizedDescription)")
        }
    }
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
    func sendVerificationCode(via method: VerificationMethod) async {
        isLoading = true
        
        do {
            let methodString = method.rawValue
            let response = try await grpcClient.forgetPassword(email: email, type: methodString)
            
            if response.statusCode == 200 {
                showSuccessToast("\(LocalizedStrings.verificationCodeSentVia(languageManager!.currentLanguage))\(method.displayName)")
            } else {
                showErrorToast(LocalizedStrings.failedToSendVerificationCode(languageManager!.currentLanguage))
            }
        } catch {
            showErrorToast("\(LocalizedStrings.errorSendingVerificationCode(languageManager!.currentLanguage)) \(error.localizedDescription)")
        }
        
        isLoading = false
        showVerificationOptions = false
    }

    func validateFields() {
        validateFullName(fullName)
        validateEmail(email)
        validatePassword(password)
        validateConfirmPassword(confirmPassword)
        validatePhone(phoneNumber)
    }
    
    var isFormValid: Bool {
        return !fullName.isEmpty &&
               !email.isEmpty &&
               !password.isEmpty &&
               !confirmPassword.isEmpty &&
               !phoneNumber.isEmpty &&
               fullNameError == nil &&
               emailError == nil &&
               passwordError == nil &&
               confirmPasswordError == nil &&
               phoneError == nil
    }
    
    private func validateAllInputs() -> Bool {
        validateFullName(fullName)
        validateEmail(email)
        validatePassword(password)
        validateConfirmPassword(confirmPassword)
        validatePhone(phoneNumber)
        validatePhoto()
        
        return fullNameError == nil &&
               emailError == nil &&
               passwordError == nil &&
               confirmPasswordError == nil &&
               phoneError == nil &&
               photoError == nil
    }
    
    private func showToast(message: String, type: ToastType) {
        toastMessage = message
        toastType = type
        showToast = true
    }
    
    func showTerms() {
        print("Terms of service tapped")
        // TODO: Implement terms of service navigation
    }
    
    func showPrivacyPolicy() {
        print("Privacy policy tapped")
        // TODO: Implement privacy policy navigation
    }
}

enum RegistrationError: LocalizedError {
    case invalidPhoto(SupportedLanguage)
    case uploadFailed(String, SupportedLanguage)
    case userCreationFailed(String, SupportedLanguage)
    case invalidUserResponse(SupportedLanguage)
    
    var errorDescription: String? {
        switch self {
        case .invalidPhoto(let language):
            return LocalizedStrings.invalidPhotoSelected(language)
        case .uploadFailed(let message, let language):
            return "\(LocalizedStrings.photoUploadFailed(language)) \(message)"
        case .userCreationFailed(let message, let language):
            return "\(LocalizedStrings.userCreationFailed(language)) \(message)"
        case .invalidUserResponse(let language):
            return LocalizedStrings.invalidUserResponse(language)
        }
    }
}
enum VerificationMethod: String, CaseIterable {
    case email = "email"
    case sms = "sms"
    case whatsapp = "whatsapp"
    
    func displayName(_ language: SupportedLanguage) -> String {
        switch self {
        case .email: return LocalizedStrings.email(language)
        case .sms: return LocalizedStrings.sms(language)
        case .whatsapp: return LocalizedStrings.whatsapp(language)
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
