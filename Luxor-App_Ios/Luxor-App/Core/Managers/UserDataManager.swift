import Foundation

class UserDataManager {
    static let shared = UserDataManager()
    private let userDefaults = UserDefaults.standard
    
    private init() {}
    
    // MARK: - User Data Keys
    private enum Keys {
        static let userId = "user_id"
        static let fullName = "full_name"
        static let email = "email"
        static let phoneNumber = "phone_number"
        static let countryCode = "country_code"
        static let photoURL = "photo_url"
        static let isLoggedIn = "is_logged_in"
    }
    
    // MARK: - Save User Data
    func saveUserData(
        userId: String,
        fullName: String,
        email: String,
        phoneNumber: String,
        countryCode: String,
        photoURL: String? = nil
    ) {
        userDefaults.set(userId, forKey: Keys.userId)
        userDefaults.set(fullName, forKey: Keys.fullName)
        userDefaults.set(email, forKey: Keys.email)
        userDefaults.set(phoneNumber, forKey: Keys.phoneNumber)
        userDefaults.set(countryCode, forKey: Keys.countryCode)
        userDefaults.set(photoURL, forKey: Keys.photoURL)
        userDefaults.set(true, forKey: Keys.isLoggedIn)
    }
    
    // MARK: - Get User Data
    func getUserId() -> String? {
        return userDefaults.string(forKey: Keys.userId)
    }
    
    func getPhotoURL() -> String? {
        return userDefaults.string(forKey: Keys.photoURL)
    }
    
    func getUserData() -> (userId: String?, fullName: String?, email: String?, phoneNumber: String?, countryCode: String?, photoURL: String?) {
        return (
            userId: userDefaults.string(forKey: Keys.userId),
            fullName: userDefaults.string(forKey: Keys.fullName),
            email: userDefaults.string(forKey: Keys.email),
            phoneNumber: userDefaults.string(forKey: Keys.phoneNumber),
            countryCode: userDefaults.string(forKey: Keys.countryCode),
            photoURL: userDefaults.string(forKey: Keys.photoURL)
        )
    }
    
    // MARK: - Update Photo URL
    func updatePhotoURL(_ photoURL: String) {
        userDefaults.set(photoURL, forKey: Keys.photoURL)
    }
    
    // MARK: - Clear Data
    func clearUserData() {
        userDefaults.removeObject(forKey: Keys.userId)
        userDefaults.removeObject(forKey: Keys.fullName)
        userDefaults.removeObject(forKey: Keys.email)
        userDefaults.removeObject(forKey: Keys.phoneNumber)
        userDefaults.removeObject(forKey: Keys.countryCode)
        userDefaults.removeObject(forKey: Keys.photoURL)
        userDefaults.set(false, forKey: Keys.isLoggedIn)
    }
    
    // MARK: - Check Login Status
    func isUserLoggedIn() -> Bool {
        return userDefaults.bool(forKey: Keys.isLoggedIn)
    }
}