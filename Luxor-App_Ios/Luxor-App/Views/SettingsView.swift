import SwiftUI

struct SettingsView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Binding var isNavBarVisible: Bool
    @Binding var isAppBarVisible: Bool
    @Binding var lastScrollOffset: CGFloat
    @Binding var scrollTimer: Timer?
    @Binding var isScrolling: Bool
    
    @State private var notificationsEnabled = true
    @State private var darkModeEnabled = false
    @State private var locationEnabled = true
    @State private var biometricEnabled = false
    @State private var autoPlayVideos = true
    @State private var dataSaver = false
    @State private var selectedLanguage = "English"
    @State private var selectedCurrency = "USD"
    
    private let languages = ["English", "Spanish", "French", "German", "Arabic", "Chinese"]
    private let currencies = ["USD", "EUR", "GBP", "JPY", "CAD", "AUD"]
    
    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            VStack(spacing: 24) {
                // Header Section
                headerSection
                
                // Account Section
                accountSection
                
                // Preferences Section
                preferencesSection
                
                // Privacy & Security Section
                privacySecuritySection
                
                // App Settings Section
                appSettingsSection
                
                // Support Section
                supportSection
                
                // Bottom Spacing
                Spacer(minLength: 100)
            }
            .padding(.horizontal, 20)
            .padding(.top, 20)
            .background(
                GeometryReader { geometry in
                    Color.clear
                        .preference(
                            key: ScrollOffsetPreferenceKey.self,
                            value: geometry.frame(in: .named("scrollView")).minY
                        )
                }
            )
        }
        .coordinateSpace(name: "scrollView")
        .onPreferenceChange(ScrollOffsetPreferenceKey.self) { currentOffset in
            handleInstagramStyleScroll(currentOffset: currentOffset)
        }
        .luxorThemedBackground(themeManager)
        .onAppear {
            darkModeEnabled = themeManager.isDarkMode
        }
    }
    
    // MARK: - Header Section
    private var headerSection: some View {
        VStack(spacing: 16) {
            ZStack {
                Circle()
                    .fill(
                        LinearGradient(
                            colors: [LuxorColors.luxorTeal.opacity(0.2), LuxorColors.luxorGold.opacity(0.2)],
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        )
                    )
                    .frame(width: 80, height: 80)
                
                Image(systemName: "person.circle.fill")
                    .font(.system(size: 50))
                    .foregroundColor(LuxorColors.luxorTeal)
            }
            
            VStack(spacing: 4) {
                Text("John Doe")
                    .font(.system(size: 24, weight: .bold))
                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                
                Text("john.doe@example.com")
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
            }
        }
        .padding(.vertical, 20)
    }
    
    // MARK: - Account Section
    private var accountSection: some View {
        SettingsSection(title: "Account") {
            SettingsRow(
                icon: "person.circle",
                title: "Edit Profile",
                subtitle: "Update your personal information",
                action: { /* Edit Profile */ }
            )
            
            SettingsRow(
                icon: "bell",
                title: "Notifications",
                subtitle: "Manage your notification preferences",
                action: { /* Notifications */ }
            )
            
            SettingsRow(
                icon: "lock.shield",
                title: "Privacy",
                subtitle: "Control your privacy settings",
                action: { /* Privacy */ }
            )
            
            SettingsRow(
                icon: "creditcard",
                title: "Payment Methods",
                subtitle: "Manage your payment options",
                action: { /* Payment Methods */ }
            )
        }
    }
    
    // MARK: - Preferences Section
    private var preferencesSection: some View {
        SettingsSection(title: "Preferences") {
            SettingsToggleRow(
                icon: "moon.fill",
                title: "Dark Mode",
                subtitle: "Switch between light and dark themes",
                isOn: $darkModeEnabled
            ) {
                themeManager.toggleTheme()
            }
            
            SettingsToggleRow(
                icon: "location.fill",
                title: "Location Services",
                subtitle: "Allow access to your location",
                isOn: $locationEnabled
            ) {}
            
            SettingsToggleRow(
                icon: "faceid",
                title: "Biometric Login",
                subtitle: "Use Face ID or Touch ID to sign in",
                isOn: $biometricEnabled
            ) {}
            
            SettingsPickerRow(
                icon: "globe",
                title: "Language",
                subtitle: "Choose your preferred language",
                selection: $selectedLanguage,
                options: languages
            )
            
            SettingsPickerRow(
                icon: "dollarsign.circle",
                title: "Currency",
                subtitle: "Select your preferred currency",
                selection: $selectedCurrency,
                options: currencies
            )
        }
    }
    
    // MARK: - Privacy & Security Section
    private var privacySecuritySection: some View {
        SettingsSection(title: "Privacy & Security") {
            SettingsRow(
                icon: "shield.checkered",
                title: "Two-Factor Authentication",
                subtitle: "Add an extra layer of security",
                action: { /* 2FA */ }
            )
            
            SettingsRow(
                icon: "key.fill",
                title: "Change Password",
                subtitle: "Update your account password",
                action: { /* Change Password */ }
            )
            
            SettingsRow(
                icon: "eye.slash",
                title: "Account Privacy",
                subtitle: "Control who can see your profile",
                action: { /* Account Privacy */ }
            )
            
            SettingsRow(
                icon: "trash",
                title: "Delete Account",
                subtitle: "Permanently delete your account",
                action: { /* Delete Account */ }
            )
            .foregroundColor(.red)
        }
    }
    
    // MARK: - App Settings Section
    private var appSettingsSection: some View {
        SettingsSection(title: "App Settings") {
            SettingsToggleRow(
                icon: "play.circle",
                title: "Auto-play Videos",
                subtitle: "Automatically play videos in feed",
                isOn: $autoPlayVideos
            ) {}
            
            SettingsToggleRow(
                icon: "network",
                title: "Data Saver",
                subtitle: "Reduce data usage",
                isOn: $dataSaver
            ) {}
            
            SettingsRow(
                icon: "arrow.up.circle",
                title: "Check for Updates",
                subtitle: "Current version: 1.0.0",
                action: { /* Check Updates */ }
            )
            
            SettingsRow(
                icon: "doc.text",
                title: "Terms of Service",
                subtitle: "Read our terms and conditions",
                action: { /* Terms */ }
            )
            
            SettingsRow(
                icon: "hand.raised",
                title: "Privacy Policy",
                subtitle: "Learn about data collection",
                action: { /* Privacy Policy */ }
            )
        }
    }
    
    // MARK: - Support Section
    private var supportSection: some View {
        SettingsSection(title: "Support") {
            SettingsRow(
                icon: "questionmark.circle",
                title: "Help Center",
                subtitle: "Get help and find answers",
                action: { /* Help Center */ }
            )
            
            SettingsRow(
                icon: "envelope",
                title: "Contact Support",
                subtitle: "Reach out to our support team",
                action: { /* Contact Support */ }
            )
            
            SettingsRow(
                icon: "star",
                title: "Rate App",
                subtitle: "Share your feedback",
                action: { /* Rate App */ }
            )
            
            SettingsRow(
                icon: "square.and.arrow.up",
                title: "Share App",
                subtitle: "Tell friends about Luxor",
                action: { /* Share App */ }
            )
        }
    }
    
    // MARK: - Instagram-Style Scroll Handling
    private func handleInstagramStyleScroll(currentOffset: CGFloat) {
        let delta = abs(currentOffset - lastScrollOffset)
        if delta > 1.0 {
            if !isScrolling {
                isScrolling = true
                withAnimation(.easeInOut(duration: 0.3)) {
                    isNavBarVisible = false
                }
            }
            scrollTimer?.invalidate()
            scrollTimer = Timer.scheduledTimer(withTimeInterval: 1.5, repeats: false) { _ in
                isScrolling = false
                withAnimation(.easeInOut(duration: 0.4)) {
                    isNavBarVisible = true
                }
            }
        }
        if currentOffset >= -10 {
            scrollTimer?.invalidate()
            isScrolling = false
            withAnimation(.easeInOut(duration: 0.3)) {
                isNavBarVisible = true
            }
        }
        
        lastScrollOffset = currentOffset
    }
}

