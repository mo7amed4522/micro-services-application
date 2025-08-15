import SwiftUI

struct LuxorTheme {

    static let primaryTeal = Color(red: 0.0, green: 0.5, blue: 0.5)
    static let darkTeal = Color(red: 0.0, green: 0.4, blue: 0.4)
    static let lightTeal = Color(red: 0.0, green: 0.6, blue: 0.6)

    static let gold = Color(red: 0.8, green: 0.7, blue: 0.3)
    static let darkGold = Color(red: 0.7, green: 0.6, blue: 0.2)
    static let goldAccent = Color(red: 0.9, green: 0.8, blue: 0.4)
    
 
    static let primaryText = Color.primary
    static let secondaryText = Color.secondary
    
    
    static let backgroundGradient = LinearGradient(
        colors: [primaryTeal, darkTeal],
        startPoint: .topLeading,
        endPoint: .bottomTrailing
    )
    
    static let overlayGradient = LinearGradient(
        colors: [Color.black.opacity(0.3), Color.clear],
        startPoint: .top,
        endPoint: .bottom
    )
    
   
    static let cardBackground = Color(.systemBackground)
    static let secondaryBackground = Color(.secondarySystemBackground)
    
   
    static let inputBackground = Color(.systemGray6)
    static let inputBorder = primaryTeal.opacity(0.3)
    
    
    static let successColor = Color.green
    static let errorColor = Color.red
    static let warningColor = Color.orange
}