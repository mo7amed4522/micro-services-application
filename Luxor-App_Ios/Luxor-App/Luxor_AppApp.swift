//
//  Luxor_AppApp.swift
//  Luxor-App
//
//  Created by Standard User on 16/08/2025.
//

import SwiftUI
import FirebaseCore
import FirebaseMessaging
import UserNotifications

// MARK: - AppDelegate (for Firebase, Notifications, etc.)
class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {
    
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        
        // Configure Firebase
        FirebaseApp.configure()
        
        // Request notification permissions
        UNUserNotificationCenter.current().delegate = self
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(
            options: authOptions,
            completionHandler: { granted, error in
                if let error = error {
                    print("❌ Notification authorization error: \(error.localizedDescription)")
                } else {
                    print("✅ Notification authorization granted: \(granted)")
                }
            }
        )
        
        // Register for remote notifications
        DispatchQueue.main.async {
            application.registerForRemoteNotifications()
        }
        
        // Firebase Messaging delegate
        Messaging.messaging().delegate = self
        
        return true
    }
    
    // Called when APNs has assigned a device token
    func application(_ application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        print("✅ APNs device token received")
        // Manually set the APNs token since method swizzling is disabled
        Messaging.messaging().apnsToken = deviceToken
    }
    
    // Called when APNs registration fails
    func application(_ application: UIApplication,
                     didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("❌ Failed to register for remote notifications: \(error.localizedDescription)")
    }
    
    // MARK: - Messaging Delegate
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("✅ FCM token: \(fcmToken ?? "nil")")
        // Send this token to your backend if needed
        let dataDict: [String: String] = ["token": fcmToken ?? ""]
        NotificationCenter.default.post(name: Notification.Name("FCMToken"), object: nil, userInfo: dataDict)
    }
    
    // MARK: - UNUserNotificationCenterDelegate
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                              willPresent notification: UNNotification,
                              withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        // Handle foreground notifications
        completionHandler([[.alert, .sound]])
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                              didReceive response: UNNotificationResponse,
                              withCompletionHandler completionHandler: @escaping () -> Void) {
        // Handle notification tap
        let userInfo = response.notification.request.content.userInfo
        print("✅ Notification tapped with userInfo: \(userInfo)")
        completionHandler()
    }
}

// MARK: - SwiftUI App
@main
struct Luxor_AppApp: App {
    // Attach AppDelegate to SwiftUI lifecycle
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    @StateObject private var themeManager = ThemeManager()
    @StateObject private var languageManager = LanguageManager()
    @StateObject private var authState = AuthState()
    @StateObject private var diContainer = DIContainer.shared
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(themeManager)
                .environmentObject(languageManager)
                .environmentObject(authState)
                .environmentObject(diContainer)
                .withToast()
                .withLoading()
                .onAppear {
                    diContainer.setAuthState(authState)
                }
        }
    }
}
