# Luxor Android App

This is the Android implementation of the Luxor app, designed to match the iOS version's design and functionality using Jetpack Compose.

## ✅ Complete Android Implementation

### 🎨 **Design System**
- **Luxor Brand Colors**: Teal (#008080) and Gold (#CC9933) matching iOS
- **Dark/Light Theme**: Complete theme system with proper color adaptation
- **Custom Components**: All iOS components recreated in Jetpack Compose

### 🔐 **Authentication Screens**
- **Login View**: Beautiful login interface with email/password validation
- **Register View**: Complete registration form with profile photo selection
- **Custom Text Fields**: Animated input fields with floating labels
- **Social Login**: Apple, Google, Facebook login buttons
- **Password Validation**: Real-time strength requirements
- **Form Validation**: Email format validation and password strength checking

### 🏠 **Home Feed**
- **Instagram-style Posts**: Social media feed with images and interactions
- **Scroll Handling**: Smart scroll behavior to hide/show app bar and navigation
- **Interactive Elements**: Like, comment, share, and bookmark functionality
- **Post Components**: Author info, location, captions, and timestamps

### 🗺️ **Places Screen**
- **Search Functionality**: Real-time search with animated search bar
- **Category Filtering**: Horizontal scrollable category selection
- **Dynamic Content**: Dynamic text based on selected category
- **Place Cards**: Grid layout with place information and ratings
- **Like System**: Interactive like buttons for places

### 💬 **Chat System**
- **Chat List**: Status items and chat conversations
- **Conversation View**: Full chat interface with message bubbles
- **Voice Recording**: Voice message recording with waveform visualization
- **Typing Indicators**: Animated typing indicators
- **Message Timestamps**: Time formatting for messages

### ⚙️ **Settings Screen**
- **Account Management**: Profile editing and account settings
- **Preferences**: Dark mode, location services, biometric login
- **Privacy & Security**: Two-factor authentication, password changes
- **App Settings**: Auto-play videos, data saver, updates
- **Support Section**: Help center, contact support, app rating

### 🎯 **Custom Components**

#### LuxorLogo
- Animated logo with gradient effects and pulsing animation
- Responsive sizing for different screen sizes

#### Custom Text Fields
- **LuxorEmailTextField**: Email input with real-time validation
- **LuxorPasswordTextField**: Password input with strength requirements
- **FullNameTextField**: Full name input with validation
- **PhoneNumberTextField**: Phone input with country code selection
- **Floating Labels**: Animated labels that move when focused

#### LiquidBottomNavBar
- Gradient background with blur effects
- Animated tab selection with scaling
- Text overlays for selected tabs
- Smooth transitions between states

#### LuxorPost
- Instagram-style post layout
- Async image loading with Coil
- Interactive action buttons
- Like count and engagement metrics

#### SocialLoginButtons
- Apple, Google, Facebook login options
- Consistent styling with brand colors
- Hover and press animations

## Technical Implementation

### Architecture
- **Jetpack Compose**: Modern declarative UI framework
- **Material 3**: Latest Material Design components
- **MVVM Pattern**: Clean architecture with separation of concerns
- **State Management**: Proper state handling with remember and mutableStateOf

### Dependencies
```kotlin
// Image Loading
implementation("io.coil-kt:coil-compose:2.5.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Material 3
implementation("androidx.compose.material3:material3")
```

### File Structure
```
app/src/main/java/com/example/luxorappandroid/
├── data/
│   └── Models.kt                 # Data models and sample data
├── ui/
│   ├── components/
│   │   ├── LuxorLogo.kt         # Animated logo component
│   │   ├── LuxorTextField.kt    # Custom text field components
│   │   ├── SocialLoginButtons.kt # Social login buttons
│   │   ├── LiquidBottomNavBar.kt # Bottom navigation
│   │   └── LuxorPost.kt         # Post component
│   ├── screens/
│   │   ├── LoginView.kt         # Login screen
│   │   ├── RegisterView.kt      # Registration screen
│   │   ├── HomeContentView.kt   # Home feed
│   │   ├── PlacesView.kt        # Places screen
│   │   ├── ChatView.kt          # Chat list
│   │   ├── ConversationView.kt  # Individual chat
│   │   ├── SettingsView.kt      # Settings screen
│   │   └── MainTabView.kt       # Main app with tabs
│   └── theme/
│       ├── Color.kt             # Color definitions
│       ├── Theme.kt             # Material 3 theme
│       ├── ThemeManager.kt      # Theme state management
│       └── Type.kt              # Typography
└── MainActivity.kt              # Main activity
```

## Design Matching iOS

### Color Scheme
- **Primary**: Luxor Teal (#008080)
- **Secondary**: Luxor Gold (#CC9933)
- **Dark Theme**: Enhanced visibility with proper contrast
- **Light Theme**: Clean white backgrounds with subtle gradients

### Animations
- **Spring Animations**: Smooth, natural-feeling transitions
- **Scale Effects**: Interactive feedback on button presses
- **Floating Labels**: Animated text field labels
- **Loading States**: Progress indicators and skeleton screens
- **Waveform Animation**: Voice recording visualization
- **Typing Indicators**: Animated dots for chat

### Layout
- **Responsive Design**: Adapts to different screen sizes
- **Edge-to-Edge**: Full-screen immersive experience
- **Proper Spacing**: Consistent padding and margins
- **Typography**: Clear hierarchy with proper font weights
- **Grid Layouts**: Responsive grid for places and posts

## Getting Started

1. **Clone the repository**
2. **Open in Android Studio**
3. **Sync Gradle dependencies**
4. **Run on device or emulator**

## Build Requirements

- **Android Studio**: Arctic Fox or later
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 35 (Android 15)
- **Kotlin**: 1.9.0 or later
- **Jetpack Compose**: Latest version

## Features Status

### ✅ Completed
- [x] Complete authentication flow (Login + Register)
- [x] Home feed with Instagram-style posts
- [x] Places screen with search and filtering
- [x] Chat system with conversations
- [x] Settings screen with all sections
- [x] Dark/Light theme support
- [x] Custom UI components
- [x] Animations and transitions
- [x] Form validation
- [x] Voice recording interface

### 🚀 Future Enhancements
- [ ] Real-time chat functionality
- [ ] Location-based features
- [ ] Push notifications
- [ ] Offline support
- [ ] Analytics integration
- [ ] Accessibility improvements
- [ ] Image picker integration
- [ ] Backend API integration

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
