import SwiftUI

struct PlacesView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Binding var isNavBarVisible: Bool
    @Binding var isAppBarVisible: Bool
    @Binding var lastScrollOffset: CGFloat
    @Binding var scrollTimer: Timer?
    @Binding var isScrolling: Bool
    @State private var searchText = ""
    @State private var selectedCategory = "All"
    @State private var animateCards = false
    
    private let categories = ["All", "Restaurants", "Hotels", "Museums", "Parks", "Shopping", "Cafes"]
    private let places = PlaceModel.samplePlaces
    
    var filteredPlaces: [PlaceModel] {
        if selectedCategory == "All" {
            return places.filter { searchText.isEmpty || $0.name.localizedCaseInsensitiveContains(searchText) }
        } else {
            return places.filter { 
                $0.category == selectedCategory && 
                (searchText.isEmpty || $0.name.localizedCaseInsensitiveContains(searchText))
            }
        }
    }
    
    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            VStack(spacing: 0) {
                searchSection
                    .padding(.horizontal, 20)
                    .padding(.top, 20)
                categoriesSection
                    .padding(.top, 25)
                dynamicTextSection
                    .padding(.horizontal, 20)
                    .padding(.top, 30)
                placesGridSection
                    .padding(.horizontal, 20)
                    .padding(.top, 20)
                    .padding(.bottom, 100)
                    
                
            }
            .background(
                GeometryReader { geometry in
                    Color.clear
                        .preference(
                            key: ScrollOffsetPreferenceKey.self,
                            value: geometry.frame(in: .named("scrollView")).minY
                        )
                }
            )
        }
        .coordinateSpace(name: "scrollView")
        .onPreferenceChange(ScrollOffsetPreferenceKey.self) { currentOffset in
            handleInstagramStyleScroll(currentOffset: currentOffset)
        }
        .luxorThemedBackground(themeManager)
        .onAppear {
            withAnimation(.spring(response: 0.8, dampingFraction: 0.8).delay(0.2)) {
                animateCards = true
            }
        }
    }
    
    // MARK: - Instagram-Style Scroll Handling
    private func handleInstagramStyleScroll(currentOffset: CGFloat) {
        let delta = abs(currentOffset - lastScrollOffset)
        if delta > 1.0 {
            if !isScrolling {
                isScrolling = true
                withAnimation(.easeInOut(duration: 0.3)) {
                    isNavBarVisible = false
                }
            }
            scrollTimer?.invalidate()
            scrollTimer = Timer.scheduledTimer(withTimeInterval: 1.5, repeats: false) { _ in
                isScrolling = false
                withAnimation(.easeInOut(duration: 0.4)) {
                    isNavBarVisible = true
                }
            }
        }
        if currentOffset >= -10 {
            scrollTimer?.invalidate()
            isScrolling = false
            withAnimation(.easeInOut(duration: 0.3)) {
                isNavBarVisible = true
            }
        }
        
        lastScrollOffset = currentOffset
    }
    
    // MARK: - Search Section
    private var searchSection: some View {
        HStack {
            HStack {
                Image(systemName: "magnifyingglass")
                    .foregroundColor(LuxorColors.luxorTeal)
                    .font(.system(size: 18, weight: .medium))
                
                TextField("Search amazing places...", text: $searchText)
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                
                if !searchText.isEmpty {
                    Button(action: { searchText = "" }) {
                        Image(systemName: "xmark.circle.fill")
                            .foregroundColor(LuxorColors.luxorTeal.opacity(0.6))
                    }
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 14)
            .background(
                RoundedRectangle(cornerRadius: 16)
                    .fill(themeManager.isDarkMode ? LuxorColors.Dark.inputBackground : LuxorColors.Light.secondaryBackground)
                    .overlay(
                        RoundedRectangle(cornerRadius: 16)
                            .stroke(
                                LinearGradient(
                                    colors: [LuxorColors.luxorTeal.opacity(0.3), LuxorColors.luxorGold.opacity(0.3)],
                                    startPoint: .leading,
                                    endPoint: .trailing
                                ),
                                lineWidth: 1
                            )
                    )
            )
        }
    }
    
    // MARK: - Categories Section
    private var categoriesSection: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 16) {
                ForEach(Array(categories.enumerated()), id: \.offset) { index, category in
                    CategoryCard(category: category, isSelected: selectedCategory == category)
                        .onTapGesture {
                            withAnimation(.spring(response: 0.5, dampingFraction: 0.7)) {
                                selectedCategory = category
                            }
                        }
                        .scaleEffect(animateCards ? 1.0 : 0.8)
                        .opacity(animateCards ? 1.0 : 0.0)
                        .animation(.spring(response: 0.6, dampingFraction: 0.8).delay(Double(index) * 0.1), value: animateCards)
                }
            }
            .padding(.horizontal, 20)
        }
    }
    
    // MARK: - Dynamic Text Section
    private var dynamicTextSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                Text(selectedCategory == "All" ? "Discover" : "Explore")
                    .font(.system(size: 28, weight: .bold, design: .rounded))
                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                
                Text(selectedCategory == "All" ? "Everything" : selectedCategory)
                    .font(.system(size: 28, weight: .bold, design: .rounded))
                    .foregroundColor(LuxorColors.luxorTeal)
                
                Spacer()
            }
            
            Text(getDescriptionText())
                .font(.system(size: 16, weight: .medium))
                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                .lineLimit(2)
        }
    }
    
    // MARK: - Places Grid Section
    private var placesGridSection: some View {
        LazyVGrid(columns: [
            GridItem(.flexible(), spacing: 16),
            GridItem(.flexible(), spacing: 20)
        ], spacing: 20) {
            ForEach(Array(filteredPlaces.enumerated()), id: \.element.id) { index, place in
                PlaceCard(place: place, themeManager: themeManager)
                    .scaleEffect(animateCards ? 1.0 : 0.8)
                    .opacity(animateCards ? 1.0 : 0.0)
                    .animation(.spring(response: 0.7, dampingFraction: 0.8).delay(Double(index) * 0.05), value: animateCards)
                    
            }
        }
    }
    
    private func getDescriptionText() -> String {
        switch selectedCategory {
        case "Restaurants": return "Taste the finest cuisines and culinary experiences"
        case "Hotels": return "Luxurious stays and comfortable accommodations"
        case "Museums": return "Explore art, history, and cultural treasures"
        case "Parks": return "Natural beauty and outdoor adventures await"
        case "Shopping": return "Premium brands and unique shopping experiences"
        case "Cafes": return "Cozy spots for coffee and delightful conversations"
        default: return "Amazing places waiting to be discovered by you"
        }
    }
}

