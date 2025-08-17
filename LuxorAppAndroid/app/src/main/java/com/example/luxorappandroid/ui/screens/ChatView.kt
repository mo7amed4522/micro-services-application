package com.example.luxorappandroid.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.data.Chat
import com.example.luxorappandroid.data.SampleData
import com.example.luxorappandroid.ui.theme.*

@Composable
fun ChatView(
    isNavBarVisible: MutableState<Boolean>,
    isAppBarVisible: MutableState<Boolean>,
    lastScrollOffset: MutableState<Float>,
    isScrolling: MutableState<Boolean>,
    isDarkMode: Boolean,
    onChatSelected: (Chat) -> Unit
) {
    val statusItems = SampleData.statusItems
    val chats = SampleData.sampleChats

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            // Status Section
            StatusSection(
                statusItems = statusItems,
                isDarkMode = isDarkMode
            )
        }
        
        item {
            // Chats Section
            ChatsSection(
                chats = chats,
                isDarkMode = isDarkMode,
                onChatSelected = onChatSelected
            )
        }
    }
}

@Composable
fun StatusSection(
    statusItems: List<String>,
    isDarkMode: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Text(
            text = "STATUS",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = LuxorTeal,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(statusItems) { status ->
                StatusCard(
                    status = status,
                    isDarkMode = isDarkMode
                )
            }
        }
    }
}

@Composable
fun StatusCard(
    status: String,
    isDarkMode: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(70.dp)
            .clickable { /* Handle status tap */ }
    ) {
        Box(
            modifier = Modifier
                .size(70.dp, 100.dp)
                .background(
                    color = LuxorTeal.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(LuxorTeal, LuxorTeal)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = status,
                tint = LuxorTeal,
                modifier = Modifier.size(30.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = status,
            fontSize = 12.sp,
            color = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
        )
    }
}

@Composable
fun ChatsSection(
    chats: List<Chat>,
    isDarkMode: Boolean,
    onChatSelected: (Chat) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Text(
            text = "CHATS",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = LuxorTeal,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn {
            items(chats) { chat ->
                ChatItem(
                    chat = chat,
                    isDarkMode = isDarkMode,
                    onClick = { onChatSelected(chat) }
                )
                
                if (chat.id != chats.lastOrNull()?.id) {
                    Divider(
                        color = if (isDarkMode) DarkColors.Border else Color.Gray.copy(alpha = 0.2f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatItem(
    chat: Chat,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = LuxorTeal.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = chat.name,
                tint = LuxorTeal,
                modifier = Modifier.size(25.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Chat Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chat.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
            )
            
            Text(
                text = chat.lastMessage,
                fontSize = 14.sp,
                fontWeight = if (chat.unread) FontWeight.Bold else FontWeight.Normal,
                color = if (chat.unread) {
                    LuxorTeal
                } else {
                    if (isDarkMode) DarkColors.TextSecondary else LightColors.TextSecondary
                }
            )
        }
        
        // Time and Unread Indicator
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = chat.time,
                fontSize = 12.sp,
                color = if (isDarkMode) DarkColors.TextSecondary else LightColors.TextSecondary
            )
            
            if (chat.unread) {
                Spacer(modifier = Modifier.height(4.dp))
                
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            color = LuxorTeal,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    isDarkMode: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.padding(end = 20.dp, bottom = 70.dp),
            containerColor = LuxorTeal,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
