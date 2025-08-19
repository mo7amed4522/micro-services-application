import SwiftUI

struct CountryCode {
    let name: String
    let code: String
    let flag: String
    let dialCode: String
}

struct LuxorPhoneTextField: View {
    @Binding var phoneNumber: String
    @EnvironmentObject var themeManager: ThemeManager
    @State private var isEditing = false
    @State private var isValid = true
    @State private var selectedCountry = CountryCode(name: "United States", code: "US", flag: "🇺🇸", dialCode: "+1")
    @State private var showDropdown = false
    
    private let countries = [
        CountryCode(name: "United States", code: "US", flag: "🇺🇸", dialCode: "+1"),
        CountryCode(name: "United Kingdom", code: "GB", flag: "🇬🇧", dialCode: "+44"),
        CountryCode(name: "Canada", code: "CA", flag: "🇨🇦", dialCode: "+1"),
        CountryCode(name: "Australia", code: "AU", flag: "🇦🇺", dialCode: "+61"),
        CountryCode(name: "Germany", code: "DE", flag: "🇩🇪", dialCode: "+49"),
        CountryCode(name: "France", code: "FR", flag: "🇫🇷", dialCode: "+33"),
        CountryCode(name: "Japan", code: "JP", flag: "🇯🇵", dialCode: "+81"),
        CountryCode(name: "India", code: "IN", flag: "🇮🇳", dialCode: "+91"),
        CountryCode(name: "Brazil", code: "BR", flag: "🇧🇷", dialCode: "+55"),
        CountryCode(name: "Mexico", code: "MX", flag: "🇲🇽", dialCode: "+52")
    ]
    
