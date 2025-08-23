# Firebase Setup for Luxor iOS App

## Configuration Files

### GoogleService-Info.plist
The `GoogleService-Info.plist` file has been added to the project with placeholder values. To complete the Firebase setup:

1. Go to the [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select an existing one
3. Add an iOS app to your Firebase project
4. Download the `GoogleService-Info.plist` file
5. Replace the placeholder file at `Luxor-App/GoogleService-Info.plist` with the downloaded file

### Required Configuration Values
Update the following values in `GoogleService-Info.plist`:
- `API_KEY`: Your Firebase API key
- `GCM_SENDER_ID`: Your GCM sender ID
- `PROJECT_ID`: Your Firebase project ID
- `GOOGLE_APP_ID`: Your Google app ID
- `CLIENT_ID`: Your OAuth 2.0 client ID
- `REVERSED_CLIENT_ID`: Your reversed client ID

## Firebase Initialization

Firebase is automatically initialized in `Luxor_AppApp.swift` using `FirebaseApp.configure()` in the app's initializer.

## Dependencies

The following Firebase dependencies are already configured in the project:
- FirebaseCore
- FirebaseAuth
- FirebaseAnalytics
- FirebaseFirestore
- And other Firebase services

## Social Authentication

The app supports:
- Apple Sign In (using AuthenticationServices)
- Google Sign In (using Firebase Auth)

Both are integrated with the backend gRPC authentication service.