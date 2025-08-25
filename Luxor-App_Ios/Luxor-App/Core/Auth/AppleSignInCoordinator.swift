//
//  AppleSignInCoordinator.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import Foundation
import AuthenticationServices
import SwiftUI

class AppleSignInCoordinator: NSObject, ObservableObject, ASAuthorizationControllerDelegate {
    private var authState: AuthState?
    private var grpcClient: GRPCClientManager?
    private var loadingCallback: ((Bool) -> Void)?
    
    override init() {
        super.init()
    }
    
    init(grpcClient: GRPCClientManager, authState: AuthState) {
        self.grpcClient = grpcClient
        self.authState = authState
        super.init()
    }
    
    func updateDependencies(grpcClient: GRPCClientManager?, authState: AuthState?) {
        self.grpcClient = grpcClient
        self.authState = authState
    }
    
    func signIn(loadingCallback: @escaping (Bool) -> Void) {
        self.loadingCallback = loadingCallback
        guard let authState = authState, let grpcClient = grpcClient else {
            loadingCallback(false)
            return
        }
        let request = ASAuthorizationAppleIDProvider().createRequest()
        request.requestedScopes = [.fullName, .email]
        
        let authorizationController = ASAuthorizationController(authorizationRequests: [request])
        authorizationController.delegate = self
        authorizationController.performRequests()
    }
    
    func authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization authorization: ASAuthorization) {
        guard let appleIDCredential = authorization.credential as? ASAuthorizationAppleIDCredential else {
            authState?.showErrorToast("Failed to get Apple ID credentials")
            loadingCallback?(false)
            return
        }
        
        guard let identityTokenData = appleIDCredential.identityToken,
              let identityToken = String(data: identityTokenData, encoding: .utf8) else {
            authState?.showErrorToast("Failed to get identity token")
            loadingCallback?(false)
            return
        }
        
        guard let authorizationCodeData = appleIDCredential.authorizationCode,
              let authorizationCode = String(data: authorizationCodeData, encoding: .utf8) else {
            authState?.showErrorToast("Failed to get authorization code")
            loadingCallback?(false)
            return
        }
        
        let fullName = formatFullName(from: appleIDCredential.fullName)
        let email = appleIDCredential.email
        
        loadingCallback?(true)
        authState?.setLoading(true)
        
        Task {
            do {
                guard let grpcClient = grpcClient else {
                    await MainActor.run {
                        authState?.setLoading(false)
                        loadingCallback?(false)
                        authState?.showErrorToast("Authentication service not available")
                    }
                    return
                }
                
                let response = try await grpcClient.authenticateWithApple(
                    identityToken: identityToken,
                    authorizationCode: authorizationCode,
                    fullName: fullName,
                    email: email
                )
                
                await MainActor.run {
                    handleAuthenticationResponse(response)
                }
            } catch {
                await MainActor.run {
                    authState?.setLoading(false)
                    loadingCallback?(false)
                    authState?.showErrorToast("Apple Sign In failed: \(error.localizedDescription)")
                }
            }
        }
    }
    
    func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error) {
        authState?.setLoading(false)
        loadingCallback?(false)
        
        if let authError = error as? ASAuthorizationError {
            switch authError.code {
            case .canceled:
                break
            case .failed:
                authState?.showErrorToast("Apple Sign In failed")
            case .invalidResponse:
                authState?.showErrorToast("Invalid response from Apple")
            case .notHandled:
                authState?.showErrorToast("Apple Sign In not handled")
            case .unknown:
                authState?.showErrorToast("Unknown Apple Sign In error")
            @unknown default:
                authState?.showErrorToast("Apple Sign In error: \(error.localizedDescription)")
            }
        } else {
            authState?.showErrorToast("Apple Sign In error: \(error.localizedDescription)")
        }
    }
    
    private func handleAuthenticationResponse(_ response: Auth_SocialAuthResponse) {
        authState?.setLoading(false)
        loadingCallback?(false)
        
        if response.statusCode == 200 {
            if case .tokens(let authTokens) = response.result {
                let user = grpcClient?.convertToDomainUser(authTokens.user) ?? User(id: "mock", fullName: "Mock User", email: "mock@example.com", role: "user")
                let tokens = grpcClient?.convertToDomainAuthTokens(authTokens) ?? AuthTokens(accessToken: "mock_token", expiresIn: 3600, user: user)
                
                authState?.login(user: user, tokens: tokens)
                authState?.showSuccessToast("Successfully signed in with Apple")
            } else {
                authState?.showErrorToast("Authentication failed: No tokens received")
            }
        } else {
            if case .error(let errorDetails) = response.result {
                authState?.showErrorToast(errorDetails.message)
            } else {
                authState?.showErrorToast("Authentication failed")
            }
        }
    }
    
    private func formatFullName(from personNameComponents: PersonNameComponents?) -> String? {
        guard let nameComponents = personNameComponents else { return nil }
        
        let formatter = PersonNameComponentsFormatter()
        formatter.style = .long
        return formatter.string(from: nameComponents)
    }
}
