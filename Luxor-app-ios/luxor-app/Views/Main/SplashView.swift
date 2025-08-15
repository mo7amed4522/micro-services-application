import SwiftUI

struct SplashView: View {
    let onFinish: () -> Void
    @State private var isAnimating = false
    @State private var logoScale: CGFloat = 0.5
    @State private var logoOpacity: Double = 0.0
    @State private var backgroundOpacity: Double = 0.0
    
    var body: some View {
        GeometryReader { geometry in
            ZStack {
                LuxorTheme.backgroundGradient
                    .ignoresSafeArea()
                    .opacity(backgroundOpacity)
                
                VStack(spacing: ResponsiveDesign.spacing(for: geometry.size) * 2) {
                    Image("logo-nobg")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(
                            width: min(geometry.size.width * 0.6, 250),
                            height: min(geometry.size.height * 0.3, 150)
                        )
                        .scaleEffect(logoScale)
                        .opacity(logoOpacity)
                }
                .position(
                    x: geometry.size.width / 2,
                    y: geometry.size.height / 2
                )
                
                LuxorTheme.overlayGradient
                    .ignoresSafeArea()
                    .opacity(0.2)
            }
        }
        .onAppear {
            startAnimations()
        }
    }
    
    private func startAnimations() {
        withAnimation(.easeIn(duration: 0.8)) {
            backgroundOpacity = 1.0
        }
        
        withAnimation(.easeOut(duration: 1.2).delay(0.3)) {
            logoScale = 1.0
            logoOpacity = 1.0
        }
        
        withAnimation(
            .easeInOut(duration: 2.0)
            .repeatForever(autoreverses: true)
            .delay(1.5)
        ) {
            isAnimating = true
        }
        
        // Auto-navigate after 3 seconds
        DispatchQueue.main.asyncAfter(deadline: .now() + 3.0) {
            onFinish()
        }
    }
}

#Preview {
    SplashView {
        print("Splash finished")
    }
}
