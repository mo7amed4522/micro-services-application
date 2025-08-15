import SwiftUI

struct LuxorButton: View {
    let title: String
    let action: () -> Void
    let style: ButtonStyle
    let isLoading: Bool
    let isDisabled: Bool
    
    enum ButtonStyle {
        case primary, secondary, outline
    }
    
    init(title: String, style: ButtonStyle = .primary, isLoading: Bool = false, isDisabled: Bool = false, action: @escaping () -> Void) {
        self.title = title
        self.style = style
        self.isLoading = isLoading
        self.isDisabled = isDisabled
        self.action = action
    }
    
    var body: some View {
        Button(action: action) {
            HStack {
                if isLoading {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle(tint: textColor))
                        .scaleEffect(0.8)
                } else {
                    Text(title)
                        .fontWeight(.semibold)
                }
            }
            .frame(maxWidth: .infinity)
            .frame(height: 50)
            .background(backgroundColor)
            .foregroundColor(textColor)
            .cornerRadius(12)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(borderColor, lineWidth: style == .outline ? 1 : 0)
            )
        }
        .disabled(isDisabled || isLoading)
        .opacity(isDisabled ? 0.6 : 1.0)
    }
    
    private var backgroundColor: Color {
        switch style {
        case .primary:
            return isDisabled ? Color.gray : LuxorTheme.primaryTeal
        case .secondary:
            return LuxorTheme.secondaryBackground
        case .outline:
            return Color.clear
        }
    }
    
    private var textColor: Color {
        switch style {
        case .primary:
            return .white
        case .secondary, .outline:
            return LuxorTheme.primaryTeal
        }
    }
    
    private var borderColor: Color {
        LuxorTheme.primaryTeal
    }
}