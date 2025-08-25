import SwiftUI
import PhotosUI

struct RegisterView: View {
    @StateObject private var viewModel = RegisterViewModel()
    @Environment(\.dismiss) private var dismiss
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 24) {
                    // Header
                    headerSection
                    
                    // Photo Upload Section
                    photoUploadSection
                    
                    // Form Fields
                    formFieldsSection
                    
                    // Register Button
                    registerButton
                    
                    // Login Link
                    loginLinkSection
                }
                .padding(.horizontal, 24)
                .padding(.vertical, 16)
            }
            .luxorBackground()
            .navigationBarHidden(true)
        }
        .toast(isPresented: $viewModel.showToast, type: viewModel.toastType, message: viewModel.toastMessage)
        .sheet(isPresented: $viewModel.showVerificationOptions) {
            verificationOptionsSheet
        }
        .overlay {
            if viewModel.isLoading {
                loadingOverlay
            }
        }
        .onChange(of: viewModel.selectedPhoto) { newValue in
            Task {
                await viewModel.loadSelectedPhoto(newValue)
            }
        }
    }
    
    // MARK: - Header Section
    private var headerSection: some View {
        VStack(spacing: 12) {
            Image("luxor_logo")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(height: 60)
            
            Text("Create Account")
                .font(.luxorTitle)
                .foregroundColor(.luxorGold)
            
            Text("Join the Luxor experience")
                .font(.luxorBody)
                .foregroundColor(.luxorSecondaryText)
        }
        .padding(.top, 20)
    }
    
    // MARK: - Photo Upload Section with Enhanced Validation
    private var photoUploadSection: some View {
        VStack(spacing: 16) {
            Text("Profile Photo")
                .font(.luxorHeadline)
                .foregroundColor(.luxorPrimaryText)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            PhotosPicker(
                selection: $viewModel.selectedPhoto,
                matching: .images,
                photoLibrary: .shared()
            ) {
                ZStack {
                    RoundedRectangle(cornerRadius: 16)
                        .fill(Color.luxorCardBackground)
                        .frame(height: 120)
                        .overlay(
                            RoundedRectangle(cornerRadius: 16)
                                .stroke(
                                    viewModel.photoError.isEmpty ? Color.luxorBorder : Color.red,
                                    lineWidth: viewModel.photoError.isEmpty ? 1 : 2
                                )
                        )
                        .animation(.easeInOut(duration: 0.3), value: viewModel.photoError.isEmpty)
                    
                    // Display uploaded user photo or placeholder
                    if let image = viewModel.profileImage {
                        Image(uiImage: image)
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(width: 100, height: 100)
                            .clipShape(Circle())
                            .overlay(
                                Circle()
                                    .stroke(Color.luxorGold, lineWidth: 3)
                            )
                            .shadow(color: .luxorGold.opacity(0.3), radius: 8, x: 0, y: 4)
                            .transition(.scale.combined(with: .opacity))
                            .animation(.spring(response: 0.6, dampingFraction: 0.8), value: viewModel.profileImage)
                    } else {
                        VStack(spacing: 8) {
                            Image(systemName: "person.crop.circle.fill.badge.plus")
                                .font(.system(size: 32))
                                .foregroundColor(.luxorGold)
                            
                            Text("Add Profile Photo")
                                .font(.luxorCaption)
                                .foregroundColor(.luxorSecondaryText)
                            
                            Text("Tap to select from gallery")
                                .font(.caption2)
                                .foregroundColor(.luxorSecondaryText.opacity(0.7))
                        }
                        .transition(.opacity)
                    }
                    
                    // Loading indicator for photo processing
                    if viewModel.isLoading && viewModel.selectedPhoto != nil {
                        RoundedRectangle(cornerRadius: 16)
                            .fill(Color.black.opacity(0.4))
                            .frame(height: 120)
                        
                        VStack(spacing: 8) {
                            ProgressView()
                                .scaleEffect(1.2)
                                .tint(.luxorGold)
                            
                            Text("Uploading...")
                                .font(.caption)
                                .foregroundColor(.white)
                        }
                    }
                    
                    // Success indicator when photo is uploaded
                    if viewModel.profileImage != nil && !viewModel.isLoading {
                        VStack {
                            HStack {
                                Spacer()
                                Image(systemName: "checkmark.circle.fill")
                                    .foregroundColor(.green)
                                    .background(Color.white, in: Circle())
                                    .font(.title2)
                            }
                            Spacer()
                        }
                        .padding(8)
                    }
                }
            }
            
            // Enhanced error message with animation
            if !viewModel.photoError.isEmpty {
                HStack {
                    Image(systemName: "exclamationmark.triangle.fill")
                        .foregroundColor(.red)
                        .font(.caption)
                    
                    Text(viewModel.photoError)
                        .font(.luxorCaption)
                        .foregroundColor(.red)
                    
                    Spacer()
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .transition(.move(edge: .top).combined(with: .opacity))
                .animation(.easeInOut(duration: 0.3), value: viewModel.photoError)
            }
        }
    }
    
    // MARK: - Form Fields Section
    private var formFieldsSection: some View {
        VStack(spacing: 20) {
            // Full Name Field
            LuxorTextField(
                title: "Full Name",
                text: $viewModel.fullName,
                placeholder: "Enter your full name",
                errorMessage: viewModel.fullNameError
            )
            
            // Email Field
            LuxorTextField(
                title: "Email",
                text: $viewModel.email,
                placeholder: "Enter your email",
                keyboardType: .emailAddress,
                errorMessage: viewModel.emailError
            )
            
            // Password Field
            LuxorSecureField(
                title: "Password",
                text: $viewModel.password,
                placeholder: "Enter your password",
                errorMessage: viewModel.passwordError
            )
            
            // Confirm Password Field
            LuxorSecureField(
                title: "Confirm Password",
                text: $viewModel.confirmPassword,
                placeholder: "Confirm your password",
                errorMessage: viewModel.confirmPasswordError
            )
            
            // Phone Number Section
            HStack(spacing: 12) {
                LuxorTextField(
                    title: "Country Code",
                    text: $viewModel.countryCode,
                    placeholder: "+1",
                    keyboardType: .phonePad,
                    errorMessage: ""
                )
                .frame(width: 100)
                
                LuxorTextField(
                    title: "Phone Number",
                    text: $viewModel.phoneNumber,
                    placeholder: "Enter phone number",
                    keyboardType: .phonePad,
                    errorMessage: viewModel.phoneError
                )
            }
        }
    }
    
    // MARK: - Register Button
    private var registerButton: some View {
        LuxorButton(
            title: "Create Account",
            style: .primary,
            isLoading: viewModel.isLoading
        ) {
            Task {
                await viewModel.register()
            }
        }
        .padding(.top, 8)
    }
    
    // MARK: - Login Link Section
    private var loginLinkSection: some View {
        HStack {
            Text("Already have an account?")
                .font(.luxorBody)
                .foregroundColor(.luxorSecondaryText)
            
            Button("Sign In") {
                dismiss()
            }
            .font(.luxorBodyBold)
            .foregroundColor(.luxorGold)
        }
        .padding(.top, 16)
    }
    
    // MARK: - Verification Options Sheet
    private var verificationOptionsSheet: some View {
        VStack(spacing: 24) {
            Text("Choose Verification Method")
                .font(.luxorTitle2)
                .foregroundColor(.luxorPrimaryText)
            
            Text("How would you like to receive your verification code?")
                .font(.luxorBody)
                .foregroundColor(.luxorSecondaryText)
                .multilineTextAlignment(.center)
            
            VStack(spacing: 16) {
                ForEach(VerificationMethod.allCases, id: \.self) { method in
                    LuxorButton(
                        title: "Send via \(method.displayName)",
                        style: .secondary,
                        icon: method.icon
                    ) {
                        Task {
                            await viewModel.sendVerificationCode(via: method)
                        }
                    }
                }
            }
            
            Button("Cancel") {
                viewModel.showVerificationOptions = false
            }
            .font(.luxorBody)
            .foregroundColor(.luxorSecondaryText)
            .padding(.top, 16)
        }
        .padding(24)
        .luxorBackground()
    }
    
    // MARK: - Loading Overlay
    private var loadingOverlay: some View {
        ZStack {
            Color.black.opacity(0.3)
                .ignoresSafeArea()
            
            VStack(spacing: 16) {
                ProgressView()
                    .scaleEffect(1.5)
                    .tint(.luxorGold)
                
                Text("Creating your account...")
                    .font(.luxorBody)
                    .foregroundColor(.luxorPrimaryText)
            }
            .padding(32)
            .background(
                RoundedRectangle(cornerRadius: 16)
                    .fill(Color.luxorCardBackground)
            )
        }
    }
}

