import SwiftUI

struct LanguageSelectionView: View {
    @EnvironmentObject var languageManager: LanguageManager
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.dismiss) private var dismiss
    
    var body: some View {
        NavigationView {
            VStack(spacing: 0) {
                VStack(spacing: 16) {
                    Text(LocalizedStrings.language(languageManager.currentLanguage))
                        .font(.system(size: 28, weight: .bold, design: .rounded))
                        .foregroundColor(LuxorColors.pureGold)
                    
                    Text("Select your preferred language")
                        .font(.system(size: 16, weight: .medium))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.papyrusBeige : LuxorColors.hieroglyphBrown)
                        .multilineTextAlignment(.center)
                }
                .padding(.top, 20)
                .padding(.horizontal, 20)    
                ScrollView {
                    LazyVStack(spacing: 16) {
                        ForEach(SupportedLanguage.allCases, id: \.self) { language in
                            LanguageOptionCard(
                                language: language,
                                isSelected: languageManager.currentLanguage == language
                            ) {
                                withAnimation(.spring(response: 0.6, dampingFraction: 0.8)) {
                                    languageManager.changeLanguage(to: language)
                                }
                            }
                        }
                    }
                    .padding(.horizontal, 20)
                }
                .padding(.top, 30)    
                Spacer()                
                Button(action: {
                    dismiss()
                }) {
                    HStack {
                        Text("Done")
                            .font(.system(size: 18, weight: .semibold))
                            .foregroundColor(.white)
                    }
                    .frame(maxWidth: .infinity)
                    .frame(height: 56)
                    .background(
                        LinearGradient(
                            colors: [LuxorColors.pureGold, LuxorColors.richGold],
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        )
                    )
                    .cornerRadius(16)
                    .shadow(
                        color: LuxorColors.pureGold.opacity(0.3),
                        radius: 8,
                        x: 0,
                        y: 4
                    )
                }
                .padding(.horizontal, 20)
                .padding(.bottom, 30)
            }
            .luxorThemedBackground(themeManager)
            .navigationBarHidden(true)
        }
    }
}

struct LanguageOptionCard: View {
    let language: SupportedLanguage
    let isSelected: Bool
    let action: () -> Void
    @EnvironmentObject var themeManager: ThemeManager
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: 16) {
                Text(language.flag)
                    .font(.system(size: 32))                
                VStack(alignment: .leading, spacing: 4) {
                    Text(language.displayName)
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.papyrusBeige : LuxorColors.hieroglyphBrown)
                    
                    Text(language.rawValue.uppercased())
                        .font(.system(size: 14, weight: .medium))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.sandstone : LuxorColors.hieroglyphBrown.opacity(0.7))
                }
                Spacer()                
                if isSelected {
                    Image(systemName: "checkmark.circle.fill")
                        .font(.system(size: 24, weight: .medium))
                        .foregroundColor(LuxorColors.pureGold)
                } else {
                    Circle()
                        .stroke(
                            themeManager.isDarkMode ? LuxorColors.sandstone.opacity(0.3) : LuxorColors.hieroglyphBrown.opacity(0.3),
                            lineWidth: 2
                        )
                        .frame(width: 24, height: 24)
                }
            }
            .padding(20)
            .background(
                RoundedRectangle(cornerRadius: 16)
                    .fill(
                        themeManager.isDarkMode ?
                        LuxorColors.hieroglyphBrown.opacity(0.1) :
                        LuxorColors.papyrusBeige.opacity(0.5)
                    )
                    .overlay(
                        RoundedRectangle(cornerRadius: 16)
                            .stroke(
                                isSelected ?
                                LinearGradient(
                                    colors: [LuxorColors.pureGold, LuxorColors.richGold],
                                    startPoint: .topLeading,
                                    endPoint: .bottomTrailing
                                ) :
                                LinearGradient(
                                    colors: [Color.clear],
                                    startPoint: .topLeading,
                                    endPoint: .bottomTrailing
                                ),
                                lineWidth: isSelected ? 2 : 0
                            )
                    )
            )
            .scaleEffect(isSelected ? 1.02 : 1.0)
            .shadow(
                color: isSelected ? LuxorColors.pureGold.opacity(0.2) : Color.clear,
                radius: isSelected ? 8 : 0,
                x: 0,
                y: isSelected ? 4 : 0
            )
        }
        .buttonStyle(PlainButtonStyle())
        .animation(.spring(response: 0.4, dampingFraction: 0.8), value: isSelected)
    }
}

#Preview {
    LanguageSelectionView()
        .environmentObject(LanguageManager())
        .environmentObject(ThemeManager())
}