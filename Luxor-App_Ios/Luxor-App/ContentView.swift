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
    
    var body: some View {
        Group {
            if isLoggedIn {
                MainTabView()
                    .environmentObject(themeManager)
            } else {
                LoginView(isLoggedIn: $isLoggedIn)
                    .environmentObject(themeManager)
            }
        }
    }
}

#Preview {
    ContentView()
}
