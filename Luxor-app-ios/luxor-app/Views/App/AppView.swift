import SwiftUI

struct AppView: View {
    @StateObject private var viewModel = AppViewModel()
    
    var body: some View {
        Group {
            switch viewModel.currentScreen {
            case .splash:
                SplashView {
                    viewModel.splashFinished()
                }
            case .login:
                LoginView()
            case .main:
                MainTabView()
            }
        }
        .preferredColorScheme(viewModel.colorScheme)
        .environment(\.locale, Locale(identifier: viewModel.selectedLanguage.rawValue))
    }
}

#Preview {
    AppView()
}