// MARK: - Category Card Component
struct CategoryCard: View {
    let category: String
    let isSelected: Bool
    @EnvironmentObject var themeManager: ThemeManager
    
    var body: some View {
        Text(category)
            .font(.system(size: 14, weight: .semibold))
            .foregroundColor(isSelected ? .white : (themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary))
            .padding(.horizontal, 20)
            .padding(.vertical, 20)
            .background(
                RoundedRectangle(cornerRadius: 25)
                    .fill(
                        isSelected ? 
                        LinearGradient(
                            colors: [LuxorColors.luxorTeal, LuxorColors.luxorGold],
                            startPoint: .leading,
                            endPoint: .trailing
                        ) :
                        LinearGradient(
                            colors: [themeManager.isDarkMode ? LuxorColors.Dark.cardBackground : LuxorColors.Light.cardBackground],
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                    )
                    .overlay(
                        RoundedRectangle(cornerRadius: 25)
                            .stroke(
                                isSelected ? Color.clear : 
                                (themeManager.isDarkMode ? LuxorColors.Dark.border : LuxorColors.Light.textSecondary.opacity(0.2)),
                                lineWidth: 1
                            )
                    )
            )
            .shadow(
                color: isSelected ? LuxorColors.luxorTeal.opacity(0.3) : Color.clear,
                radius: 8,
                x: 0,
                y: 4
            )
    }
}

// MARK: - Place Card Component
struct PlaceCard: View {
    let place: PlaceModel
    let themeManager: ThemeManager
    @State private var isLiked = false
    
