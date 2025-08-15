import Foundation

struct User: Codable, Identifiable {
    let id: UUID
    let email: String
    let firstName: String
    let lastName: String
    let profileImageURL: String?
    let createdAt: Date?
    let updatedAt: Date?
    
    var fullName: String {
        "\(firstName) \(lastName)"
    }
    
    var initials: String {
        let firstInitial = firstName.first?.uppercased() ?? ""
        let lastInitial = lastName.first?.uppercased() ?? ""
        return "\(firstInitial)\(lastInitial)"
    }
}

// MARK: - User Authentication State
struct AuthenticationState {
    var isAuthenticated: Bool = false
    var currentUser: User?
    var authToken: String?
}