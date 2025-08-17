import SwiftUI

struct LiquidBottomNavBar: View {
    @Binding var selectedTab: Int
    @EnvironmentObject var themeManager: ThemeManager
    let isVisible: Bool
    
    private let tabs = [
        TabItem(icon: "house", title: "Home", index: 0),
        TabItem(icon: "location", title: "Places", index: 1),
        TabItem(icon: "message", title: "Chat", index: 2),
        TabItem(icon: "gearshape", title: "Settings", index: 3)
    ]
    
    private var backgroundColor: Color {
        themeManager.isDarkMode ? Color.black : Color.white
    }
    
    private var borderColor: Color {
        themeManager.isDarkMode ? Color.white : Color.black
    }
    
    private var shadowColor: Color {
        themeManager.isDarkMode ? Color.black : Color.gray
    }
    
    var body: some View {
        HStack(spacing: 0) {
            ForEach(tabs, id: \.index) { tab in
                TabButton(
                    tab: tab,
                    isSelected: selectedTab == tab.index,
                    action: {
                        withAnimation(.spring(response: 0.6, dampingFraction: 0.8)) {
                            selectedTab = tab.index
                        }
                    }
                )
                .frame(maxWidth: .infinity)
            }
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 16)
        .background(navBarBackground)
        .frame(height: 70)
    }
    
    // Extract background as a separate computed property
    private var navBarBackground: some View {
        RoundedRectangle(cornerRadius: 30)
            .fill(backgroundColor.opacity(0.1))
            .background(blurBackground)
            .overlay(borderOverlay)
            .shadow(
                color: shadowColor.opacity(0.1),
                radius: 20,
                x: 0,
                y: 5
            )
    }
    
    private var blurBackground: some View {
        RoundedRectangle(cornerRadius: 30)
            .fill(.ultraThinMaterial)
            .opacity(0.3)
    }
    
    private var borderOverlay: some View {
        RoundedRectangle(cornerRadius: 30)
            .stroke(
                borderColor.opacity(0.1),
                lineWidth: 0.5
            )
    }
}

struct TabButton: View {
    let tab: TabItem
    let isSelected: Bool
    let action: () -> Void
    @EnvironmentObject var themeManager: ThemeManager
    @State private var showText = false
    
    // Break down complex color expressions
    private var iconColor: Color {
        if isSelected {
            return .white
        } else {
            return themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary
        }
    }
    
    var body: some View {
        Button(action: {
            action()
            // Animate text appearance
            withAnimation(.spring(response: 0.4, dampingFraction: 0.7).delay(0.1)) {
                showText = isSelected
            }
        }) {
            ZStack {
                // Background circle for selected state
                if isSelected {
                    selectedBackground
                }
                
                VStack(spacing: 0) {
                    // Icon with text overlay
                    ZStack {
                        iconView
                        
                        // Text that appears inside the icon area when selected
                        if isSelected {
                            selectedText
                        }
                    }
                }
            }
            .frame(width: 50, height: 50)
        }
        .buttonStyle(PlainButtonStyle())
        .onChange(of: isSelected) { newValue in
            withAnimation(.spring(response: 0.3, dampingFraction: 0.8)) {
                showText = newValue
            }
        }
    }
    
    // Extract complex views as separate computed properties
    private var selectedBackground: some View {
        Circle()
            .fill(
                LinearGradient(
                    colors: [LuxorColors.luxorTeal, LuxorColors.luxorGold],
                    startPoint: .topLeading,
                    endPoint: .bottomTrailing
                )
            )
            .frame(width: 50, height: 50)
            .scaleEffect(isSelected ? 1.0 : 0.8)
            .animation(.spring(response: 0.5, dampingFraction: 0.7), value: isSelected)
    }
    
    private var iconView: some View {
        Image(systemName: tab.icon)
            .font(.system(size: isSelected ? 18 : 20, weight: .medium))
            .foregroundColor(iconColor)
            .scaleEffect(isSelected ? 0.9 : 1.0)
            .opacity(isSelected && showText ? 0.3 : 1.0)
            .animation(.spring(response: 0.4, dampingFraction: 0.6), value: isSelected)
    }
    
    private var selectedText: some View {
        Text(tab.title)
            .font(.system(size: 10, weight: .bold))
            .foregroundColor(.white)
            .opacity(showText ? 1.0 : 0.0)
            .scaleEffect(showText ? 1.0 : 0.5)
            .animation(
                .spring(response: 0.4, dampingFraction: 0.6).delay(0.15),
                value: showText
            )
    }
}

struct TabItem {
    let icon: String
    let title: String
    let index: Int
}

#Preview {
    VStack {
        Spacer()
        LiquidBottomNavBar(selectedTab: .constant(0), isVisible: true)
            .padding(.horizontal, 20)
            .padding(.bottom, 34)
    }
    .background(
        LinearGradient(
            colors: [Color.blue.opacity(0.3), Color.purple.opacity(0.3)],
            startPoint: .top,
            endPoint: .bottom
        )
    )
    .environmentObject(ThemeManager())
}