    private var backgroundGradient: LinearGradient {
        if themeManager.isDarkMode {
            return LinearGradient(
                colors: [
                    LuxorColors.sandstone.opacity(0.1),
                    LuxorColors.hieroglyphBrown.opacity(0.05)
                ],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        } else {
            return LinearGradient(
                colors: [
                    LuxorColors.papyrusBeige,
                    LuxorColors.sandstone.opacity(0.3)
                ],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        }
    }
    
    private var borderGradient: LinearGradient {
        if isEditing {
            return LinearGradient(
                colors: [LuxorColors.pureGold, LuxorColors.richGold],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        } else if !isValid {
            return LinearGradient(
                colors: [Color.red.opacity(0.6), Color.red.opacity(0.3)],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        } else if themeManager.isDarkMode {
            return LinearGradient(
                colors: [LuxorColors.bronzeGold.opacity(0.3), LuxorColors.deepGold.opacity(0.2)],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        } else {
            return LinearGradient(
                colors: [LuxorColors.hieroglyphBrown.opacity(0.2), LuxorColors.sandstone.opacity(0.3)],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        }
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            ZStack {
                RoundedRectangle(cornerRadius: 16)
                    .fill(backgroundGradient)
                    .overlay(
                        RoundedRectangle(cornerRadius: 16)
                            .stroke(borderGradient, lineWidth: isEditing ? 2 : 1)
                    )
                    .shadow(
                        color: themeManager.isDarkMode ? LuxorColors.Dark.shadow : LuxorColors.Light.shadow,
                        radius: isEditing ? 12 : 6,
                        x: 0,
                        y: isEditing ? 6 : 3
                    )
                    .frame(height: 64)
                
                HStack(spacing: 12) {
                    
                    Button(action: {
                        withAnimation(.spring(response: 0.3, dampingFraction: 0.7)) {
                            showDropdown.toggle()
                        }
                    }) {
                        HStack(spacing: 8) {
                            Text(selectedCountry.flag)
                                .font(.system(size: 20))
                            
                            Text(selectedCountry.dialCode)
                                .font(.system(size: 16, weight: .medium))
                                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                            
                            Image(systemName: "chevron.down")
                                .font(.system(size: 12, weight: .medium))
                                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                                .rotationEffect(.degrees(showDropdown ? 180 : 0))
                                .animation(.spring(response: 0.3, dampingFraction: 0.7), value: showDropdown)
                        }
                        .padding(.horizontal, 12)
                        .padding(.vertical, 8)
                        .background(
                            RoundedRectangle(cornerRadius: 8)
                                .fill(themeManager.isDarkMode ? LuxorColors.Dark.cardBackground : Color(UIColor.systemGray6))
                        )
                    }
                    
                    Rectangle()
                        .fill(themeManager.isDarkMode ? LuxorColors.Dark.border : Color.gray.opacity(0.3))
                        .frame(width: 1, height: 30)
                    
                    VStack(alignment: .leading, spacing: 4) {
                        HStack {
                            Text("Phone Number")
                                .font(.system(
                                    size: (isEditing || !phoneNumber.isEmpty) ? 11 : 16,
                                    weight: .medium
                                ))
                                .foregroundColor(
                                    isEditing ? LuxorColors.luxorTeal :
                                    !isValid ? Color.red :
                                    themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : Color.gray
                                )
                                .offset(y: (isEditing || !phoneNumber.isEmpty) ? -8 : 8)
                                .animation(.spring(response: 0.4, dampingFraction: 0.8), value: isEditing || !phoneNumber.isEmpty)
                            Spacer()
                        }
                        
                        TextField("", text: $phoneNumber)
                            .font(.system(size: 16, weight: .medium))
                            .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                            .keyboardType(.phonePad)
                            .onTapGesture {
                                withAnimation(.spring(response: 0.4, dampingFraction: 0.8)) {
                                    isEditing = true
                                }
                                // Close dropdown when tapping on text field
                                if showDropdown {
                                    withAnimation(.spring(response: 0.3, dampingFraction: 0.7)) {
                                        showDropdown = false
                                    }
                                }
                            }
                            .onChange(of: phoneNumber) { newValue in
                                validatePhoneNumber(newValue)
                            }
                    }
                    
                    ZStack {
                        Circle()
                            .fill(
                                phoneNumber.isEmpty ? LinearGradient(colors: [Color.clear], startPoint: .top, endPoint: .bottom) :
                                isValid ? LinearGradient(colors: [Color.green.opacity(0.2), Color.green.opacity(0.1)], startPoint: .topLeading, endPoint: .bottomTrailing) :
                                LinearGradient(colors: [Color.red.opacity(0.2), Color.red.opacity(0.1)], startPoint: .topLeading, endPoint: .bottomTrailing)
                            )
                            .frame(width: 32, height: 32)
                        
                        if !phoneNumber.isEmpty {
                            Image(systemName: isValid ? "checkmark.circle.fill" : "xmark.circle.fill")
                                .foregroundColor(isValid ? .green : .red)
                                .font(.system(size: 16, weight: .medium))
                        }
                    }
                }
                .padding(.horizontal, 20)
            }
            if showDropdown {
                VStack(spacing: 0) {
                    ForEach(countries, id: \.code) { country in
                        Button(action: {
                            selectedCountry = country
                            withAnimation(.spring(response: 0.3, dampingFraction: 0.7)) {
                                showDropdown = false
                            }
                        }) {
                            HStack(spacing: 12) {
                                Text(country.flag)
                                    .font(.system(size: 20))
                                
                                VStack(alignment: .leading, spacing: 2) {
                                    Text(country.name)
                                        .font(.system(size: 14, weight: .medium))
                                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                                    
                                    Text(country.dialCode)
                                        .font(.system(size: 12))
                                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                                }
                                
                                Spacer()
                                
                                if selectedCountry.code == country.code {
                                    Image(systemName: "checkmark")
                                        .foregroundColor(LuxorColors.luxorTeal)
                                        .font(.system(size: 14, weight: .medium))
                                }
                            }
                            .padding(.horizontal, 16)
                            .padding(.vertical, 12)
                            .background(
                                (themeManager.isDarkMode ? LuxorColors.Dark.inputBackground : Color.white)
                                    .opacity(selectedCountry.code == country.code ? 0.1 : 0)
                            )
                        }
                        .buttonStyle(PlainButtonStyle())
                        
                        if country.code != countries.last?.code {
                            Divider()
                                .background(themeManager.isDarkMode ? LuxorColors.Dark.border : Color.gray.opacity(0.2))
                        }
                    }
                }
                .background(
                    RoundedRectangle(cornerRadius: 12)
                        .fill(themeManager.isDarkMode ? LuxorColors.Dark.cardBackground : Color.white)
                        .shadow(
                            color: themeManager.isDarkMode ? LuxorColors.Dark.shadow : LuxorColors.Light.shadow,
                            radius: 12,
                            x: 0,
                            y: 6
                        )
                )
                .transition(.asymmetric(
                    insertion: .scale(scale: 0.95).combined(with: .opacity).combined(with: .move(edge: .top)),
                    removal: .scale(scale: 0.95).combined(with: .opacity).combined(with: .move(edge: .top))
                ))
                .zIndex(1)
            }
            
            if !isValid && !phoneNumber.isEmpty {
                HStack(spacing: 8) {
                    Image(systemName: "exclamationmark.triangle.fill")
                        .foregroundColor(.red)
                        .font(.system(size: 12))
                    
                    Text("Please enter a valid phone number")
                        .font(.system(size: 12, weight: .medium))
                        .foregroundColor(.red)
                }
                .padding(.horizontal, 4)
            }
        }
        .onTapGesture {
            if showDropdown {
                withAnimation(.spring(response: 0.3, dampingFraction: 0.7)) {
                    showDropdown = false
                }
            }
        }
    }
    
    private func validatePhoneNumber(_ number: String) {
        let cleanedNumber = number.replacingOccurrences(of: "[^0-9]", with: "", options: .regularExpression)
        withAnimation(.spring(response: 0.4, dampingFraction: 0.8)) {
            isValid = number.isEmpty || (cleanedNumber.count >= 7 && cleanedNumber.count <= 15)
        }
    }
}

#Preview {
    VStack(spacing: 40) {
        LuxorPhoneTextField(phoneNumber: .constant("1234567890"))
            .environmentObject(ThemeManager())
        LuxorPhoneTextField(phoneNumber: .constant(""))
            .environmentObject({
                let theme = ThemeManager()
                theme.isDarkMode = true
                return theme
            }())
            .background(Color.black)
    }
    .padding()
}
