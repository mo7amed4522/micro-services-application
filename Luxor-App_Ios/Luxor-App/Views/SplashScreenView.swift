import SwiftUI

struct SplashScreenView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @State private var logoScale: CGFloat = 0.5
    @State private var logoOpacity: Double = 0.0
    @State private var backgroundOpacity: Double = 0.0
    @State private var showGoldenParticles: Bool = false
    @State private var isAnimationComplete: Bool = false
    
    let onAnimationComplete: () -> Void
    
    var body: some View {
        ZStack {
            LinearGradient(
                colors: [
                    LuxorColors.hieroglyphBrown.opacity(0.9),
                    LuxorColors.deepGold.opacity(0.3),
                    LuxorColors.pureGold.opacity(0.1)
                ],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .opacity(backgroundOpacity)
            .ignoresSafeArea()
            if showGoldenParticles {
                ForEach(0..<20, id: \.self) { index in
                    Circle()
                        .fill(LuxorColors.pureGold.opacity(0.6))
                        .frame(width: CGFloat.random(in: 2...6))
                        .position(
                            x: CGFloat.random(in: 0...UIScreen.main.bounds.width),
                            y: CGFloat.random(in: 0...UIScreen.main.bounds.height)
                        )
                        .opacity(Double.random(in: 0.3...0.8))
                        .animation(
                            Animation.easeInOut(duration: Double.random(in: 2...4))
                                .repeatForever(autoreverses: true)
                                .delay(Double.random(in: 0...2)),
                            value: showGoldenParticles
                        )
                }
            }
            VStack(spacing: 30) {
                ZStack {
                    Image("luxor-logo")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 120, height: 120)
                        .blur(radius: 20)
                        .opacity(0.6)
                    Image("luxor-logo")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 120, height: 120)
                }
                .scaleEffect(logoScale)
                .opacity(logoOpacity)
                VStack(spacing: 8) {
                    Text("EGYPT FOR EVERY ONE !!")
                        .font(.system(size: 30, weight: .bold, design: .serif))
                        .foregroundStyle(
                            LinearGradient(
                                colors: [
                                    LuxorColors.pureGold,
                                    LuxorColors.richGold,
                                    LuxorColors.deepGold
                                ],
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                        )
                        .shadow(color: LuxorColors.hieroglyphBrown.opacity(0.5), radius: 2, x: 1, y: 1)
                    
                    Text("Journey Through Time")
                        .font(.system(size: 14, weight: .medium, design: .serif))
                        .foregroundColor(LuxorColors.papyrusBeige)
                        .opacity(0.9)
                }
                .opacity(logoOpacity)
                .scaleEffect(logoScale * 0.9)
            }
        }
        .onAppear {
            startSplashAnimation()
        }
    }
    
    private func startSplashAnimation() {
        withAnimation(.easeIn(duration: 0.5)) {
            backgroundOpacity = 1.0
        }
        withAnimation(.spring(response: 1.2, dampingFraction: 0.8, blendDuration: 0).delay(0.3)) {
            logoScale = 1.0
            logoOpacity = 1.0
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.8) {
            withAnimation(.easeInOut(duration: 0.5)) {
                showGoldenParticles = true
            }
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 2.5) {
            withAnimation(.easeOut(duration: 0.5)) {
                logoOpacity = 0.0
                backgroundOpacity = 0.0
            }
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                isAnimationComplete = true
                onAnimationComplete()
            }
        }
    }
}

#Preview {
    SplashScreenView(onAnimationComplete: {})
        .environmentObject(ThemeManager())
}
