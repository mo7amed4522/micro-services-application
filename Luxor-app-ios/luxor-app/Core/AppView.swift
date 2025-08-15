import SwiftUI
import ComposableArchitecture

struct AppView: View {
    let store: StoreOf<AppFeature>
    
    var body: some View {
        WithViewStore(store, observe: { $0 }) { viewStore in
            Group {
                switch viewStore.currentScreen {
                case .splash:
                    SplashView {
                        viewStore.send(.splashFinished)
                    }
                case .login:
                    LoginView()
                }
            }
            .preferredColorScheme(viewStore.colorScheme)
            .environment(\.locale, Locale(identifier: viewStore.selectedLanguage.rawValue))
        }
    }
}

#Preview {
    AppView(
        store: Store(initialState: AppFeature.State()) {
            AppFeature()
        }
    )
}
