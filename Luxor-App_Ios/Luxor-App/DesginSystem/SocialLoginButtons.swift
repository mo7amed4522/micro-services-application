import SwiftUI
import AuthenticationServices

struct SocialLoginButtons: View {
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var authState: AuthState
    @EnvironmentObject var diContainer: DIContainer
    @StateObject private var appleSignInCoordinator = AppleSignInCoordinator()
    @StateObject private var googleSignInCoordinator = GoogleSignInCoordinator()
    @State private var isLoading = false
    
    var body: some View {
        VStack(spacing: 16) {
            Text("Or continue with")
                .font(.system(size: 14, weight: .medium))
                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
            
            HStack(spacing: 20) {
                SocialButton(
                    icon: "apple.logo",
                    color: themeManager.isDarkMode ? .white : .black,
                    action: { handleAppleSignIn() }
                )
                
                SocialButton(
                    icon: "globe",
                    color: .red,
                    action: { handleGoogleSignIn() }
                )
                
                SocialButton(
                    icon: "f.circle.fill",
                    color: .blue,
                    action: { /* Facebook Sign In */ }
                )
            }
        }
        .disabled(isLoading)
        .onAppear {
            appleSignInCoordinator.updateDependencies(
                grpcClient: diContainer.grpcClient,
                authState: authState
            )
            googleSignInCoordinator.updateDependencies(
                grpcClient: diContainer.grpcClient,
                authState: authState
            )
        }
    }
    
    private func handleAppleSignIn() {
        guard !isLoading else { return }
        
        appleSignInCoordinator.signIn { loading in
            isLoading = loading
        }
    }
    
    private func handleGoogleSignIn() {
        guard !isLoading else { return }
        
        googleSignInCoordinator.signIn { loading in
            isLoading = loading
        }
    }
}

struct SocialButton: View {
    let icon: String
    let color: Color
    let action: () -> Void
    @EnvironmentObject var themeManager: ThemeManager
    
    var body: some View {
        Button(action: action) {
            Image(systemName: icon)
                .font(.system(size: 20, weight: .medium))
                .foregroundColor(color)
                .frame(width: 50, height: 50)
                .background(
                    Circle()
                        .fill(themeManager.isDarkMode ? LuxorColors.Dark.cardBackground : LuxorColors.Light.cardBackground)
                        .shadow(
                            color: themeManager.isDarkMode ? LuxorColors.Dark.shadow : LuxorColors.Light.shadow,
                            radius: 5,
                            x: 0,
                            y: 2
                        )
                )
        }
        .scaleEffect(1.0)
        .animation(.easeInOut(duration: 0.1), value: false)
    }
}

#Preview {
    SocialLoginButtons()
        .environmentObject(ThemeManager())
}
