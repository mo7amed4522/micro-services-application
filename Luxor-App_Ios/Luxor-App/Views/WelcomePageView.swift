import SwiftUI

struct WelcomePageView: View {
    @EnvironmentObject var themeManager: ThemeManager
    let imageName: String
    let title: String
    let subtitle: String
    let description: String
    
    @State private var animateContent = false
    @State private var animateGradient = false
    @State private var parallaxOffset: CGFloat = 0
    
    var body: some View {
        GeometryReader { geometry in
            ZStack {
                Image(imageName)
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(
                        width: geometry.size.width + 100,
                        height: geometry.size.height + 100
                    )
                    .offset(x: parallaxOffset * 0.5, y: parallaxOffset * 0.3)
                    .clipped()
                    .animation(.easeOut(duration: 0.8), value: parallaxOffset)
                  LinearGradient(
                    colors: [
                        Color.black.opacity(animateGradient ? 0.2 : 0.4),
                        Color.black.opacity(0.1),
                        LuxorColors.hieroglyphBrown.opacity(animateGradient ? 0.3 : 0.5),
                        LuxorColors.deepGold.opacity(animateGradient ? 0.7 : 0.5)
                    ],
                    startPoint: .top,
                    endPoint: .bottom
                )
                .animation(.easeInOut(duration: 2.0).repeatForever(autoreverses: true), value: animateGradient)
                   ForEach(0..<8, id: \.self) { index in
                    Circle()
                        .fill(LuxorColors.pureGold.opacity(0.3))
                        .frame(width: CGFloat.random(in: 2...6))
                        .offset(
                            x: CGFloat.random(in: -geometry.size.width/2...geometry.size.width/2),
                            y: animateContent ? -geometry.size.height : geometry.size.height
                        )
                        .animation(
                            .linear(duration: Double.random(in: 3...8))
                            .repeatForever(autoreverses: false)
                            .delay(Double.random(in: 0...2)),
                            value: animateContent
                        )
                }
                VStack {
                    Spacer()
                    VStack(spacing: 20) {
                        Rectangle()
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
                            .frame(width: animateContent ? 60 : 0, height: 3)
                            .cornerRadius(1.5)
                            .animation(.spring(response: 0.8, dampingFraction: 0.6).delay(0.3), value: animateContent)
                        Text(title)
                            .font(.system(size: 32, weight: .bold, design: .serif))
                            .foregroundStyle(
                                LinearGradient(
                                    colors: [
                                        LuxorColors.pureGold,
                                        LuxorColors.richGold
                                    ],
                                    startPoint: .leading,
                                    endPoint: .trailing
                                )
                            )
                            .multilineTextAlignment(.center)
                            .shadow(color: .black.opacity(0.5), radius: 2, x: 1, y: 1)
                            .scaleEffect(animateContent ? 1.0 : 0.8)
                            .opacity(animateContent ? 1.0 : 0.0)
                            .animation(.spring(response: 0.6, dampingFraction: 0.8).delay(0.5), value: animateContent)

                        Text(subtitle)
                            .font(.system(size: 18, weight: .semibold, design: .serif))
                            .foregroundColor(LuxorColors.papyrusBeige)
                            .multilineTextAlignment(.center)
                            .shadow(color: .black.opacity(0.3), radius: 1, x: 0, y: 1)
                            .offset(y: animateContent ? 0 : 30)
                            .opacity(animateContent ? 1.0 : 0.0)
                            .animation(.spring(response: 0.6, dampingFraction: 0.8).delay(0.7), value: animateContent)
                     
                        Text(description)
                            .font(.system(size: 16, weight: .regular, design: .default))
                            .foregroundColor(.white.opacity(0.9))
                            .multilineTextAlignment(.center)
                            .lineLimit(nil)
                            .padding(.horizontal, 20)
                            .shadow(color: .black.opacity(0.5), radius: 1, x: 0, y: 1)
                            .opacity(animateContent ? 1.0 : 0.0)
                            .animation(.easeInOut(duration: 0.8).delay(0.9), value: animateContent)
                    }
                    .padding(.horizontal, 40)
                    .padding(.bottom, 270)
                    .padding()
                    
                }
            }
        }
        .ignoresSafeArea()
        .onAppear {
            withAnimation {
                animateContent = true
                animateGradient = true
            }
        }
        .onDisappear {
            animateContent = false
        }
        .gesture(
            DragGesture()
                .onChanged { value in
                    parallaxOffset = value.translation.width
                }
                .onEnded { _ in
                    withAnimation(.spring()) {
                        parallaxOffset = 0
                    }
                }
        )
    }
}

#Preview {
    WelcomePageView(
        imageName: "temp1",
        title: "Discover Ancient Wonders",
        subtitle: "Journey Through History",
        description: "Explore the magnificent temples and tombs of ancient Egypt with our immersive historical experience."
    )
    .environmentObject(ThemeManager())
}
