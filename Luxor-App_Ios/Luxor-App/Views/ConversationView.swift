import SwiftUI

struct Message: Identifiable {
    let id = UUID()
    let content: String
    let isFromCurrentUser: Bool
    let timestamp: Date
    var isRead: Bool = false
}

struct ConversationView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.presentationMode) var presentationMode

    let chat: Chat
    @Binding var isInDetailView: Bool
    @State private var isTyping = false
    @State private var messageText = ""
    @State private var isRecording = false
    @State private var recordingTime: TimeInterval = 0
    @State private var recordingTimer: Timer? = nil
    @State private var waveformValues: [CGFloat] = Array(
        repeating: 0.1,
        count: 30
    )
    @State private var messages: [Message] = [
        Message(
            content: "Hey there!",
            isFromCurrentUser: false,
            timestamp: Date().addingTimeInterval(-3600)
        ),
        Message(
            content: "Hi! How are you?",
            isFromCurrentUser: true,
            timestamp: Date().addingTimeInterval(-3500)
        ),
        Message(
            content: "I'm good, thanks for asking. What about you?",
            isFromCurrentUser: false,
            timestamp: Date().addingTimeInterval(-3400)
        ),
        Message(
            content: "I'm doing well. Just wanted to check in.",
            isFromCurrentUser: true,
            timestamp: Date().addingTimeInterval(-3300)
        ),
    ]

    var body: some View {
        ZStack(alignment: .bottom) {
            VStack(spacing: 0) {
                chatHeader
                ScrollView {
                    LazyVStack(spacing: 12) {
                        ForEach(messages) { message in
                            MessageBubble(
                                message: message,
                                themeManager: themeManager
                            )
                        }

                        if isTyping {
                            TypingIndicator(themeManager: themeManager)
                                .padding(.leading, 12)
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }
                    }
                    .padding(.horizontal, 12)
                    .padding(.top, 12)
                    .padding(.bottom, 8)
                }
                .background(
                    themeManager.isDarkMode
                        ? LuxorColors.Dark.background
                        : LuxorColors.Light.background
                )
                chatInputBar
            }
        }
        .navigationBarHidden(true)
        .luxorThemedBackground(themeManager)
        .overlay(recordingOverlay)
        .onAppear {
            isInDetailView = true
        }
        .onDisappear {
            isInDetailView = false
        }
    }
    private var chatHeader: some View {
        HStack(spacing: 12) {
            Button(action: {
                presentationMode.wrappedValue.dismiss()
            }) {
                Image(systemName: "chevron.left")
                    .font(.system(size: 20, weight: .semibold))
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.accent : LuxorColors.Light.accent
                    )
                    .padding(8)
                    .background(
                        Circle()
                            .fill(
                                themeManager.isDarkMode
                                    ? LuxorColors.Dark.cardBackground
                                    : LuxorColors.Light.cardBackground
                            )
                            .shadow(
                                color: themeManager.isDarkMode
                                    ? LuxorColors.Dark.shadow
                                    : LuxorColors.Light.shadow,
                                radius: 3,
                                x: 0,
                                y: 1
                            )
                    )
            }

            ZStack {
                Circle()
                    .fill(
                        (themeManager.isDarkMode
                            ? LuxorColors.Dark.accent
                            : LuxorColors.Light.accent).opacity(0.1)
                    )
                    .frame(width: 40, height: 40)

                Image(systemName: "person.crop.circle.fill")
                    .font(.system(size: 30))
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.accent : LuxorColors.Light.accent
                    )
            }

            VStack(alignment: .leading, spacing: 2) {
                Text(chat.name)
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.textPrimary
                            : LuxorColors.Light.textPrimary
                    )

                Text("Online")
                    .font(.system(size: 12))
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.accent : LuxorColors.Light.accent
                    )
            }

            Spacer()
            Button(action: {}) {
                Image(systemName: "phone.fill")
                    .font(.system(size: 18))
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.accent : LuxorColors.Light.accent
                    )
                    .padding(8)
                    .background(
                        Circle()
                            .fill(
                                themeManager.isDarkMode
                                    ? LuxorColors.Dark.cardBackground
                                    : LuxorColors.Light.cardBackground
                            )
                            .shadow(
                                color: themeManager.isDarkMode
                                    ? LuxorColors.Dark.shadow
                                    : LuxorColors.Light.shadow,
                                radius: 3,
                                x: 0,
                                y: 1
                            )
                    )
            }

            Button(action: {}) {
                Image(systemName: "video.fill")
                    .font(.system(size: 18))
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.accent : LuxorColors.Light.accent
                    )
                    .padding(8)
                    .background(
                        Circle()
                            .fill(
                                themeManager.isDarkMode
                                    ? LuxorColors.Dark.cardBackground
                                    : LuxorColors.Light.cardBackground
                            )
                            .shadow(
                                color: themeManager.isDarkMode
                                    ? LuxorColors.Dark.shadow
                                    : LuxorColors.Light.shadow,
                                radius: 3,
                                x: 0,
                                y: 1
                            )
                    )
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(
            themeManager.isDarkMode
                ? LuxorColors.Dark.cardBackground
                : LuxorColors.Light.cardBackground
        )
        .shadow(
            color: themeManager.isDarkMode
                ? Color.black.opacity(0.3) : Color.gray.opacity(0.2),
            radius: 5,
            x: 0,
            y: 2
        )
    }

    private var chatInputBar: some View {
        VStack(spacing: 0) {
            HStack(spacing: 12) {
                Button(action: {}) {
                    Image(systemName: "plus")
                        .font(.system(size: 20))
                        .foregroundColor(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.accent
                                : LuxorColors.Light.accent
                        )
                        .frame(width: 40, height: 40)
                        .background(
                            Circle()
                                .fill(
                                    themeManager.isDarkMode
                                        ? LuxorColors.Dark.cardBackground
                                        : LuxorColors.Light.cardBackground
                                )
                                .shadow(
                                    color: themeManager.isDarkMode
                                        ? LuxorColors.Dark.shadow
                                        : LuxorColors.Light.shadow,
                                    radius: 3,
                                    x: 0,
                                    y: 1
                                )
                        )
                }
                ZStack(alignment: .trailing) {
                    TextField("Message", text: $messageText)
                        .padding(.horizontal, 16)
                        .padding(.vertical, 12)
                        .background(
                            RoundedRectangle(cornerRadius: 20)
                                .fill(
                                    themeManager.isDarkMode
                                        ? LuxorColors.Dark.inputBackground
                                        : Color(UIColor.systemGray6)
                                )
                        )
                        .foregroundColor(
                            themeManager.isDarkMode
                                ? LuxorColors.Dark.textPrimary
                                : LuxorColors.Light.textPrimary
                        )
                        .onChange(of: messageText) { _ in
                            if !isTyping && !messageText.isEmpty {
                                isTyping = true
                                DispatchQueue.main.asyncAfter(
                                    deadline: .now() + 2
                                ) {
                                    isTyping = false
                                }
                            }
                        }
                }
                Button(action: {
                    if messageText.isEmpty {
                        startRecording()
                    } else {
                        sendMessage()
                    }
                }) {
                    Image(
                        systemName: messageText.isEmpty
                            ? "mic.fill" : "arrow.up.circle.fill"
                    )
                    .font(.system(size: 24))
                    .foregroundColor(.white)
                    .frame(width: 40, height: 40)
                    .background(
                        Circle()
                            .fill(
                                themeManager.isDarkMode
                                    ? LuxorColors.Dark.accent
                                    : LuxorColors.Light.accent
                            )
                            .shadow(
                                color: themeManager.isDarkMode
                                    ? LuxorColors.Dark.shadow
                                    : LuxorColors.Light.shadow,
                                radius: 3,
                                x: 0,
                                y: 1
                            )
                    )
                }
                .simultaneousGesture(
                    LongPressGesture(minimumDuration: 0.5)
                        .onEnded { _ in
                            if messageText.isEmpty {
                                startRecording()
                            }
                        }
                )
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
            .background(
                themeManager.isDarkMode
                    ? LuxorColors.Dark.cardBackground
                    : LuxorColors.Light.cardBackground
            )
            .shadow(
                color: themeManager.isDarkMode
                    ? Color.black.opacity(0.3) : Color.gray.opacity(0.2),
                radius: 5,
                x: 0,
                y: -2
            )
        }
    }
    private var recordingOverlay: some View {
        Group {
            if isRecording {
                VStack {
                    HStack {
                        Circle()
                            .fill(Color.red)
                            .frame(width: 10, height: 10)
                        Text("Recording... \(Int(recordingTime))s")
                            .font(.system(size: 14, weight: .medium))
                            .foregroundColor(.white)
                        Spacer()
                        Text("Slide to cancel")
                            .font(.system(size: 14))
                            .foregroundColor(.white.opacity(0.7))
                    }
                    .padding(.horizontal, 16)
                    .padding(.vertical, 8)
                    .background(Color.red.opacity(0.8))
                    .frame(maxWidth: .infinity)
                    .frame(height: 36)
                    .transition(.move(edge: .top).combined(with: .opacity))
                    Spacer()
                    HStack(spacing: 3) {
                        ForEach(0..<waveformValues.count, id: \.self) { index in
                            RoundedRectangle(cornerRadius: 1.5)
                                .fill(Color.white)
                                .frame(
                                    width: 3,
                                    height: 20 * waveformValues[index]
                                )
                        }
                    }
                    .frame(height: 40)
                    .padding(.horizontal, 20)
                    .padding(.vertical, 10)
                    .background(
                        RoundedRectangle(cornerRadius: 20)
                            .fill(
                                (themeManager.isDarkMode
                                    ? LuxorColors.Dark.accent
                                    : LuxorColors.Light.accent).opacity(0.8)
                            )
                    )
                    .padding(.horizontal, 16)
                    .padding(.bottom, 80)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.black.opacity(0.3))
                .edgesIgnoringSafeArea(.all)
                .transition(.opacity)
            }
        }
    }
    private func sendMessage() {
        guard !messageText.isEmpty else { return }
        let newMessage = Message(
            content: messageText,
            isFromCurrentUser: true,
            timestamp: Date()
        )
        messages.append(newMessage)
        messageText = ""
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            isTyping = true

            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                isTyping = false
                let reply = Message(
                    content: "Thanks for your message!",
                    isFromCurrentUser: false,
                    timestamp: Date()
                )
                messages.append(reply)
            }
        }
    }
    private func startRecording() {
        isRecording = true
        recordingTime = 0
        recordingTimer = Timer.scheduledTimer(
            withTimeInterval: 1.0,
            repeats: true
        ) { _ in
            recordingTime += 1
            for i in 0..<waveformValues.count {
                waveformValues[i] = CGFloat.random(in: 0.1...1.0)
            }
        }
    }
    private func stopRecording() {
        isRecording = false
        recordingTimer?.invalidate()
        recordingTimer = nil
        let voiceMessage = Message(
            content: "[Voice Message: \(Int(recordingTime))s]",
            isFromCurrentUser: true,
            timestamp: Date()
        )
        messages.append(voiceMessage)
    }
}
struct MessageBubble: View {
    let message: Message
    let themeManager: ThemeManager