// MARK: - Settings Section Component
struct SettingsSection<Content: View>: View {
    let title: String
    let content: Content
    @EnvironmentObject var themeManager: ThemeManager
    
    init(title: String, @ViewBuilder content: () -> Content) {
        self.title = title
        self.content = content()
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(title)
                .font(.system(size: 18, weight: .bold))
                .foregroundColor(LuxorColors.luxorTeal)
                .padding(.horizontal, 4)
            
            VStack(spacing: 0) {
                content
            }
            .background(
                RoundedRectangle(cornerRadius: 16)
                    .fill(themeManager.isDarkMode ? LuxorColors.Dark.cardBackground : LuxorColors.Light.cardBackground)
                    .overlay(
                        RoundedRectangle(cornerRadius: 16)
                            .stroke(
                                themeManager.isDarkMode ? LuxorColors.Dark.border : Color.gray.opacity(0.1),
                                lineWidth: 1
                            )
                    )
                    .shadow(
                        color: themeManager.isDarkMode ? LuxorColors.Dark.shadow : LuxorColors.Light.shadow,
                        radius: 8,
                        x: 0,
                        y: 4
                    )
            )
        }
    }
}

// MARK: - Settings Row Component
struct SettingsRow: View {
    let icon: String
    let title: String
    let subtitle: String
    let action: () -> Void
    @EnvironmentObject var themeManager: ThemeManager
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: 16) {
                ZStack {
                    Circle()
                        .fill(LuxorColors.luxorTeal.opacity(0.1))
                        .frame(width: 40, height: 40)
                    
                    Image(systemName: icon)
                        .font(.system(size: 18, weight: .medium))
                        .foregroundColor(LuxorColors.luxorTeal)
                }
                
                VStack(alignment: .leading, spacing: 2) {
                    Text(title)
                        .font(.system(size: 16, weight: .semibold))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                    
                    Text(subtitle)
                        .font(.system(size: 14, weight: .medium))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                }
                
                Spacer()
                
                Image(systemName: "chevron.right")
                    .font(.system(size: 14, weight: .medium))
                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
        }
        .buttonStyle(PlainButtonStyle())
        
        Divider()
            .background(themeManager.isDarkMode ? LuxorColors.Dark.border : Color.gray.opacity(0.1))
            .padding(.horizontal, 16)
    }
}