#Preview {
    RegisterView()
        .preferredColorScheme(.dark)
}

// MARK: - RegisterViewModel
class RegisterViewModel: ObservableObject {
    @Published var fullName = ""
    @Published var email = ""
    @Published var phoneNumber = ""
    @Published var password = ""
    @Published var profileImage: UIImage?
    @Published var isLoading = false
    @Published var showImagePicker = false
    
    var isFormValid: Bool {
        !fullName.isEmpty &&
        !email.isEmpty &&
        !phoneNumber.isEmpty &&
        !password.isEmpty &&
        isValidEmail(email) &&
        isValidFullName(fullName) &&
        isValidPassword(password)
    }
    
    func register() {
        guard isFormValid else { return }
        
        isLoading = true
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            self.isLoading = false
            
            print("Registration attempted with:")
            print("Name: \(self.fullName)")
            print("Email: \(self.email)")
            print("Phone: \(self.phoneNumber)")
        }
    }
    
    func showTerms() {
        
        print("Show Terms of Service")
    }
    
    func showPrivacyPolicy() {
        
        print("Show Privacy Policy")
    }
    
    private func isValidEmail(_ email: String) -> Bool {
        let emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let emailPredicate = NSPredicate(format: "SELF MATCHES %@", emailRegex)
        return emailPredicate.evaluate(with: email)
    }
    
    private func isValidFullName(_ name: String) -> Bool {
        let trimmedName = name.trimmingCharacters(in: .whitespacesAndNewlines)
        let nameComponents = trimmedName.components(separatedBy: .whitespaces).filter { !$0.isEmpty }
        return nameComponents.count >= 2 && trimmedName.count >= 3
    }
    
    private func isValidPassword(_ password: String) -> Bool {
        return password.count >= 8 &&
               password.range(of: "[A-Z]", options: .regularExpression) != nil &&
               password.range(of: "[!@#$%^&*(),.?\":{}|<>]", options: .regularExpression) != nil
    }
}

// MARK: - ImagePicker
struct ImagePicker: UIViewControllerRepresentable {
    @Binding var image: UIImage?
    @Environment(\.dismiss) var dismiss
    
    func makeUIViewController(context: Context) -> UIImagePickerController {
        let picker = UIImagePickerController()
        picker.delegate = context.coordinator
        picker.sourceType = .photoLibrary
        picker.allowsEditing = true
        return picker
    }
    
    func updateUIViewController(_ uiViewController: UIImagePickerController, context: Context) {}
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    class Coordinator: NSObject, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
        let parent: ImagePicker
        
        init(_ parent: ImagePicker) {
            self.parent = parent
        }
        
        func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
            if let editedImage = info[.editedImage] as? UIImage {
                parent.image = editedImage
            } else if let originalImage = info[.originalImage] as? UIImage {
                parent.image = originalImage
            }
            parent.dismiss()
        }
        
        func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
            parent.dismiss()
        }
    }
}

#Preview {
    RegisterView()
        .environmentObject(ThemeManager())
        .environmentObject(LanguageManager())
}
