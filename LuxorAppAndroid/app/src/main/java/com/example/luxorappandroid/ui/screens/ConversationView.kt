package com.example.luxorappandroid.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.data.Chat
import com.example.luxorappandroid.data.Message
import com.example.luxorappandroid.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ConversationView(
    chat: Chat,
    isDarkMode: Boolean,
    onBackPressed: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var recordingTime by remember { mutableStateOf(0L) }
    var waveformValues by remember { mutableStateOf(List(30) { 0.1f }) }
    
    val scope = rememberCoroutineScope()
    
    val messages = remember {
        mutableStateListOf(
            Message(
                content = "Hey there!",
                isFromCurrentUser = false,
                timestamp = System.currentTimeMillis() - 3600000
            ),
            Message(
                content = "Hi! How are you?",
                isFromCurrentUser = true,
                timestamp = System.currentTimeMillis() - 3500000
            ),
            Message(
                content = "I'm good, thanks for asking. What about you?",
                isFromCurrentUser = false,
                timestamp = System.currentTimeMillis() - 3400000
            ),
            Message(
                content = "I'm doing well. Just wanted to check in.",
                isFromCurrentUser = true,
                timestamp = System.currentTimeMillis() - 3300000
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Chat Header
        ChatHeader(
            chat = chat,
            isDarkMode = isDarkMode,
            onBackPressed = onBackPressed
        )
        
        // Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(
                    color = if (isDarkMode) DarkColors.Background else LightColors.Background
                ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    isDarkMode = isDarkMode
                )
            }
            
            if (isTyping) {
                item {
                    TypingIndicator(isDarkMode = isDarkMode)
                }
            }
        }
        
        // Chat Input Bar
        ChatInputBar(
            messageText = messageText,
            onMessageTextChange = { messageText = it },
            isRecording = isRecording,
            onSendMessage = {
                if (messageText.isNotEmpty()) {
                    sendMessage(messageText, messages, scope) { isTyping = it }
                    messageText = ""
                }
            },
            onStartRecording = {
                isRecording = true
                recordingTime = 0
                startRecording(scope) { recordingTime = it }
                animateWaveform(scope) { waveformValues = it }
            },
            onStopRecording = {
                isRecording = false
                val voiceMessage = Message(
                    content = "[Voice Message: ${recordingTime}s]",
                    isFromCurrentUser = true
                )
                messages.add(voiceMessage)
            },
            isDarkMode = isDarkMode
        )
    }
    
    // Recording Overlay
    if (isRecording) {
        RecordingOverlay(
            recordingTime = recordingTime,
            waveformValues = waveformValues,
            isDarkMode = isDarkMode
        )
    }
}

@Composable
fun ChatHeader(
    chat: Chat,
    isDarkMode: Boolean,
    onBackPressed: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isDarkMode) DarkColors.CardBackground else LightColors.CardBackground
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (isDarkMode) DarkColors.CardBackground else LightColors.CardBackground,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = LuxorTeal
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = LuxorTeal.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = chat.name,
                tint = LuxorTeal,
                modifier = Modifier.size(30.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Chat Info
        Column {
            Text(
                text = chat.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
            )
            
            Text(
                text = "Online",
                fontSize = 12.sp,
                color = LuxorTeal
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Call Buttons
        IconButton(
            onClick = { /* Handle call */ },
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (isDarkMode) DarkColors.CardBackground else LightColors.CardBackground,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call",
                tint = LuxorTeal
            )
        }
        
        IconButton(
            onClick = { /* Handle video call */ },
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (isDarkMode) DarkColors.CardBackground else LightColors.CardBackground,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Videocam,
                contentDescription = "Video Call",
                tint = LuxorTeal
            )
        }
    }
}

