//
//  LuxorTextField.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import SwiftUI

struct LuxorTextField: View {
    let title: String
    @Binding var text: String
    let placeholder: String
    var keyboardType: UIKeyboardType = .default
    let errorMessage: String
    
    @State private var isEditing = false
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(title)
                .font(.luxorCaption)
                .foregroundColor(.luxorPrimaryText)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            TextField(placeholder, text: $text)
                .keyboardType(keyboardType)
                .textFieldStyle(LuxorTextFieldStyle())
                .onTapGesture {
                    isEditing = true
                }
                .onSubmit {
                    isEditing = false
                }
            
            if !errorMessage.isEmpty {
                HStack {
                    Image(systemName: "exclamationmark.triangle.fill")
                        .foregroundColor(.red)
                        .font(.caption)
                    
                    Text(errorMessage)
                        .font(.luxorCaption)
                        .foregroundColor(.red)
                    
                    Spacer()
                }
                .transition(.move(edge: .top).combined(with: .opacity))
                .animation(.easeInOut(duration: 0.3), value: errorMessage)
            }
        }
    }
}

struct LuxorTextFieldStyle: TextFieldStyle {
    func _body(configuration: TextField<Self._Label>) -> some View {
        configuration
            .padding(16)
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(
                        LinearGradient(
                            colors: [
                                LuxorColors.hieroglyphBrown.opacity(0.1),
                                LuxorColors.sandstone.opacity(0.05)
                            ],
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        )
                    )
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(
                                LinearGradient(
                                    colors: [
                                        LuxorColors.pureGold.opacity(0.3),
                                        LuxorColors.richGold.opacity(0.2)
                                    ],
                                    startPoint: .topLeading,
                                    endPoint: .bottomTrailing
                                ),
                                lineWidth: 1
                            )
                    )
            )
            .foregroundColor(.luxorPrimaryText)
    }
}

#Preview {
    VStack {
        LuxorTextField(
            title: "Email",
            text: .constant("test@example.com"),
            placeholder: "Enter your email",
            keyboardType: .emailAddress,
            errorMessage: ""
        )
        
        LuxorTextField(
            title: "Name",
            text: .constant(""),
            placeholder: "Enter your name",
            errorMessage: "This field is required"
        )
    }
    .padding()
    .background(Color.black)
}