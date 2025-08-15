//
//  luxor_appApp.swift
//  luxor-app
//
//  Created by 5ald on 07/08/2025.
//

import SwiftUI
import ComposableArchitecture

@main
struct luxor_appApp: App {
    var body: some Scene {
        WindowGroup {
            AppView(
                store: Store(initialState: AppFeature.State()) {
                    AppFeature()
                }
            )
        }
    }
}