    var body: some View {
        HStack {
            if message.isFromCurrentUser {
                Spacer()
            }
            VStack(
                alignment: message.isFromCurrentUser ? .trailing : .leading,
                spacing: 4
            ) {
                Text(message.content)
                    .padding(.horizontal, 12)
                    .padding(.vertical, 8)
                    .background(
                        RoundedRectangle(cornerRadius: 16)
                            .fill(
                                message.isFromCurrentUser
                                    ? (themeManager.isDarkMode
                                        ? LuxorColors.Dark.accent
                                        : LuxorColors.Light.accent)
                                    : (themeManager.isDarkMode
                                        ? LuxorColors.Dark.cardBackground
                                        : Color(UIColor.systemGray6))
                            )
                    )
                    .foregroundColor(
                        message.isFromCurrentUser
                            ? Color.white
                            : (themeManager.isDarkMode
                                ? LuxorColors.Dark.textPrimary
                                : LuxorColors.Light.textPrimary)
                    )

                Text(formatTime(message.timestamp))
                    .font(.system(size: 10))
                    .foregroundColor(
                        themeManager.isDarkMode
                            ? LuxorColors.Dark.textSecondary
                            : LuxorColors.Light.textSecondary
                    )
            }

            if !message.isFromCurrentUser {
                Spacer()
            }
        }
    }
    private func formatTime(_ date: Date) -> String {
        let formatter = DateFormatter()
        formatter.timeStyle = .short
        return formatter.string(from: date)
    }
}
struct TypingIndicator: View {
    let themeManager: ThemeManager
    @State private var firstDotOffset: CGFloat = 0
    @State private var secondDotOffset: CGFloat = 0
    @State private var thirdDotOffset: CGFloat = 0

