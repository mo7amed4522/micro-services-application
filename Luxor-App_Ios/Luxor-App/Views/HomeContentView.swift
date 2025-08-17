import SwiftUI

struct HomeContentView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Binding var isNavBarVisible: Bool
    @Binding var isAppBarVisible: Bool
    @Binding var lastScrollOffset: CGFloat
    @Binding var scrollTimer: Timer?
    @Binding var isScrolling: Bool
    
    private let posts = PostModel.samplePosts
    
    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            LazyVStack(spacing: 0) {
                ForEach(posts) { post in
                    LuxorPost(post: post)
                        .id(post.id)
                }
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
    }
    
    // MARK: - Instagram-Style Scroll Handling
    private func handleInstagramStyleScroll(currentOffset: CGFloat) {
        let delta = abs(currentOffset - lastScrollOffset)
        if delta > 1.0 {
            if !isScrolling {
                isScrolling = true
                withAnimation(.easeInOut(duration: 0.3)) {
                    isAppBarVisible = false
                    isNavBarVisible = false
                }
            }
            scrollTimer?.invalidate()
            scrollTimer = Timer.scheduledTimer(withTimeInterval: 1.5, repeats: false) { _ in
                isScrolling = false
                withAnimation(.easeInOut(duration: 0.4)) {
                    isAppBarVisible = true
                    isNavBarVisible = true
                }
            }
        }
        if currentOffset >= -10 {
            scrollTimer?.invalidate()
            isScrolling = false
            withAnimation(.easeInOut(duration: 0.3)) {
                isAppBarVisible = true
                isNavBarVisible = true
            }
        }
        
        lastScrollOffset = currentOffset
    }
}

// MARK: - ScrollOffsetPreferenceKey
struct ScrollOffsetPreferenceKey: PreferenceKey {
    static var defaultValue: CGFloat = 0
    
    static func reduce(value: inout CGFloat, nextValue: () -> CGFloat) {
        value = nextValue()
    }
}

#Preview {
    HomeContentView(
        isNavBarVisible: .constant(true),
        isAppBarVisible: .constant(true),
        lastScrollOffset: .constant(0),
        scrollTimer: .constant(nil),
        isScrolling: .constant(false)
    )
    .environmentObject(ThemeManager())
}