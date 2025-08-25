//
//  LuxorSecureField.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import SwiftUI

struct LuxorSecureField: View {
    let title: String
    @Binding var text: String
    let placeholder: String
    let errorMessage: String
    
    @State private var isSecure = true
    @State private var isEditing = false
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(title)
                .font(.luxorCaption)
                .foregroundColor(.luxorPrimaryText)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            HStack {
                Group {
                    if isSecure {
                        SecureField(placeholder, text: $text)
                    } else {
                        TextField(placeholder, text: $text)
                    }
                }
                .textFieldStyle(LuxorSecureFieldStyle())
                .onTapGesture {
                    isEditing = true
                }
                .onSubmit {
                    isEditing = false
                }
                
                Button(action: {
                    isSecure.toggle()
                }) {
                    Image(systemName: isSecure ? "eye.slash" : "eye")
                        .foregroundColor(.luxorGold)
                        .font(.system(size: 16))
                }
                .padding(.trailing, 16)
            }
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

struct LuxorSecureFieldStyle: TextFieldStyle {
    func _body(configuration: TextField<Self._Label>) -> some View {
        configuration
            .padding(.leading, 16)
            .padding(.vertical, 16)
            .foregroundColor(.luxorPrimaryText)
    }
}

#Preview {
    VStack {
        LuxorSecureField(
            title: "Password",
            text: .constant("password123"),
            placeholder: "Enter your password",
            errorMessage: ""
        )
        
        LuxorSecureField(
            title: "Confirm Password",
            text: .constant(""),
            placeholder: "Confirm your password",
            errorMessage: "Passwords do not match"
        )
    }
    .padding()
    .background(Color.black)
}