    var body: some View {
        HStack(spacing: 4) {
            ForEach(0..<3) { index in
                Circle()
                    .fill(
                        themeManager.isDarkMode
                            ? Color.white.opacity(0.7) : Color.gray.opacity(0.7)
                    )
                    .frame(width: 6, height: 6)
                    .offset(
                        y: index == 0
                            ? firstDotOffset
                            : (index == 1 ? secondDotOffset : thirdDotOffset)
                    )
            }
        }
        .padding(.horizontal, 12)
        .padding(.vertical, 8)
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(
                    themeManager.isDarkMode
                        ? LuxorColors.Dark.cardBackground
                        : Color(UIColor.systemGray6)
                )
        )
        .onAppear {
            withAnimation(Animation.easeInOut(duration: 0.5).repeatForever()) {
                firstDotOffset = -5
            }

            withAnimation(
                Animation.easeInOut(duration: 0.5).repeatForever().delay(0.2)
            ) {
                secondDotOffset = -5
            }

            withAnimation(
                Animation.easeInOut(duration: 0.5).repeatForever().delay(0.4)
            ) {
                thirdDotOffset = -5
            }
        }
    }
}

#Preview {
    ConversationView(
        chat: Chat(
            id: 1,
            name: "John Doe",
            lastMessage: "Hey there!",
            time: "10:30 AM",
            unread: true
        ),
        isInDetailView: .constant(false)
    )
    .environmentObject(ThemeManager())
}
