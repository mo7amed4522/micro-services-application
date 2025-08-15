import ComposableArchitecture
import SwiftUI

@Reducer
struct AppFeature {
    @ObservableState
    struct State: Equatable {
        var currentScreen: Screen = .splash
        var selectedLanguage: Language = .english
        var colorScheme: ColorScheme? = nil
        var loginState = LoginFeature.State()
        
        enum Screen {
            case splash
            case login
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
    }
    
    enum Action {
        case splashFinished
        case languageChanged(State.Language)
        case colorSchemeChanged(ColorScheme?)
        case login(LoginFeature.Action)
    }
    
    var body: some ReducerOf<Self> {
        Scope(state: \.loginState, action: \Action.Cases.login) {
            LoginFeature()
        }
        
        Reduce { state, action in
            switch action {
            case .splashFinished:
                state.currentScreen = .login
                return .none
                
            case .languageChanged(let language):
                state.selectedLanguage = language
                return .none
                
            case .colorSchemeChanged(let colorScheme):
                state.colorScheme = colorScheme
                return .none
                
            case .login:
                return .none
            }
        }
    }
}