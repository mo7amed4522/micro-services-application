//
//  ContentView.swift
//  Luxor-App
//
//  Created by Standard User on 16/08/2025.
//

import SwiftUI

struct ContentView: View {
    @StateObject private var themeManager = ThemeManager()
    @State private var isLoggedIn = false
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
                .environmentObject(themeManager)
            } else if !hasSeenWelcome {
                WelcomeContainerView {
                    withAnimation(.easeInOut(duration: 0.5)) {
                        hasSeenWelcome = true
                    }
                }
                .environmentObject(themeManager)
            } else if isLoggedIn {
                MainTabView()
                    .environmentObject(themeManager)
            } else {
                LoginView(isLoggedIn: $isLoggedIn)
                    .environmentObject(themeManager)
            }
        }
        .animation(.easeInOut(duration: 0.5), value: showSplash)
        .animation(.easeInOut(duration: 0.5), value: hasSeenWelcome)
        .animation(.easeInOut(duration: 0.5), value: isLoggedIn)
    }
}

#Preview {
    ContentView()
}
