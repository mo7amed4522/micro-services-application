import SwiftUI

struct MainView: View {
    var body: some View {
        NavigationView {
            VStack(spacing: 20) {
                Image(systemName: "building.2")
                    .font(.system(size: 60))
                    .foregroundColor(LuxorTheme.primaryTeal)
                
                Text("Luxor Properties")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundColor(LuxorTheme.primaryText)
                
                Text("Coming Soon...")
                    .font(.title2)
                    .foregroundColor(LuxorTheme.secondaryText)
                
                Spacer()
            }
            .padding()
            .navigationTitle("Luxor")
        }
        .tint(LuxorTheme.primaryTeal)
    }
}

#Preview {
    MainView()
}