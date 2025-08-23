import SwiftUI

struct LuxorFullNameTextField: View {
    @Binding var fullName: String
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var languageManager: LanguageManager
    @State private var isEditing = false
    @State private var isValid = true

    // MARK: - Gradients & Colors
    private var backgroundGradient: LinearGradient {
        if themeManager.isDarkMode {
            return LinearGradient(
                colors: [LuxorColors.sandstone.opacity(0.1), LuxorColors.hieroglyphBrown.opacity(0.05)],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        } else {
            return LinearGradient(
                colors: [LuxorColors.papyrusBeige, LuxorColors.sandstone.opacity(0.3)],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        }
    }

    private var borderGradient: LinearGradient {
        if isEditing {
            return LinearGradient(colors: [LuxorColors.pureGold, LuxorColors.richGold],
                                  startPoint: .topLeading,
                                  endPoint: .bottomTrailing)
        } else if !isValid {
            return LinearGradient(colors: [Color.red.opacity(0.6), Color.red.opacity(0.3)],
                                  startPoint: .topLeading,
                                  endPoint: .bottomTrailing)
        } else if themeManager.isDarkMode {
            return LinearGradient(colors: [LuxorColors.bronzeGold.opacity(0.3), LuxorColors.deepGold.opacity(0.2)],
                                  startPoint: .topLeading,
                                  endPoint: .bottomTrailing)
        } else {
            return LinearGradient(colors: [LuxorColors.hieroglyphBrown.opacity(0.2), LuxorColors.sandstone.opacity(0.3)],
                                  startPoint: .topLeading,
                                  endPoint: .bottomTrailing)
        }
    }

    private var iconBackgroundGradient: LinearGradient {
        if isEditing {
            return LinearGradient(colors: [LuxorColors.pureGold.opacity(0.2), LuxorColors.richGold.opacity(0.1)],
                                  startPoint: .topLeading,
                                  endPoint: .bottomTrailing)
        } else {
            return LinearGradient(colors: [Color.clear], startPoint: .topLeading, endPoint: .bottomTrailing)
        }
    }

    private var iconColor: Color {
        if isEditing {
            return LuxorColors.deepGold
        } else if !isValid {
            return .red
        } else if themeManager.isDarkMode {
            return LuxorColors.bronzeGold
        } else {
            return LuxorColors.hieroglyphBrown
        }
    }

    private var labelColor: Color {
        if isEditing {
            return LuxorColors.deepGold
        } else if !isValid {
            return .red
        } else if themeManager.isDarkMode {
            return LuxorColors.papyrusBeige
        } else {
            return LuxorColors.hieroglyphBrown
        }
    }

    private var textColor: Color {
        themeManager.isDarkMode ? LuxorColors.papyrusBeige : LuxorColors.hieroglyphBrown
    }

    private var shadowColor: Color {
        LuxorColors.hieroglyphBrown.opacity(0.2)
    }

    private var validationIconGradient: LinearGradient {
        if fullName.isEmpty {
            return LinearGradient(colors: [Color.clear], startPoint: .top, endPoint: .bottom)
        } else if isValid {
            return LinearGradient(colors: [LuxorColors.egyptianBlue.opacity(0.2), LuxorColors.egyptianBlue.opacity(0.1)],
                                  startPoint: .topLeading,
                                  endPoint: .bottomTrailing)
        } else {
            return LinearGradient(colors: [Color.red.opacity(0.2), Color.red.opacity(0.1)],
                                  startPoint: .topLeading,
                                  endPoint: .bottomTrailing)
        }
    }

    // MARK: - Body
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            ZStack {
                RoundedRectangle(cornerRadius: 16)
                    .fill(backgroundGradient)
                    .overlay(RoundedRectangle(cornerRadius: 16)
                        .stroke(borderGradient, lineWidth: isEditing ? 2 : 1))
                    .shadow(color: shadowColor, radius: isEditing ? 12 : 6, x: 0, y: isEditing ? 6 : 3)
                    .frame(height: 64)

                HStack(spacing: 16) {
                    if languageManager.currentLanguage == .arabic {
                        ZStack {
                            Circle()
                                .fill(validationIconGradient)
                                .frame(width: 32, height: 32)
                            if !fullName.isEmpty {
                                Image(systemName: isValid ? "checkmark.circle.fill" : "xmark.circle.fill")
                                    .foregroundColor(isValid ? .green : .red)
                                    .font(.system(size: 16, weight: .medium))
                                    .scaleEffect(isValid ? 1.1 : 1.0)
                            }
                        }
                        // RTL Layout
                        VStack(alignment: .trailing, spacing: 4) {
                            HStack {
                                Spacer()
                                Text(LocalizedStrings.fullName(languageManager.currentLanguage))
                                    .font(.system(size: (isEditing || !fullName.isEmpty) ? 11 : 16, weight: .medium))
                                    .foregroundColor(labelColor)
                                    .offset(y: (isEditing || !fullName.isEmpty) ? -8 : 8)
                                    .animation(.spring(response: 0.4, dampingFraction: 0.8), value: isEditing || !fullName.isEmpty)
                            }
                            HStack {
                                Spacer()
                                TextField("", text: $fullName)
                                    .font(.system(size: 16, weight: .medium))
                                    .foregroundColor(textColor)
                                    .autocapitalization(.words)
                                    .autocorrectionDisabled()
                                    .multilineTextAlignment(.trailing)
                                    .onTapGesture { isEditing = true }
                                    .onChange(of: fullName) { validateFullName($0) }
                                    .onSubmit { isEditing = false }
                            }
                        }
                       
                    } else {
                        // LTR Layout
                        ZStack {
                            Circle()
                                .fill(iconBackgroundGradient)
                                .frame(width: 36, height: 36)
                            Image(systemName: isEditing ? "person.fill.badge.plus" : "person.fill")
                                .foregroundColor(iconColor)
                                .font(.system(size: 18, weight: .medium))
                                .scaleEffect(isEditing ? 1.1 : 1.0)
                        }
                        VStack(alignment: .leading, spacing: 4) {
                            HStack {
                                Text(LocalizedStrings.fullName(languageManager.currentLanguage))
                                    .font(.system(size: (isEditing || !fullName.isEmpty) ? 11 : 16, weight: .medium))
                                    .foregroundColor(labelColor)
                                    .offset(y: (isEditing || !fullName.isEmpty) ? -8 : 8)
                                    .animation(.spring(response: 0.4, dampingFraction: 0.8), value: isEditing || !fullName.isEmpty)
                                Spacer()
                            }
                            HStack {
                                TextField("", text: $fullName)
                                    .font(.system(size: 16, weight: .medium))
                                    .foregroundColor(textColor)
                                    .autocapitalization(.words)
                                    .autocorrectionDisabled()
                                    .onTapGesture { isEditing = true }
                                    .onChange(of: fullName) { validateFullName($0) }
                                    .onSubmit { isEditing = false }
                            }
                        }
                        ZStack {
                            Circle()
                                .fill(validationIconGradient)
                                .frame(width: 32, height: 32)
                            if !fullName.isEmpty {
                                Image(systemName: isValid ? "checkmark.circle.fill" : "xmark.circle.fill")
                                    .foregroundColor(isValid ? .green : .red)
                                    .font(.system(size: 16, weight: .medium))
                                    .scaleEffect(isValid ? 1.1 : 1.0)
                            }
                        }
                    }
                }
                .padding(.horizontal, 20)
            }

            if !isValid && !fullName.isEmpty {
                HStack(spacing: 8) {
                    Image(systemName: "exclamationmark.triangle.fill")
                        .foregroundColor(.red)
                        .font(.system(size: 12))
                    Text(LocalizedStrings.FullNameValidationMessage(languageManager.currentLanguage))
                        .font(.system(size: 12, weight: .medium))
                        .foregroundColor(.red)
                }
                .padding(.horizontal, 4)
                .transition(.asymmetric(
                    insertion: .opacity.combined(with: .move(edge: .top)).combined(with: .scale(scale: 0.95)),
                    removal: .opacity.combined(with: .move(edge: .top))
                ))
            }
        }
        .onTapGesture {
            if !isEditing { isEditing = true }
        }
    }

    // MARK: - Validation
    private func validateFullName(_ name: String) {
        let trimmedName = name.trimmingCharacters(in: .whitespacesAndNewlines)
        let components = trimmedName.components(separatedBy: .whitespaces).filter { !$0.isEmpty }
        isValid = name.isEmpty || (components.count >= 2 && trimmedName.count >= 3)
    }
}

#Preview {
    VStack(spacing: 40) {
        LuxorFullNameTextField(fullName: .constant("John Doe"))
            .environmentObject(ThemeManager())
            .environmentObject(LanguageManager())
        LuxorFullNameTextField(fullName: .constant("Invalid"))
            .environmentObject({
                let theme = ThemeManager()
                theme.isDarkMode = true
                return theme
            }())
            .background(Color.black)
            .environmentObject(LanguageManager())
    }
    .padding()
}