    var body: some View {
        VStack(alignment: .leading, spacing: 9) {
            // Image Section
            ZStack(alignment: .topTrailing) {
                AsyncImage(url: URL(string: place.imageURL)) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                } placeholder: {
                    RoundedRectangle(cornerRadius: 16)
                        .fill(LuxorColors.luxorTeal.opacity(0.1))
                        .overlay(
                            Image(systemName: "photo")
                                .font(.system(size: 30))
                                .foregroundColor(LuxorColors.luxorTeal.opacity(0.5))
                        )
                }
                .frame(height: 140)
                .clipped()
                .cornerRadius(16, corners: [.topLeft, .topRight])
                Button(action: {
                    withAnimation(.spring(response: 0.3, dampingFraction: 0.6)) {
                        isLiked.toggle()
                    }
                }) {
                    Image(systemName: isLiked ? "heart.fill" : "heart")
                        .font(.system(size: 16, weight: .medium))
                        .foregroundColor(isLiked ? .red : .white)
                        .padding(8)
                        .background(
                            Circle()
                                .fill(Color.black.opacity(0.3))
                                .background(.ultraThinMaterial, in: Circle())
                        )
                }
                .padding(12)
                .scaleEffect(isLiked ? 1.2 : 1.0)
            }
            VStack(alignment: .leading, spacing: 8) {
                HStack {
                    Image(systemName: "heart.fill")
                        .font(.system(size: 12, weight: .medium))
                        .foregroundColor(.red)
                    
                    Text("\(place.likes)")
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                    
                    Spacer()
                    
                    HStack(spacing: 2) {
                        Image(systemName: "star.fill")
                            .font(.system(size: 10))
                            .foregroundColor(LuxorColors.luxorGold)
                        
                        Text(String(format: "%.1f", place.rating))
                            .font(.system(size: 11, weight: .medium))
                            .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                    }
                }
                Text(place.name)
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                    .lineLimit(2)
                    .multilineTextAlignment(.leading)
            HStack {
                    Image(systemName: "location")
                        .font(.system(size: 10))
                        .foregroundColor(LuxorColors.luxorTeal)
                    
                    Text(place.location)
                        .font(.system(size: 12, weight: .medium))
                        .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                        .lineLimit(1)
                }
            }
            .padding(16)
        }
        .luxorCard(themeManager)
        .onTapGesture {
        }
    }
}

// MARK: - Place Model
struct PlaceModel: Identifiable {
    let id = UUID()
    let name: String
    let location: String
    let category: String
    let imageURL: String
    let likes: Int
    let rating: Double
    
    static let samplePlaces = [
        PlaceModel(name: "Luxor Temple", location: "Luxor, Egypt", category: "Museums", imageURL: "https://images.unsplash.com/photo-1539650116574-75c0c6d73f6e?w=400", likes: 1250, rating: 4.8),
        PlaceModel(name: "Golden Café", location: "Downtown", category: "Cafes", imageURL: "https://images.unsplash.com/photo-1554118811-1e0d58224f24?w=400", likes: 890, rating: 4.6),
        PlaceModel(name: "Royal Restaurant", location: "City Center", category: "Restaurants", imageURL: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=400", likes: 2100, rating: 4.9),
        PlaceModel(name: "Grand Hotel", location: "Marina", category: "Hotels", imageURL: "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=400", likes: 1680, rating: 4.7),
        PlaceModel(name: "Central Park", location: "Uptown", category: "Parks", imageURL: "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=400", likes: 3200, rating: 4.5),
        PlaceModel(name: "Luxury Mall", location: "Shopping District", category: "Shopping", imageURL: "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=400", likes: 950, rating: 4.4),
        PlaceModel(name: "Art Gallery", location: "Cultural Quarter", category: "Museums", imageURL: "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400", likes: 720, rating: 4.6),
        PlaceModel(name: "Rooftop Café", location: "Skyline View", category: "Cafes", imageURL: "https://images.unsplash.com/photo-1559925393-8be0ec4767c8?w=400", likes: 1450, rating: 4.8)
    ]
}

// MARK: - Corner Radius Extension
extension View {
    func cornerRadius(_ radius: CGFloat, corners: UIRectCorner) -> some View {
        clipShape(RoundedCorner(radius: radius, corners: corners))
    }
}

struct RoundedCorner: Shape {
    var radius: CGFloat = .infinity
    var corners: UIRectCorner = .allCorners

    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(
            roundedRect: rect,
            byRoundingCorners: corners,
            cornerRadii: CGSize(width: radius, height: radius)
        )
        return Path(path.cgPath)
    }
}

#Preview {
    PlacesView(
        isNavBarVisible: .constant(true),
        isAppBarVisible: .constant(true),
        lastScrollOffset: .constant(0),
        scrollTimer: .constant(nil),
        isScrolling: .constant(false)
    )
    .environmentObject(ThemeManager())
}
