//
//  LuxorButton.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import SwiftUI

struct LuxorButton: View {
    let title: String
    let style: LuxorButtonStyle
    var isLoading: Bool = false
    var icon: String? = nil
    let action: () -> Void
    
    @State private var isPressed = false
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: 8) {
                if isLoading {
                    ProgressView()
                        .scaleEffect(0.8)
                        .tint(style.textColor)
                } else if let icon = icon {
                    Image(systemName: icon)
                        .font(.system(size: 16, weight: .semibold))
                }
                
                Text(title)
                    .font(.luxorBodyBold)
                
                Spacer(minLength: 0)
            }
            .foregroundColor(style.textColor)
            .frame(maxWidth: .infinity)
            .padding(.vertical, 16)
            .padding(.horizontal, 20)
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(style.backgroundColor)
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(style.borderColor, lineWidth: style.borderWidth)
                    )
            )
        }
        .disabled(isLoading)
        .scaleEffect(isPressed ? 0.95 : 1.0)
        .onLongPressGesture(minimumDuration: 0, maximumDistance: .infinity, pressing: { pressing in
            withAnimation(.easeInOut(duration: 0.1)) {
                isPressed = pressing
            }
        }, perform: {})
    }
}

enum LuxorButtonStyle {
    case primary
    case secondary
    case outline
    
    var backgroundColor: LinearGradient {
        switch self {
        case .primary:
            return LinearGradient(
                colors: [
                    LuxorColors.pureGold,
                    LuxorColors.richGold
                ],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        case .secondary:
            return LinearGradient(
                colors: [
                    LuxorColors.hieroglyphBrown.opacity(0.3),
                    LuxorColors.sandstone.opacity(0.2)
                ],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        case .outline:
            return LinearGradient(
                colors: [Color.clear, Color.clear],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        }
    }
    
    var textColor: Color {
        switch self {
        case .primary:
            return LuxorColors.hieroglyphBrown
        case .secondary, .outline:
            return LuxorColors.pureGold
        }
    }
    
    var borderColor: Color {
        switch self {
        case .primary, .secondary:
            return Color.clear
        case .outline:
            return LuxorColors.pureGold
        }
    }
    
    var borderWidth: CGFloat {
        switch self {
        case .primary, .secondary:
            return 0
        case .outline:
            return 1
        }
    }
}

#Preview {
    VStack(spacing: 20) {
        LuxorButton(
            title: "Primary Button",
            style: .primary
        ) {
            print("Primary tapped")
        }
        
        LuxorButton(
            title: "Secondary Button",
            style: .secondary
        ) {
            print("Secondary tapped")
        }
        
        LuxorButton(
            title: "Outline Button",
            style: .outline
        ) {
            print("Outline tapped")
        }
    }
    .padding()
    .background(Color.black)
}