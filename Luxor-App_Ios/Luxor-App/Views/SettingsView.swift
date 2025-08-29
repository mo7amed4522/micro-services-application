import SwiftUI

struct SettingsView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var languageManager: LanguageManager
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
    @State private var selectedCurrency = "USD"

    private let currencies = ["USD", "EUR", "GBP", "JPY", "CAD", "AUD"]

    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            VStack(spacing: 24) {
                headerSection
                accountSection
                preferencesSection
                privacySecuritySection
                appSettingsSection
                supportSection
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
        .localizedEnvironment(languageManager)
        .onAppear {
            darkModeEnabled = themeManager.isDarkMode
        }
        .onChange(of: themeManager.isDarkMode) { newValue in
            darkModeEnabled = newValue
        }
    }

    private var headerSection: some View {
        VStack(spacing: 16) {
            ZStack {
                Circle()
                    .fill(
                        LinearGradient(
                            colors: [
                                LuxorColors.luxorTeal.opacity(0.2),
                                LuxorColors.luxorGold.opacity(0.2),
                            ],
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
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.textPrimary
                            : LuxorColors.Light.textPrimary
                    )

                Text("john.doe@example.com")
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.textSecondary
                            : LuxorColors.Light.textSecondary
                    )
            }
        }
        .padding(.vertical, 20)
    }

    private var accountSection: some View {
        SettingsSection(
            title: LocalizedStrings.settings(languageManager.currentLanguage)
        ) {
            SettingsRow(
                icon: "person.circle",
                title: LocalizedStrings.EditProfile(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubtitleEditProfile(
                    languageManager.currentLanguage
                ),
                action: { /* Edit Profile */  }
            )

            SettingsRow(
                icon: "bell",
                title: LocalizedStrings.notifications(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleNotifications(
                    languageManager.currentLanguage
                ),
                action: { /* Notifications */  }
            )

            SettingsRow(
                icon: "lock.shield",
                title: LocalizedStrings.Privacy(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitlePrivacy(
                    languageManager.currentLanguage
                ),
                action: { /* Privacy */  }
            )

            SettingsRow(
                icon: "creditcard",
                title: LocalizedStrings.PaymentMethod(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitlePaymentMethod(
                    languageManager.currentLanguage
                ),
                action: { /* Payment Methods */  }
            )
        }
    }

    private var preferencesSection: some View {
        SettingsSection(
            title: LocalizedStrings.Preferences(languageManager.currentLanguage)
        ) {
            SettingsToggleRow(
                icon: "moon.fill",
                title: LocalizedStrings.darkMode(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitledarkMode(
                    languageManager.currentLanguage
                ),
                isOn: $darkModeEnabled
            ) {
                themeManager.toggleTheme()
            }

            SettingsToggleRow(
                icon: "location.fill",
                title: LocalizedStrings.LocationServices(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleLocationServices(
                    languageManager.currentLanguage
                ),
                isOn: $locationEnabled
            ) {}

            SettingsToggleRow(
                icon: "faceid",
                title: LocalizedStrings.BiometricLogin(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleBiometricLogin(
                    languageManager.currentLanguage
                ),
                isOn: $biometricEnabled
            ) {}

            LanguagePickerRow(
                icon: "globe",
                title: LocalizedStrings.language(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitlelanguage(
                    languageManager.currentLanguage
                ),
                languageManager: languageManager
            )

            SettingsPickerRow(
                icon: "dollarsign.circle",
                title: LocalizedStrings.Currency(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleCurrency(
                    languageManager.currentLanguage
                ),
                selection: $selectedCurrency,
                options: currencies
            )
        }
    }

    private var privacySecuritySection: some View {
        SettingsSection(
            title: LocalizedStrings.PrivacySecurity(
                languageManager.currentLanguage
            )
        ) {
            SettingsRow(
                icon: "shield.checkered",
                title: LocalizedStrings.TwoFactorAuthentication(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleBiometricLogin(
                    languageManager.currentLanguage
                ),
                action: { /* 2FA */  }
            )

            SettingsRow(
                icon: "key.fill",
                title: LocalizedStrings.ChangePassword(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleChangePassword(
                    languageManager.currentLanguage
                ),
                action: { /* Change Password */  }
            )

            SettingsRow(
                icon: "eye.slash",
                title: LocalizedStrings.AccountPrivacy(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleAccountPrivacy(
                    languageManager.currentLanguage
                ),
                action: { /* Account Privacy */  }
            )

            SettingsRow(
                icon: "trash",
                title: LocalizedStrings.DeleteAccount(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleDeleteAccount(
                    languageManager.currentLanguage
                ),
                action: { /* Delete Account */  }
            )
            .foregroundColor(.red)
        }
    }

    private var appSettingsSection: some View {
        SettingsSection(
            title: LocalizedStrings.AppSeetting(languageManager.currentLanguage)
        ) {
            SettingsToggleRow(
                icon: "play.circle",
                title: LocalizedStrings.AutoPlayVideos(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleAutoPlayVideos(
                    languageManager.currentLanguage
                ),
                isOn: $autoPlayVideos
            ) {}

            SettingsToggleRow(
                icon: "network",
                title: LocalizedStrings.DataSaver(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleDataSaver(
                    languageManager.currentLanguage
                ),
                isOn: $dataSaver
            ) {}

            SettingsRow(
                icon: "arrow.up.circle",
                title: LocalizedStrings.CheckForUpdate(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleCheckForUpdate(
                    languageManager.currentLanguage
                ),
                action: { /* Check Updates */  }
            )

            SettingsRow(
                icon: "doc.text",
                title: LocalizedStrings.TermsOfServices(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleTermsOfServices(
                    languageManager.currentLanguage
                ),
                action: { /* Terms */  }
            )

            SettingsRow(
                icon: "hand.raised",
                title: LocalizedStrings.PrivacyAndPolicy(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitlePrivacyAndPolicy(
                    languageManager.currentLanguage
                ),
                action: { /* Privacy Policy */  }
            )
        }
    }

    private var supportSection: some View {
        SettingsSection(
            title: LocalizedStrings.Support(languageManager.currentLanguage)
        ) {
            SettingsRow(
                icon: "questionmark.circle",
                title: LocalizedStrings.HelpCenter(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleHelpCenter(
                    languageManager.currentLanguage
                ),
                action: { /* Help Center */  }
            )

            SettingsRow(
                icon: "envelope",
                title: LocalizedStrings.HelpCenter(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleHelpCenter(
                    languageManager.currentLanguage
                ),
                action: { /* Contact Support */  }
            )

            SettingsRow(
                icon: "star",
                title: LocalizedStrings.RateApp(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleRateApp(
                    languageManager.currentLanguage
                ),
                action: { /* Rate App */  }
            )

            SettingsRow(
                icon: "square.and.arrow.up",
                title: LocalizedStrings.ShareApp(
                    languageManager.currentLanguage
                ),
                subtitle: LocalizedStrings.SubTitleShareApp(
                    languageManager.currentLanguage
                ),
                action: { /* Share App */  }
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
            scrollTimer = Timer.scheduledTimer(
                withTimeInterval: 1.5,
                repeats: false
            ) { _ in
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
                    .fill(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.cardBackground
                            : LuxorColors.Light.cardBackground
                    )
                    .overlay(
                        RoundedRectangle(cornerRadius: 16)
                            .stroke(
                                themeManager.isDarkMode
                                    ? LuxorColors.Dark.border
                                    : Color.gray.opacity(0.1),
                                lineWidth: 1
                            )
                    )
                    .shadow(
                        color: themeManager.isDarkMode
                            ? LuxorColors.Dark.shadow
                            : LuxorColors.Light.shadow,
                        radius: 8,
                        x: 0,
                        y: 4
                    )
            )
        }
    }
}

struct SettingsRow: View {
    let icon: String
    let title: String
    let subtitle: String
    let action: () -> Void
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var langManger: LanguageManager

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
                        .foregroundColor(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.textPrimary
                                : LuxorColors.Light.textPrimary
                        )

                    Text(subtitle)
                        .font(.system(size: 10, weight: .medium))
                        .foregroundColor(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.textSecondary
                                : LuxorColors.Light.textSecondary
                        )
                }

                Spacer()

                Image(
                    systemName: langManger.currentLanguage == .arabic
                        ? "chevron.left" : "chevron.right"
                )
                .font(.system(size: 14, weight: .medium))
                .foregroundColor(
                    themeManager.isDarkMode
                        ? LuxorColors.Dark.textSecondary
                        : LuxorColors.Light.textSecondary
                )
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
        }
        .buttonStyle(PlainButtonStyle())

        Divider()
            .background(
                themeManager.isDarkMode
                    ? LuxorColors.Dark.border : Color.gray.opacity(0.1)
            )
            .padding(.horizontal, 16)
    }
}

struct SettingsToggleRow: View {
    let icon: String
    let title: String
    let subtitle: String
    @Binding var isOn: Bool
    let action: () -> Void
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var langManger: LanguageManager

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
                    .font(.system(size: 14, weight: .semibold))
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.textPrimary
                            : LuxorColors.Light.textPrimary
                    )

                Text(subtitle)
                    .font(.system(size: 12, weight: .regular))
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.textSecondary
                            : LuxorColors.Light.textSecondary
                    )

            }
            Spacer()
            Toggle("", isOn: $isOn)
                .toggleStyle(SwitchToggleStyle(tint: LuxorColors.luxorTeal))
                .disabled(true)
                .onTapGesture {
                    isOn.toggle()
                    action()
                }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)

        Divider()
            .background(
                themeManager.isDarkMode
                    ? LuxorColors.Dark.border : Color.gray.opacity(0.1)
            )
            .padding(.horizontal, 16)
    }
}

struct SettingsPickerRow: View {
    let icon: String
    let title: String
    let subtitle: String
    @Binding var selection: String
    let options: [String]
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var langManger: LanguageManager
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
                        .foregroundColor(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.textPrimary
                                : LuxorColors.Light.textPrimary
                        )

                    Text(subtitle)
                        .font(.system(size: 14, weight: .medium))
                        .foregroundColor(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.textSecondary
                                : LuxorColors.Light.textSecondary
                        )
                }

                Spacer()

                HStack(spacing: 8) {
                    Text(selection)
                        .font(.system(size: 14, weight: .medium))
                        .foregroundColor(LuxorColors.luxorTeal)

                    Image(
                        systemName: langManger.currentLanguage == .arabic
                            ? "chevron.left" : "chevron.right"
                    )
                    .font(.system(size: 14, weight: .medium))
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.textSecondary
                            : LuxorColors.Light.textSecondary
                    )
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
            .background(
                themeManager.isDarkMode
                    ? LuxorColors.Dark.border : Color.gray.opacity(0.1)
            )
            .padding(.horizontal, 16)
    }
}

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
                                    .foregroundColor(
                                        themeManager.isDarkMode
                                            ? LuxorColors.Dark.textPrimary
                                            : LuxorColors.Light.textPrimary
                                    )

                                Spacer()

                                if selection == option {
                                    Image(systemName: "checkmark")
                                        .foregroundColor(LuxorColors.luxorTeal)
                                        .font(
                                            .system(size: 16, weight: .medium)
                                        )
                                }
                            }
                        }
                        .listRowBackground(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.cardBackground
                                : LuxorColors.Light.cardBackground
                        )
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

struct LanguagePickerRow: View {
    let icon: String
    let title: String
    let subtitle: String
    @ObservedObject var languageManager: LanguageManager
    @EnvironmentObject var themeManager: ThemeManager
    @State private var showLanguagePicker = false

    var body: some View {
        Button(action: { showLanguagePicker.toggle() }) {
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
                        .foregroundColor(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.textPrimary
                                : LuxorColors.Light.textPrimary
                        )

                    Text(subtitle)
                        .font(.system(size: 14, weight: .medium))
                        .foregroundColor(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.textSecondary
                                : LuxorColors.Light.textSecondary
                        )
                }

                Spacer()

                HStack(spacing: 8) {
                    Text(
                        "\(languageManager.currentLanguage.flag) \(languageManager.currentLanguage.displayName)"
                    )
                    .font(.system(size: 14, weight: .medium))
                    .foregroundColor(LuxorColors.luxorTeal)

                    Image(systemName: "chevron.right")
                        .font(.system(size: 14, weight: .medium))
                        .foregroundColor(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.textSecondary
                                : LuxorColors.Light.textSecondary
                        )
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
        }
        .buttonStyle(PlainButtonStyle())
        .sheet(isPresented: $showLanguagePicker) {
            LanguagePickerView(languageManager: languageManager)
        }

        Divider()
            .background(
                themeManager.isDarkMode
                    ? LuxorColors.Dark.border : Color.gray.opacity(0.1)
            )
            .padding(.horizontal, 16)
    }
}

struct LanguagePickerView: View {
    @ObservedObject var languageManager: LanguageManager
    @Environment(\.dismiss) var dismiss
    @EnvironmentObject var themeManager: ThemeManager

    var body: some View {
        NavigationView {
            VStack {
                List {
                    ForEach(SupportedLanguage.allCases, id: \.self) {
                        language in
                        Button(action: {
                            languageManager.changeLanguage(to: language)
                            dismiss()
                        }) {
                            HStack {
                                Text("\(language.flag) \(language.displayName)")
                                    .font(.system(size: 16, weight: .medium))
                                    .foregroundColor(
                                        themeManager.isDarkMode
                                            ? LuxorColors.Dark.textPrimary
                                            : LuxorColors.Light.textPrimary
                                    )

                                Spacer()

                                if languageManager.currentLanguage == language {
                                    Image(systemName: "checkmark")
                                        .foregroundColor(LuxorColors.luxorTeal)
                                        .font(
                                            .system(size: 16, weight: .medium)
                                        )
                                }
                            }
                        }
                        .listRowBackground(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.cardBackground
                                : LuxorColors.Light.cardBackground
                        )
                    }
                }
                .listStyle(PlainListStyle())
            }
            .navigationTitle(
                LocalizedStrings.language(languageManager.currentLanguage)
            )
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
        .localizedEnvironment(languageManager)
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
    .environmentObject(LanguageManager())
}
