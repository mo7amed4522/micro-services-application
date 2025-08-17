package com.example.luxorappandroid.data

import androidx.compose.runtime.Immutable

// Post data model
@Immutable
data class PostModel(
    val id: String = java.util.UUID.randomUUID().toString(),
    val authorName: String,
    val authorProfileImage: String,
    val location: String?,
    val imageUrl: String,
    val caption: String,
    val likeCount: Int,
    val timeAgo: String
)

// Chat data model
@Immutable
data class Chat(
    val id: Int,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unread: Boolean
)

// Message data model
@Immutable
data class Message(
    val id: String = java.util.UUID.randomUUID().toString(),
    val content: String,
    val isFromCurrentUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

// Place data model
@Immutable
data class PlaceModel(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val location: String,
    val category: String,
    val imageURL: String,
    val likes: Int,
    val rating: Double
)

// Sample data
object SampleData {
    val samplePosts = listOf(
        PostModel(
            authorName = "john_doe",
            authorProfileImage = "https://picsum.photos/100/100?random=1",
            location = "New York, NY",
            imageUrl = "https://picsum.photos/400/400?random=1",
            caption = "Beautiful sunset in the city! 🌅 #sunset #newyork #photography",
            likeCount = 142,
            timeAgo = "2 hours ago"
        ),
        PostModel(
            authorName = "jane_smith",
            authorProfileImage = "https://picsum.photos/100/100?random=2",
            location = "Los Angeles, CA",
            imageUrl = "https://picsum.photos/400/400?random=2",
            caption = "Coffee and good vibes ☕️ Starting the day right!",
            likeCount = 89,
            timeAgo = "4 hours ago"
        ),
        PostModel(
            authorName = "travel_enthusiast",
            authorProfileImage = "https://picsum.photos/100/100?random=3",
            location = "Paris, France",
            imageUrl = "https://picsum.photos/400/400?random=3",
            caption = "Exploring the streets of Paris. Every corner tells a story 🇫🇷 #travel #paris #adventure",
            likeCount = 256,
            timeAgo = "6 hours ago"
        ),
        PostModel(
            authorName = "foodie_life",
            authorProfileImage = "https://picsum.photos/100/100?random=4",
            location = "Tokyo, Japan",
            imageUrl = "https://picsum.photos/400/400?random=4",
            caption = "Authentic ramen in Tokyo! The flavors are incredible 🍜 #food #tokyo #ramen",
            likeCount = 178,
            timeAgo = "8 hours ago"
        ),
        PostModel(
            authorName = "nature_lover",
            authorProfileImage = "https://picsum.photos/100/100?random=5",
            location = "Yosemite National Park",
            imageUrl = "https://picsum.photos/400/400?random=5",
            caption = "Nature's masterpiece 🏔️ Nothing beats the serenity of the mountains",
            likeCount = 312,
            timeAgo = "12 hours ago"
        )
    )

    val sampleChats = listOf(
        Chat(id = 1, name = "John Doe", lastMessage = "Hey there!", time = "10:30 AM", unread = true),
        Chat(id = 2, name = "Jane Smith", lastMessage = "Meeting at 3pm", time = "Yesterday", unread = false),
        Chat(id = 3, name = "Travel Group", lastMessage = "New photos uploaded!", time = "2 hours ago", unread = true),
        Chat(id = 4, name = "Work Team", lastMessage = "Project deadline updated", time = "1 hour ago", unread = false)
    )

    val samplePlaces = listOf(
        PlaceModel(name = "Luxor Temple", location = "Luxor, Egypt", category = "Museums", imageURL = "https://images.unsplash.com/photo-1539650116574-75c0c6d73f6e?w=400", likes = 1250, rating = 4.8),
        PlaceModel(name = "Golden Café", location = "Downtown", category = "Cafes", imageURL = "https://images.unsplash.com/photo-1554118811-1e0d58224f24?w=400", likes = 890, rating = 4.6),
        PlaceModel(name = "Royal Restaurant", location = "City Center", category = "Restaurants", imageURL = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=400", likes = 2100, rating = 4.9),
        PlaceModel(name = "Grand Hotel", location = "Marina", category = "Hotels", imageURL = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=400", likes = 1680, rating = 4.7),
        PlaceModel(name = "Central Park", location = "Uptown", category = "Parks", imageURL = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=400", likes = 3200, rating = 4.5),
        PlaceModel(name = "Luxury Mall", location = "Shopping District", category = "Shopping", imageURL = "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=400", likes = 950, rating = 4.4),
        PlaceModel(name = "Art Gallery", location = "Cultural Quarter", category = "Museums", imageURL = "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400", likes = 720, rating = 4.6),
        PlaceModel(name = "Rooftop Café", location = "Skyline View", category = "Cafes", imageURL = "https://images.unsplash.com/photo-1559925393-8be0ec4767c8?w=400", likes = 1450, rating = 4.8)
    )

    val statusItems = listOf("Online", "Away", "Busy", "Offline")
    val categories = listOf("All", "Restaurants", "Hotels", "Museums", "Parks", "Shopping", "Cafes")
    val languages = listOf("English", "Spanish", "French", "German", "Arabic", "Chinese")
    val currencies = listOf("USD", "EUR", "GBP", "JPY", "CAD", "AUD")
}
