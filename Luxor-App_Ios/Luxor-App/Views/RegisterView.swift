import SwiftUI

struct RegisterView: View {
    @StateObject private var viewModel = RegisterViewModel()
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var languageManager: LanguageManager
    @Environment(\.dismiss) var dismiss
    @Environment(\.horizontalSizeClass) var horizontalSizeClass
    @Environment(\.verticalSizeClass) var verticalSizeClass
    
    private var isIPad: Bool {
        horizontalSizeClass == .regular && verticalSizeClass == .regular
    }
    
    var body: some View {
        GeometryReader { geometry in
            VStack(spacing: 0) {
                
                customAppBar
                
                ScrollView {
                    VStack(spacing: 24) {
                        
                        profilePhotoSection
                            .padding(.top, 20)
                        
                        
                        registrationForm
                            .padding(.horizontal, isIPad ? 60 : 24)
                        
                        Spacer(minLength: 40)
                    }
                }
            }
        }
        .luxorThemedBackground(themeManager)
        .navigationBarHidden(true)
    }
    
    @ViewBuilder
    private var customAppBar: some View {
        HStack {
            Button(action: {
                dismiss()
            }) {
                ZStack {
                    Circle()
                        .fill(
                            themeManager.isDarkMode ?
                            LuxorColors.Dark.cardBackground :
                            Color.white
                        )
                        .frame(width: 44, height: 44)
                        .shadow(
                            color: themeManager.isDarkMode ? LuxorColors.Dark.shadow : LuxorColors.Light.shadow,
                            radius: 8,
                            x: 0,
                            y: 2
                        )
                    
                    Image(systemName: "chevron.left")
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                }
            }
            
            Spacer()
            
            Text(LocalizedStrings.CreateAccount(languageManager.currentLanguage))
                .font(.system(size: 20, weight: .bold))
                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
            
            Spacer()
            
            
            ZStack {
                Circle()
                    .fill(
                        LinearGradient(
                            colors: [LuxorColors.luxorTeal, LuxorColors.luxorGold],
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        )
                    )
                    .frame(width: 44, height: 44)
                    .shadow(
                        color: LuxorColors.luxorTeal.opacity(0.3),
                        radius: 8,
                        x: 0,
                        y: 2
                    )
                
                Image("logo-nobg")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 32, height: 32)
                    .clipShape(Circle())
            }
        }
        .padding(.horizontal, 20)
        .padding(.top, 10)
        .padding(.bottom, 16)
        .background(
            (themeManager.isDarkMode ? LuxorColors.Dark.background : LuxorColors.Light.background)
                .ignoresSafeArea(.all, edges: .top)
        )
    }
    
    @ViewBuilder
    private var profilePhotoSection: some View {
        VStack(spacing: 16) {
            ZStack {
                Circle()
                    .fill(
                        LinearGradient(
                            colors: [
                                themeManager.isDarkMode ? LuxorColors.Dark.cardBackground : Color(UIColor.systemGray6),
                                themeManager.isDarkMode ? LuxorColors.Dark.inputBackground : Color.white
                            ],
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        )
                    )
                    .frame(width: 120, height: 120)
                    .overlay(
                        Circle()
                            .stroke(
                                LinearGradient(
                                    colors: [LuxorColors.luxorTeal, LuxorColors.luxorGold],
                                    startPoint: .topLeading,
                                    endPoint: .bottomTrailing
                                ),
                                lineWidth: 3
                            )
                    )
                    .shadow(
                        color: themeManager.isDarkMode ? LuxorColors.Dark.shadow : LuxorColors.Light.shadow,
                        radius: 12,
                        x: 0,
                        y: 6
                    )
                
                if let profileImage = viewModel.profileImage {
                    Image(uiImage: profileImage)
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(width: 114, height: 114)
                        .clipShape(Circle())
                } else {
                    Image(systemName: "person.fill")
                        .font(.system(size: 40, weight: .medium))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                }
                Button(action: {
                    viewModel.showImagePicker = true
                }) {
                    ZStack {
                        Circle()
                            .fill(
                                LinearGradient(
                                    colors: [LuxorColors.luxorTeal, LuxorColors.luxorGold],
                                    startPoint: .topLeading,
                                    endPoint: .bottomTrailing
                                )
                            )
                            .frame(width: 36, height: 36)
                            .shadow(
                                color: LuxorColors.luxorTeal.opacity(0.4),
                                radius: 8,
                                x: 0,
                                y: 2
                            )
                        
                        Image(systemName: "camera.fill")
                            .font(.system(size: 16, weight: .semibold))
                            .foregroundColor(.white)
                    }
                }
                .offset(x: 40, y: 42)
            }
            
            Text(LocalizedStrings.AddProfilePhoto(languageManager.currentLanguage))
                .font(.system(size: 16, weight: .medium))
                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
        }
        .onTapGesture {
            viewModel.showImagePicker = true
        }
    }
    
    @ViewBuilder
    private var registrationForm: some View {
        VStack(spacing: 24) {
            LuxorFullNameTextField(fullName: $viewModel.fullName)
                .environmentObject(themeManager)
            
            LuxorEmailTextField(email: $viewModel.email)
                .environmentObject(themeManager)
            LuxorPhoneTextField(phoneNumber: $viewModel.phoneNumber)
                .environmentObject(themeManager)
            LuxorPasswordTextField(password: $viewModel.password)
                .environmentObject(themeManager)
            
            Button(action: {
                viewModel.register()
            }) {
                HStack {
                    if viewModel.isLoading {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: .white))
                            .scaleEffect(0.8)
                    } else {
                        Text(LocalizedStrings.CreateAccount(languageManager.currentLanguage))
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
                                colors: viewModel.isFormValid ?
                                    [LuxorColors.luxorTeal, LuxorColors.luxorGold] :
                                    [Color.gray.opacity(0.5), Color.gray.opacity(0.3)],
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                        )
                )
                .disabled(!viewModel.isFormValid || viewModel.isLoading)
                .scaleEffect(viewModel.isLoading ? 0.95 : 1.0)
                .animation(.easeInOut(duration: 0.1), value: viewModel.isLoading)
            }
            
            VStack(spacing: 8) {
                Text(LocalizedStrings.ByCreatingAccount(languageManager.currentLanguage))
                    .font(.system(size: 12, weight: .regular))
                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                
                HStack(spacing: 4) {
                    Button(action: {
                        viewModel.showTerms()
                    }) {
                        Text(LocalizedStrings.TermsOfServices(languageManager.currentLanguage))
                            .font(.system(size: 12, weight: .medium))
                            .foregroundColor(LuxorColors.luxorTeal)
                            .underline()
                    }
                    
                    Text(LocalizedStrings.And(languageManager.currentLanguage))
                        .font(.system(size: 12, weight: .regular))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                    
                    Button(action: {
                        viewModel.showPrivacyPolicy()
                    }) {
                        Text(LocalizedStrings.PrivacyAndPolicy(languageManager.currentLanguage))
                            .font(.system(size: 12, weight: .medium))
                            .foregroundColor(LuxorColors.luxorTeal)
                            .underline()
                    }
                }
            }
            .padding(.top, 8)
        }
        .frame(maxWidth: isIPad ? 500 : .infinity)
        .sheet(isPresented: $viewModel.showImagePicker) {
            ImagePicker(image: $viewModel.profileImage)
        }
    }
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