// MARK: - Settings Toggle Row Component
struct SettingsToggleRow: View {
    let icon: String
    let title: String
    let subtitle: String
    @Binding var isOn: Bool
    let action: () -> Void
    @EnvironmentObject var themeManager: ThemeManager
    
    var body: some View {
        HStack(spacing: 16) {
            ZStack {
                Circle()
                    .fill(LuxorColors.luxorTeal.opacity(0.1))
                    .frame(width: 40, height: 40)
                
                Image(systemName: icon)
                    .font(.system(size: 18, weight: .medium))
                    .foregroundColor(LuxorColors.luxorTeal)
            }
            
            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                
                Text(subtitle)
                    .font(.system(size: 14, weight: .medium))
                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
            }
            
            Spacer()
            
            Toggle("", isOn: $isOn)
                .toggleStyle(SwitchToggleStyle(tint: LuxorColors.luxorTeal))
                .onChange(of: isOn) { _ in
                    action()
                }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        
        Divider()
            .background(themeManager.isDarkMode ? LuxorColors.Dark.border : Color.gray.opacity(0.1))
            .padding(.horizontal, 16)
    }
}

// MARK: - Settings Picker Row Component
struct SettingsPickerRow: View {
    let icon: String
    let title: String
    let subtitle: String
    @Binding var selection: String
    let options: [String]
    @EnvironmentObject var themeManager: ThemeManager
    @State private var showPicker = false
    
    var body: some View {
        Button(action: { showPicker.toggle() }) {
            HStack(spacing: 16) {
                ZStack {
                    Circle()
                        .fill(LuxorColors.luxorTeal.opacity(0.1))
                        .frame(width: 40, height: 40)
                    
                    Image(systemName: icon)
                        .font(.system(size: 18, weight: .medium))
                        .foregroundColor(LuxorColors.luxorTeal)
                }
                
                VStack(alignment: .leading, spacing: 2) {
                    Text(title)
                        .font(.system(size: 16, weight: .semibold))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                    
                    Text(subtitle)
                        .font(.system(size: 14, weight: .medium))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                }
                
                Spacer()
                
                HStack(spacing: 8) {
                    Text(selection)
                        .font(.system(size: 14, weight: .medium))
                        .foregroundColor(LuxorColors.luxorTeal)
                    
                    Image(systemName: "chevron.right")
                        .font(.system(size: 14, weight: .medium))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
        }
        .buttonStyle(PlainButtonStyle())
        .sheet(isPresented: $showPicker) {
            PickerView(selection: $selection, options: options, title: title)
        }
        
        Divider()
            .background(themeManager.isDarkMode ? LuxorColors.Dark.border : Color.gray.opacity(0.1))
            .padding(.horizontal, 16)
    }
}

// MARK: - Picker View Component
struct PickerView: View {
    @Binding var selection: String
    let options: [String]
    let title: String
    @Environment(\.dismiss) var dismiss
    @EnvironmentObject var themeManager: ThemeManager
    
    var body: some View {
        NavigationView {
            VStack {
                List {
                    ForEach(options, id: \.self) { option in
                        Button(action: {
                            selection = option
                            dismiss()
                        }) {
                            HStack {
                                Text(option)
                                    .font(.system(size: 16, weight: .medium))
                                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                                
                                Spacer()
                                
                                if selection == option {
                                    Image(systemName: "checkmark")
                                        .foregroundColor(LuxorColors.luxorTeal)
                                        .font(.system(size: 16, weight: .medium))
                                }
                            }
                        }
                        .listRowBackground(themeManager.isDarkMode ? LuxorColors.Dark.cardBackground : LuxorColors.Light.cardBackground)
                    }
                }
                .listStyle(PlainListStyle())
            }
            .navigationTitle(title)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("Done") {
                        dismiss()
                    }
                    .foregroundColor(LuxorColors.luxorTeal)
                }
            }
        }
        .luxorThemedBackground(themeManager)
    }
}

#Preview {
    SettingsView(
        isNavBarVisible: .constant(true),
        isAppBarVisible: .constant(true),
        lastScrollOffset: .constant(0),
        scrollTimer: .constant(nil),
        isScrolling: .constant(false)
    )
    .environmentObject(ThemeManager())
}