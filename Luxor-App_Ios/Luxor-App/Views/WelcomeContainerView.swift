import SwiftUI

struct WelcomeContainerView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @State private var currentPage = 0
    @State private var dragOffset: CGFloat = 0
    @State private var isAnimating = false
    @State private var showContent = false
    @State private var backgroundScale: CGFloat = 1.0
    
    let onComplete: () -> Void
    
    private let welcomePages = [
        WelcomePageData(
            imageName: "temp1",
            title: "Discover Ancient Wonders",
            subtitle: "Journey Through History",
            description: "Explore the magnificent temples and tombs of ancient Egypt with our immersive historical experience."
        ),
        WelcomePageData(
            imageName: "temp2",
            title: "Uncover Hidden Treasures",
            subtitle: "Archaeological Adventures",
            description: "Join archaeological expeditions and discover artifacts that tell the story of ancient civilizations."
        ),
        WelcomePageData(
            imageName: "temp3",
            title: "Walk Among Pharaohs",
            subtitle: "Royal Experiences",
            description: "Experience the grandeur of pharaohs and learn about the powerful rulers of ancient Egypt."
        ),
        WelcomePageData(
            imageName: "temp4",
            title: "Decode Ancient Mysteries",
            subtitle: "Hieroglyphic Secrets",
            description: "Learn to read hieroglyphs and unlock the secrets written in stone thousands of years ago."
        ),
        WelcomePageData(
            imageName: "temp5",
            title: "Sacred Rituals",
            subtitle: "Spiritual Journey",
            description: "Understand the spiritual beliefs and sacred rituals that guided ancient Egyptian life."
        ),
        WelcomePageData(
            imageName: "temp6",
            title: "Begin Your Adventure",
            subtitle: "Your Journey Awaits",
            description: "Ready to embark on an unforgettable journey through the wonders of ancient Egypt?"
        )
    ]
    
    var body: some View {
        GeometryReader { geometry in
            ZStack {
                TabView(selection: $currentPage) {
                    ForEach(0..<welcomePages.count, id: \.self) { index in
                        WelcomePageView(
                            imageName: welcomePages[index].imageName,
                            title: welcomePages[index].title,
                            subtitle: welcomePages[index].subtitle,
                            description: welcomePages[index].description
                        )
                        .tag(index)
                        .scaleEffect(backgroundScale)
                    }
                }
                .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
                .animation(.spring(response: 0.6, dampingFraction: 0.8), value: currentPage)
                .onChange(of: currentPage) { _ in
                    // Scale animation on page change
                    withAnimation(.easeInOut(duration: 0.3)) {
                        backgroundScale = 0.95
                    }
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
                        withAnimation(.easeInOut(duration: 0.3)) {
                            backgroundScale = 1.0
                        }
                    }
                }

                VStack {
                    HStack {
                        Spacer()
                        
                        Button(action: skipToEnd) {
                            Text("Skip")
                                .font(.system(size: 16, weight: .semibold))
                                .foregroundColor(.white)
                                .padding(.horizontal, 20)
                                .padding(.vertical, 10)
                                .background(
                                    Capsule()
                                        .fill(Color.black.opacity(0.3))
                                        .overlay(
                                            Capsule()
                                                .stroke(LuxorColors.pureGold.opacity(0.5), lineWidth: 1)
                                        )
                                )
                        }
                        .scaleEffect(showContent ? 1.0 : 0.8)
                        .opacity(showContent ? 1.0 : 0.0)
                        .animation(.spring(response: 0.6, dampingFraction: 0.8).delay(0.5), value: showContent)
                    }
                    .padding(.horizontal, 20)
                    .padding(.top, 50)
                    
                    Spacer()
                }
                VStack {
                    Spacer()
                    VStack(spacing: 30) {
                        HStack(spacing: 8) {
                            ForEach(0..<welcomePages.count, id: \.self) { index in
                                Circle()
                                    .fill(
                                        index == currentPage ? 
                                        LuxorColors.pureGold : 
                                        Color.white.opacity(0.4)
                                    )
                                    .frame(
                                        width: index == currentPage ? 12 : 8,
                                        height: index == currentPage ? 12 : 8
                                    )
                                    .overlay(
                                        Circle()
                                            .stroke(LuxorColors.richGold.opacity(0.6), lineWidth: index == currentPage ? 2 : 0)
                                    )
                                    .animation(.spring(response: 0.4, dampingFraction: 0.6), value: currentPage)
                            }
                        }
                        .offset(y: showContent ? 0 : 50)
                        .opacity(showContent ? 1.0 : 0.0)
                        .animation(.spring(response: 0.6, dampingFraction: 0.8).delay(0.7), value: showContent)
                        
                        Button(action: nextPage) {
                            HStack {
                                Text(currentPage == welcomePages.count - 1 ? "Get Started" : "Next")
                                    .font(.system(size: 18, weight: .semibold))
                                    .foregroundColor(LuxorColors.hieroglyphBrown)
                                
                                if currentPage < welcomePages.count - 1 {
                                    Image(systemName: "arrow.right")
                                        .font(.system(size: 16, weight: .semibold))
                                        .foregroundColor(LuxorColors.hieroglyphBrown)
                                        .offset(x: isAnimating ? 5 : 0)
                                        .animation(.easeInOut(duration: 0.3), value: isAnimating)
                                }
                            }
                            .padding(.horizontal, 40)
                            .padding(.vertical, 16)
                            .background(
                                Capsule()
                                    .fill(
                                        LinearGradient(
                                            colors: [
                                                LuxorColors.pureGold,
                                                LuxorColors.richGold
                                            ],
                                            startPoint: .leading,
                                            endPoint: .trailing
                                        )
                                    )
                                    .shadow(
                                        color: LuxorColors.hieroglyphBrown.opacity(0.3),
                                        radius: isAnimating ? 12 : 8,
                                        x: 0,
                                        y: isAnimating ? 6 : 4
                                    )
                            )
                        }
                        .scaleEffect(isAnimating ? 0.95 : 1.0)
                        .animation(.spring(response: 0.3, dampingFraction: 0.6), value: isAnimating)
                        .offset(y: showContent ? 0 : 80)
                        .opacity(showContent ? 1.0 : 0.0)
                        .animation(.spring(response: 0.6, dampingFraction: 0.8).delay(0.9), value: showContent)
                    }
                    .padding(.bottom, 50)
                }
            }
        }
        .ignoresSafeArea()
        .onAppear {
            withAnimation {
                showContent = true
            }
        }
    }
    
    private func nextPage() {
        withAnimation(.spring(response: 0.6, dampingFraction: 0.8)) {
            isAnimating = true
        }
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            if currentPage < welcomePages.count - 1 {
                withAnimation(.spring(response: 0.6, dampingFraction: 0.8)) {
                    currentPage += 1
                }
            } else {
                completeWelcome()
            }
            
            withAnimation(.spring(response: 0.6, dampingFraction: 0.8)) {
                isAnimating = false
            }
        }
    }
    
    private func skipToEnd() {
        withAnimation(.spring(response: 0.6, dampingFraction: 0.8)) {
            currentPage = welcomePages.count - 1
        }
    }
    
    private func completeWelcome() {
        UserDefaults.standard.set(true, forKey: "hasSeenWelcome")
        withAnimation(.spring(response: 0.6, dampingFraction: 0.8)) {
            onComplete()
        }
    }
}

struct WelcomePageData {
    let imageName: String
    let title: String
    let subtitle: String
    let description: String
}

#Preview {
    WelcomeContainerView(onComplete: {})
        .environmentObject(ThemeManager())
}
