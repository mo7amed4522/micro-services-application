import SwiftUI

struct SettingsView: View {
    @State private var selectedLanguage: String = "English"
    @State private var isDarkMode: Bool = false
    
    var body: some View {
        NavigationView {
            Form {
                Section("Appearance") {
                    Toggle("Dark Mode", isOn: $isDarkMode)
                        .tint(LuxorTheme.primaryTeal)
                }
                
                Section("Language") {
                    Picker("Select Language", selection: $selectedLanguage) {
                        Text("English").tag("English")
                        Text("العربية").tag("Arabic")
                        Text("Français").tag("French")
                    }
                    .pickerStyle(MenuPickerStyle())
                    .tint(LuxorTheme.primaryTeal)
                }
                
                Section("About") {
                    HStack {
                        Text("Version")
                        Spacer()
                        Text("1.0.0")
                            .foregroundColor(LuxorTheme.secondaryText)
                    }
                    
                    HStack {
                        Text("Company")
                        Spacer()
                        Text("Luxor Properties")
                            .foregroundColor(LuxorTheme.primaryTeal)
                    }
                }
            }
            .navigationTitle("Settings")
            .navigationBarTitleDisplayMode(.large)
        }
        .tint(LuxorTheme.primaryTeal)
    }
}

#Preview {
    SettingsView()
}