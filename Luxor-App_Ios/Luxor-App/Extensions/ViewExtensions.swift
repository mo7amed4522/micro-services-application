//
//  ViewExtensions.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import SwiftUI


extension View {
    func luxorBackground() -> some View {
        self.background(
            LinearGradient(
                colors: [
                    LuxorColors.Dark.background,
                    LuxorColors.Dark.secondaryBackground
                ],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        )
    }
    func toast(isPresented: Binding<Bool>, type: ToastType, message: String) -> some View {
        self.overlay(
            VStack {
                Spacer()
                
                if isPresented.wrappedValue {
                    ToastView(
                        message: message,
                        type: type
                    ) {
                        isPresented.wrappedValue = false
                    }
                    .padding(.horizontal, 20)
                    .padding(.bottom, 50)
                }
            }
            .animation(.spring(), value: isPresented.wrappedValue)
        )
    }
}
