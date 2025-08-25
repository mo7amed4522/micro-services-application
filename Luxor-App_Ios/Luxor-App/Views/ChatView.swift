import SwiftUI

struct ChatView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Binding var isInDetailView: Bool
    @Binding var isNavBarVisible: Bool
    @Binding var isAppBarVisible: Bool
    @Binding var lastScrollOffset: CGFloat
    @Binding var scrollTimer: Timer?
    @Binding var isScrolling: Bool
    @State private var statusItems = ["Online", "Away", "Busy", "Offline"]
    @State private var chats = [
        Chat(id: 1, name: "John Doe", lastMessage: "Hey there!", time: "10:30 AM", unread: true),
        Chat(id: 2, name: "Jane Smith", lastMessage: "Meeting at 3pm", time: "Yesterday", unread: false)
    ]
    
    var body: some View {
        NavigationView {
            ZStack(alignment: .bottomTrailing) {
                ScrollView(.vertical, showsIndicators: false) {
                    VStack {
                        Text("STATUS")
                            .font(.system(size: 22, weight: .bold, design: .rounded))
                            .foregroundColor(LuxorColors.luxorTeal)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.horizontal)
                        ScrollView(.horizontal, showsIndicators: false) {
                            HStack(spacing: 15) {
                                ForEach(statusItems, id: \.self) { status in
                                    VStack {
                                        
                                        RoundedRectangle(cornerRadius: 8)
                                            .fill(LuxorColors.luxorTeal.opacity(0.1))
                                            .frame(width: 70, height: 100)
                                            .overlay(
                                                Image(systemName: "person.crop.square.fill")
                                                    .font(.system(size: 30))
                                                    .foregroundColor(LuxorColors.luxorTeal)
                                            )
                                            .overlay(
                                                RoundedRectangle(cornerRadius: 8)
                                                    .stroke(LuxorColors.luxorTeal, lineWidth: 2)
                                            )
                                        Text(status)
                                            .font(.caption)
                                            .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                                    }
                                }
                            }
                            .padding()
                        }
                        Text("CHATS")
                            .font(.system(size: 22, weight: .bold, design: .rounded))
                            .foregroundColor(LuxorColors.luxorTeal)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.horizontal)
                        
                        LazyVStack(spacing: 0) {
                            ForEach(chats) { chat in
                                NavigationLink(destination: ConversationView(chat: chat, isInDetailView: $isInDetailView)) {
                                    HStack {
                                        RoundedRectangle(cornerRadius: 8)
                                            .fill(LuxorColors.luxorTeal.opacity(0.1))
                                            .frame(width: 50, height: 50)
                                            .overlay(
                                                Image(systemName: "person.crop.square.fill")
                                                    .font(.system(size: 25))
                                                    .foregroundColor(LuxorColors.luxorTeal)
                                            )
                                        VStack(alignment: .leading) {
                                            Text(chat.name)
                                                .font(.headline)
                                                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textPrimary : LuxorColors.Light.textPrimary)
                                            Text(chat.lastMessage)
                                                .font(.subheadline)
                                                .foregroundColor(chat.unread ? 
                                                    LuxorColors.luxorTeal : 
                                                    (themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary))
                                                .fontWeight(chat.unread ? .bold : .regular)
                                        }
                                        Spacer()
                                        VStack(alignment: .trailing) {
                                            Text(chat.time)
                                                .font(.caption)
                                                .foregroundColor(themeManager.isDarkMode ? LuxorColors.Dark.textSecondary : LuxorColors.Light.textSecondary)
                                            if chat.unread {
                                                Circle()
                                                    .fill(LuxorColors.luxorTeal)
                                                    .frame(width: 10, height: 10)
                                            }
                                        }
                                    }
                                    .padding(.horizontal, 16)
                                    .padding(.vertical, 12)
                                    .background(themeManager.isDarkMode ? LuxorColors.Dark.background : LuxorColors.Light.background)
                                }
                                .buttonStyle(PlainButtonStyle())
                                
                                if chat.id != chats.last?.id {
                                    Divider()
                                        .background(themeManager.isDarkMode ? LuxorColors.Dark.border : Color.gray.opacity(0.2))
                                        .padding(.horizontal, 16)
                                }
                            }
                        }
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
                
                Button(action: {}) {
                    Image(systemName: "plus")
                        .font(.title.weight(.bold))
                        .foregroundColor(.white)
                        .padding(15)
                        .background(LuxorColors.luxorTeal)
                        .clipShape(Circle())
                        .shadow(radius: 4)
                }
                .padding(.trailing, 20)
                .padding(.bottom, 70)
            }
            .luxorThemedBackground(themeManager)
            .navigationBarHidden(true)
        }
    }

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
}

struct Chat: Identifiable {
    let id: Int
    let name: String
    let lastMessage: String
    let time: String
    let unread: Bool
}

#Preview {
    ChatView(
        isInDetailView: .constant(false),
        isNavBarVisible: .constant(true),
        isAppBarVisible: .constant(true),
        lastScrollOffset: .constant(0),
        scrollTimer: .constant(nil),
        isScrolling: .constant(false)
    )
    .environmentObject(ThemeManager())
    .environmentObject(LanguageManager())
}
