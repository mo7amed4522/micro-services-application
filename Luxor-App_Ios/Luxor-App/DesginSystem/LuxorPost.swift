import SwiftUI

struct LuxorPost: View {
    let post: PostModel
    @EnvironmentObject var themeManager: ThemeManager
    @State private var isLiked = false
    @State private var likeCount = 0
    @State private var showComments = false
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            // Header with author info
            HStack {
                // Profile picture
                AsyncImage(url: URL(string: post.authorProfileImage)) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                } placeholder: {
                    Circle()
                        .fill(themeManager.isDarkMode ? LuxorColors.Dark.inputBackground : LuxorColors.Light.secondaryBackground)
                        .overlay(
                            Image(systemName: "person.fill")
                                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                        )
                }
                .frame(width: 40, height: 40)
                .clipShape(Circle())
                
                VStack(alignment: .leading, spacing: 2) {
                    Text(post.authorName)
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                    
                    if let location = post.location {
                        Text(location)
                            .font(.system(size: 12))
                            .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                    }
                }
                
                Spacer()
                
                Button(action: {}) {
                    Image(systemName: "ellipsis")
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                        .font(.system(size: 16, weight: .medium))
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
            
            // Main post image
            AsyncImage(url: URL(string: post.imageUrl)) { image in
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
            } placeholder: {
                Rectangle()
                    .fill(themeManager.isDarkMode ? LuxorColors.Dark.inputBackground : LuxorColors.Light.secondaryBackground)
                    .overlay(
                        ProgressView()
                            .tint(LuxorColors.luxorTeal)
                    )
            }
            .frame(maxWidth: .infinity)
            .frame(height: UIScreen.main.bounds.width) // Square aspect ratio
            .clipped()
            
            // Action buttons
            HStack(spacing: 16) {
                Button(action: {
                    withAnimation(.spring(response: 0.3, dampingFraction: 0.6)) {
                        isLiked.toggle()
                        likeCount += isLiked ? 1 : -1
                    }
                }) {
                    Image(systemName: isLiked ? "heart.fill" : "heart")
                        .font(.system(size: 24, weight: .medium))
                        .foregroundColor(isLiked ? .red : (themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary))
                        .scaleEffect(isLiked ? 1.1 : 1.0)
                }
                
                Button(action: {
                    showComments.toggle()
                }) {
                    Image(systemName: "message")
                        .font(.system(size: 24, weight: .medium))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                }
                
                Button(action: {}) {
                    Image(systemName: "paperplane")
                        .font(.system(size: 24, weight: .medium))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                }
                
                Spacer()
                
                Button(action: {}) {
                    Image(systemName: "bookmark")
                        .font(.system(size: 24, weight: .medium))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                }
            }
            .padding(.horizontal, 16)
            .padding(.top, 12)
            
            // Like count
            if likeCount > 0 {
                Text("\(likeCount) likes")
                    .font(.system(size: 14, weight: .semibold))
                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                    .padding(.horizontal, 16)
                    .padding(.top, 8)
            }
            
            // Caption
            if !post.caption.isEmpty {
                HStack(alignment: .top, spacing: 4) {
                    Text(post.authorName)
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                    
                    Text(post.caption)
                        .font(.system(size: 14))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                        .multilineTextAlignment(.leading)
                }
                .padding(.horizontal, 16)
                .padding(.top, 4)
            }
            
            // Time ago
            Text(post.timeAgo)
                .font(.system(size: 12))
                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                .padding(.horizontal, 16)
                .padding(.top, 8)
                .padding(.bottom, 16)
        }
        .background(themeManager.isDarkMode ? LuxorColors.Dark.background : LuxorColors.Light.background)
        .onAppear {
            likeCount = post.likeCount
        }
    }
}

// Post data model
struct PostModel: Identifiable {
    let id = UUID()
    let authorName: String
    let authorProfileImage: String
    let location: String?
    let imageUrl: String
    let caption: String
    let likeCount: Int
    let timeAgo: String
}

// Sample data for preview
extension PostModel {
    static let samplePosts = [
        PostModel(
            authorName: "john_doe",
            authorProfileImage: "https://picsum.photos/100/100?random=1",
            location: "New York, NY",
            imageUrl: "https://picsum.photos/400/400?random=1",
            caption: "Beautiful sunset in the city! 🌅 #sunset #newyork #photography",
            likeCount: 142,
            timeAgo: "2 hours ago"
        ),
        PostModel(
            authorName: "jane_smith",
            authorProfileImage: "https://picsum.photos/100/100?random=2",
            location: "Los Angeles, CA",
            imageUrl: "https://picsum.photos/400/400?random=2",
            caption: "Coffee and good vibes ☕️ Starting the day right!",
            likeCount: 89,
            timeAgo: "4 hours ago"
        ),
        PostModel(
            authorName: "travel_enthusiast",
            authorProfileImage: "https://picsum.photos/100/100?random=3",
            location: "Paris, France",
            imageUrl: "https://picsum.photos/400/400?random=3",
            caption: "Exploring the streets of Paris. Every corner tells a story 🇫🇷 #travel #paris #adventure",
            likeCount: 256,
            timeAgo: "6 hours ago"
        ),
        PostModel(
            authorName: "foodie_life",
            authorProfileImage: "https://picsum.photos/100/100?random=4",
            location: "Tokyo, Japan",
            imageUrl: "https://picsum.photos/400/400?random=4",
            caption: "Authentic ramen in Tokyo! The flavors are incredible 🍜 #food #tokyo #ramen",
            likeCount: 178,
            timeAgo: "8 hours ago"
        ),
        PostModel(
            authorName: "nature_lover",
            authorProfileImage: "https://picsum.photos/100/100?random=5",
            location: "Yosemite National Park",
            imageUrl: "https://picsum.photos/400/400?random=5",
            caption: "Nature's masterpiece 🏔️ Nothing beats the serenity of the mountains",
            likeCount: 312,
            timeAgo: "12 hours ago"
        )
    ]
}

#Preview {
    LuxorPost(post: PostModel.samplePosts[0])
        .environmentObject(ThemeManager())
}