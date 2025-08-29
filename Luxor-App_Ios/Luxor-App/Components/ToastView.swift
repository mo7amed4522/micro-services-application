//
//  ToastView.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import SwiftUI

struct ToastView: View {
    let message: String
    let type: ToastType
    let onDismiss: () -> Void

    @State private var offset: CGFloat = 100
    @State private var opacity: Double = 0
    @EnvironmentObject var languageManager: LanguageManager

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: type.icon)
                .foregroundColor(.white)
                .font(.system(size: 16, weight: .semibold))

            Text(message)
                .foregroundColor(.white)
                .font(.system(size: 14, weight: .medium))
                .multilineTextAlignment(.leading)

            Spacer()

            Button(action: dismissToast) {
                Image(systemName: "xmark")
                    .foregroundColor(.white.opacity(0.8))
                    .font(.system(size: 12, weight: .bold))
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(type.color.gradient)
                .shadow(color: type.color.opacity(0.3), radius: 8, x: 0, y: 4)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(type.color.opacity(0.2), lineWidth: 1)
        )
        .offset(y: offset)
        .opacity(opacity)
        .onAppear {
            showToast()
            DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                dismissToast()
            }
        }
    }

    private func showToast() {
        withAnimation(
            .spring(response: 0.6, dampingFraction: 0.8, blendDuration: 0)
        ) {
            offset = 0
            opacity = 1
        }
    }

    private func dismissToast() {
        withAnimation(
            .spring(response: 0.4, dampingFraction: 0.8, blendDuration: 0)
        ) {
            offset = 100
            opacity = 0
        }

        DispatchQueue.main.asyncAfter(deadline: .now() + 0.4) {
            onDismiss()
        }
    }
}

struct ToastModifier: ViewModifier {
    @EnvironmentObject var authState: AuthState

    func body(content: Content) -> some View {
        content
            .overlay(
                VStack {
                    Spacer()

                    if authState.showToast {
                        ToastView(
                            message: authState.toastMessage,
                            type: authState.toastType
                        ) {
                            authState.hideToast()
                        }
                        .padding(.horizontal, 20)
                        .padding(.bottom, 50)
                    }
                }
                .animation(.spring(), value: authState.showToast)
            )
    }
}

extension View {
    func withToast() -> some View {
        self.modifier(ToastModifier())
    }
}

#Preview {
    VStack {
        Spacer()

        ToastView(
            message: "Login successful! Welcome back.",
            type: ToastType.success
        ) {

        }
        .padding(.horizontal, 20)
        .padding(.bottom, 50)
    }
    .background(Color.gray.opacity(0.1))
    .environmentObject(LanguageManager())

}
