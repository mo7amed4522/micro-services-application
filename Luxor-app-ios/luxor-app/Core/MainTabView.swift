import SwiftUI

struct MainTabView: View {
    var body: some View {
        TabView {
            MainView()
                .tabItem {
                    Image(systemName: "house")
                    Text(LocalizedStringKey("home"))
                }
            
            SettingsView()
                .tabItem {
                    Image(systemName: "gear")
                    Text(LocalizedStringKey("settings"))
                }
        }
        .tint(LuxorTheme.primaryTeal)
    }
}

#Preview {
    MainTabView()
}