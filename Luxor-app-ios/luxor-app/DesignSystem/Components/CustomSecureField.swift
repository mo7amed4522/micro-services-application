import SwiftUI

struct CustomSecureField: View {
    let placeholder: String
    @Binding var text: String
    let isSecure: Bool
    
    var body: some View {
        if isSecure {
            HStack {
                Text(String(repeating: "*", count: text.count))
                    .foregroundColor(.primary)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                // Hidden TextField for actual input
                TextField(placeholder, text: $text)
                    .opacity(0)
                    .overlay(
                        Rectangle()
                            .fill(Color.clear)
                            .contentShape(Rectangle())
                    )
            }
        } else {
            TextField(placeholder, text: $text)
        }
    }
}