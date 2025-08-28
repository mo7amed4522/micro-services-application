//
//  RootView.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import SwiftUI

struct RootView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var languageManager: LanguageManager
    @EnvironmentObject var authState: AuthState
    @EnvironmentObject var diContainer: DIContainer
    
    var body: some View {
        ContentView()
            .withToast()
            .withLoading()
    }
}

#Preview {
    RootView()
        .environmentObject(ThemeManager())
        .environmentObject(LanguageManager())
        .environmentObject(AuthState())
        .environmentObject(DIContainer.shared)
}