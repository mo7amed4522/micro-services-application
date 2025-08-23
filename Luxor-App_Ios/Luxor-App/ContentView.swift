//
//  ContentView.swift
//  Luxor-App
//
//  Created by Standard User on 16/08/2025.
//

import SwiftUI

struct ContentView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var authState: AuthState
    @State private var showSplash = true
    @State private var hasSeenWelcome = UserDefaults.standard.bool(forKey: "hasSeenWelcome")
    
    var body: some View {
        Group {
            if showSplash {
                SplashScreenView {
                    withAnimation(.easeInOut(duration: 0.5)) {
                        showSplash = false
                    }
                }
            } else if !hasSeenWelcome {
                WelcomeContainerView {
                    withAnimation(.easeInOut(duration: 0.5)) {
                        hasSeenWelcome = true
                        UserDefaults.standard.set(true, forKey: "hasSeenWelcome")
                    }
                }
            } else if authState.isAuthenticated {
                MainTabView()
            } else {
                LoginView()
            }
        }
        .animation(.easeInOut(duration: 0.5), value: showSplash)
        .animation(.easeInOut(duration: 0.5), value: hasSeenWelcome)
        .animation(.easeInOut(duration: 0.5), value: authState.isAuthenticated)
    }
}

#Preview {
    ContentView()
}
