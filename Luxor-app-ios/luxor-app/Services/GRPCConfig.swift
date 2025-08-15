import Foundation

struct GRPCConfig {
    private static let baseHost = "localhost"
    
    static let authServiceURL: URL = {
        guard let url = URL(string: "http://\(baseHost):3001") else {
            fatalError("Invalid auth service URL")
        }
        return url
    }()
    
    static let userServiceURL: URL = {
        guard let url = URL(string: "http://\(baseHost):3002") else {
            fatalError("Invalid user service URL")
        }
        return url
    }()
    
}