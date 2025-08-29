import SwiftUI

struct MainTabView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var languageManager: LanguageManager
    @State private var selectedTab = 0
    @State private var isNavBarVisible = true
    @State private var isAppBarVisible = true
    @State private var lastScrollOffset: CGFloat = 0
    @State private var scrollTimer: Timer?
    @State private var isScrolling = false
    @State private var isInDetailView = false 
    
    
    private var appBarBackgroundColor: Color {
        themeManager.isDarkMode ? Color.black : Color.white
    }
    
    private var appBarBorderColor: Color {
        themeManager.isDarkMode ? Color.white : Color.black
    }
    
    private var appBarShadowColor: Color {
        themeManager.isDarkMode ? Color.black : Color.gray
    }
    
    var body: some View {
        NavigationView {
            ZStack(alignment: .bottom) {
                VStack(spacing: 0) {
                    if isAppBarVisible && !isInDetailView && selectedTab == 0 {
                        appBarView
                        
                    }
                    TabView(selection: $selectedTab) {

                        HomeContentView(
                            isNavBarVisible: $isNavBarVisible,
                            isAppBarVisible: $isAppBarVisible,
                            lastScrollOffset: $lastScrollOffset,
                            scrollTimer: $scrollTimer,
                            isScrolling: $isScrolling
                        )
                        .tag(0)
                            
                        PlacesView(
                            isNavBarVisible: $isNavBarVisible,
                            isAppBarVisible: $isAppBarVisible,
                            lastScrollOffset: $lastScrollOffset,
                            scrollTimer: $scrollTimer,
                            isScrolling: $isScrolling
                        )
                        .tag(1)
                        
                        ChatView(
                            isInDetailView: $isInDetailView,
                            isNavBarVisible: $isNavBarVisible,
                            isAppBarVisible: $isAppBarVisible,
                            lastScrollOffset: $lastScrollOffset,
                            scrollTimer: $scrollTimer,
                            isScrolling: $isScrolling
                        )
                        .tag(2)

                        SettingsView(
                            isNavBarVisible: $isNavBarVisible,
                            isAppBarVisible: $isAppBarVisible,
                            lastScrollOffset: $lastScrollOffset,
                            scrollTimer: $scrollTimer,
                            isScrolling: $isScrolling
                        )
                        .tag(3)
                    }
                    .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
                }
                .luxorThemedBackground(themeManager)

                VStack {
                    Spacer()
                    if isNavBarVisible && !isInDetailView {
                        LiquidBottomNavBar(
                            selectedTab: $selectedTab,
                            isVisible: isNavBarVisible
                        )
                        .padding(.horizontal, 20)
                        .padding(.bottom, 34)
                    }
                }
            }
            .navigationBarHidden(true)
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .navigationBarBackButtonHidden(true)
    }

    private var appBarView: some View {
        HStack {
            Text("Luxor")
                .font(.system(size: 28, weight: .bold, design: .rounded))
                .foregroundColor(LuxorColors.luxorTeal)
            
            Spacer()
            
            Button(action: {}) {
                Image(systemName: "heart")
                    .font(.system(size: 24, weight: .medium))
                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
            }
        }
        .padding(.horizontal, 16)
        .padding(.top, 8)
        .padding(.bottom, 12)
        .background(appBarBackground)
        .transition(.move(edge: .top).combined(with: .opacity))
    }
    

    private var appBarBackground: some View {
        RoundedRectangle(cornerRadius: 0)
            .fill(appBarBackgroundColor.opacity(0.1))
            .background(appBarBlurBackground)
            .overlay(appBarBorderOverlay)
            .shadow(
                color: appBarShadowColor.opacity(0.1),
                radius: 15,
                x: 0,
                y: 3
            )
    }
    
    private var appBarBlurBackground: some View {
        RoundedRectangle(cornerRadius: 0)
            .fill(.ultraThinMaterial)
            .opacity(0.3)
    }
    
    private var appBarBorderOverlay: some View {
        VStack {
            Spacer()
            Rectangle()
                .fill(appBarBorderColor.opacity(0.1))
                .frame(height: 0.5)
        }
    }
}
    

#Preview {
    MainTabView()
        .environmentObject(ThemeManager())
        .environmentObject(LanguageManager())
}