@Composable
fun MessageBubble(
    message: Message,
    isDarkMode: Boolean
) {
    val isFromCurrentUser = message.isFromCurrentUser
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isFromCurrentUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            // Message Content
            Box(
                modifier = Modifier
                    .background(
                        color = if (isFromCurrentUser) {
                            LuxorTeal
                        } else {
                            if (isDarkMode) DarkColors.CardBackground else Color.LightGray
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = message.content,
                    color = if (isFromCurrentUser) Color.White else {
                        if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
                    },
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Timestamp
            Text(
                text = formatTime(message.timestamp),
                fontSize = 10.sp,
                color = if (isDarkMode) DarkColors.TextSecondary else LightColors.TextSecondary
            )
        }
    }
}

@Composable
fun TypingIndicator(isDarkMode: Boolean) {
    var firstDotOffset by remember { mutableStateOf(0f) }
    var secondDotOffset by remember { mutableStateOf(0f) }
    var thirdDotOffset by remember { mutableStateOf(0f) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            firstDotOffset = -5f
            delay(200)
            secondDotOffset = -5f
            delay(200)
            thirdDotOffset = -5f
            delay(100)
            firstDotOffset = 0f
            secondDotOffset = 0f
            thirdDotOffset = 0f
        }
    }
    
    Row(
        modifier = Modifier
            .background(
                color = if (isDarkMode) DarkColors.CardBackground else Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        color = if (isDarkMode) Color.White.copy(alpha = 0.7f) else Color.Gray.copy(alpha = 0.7f),
                        shape = CircleShape
                    )
                    .offset(y = when (index) {
                        0 -> firstDotOffset.dp
                        1 -> secondDotOffset.dp
                        else -> thirdDotOffset.dp
                    })
            )
            
            if (index < 2) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
fun ChatInputBar(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    isRecording: Boolean,
    onSendMessage: () -> Unit,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isDarkMode) DarkColors.CardBackground else LightColors.CardBackground
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Plus Button
        IconButton(
            onClick = { /* Handle plus action */ },
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (isDarkMode) DarkColors.CardBackground else LightColors.CardBackground,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = LuxorTeal
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Text Input
        TextField(
            value = messageText,
            onValueChange = onMessageTextChange,
            placeholder = {
                Text(
                    text = "Message",
                    color = if (isDarkMode) DarkColors.TextSecondary else LightColors.TextSecondary
                )
            },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = if (isDarkMode) DarkColors.InputBackground else Color.LightGray,
                unfocusedContainerColor = if (isDarkMode) DarkColors.InputBackground else Color.LightGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = LuxorTeal,
                textColor = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
            ),
            shape = RoundedCornerShape(20.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Send/Record Button
        IconButton(
            onClick = if (messageText.isEmpty()) onStartRecording else onSendMessage,
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = LuxorTeal,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = if (messageText.isEmpty()) Icons.Default.Mic else Icons.Default.Send,
                contentDescription = if (messageText.isEmpty()) "Record" else "Send",
                tint = Color.White
            )
        }
    }
}

@Composable
fun RecordingOverlay(
    recordingTime: Long,
    waveformValues: List<Float>,
    isDarkMode: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Recording Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.Red.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.Red, CircleShape)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Recording... ${recordingTime}s",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = "Slide to cancel",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Waveform
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = LuxorTeal.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                waveformValues.forEach { value ->
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height((20 * value).dp)
                            .background(Color.White, RoundedCornerShape(1.5.dp))
                    )
                    
                    Spacer(modifier = Modifier.width(3.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

private fun sendMessage(
    content: String,
    messages: MutableList<Message>,
    scope: androidx.compose.runtime.Composer,
    onTypingChange: (Boolean) -> Unit
) {
    val newMessage = Message(
        content = content,
        isFromCurrentUser = true
    )
    messages.add(newMessage)
    
    // Simulate typing and reply
    scope.launch {
        delay(2000)
        onTypingChange(true)
        delay(2000)
        onTypingChange(false)
        
        val reply = Message(
            content = "Thanks for your message!",
            isFromCurrentUser = false
        )
        messages.add(reply)
    }
}

private fun startRecording(
    scope: androidx.compose.runtime.Composer,
    onTimeUpdate: (Long) -> Unit
) {
    scope.launch {
        var time = 0L
        while (true) {
            delay(1000)
            time++
            onTimeUpdate(time)
        }
    }
}

private fun animateWaveform(
    scope: androidx.compose.runtime.Composer,
    onWaveformUpdate: (List<Float>) -> Unit
) {
    scope.launch {
        while (true) {
            delay(100)
            val newValues = List(30) { (0.1f..1.0f).random() }
            onWaveformUpdate(newValues)
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(date)
}
