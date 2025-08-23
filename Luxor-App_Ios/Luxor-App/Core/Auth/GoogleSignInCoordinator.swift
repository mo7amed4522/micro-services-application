//
//  GoogleSignInCoordinator.swift
//  Luxor-App
//
//  Created by Assistant on 2024.
//

import Foundation
import FirebaseAuth
import UIKit

@MainActor
class GoogleSignInCoordinator: NSObject,ObservableObject {
    private let grpcClient: GRPCClientManager
    private let authState: AuthState
    
    @Published var isLoading = false
    
    init(grpcClient: GRPCClientManager, authState: AuthState) {
        self.grpcClient = grpcClient
        self.authState = authState
    }
    
    func updateDependencies(grpcClient: GRPCClientManager, authState: AuthState) {
        // Update dependencies when environment objects are available
    }
    
    func signIn(loadingCallback: @escaping (Bool) -> Void) {
        signInWithGoogle(loadingCallback: loadingCallback)
    }
    
    private func signInWithGoogle(loadingCallback: @escaping (Bool) -> Void) {
        isLoading = true
        authState.setLoading(true)
        loadingCallback(true)
        
        // For now, we'll simulate Google Sign In since the SDK isn't configured
        // In a real implementation, you would need to add GoogleSignIn SDK to the project
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) { [weak self] in
            self?.isLoading = false
            self?.authState.setLoading(false)
            loadingCallback(false)
            
            // Simulate successful Google Sign In with mock data
            let mockIdToken = "mock_google_id_token_\(UUID().uuidString)"
            let mockAccessToken = "mock_google_access_token_\(UUID().uuidString)"
            
            Task {
                await self?.authenticateWithBackend(idToken: mockIdToken, accessToken: mockAccessToken)
            }
        }
    }
    
    private func authenticateWithBackend(idToken: String, accessToken: String) async {
        do {
            let response = try await grpcClient.authenticateWithGoogle(
                idToken: idToken,
                accessToken: accessToken
            )
            
            if response.statusCode == 200 {
                let user = grpcClient.convertToDomainUser(response.tokens.user)
                let tokens = grpcClient.convertToDomainAuthTokens(response.tokens) ?? AuthTokens(accessToken: "mock_token", expiresIn: 3600, user: user)
                 authState.login(user: user, tokens: tokens)
                 authState.showSuccessToast("Successfully signed in with Google")
            } else {
                if case .error(let errorDetails) = response.result {
                     authState.showErrorToast(errorDetails.message)
                 } else {
                     authState.showErrorToast("Authentication failed")
                 }
            }
        } catch {
             authState.showErrorToast("Google Sign In failed: \(error.localizedDescription)")
        }
    }
}
