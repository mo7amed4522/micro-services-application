//
//  Luxor_AppApp.swift
//  Luxor-App
//
//  Created by Standard User on 16/08/2025.
//

import SwiftUI

@main
struct Luxor_AppApp: App {
    @StateObject private var themeManager = ThemeManager()
    @StateObject private var languageManager = LanguageManager()
    @StateObject private var authState = AuthState()
    @StateObject private var diContainer = DIContainer.shared
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(themeManager)
                .environmentObject(languageManager)
                .environmentObject(authState)
                .environmentObject(diContainer)
                .withToast()
                .withLoading()
                .onAppear {
                    diContainer.setAuthState(authState)
                }
        }
    }
}
