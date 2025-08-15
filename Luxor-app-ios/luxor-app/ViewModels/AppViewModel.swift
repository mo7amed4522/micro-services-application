import SwiftUI
import Combine

class AppViewModel: ObservableObject {
    @Published var currentScreen: Screen = .splash
    @Published var selectedLanguage: Language = .english
    @Published var colorScheme: ColorScheme? = nil
    
    enum Screen {
        case splash, login, main
    }
    
    enum Language: String, CaseIterable {
        case english = "en"
        case arabic = "ar"
        case french = "fr"
        
        var displayName: String {
            switch self {
            case .english: return "English"
            case .arabic: return "العربية"
            case .french: return "Français"
            }
        }
    }
    
    func splashFinished() {
        currentScreen = .login
    }
    
    func loginSuccessful() {
        currentScreen = .main
    }
    
    func changeLanguage(_ language: Language) {
        selectedLanguage = language
    }
    
    func changeColorScheme(_ scheme: ColorScheme?) {
        colorScheme = scheme
    }
}