import SwiftUI

// MARK: - Theme Manager
class ThemeManager: ObservableObject {
    @Published var isDarkMode: Bool = false
    
    func toggleTheme() {
        isDarkMode.toggle()
    }
}

// MARK: - Luxor Colors
struct LuxorColors {
    // Primary Luxor brand colors (from logo)
    static let luxorTeal = Color(red: 0.0, green: 0.5, blue: 0.5)
    static let luxorGold = Color(red: 0.8, green: 0.6, blue: 0.2)
    static let luxorDarkTeal = Color(red: 0.0, green: 0.3, blue: 0.3)
    
    // Light mode colors
    struct Light {
        static let background = Color.white
        static let secondaryBackground = Color(UIColor.systemGray6)
        static let textPrimary = Color.black
        static let textSecondary = Color.gray
        static let cardBackground = Color.white
        static let shadow = Color.black.opacity(0.1)
    }
    
    // Enhanced Dark mode colors - Much more visible!
    struct Dark {
        static let background = Color(red: 0.08, green: 0.08, blue: 0.12) // Dark blue-gray instead of pure black
        static let secondaryBackground = Color(red: 0.12, green: 0.12, blue: 0.18) // Lighter secondary
        static let textPrimary = Color.white
        static let textSecondary = Color(red: 0.8, green: 0.8, blue: 0.85) // Much lighter gray
        static let cardBackground = Color(red: 0.15, green: 0.15, blue: 0.22) // Visible card background
        static let shadow = Color.black.opacity(0.4) // Stronger shadow for depth
        static let border = Color(red: 0.25, green: 0.25, blue: 0.35) // Visible borders
        static let inputBackground = Color(red: 0.18, green: 0.18, blue: 0.25) // Input field background
    }
}

// MARK: - Theme Extension
extension View {
    func luxorThemedBackground(_ themeManager: ThemeManager) -> some View {
        self.background(
            LinearGradient(
                colors: themeManager.isDarkMode ? 
                    [LuxorColors.Dark.background, LuxorColors.Dark.secondaryBackground] :
                    [LuxorColors.Light.background, LuxorColors.luxorTeal.opacity(0.05)],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        )
    }
    
    func luxorCard(_ themeManager: ThemeManager) -> some View {
        self
            .background(
                RoundedRectangle(cornerRadius: 16)
                    .fill(themeManager.isDarkMode ? LuxorColors.Dark.cardBackground : LuxorColors.Light.cardBackground)
                    .overlay(
                        RoundedRectangle(cornerRadius: 16)
                            .stroke(
                                themeManager.isDarkMode ? LuxorColors.Dark.border : Color.clear,
                                lineWidth: 1
                            )
                    )
                    .shadow(
                        color: themeManager.isDarkMode ? LuxorColors.Dark.shadow : LuxorColors.Light.shadow,
                        radius: 10,
                        x: 0,
                        y: 5
                    )
            )
    }
}