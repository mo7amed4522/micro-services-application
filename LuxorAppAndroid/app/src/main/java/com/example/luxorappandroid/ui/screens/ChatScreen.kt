package com.example.luxorappandroid.ui.screens

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.components.LuxorTextField
import com.example.luxorappandroid.ui.theme.LuxorColors
import java.text.SimpleDateFormat
import java.util.*

data class ChatConversation(
    val id: String,
    val name: String,
    val type: ChatType,
    val lastMessage: String,
    val lastMessageTime: Date,
    val unreadCount: Int,
    val isOnline: Boolean,
    val avatar: String? = null
)

data class Message(
    val id: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val timestamp: Date,
    val type: MessageType = MessageType.TEXT,
    val isFromCurrentUser: Boolean
)

enum class ChatType(val displayName: String, val icon: ImageVector) {
    GUIDE("Tour Guide", Icons.Default.Person),
    GROUP("Travel Group", Icons.Default.Group),
    SUPPORT("Customer Support", Icons.Default.Support),
    COMMUNITY("Community", Icons.Default.Forum)
}

enum class MessageType {
    TEXT, IMAGE, LOCATION, AUDIO
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    
    // Sample data
    val conversations = listOf(
        ChatConversation(
            id = "1",
            name = "Ahmed - Tour Guide",
            type = ChatType.GUIDE,
            lastMessage = "The Valley of Kings tour starts at 8 AM tomorrow. Don't forget to bring water!",
            lastMessageTime = Date(System.currentTimeMillis() - 300000), // 5 minutes ago
            unreadCount = 2,
            isOnline = true
        ),
        ChatConversation(
            id = "2",
            name = "Luxor Explorers Group",
            type = ChatType.GROUP,
            lastMessage = "Sarah: Has anyone been to the Colossi of Memnon yet?",
            lastMessageTime = Date(System.currentTimeMillis() - 1800000), // 30 minutes ago
            unreadCount = 5,
            isOnline = false
        ),
        ChatConversation(
            id = "3",
            name = "Customer Support",
            type = ChatType.SUPPORT,
            lastMessage = "Thank you for contacting us. How can we help you today?",
            lastMessageTime = Date(System.currentTimeMillis() - 3600000), // 1 hour ago
            unreadCount = 0,
            isOnline = true
        ),
        ChatConversation(
            id = "4",
            name = "Fatima - Local Guide",
            type = ChatType.GUIDE,
            lastMessage = "The sunset view from the West Bank is absolutely stunning today!",
            lastMessageTime = Date(System.currentTimeMillis() - 7200000), // 2 hours ago
            unreadCount = 1,
            isOnline = false
        ),
        ChatConversation(
            id = "5",
            name = "Photography Enthusiasts",
            type = ChatType.COMMUNITY,
            lastMessage = "Mike: Check out these amazing shots from Karnak Temple!",
            lastMessageTime = Date(System.currentTimeMillis() - 14400000), // 4 hours ago
            unreadCount = 3,
            isOnline = false
        )
    )
    
    val filteredConversations = conversations.filter { conversation ->
        conversation.name.contains(searchQuery, ignoreCase = true) ||
        conversation.lastMessage.contains(searchQuery, ignoreCase = true)
    }
    
    val tabs = listOf("All", "Guides", "Groups", "Support")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LuxorColors.current.background,
                        LuxorColors.current.surface.copy(alpha = 0.3f),
                        LuxorColors.current.background
                    )
                )
            )
    ) {
        // Header
        Column(
            modifier = Modifier.padding(
                horizontal = if (isTablet) 32.dp else 16.dp,
                vertical = 16.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Messages",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    LuxorColors.current.primary,
                                    LuxorColors.current.secondary
                                )
                            )
                        )
                    )
                    
                    Text(
                        text = "Connect with guides and fellow travelers",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = LuxorColors.current.onSurface.copy(alpha = 0.8f)
                        )
                    )
                }
                
                IconButton(
                    onClick = { /* Start new conversation */ },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = LuxorColors.current.primary,
                        contentColor = LuxorColors.current.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New conversation"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Bar
            LuxorTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "Search conversations...",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = LuxorColors.current.surface,
                contentColor = LuxorColors.current.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = LuxorColors.current.primary
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        selectedContentColor = LuxorColors.current.primary,
                        unselectedContentColor = LuxorColors.current.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        // Quick Actions
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = if (isTablet) 32.dp else 16.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = LuxorColors.current.primary.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        QuickActionButton(
                            icon = Icons.Default.PersonAdd,
                            label = "Find Guide",
                            onClick = { /* Find guide */ }
                        )
                        
                        QuickActionButton(
                            icon = Icons.Default.GroupAdd,
                            label = "Join Group",
                            onClick = { /* Join group */ }
                        )
                        
                        QuickActionButton(
                            icon = Icons.Default.Help,
                            label = "Get Help",
                            onClick = { /* Get help */ }
                        )
                        
                        QuickActionButton(
                            icon = Icons.Default.Translate,
                            label = "Translate",
                            onClick = { /* Translation service */ }
                        )
                    }
                }
            }
            
            // Conversations List
            if (filteredConversations.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = LuxorColors.current.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = LuxorColors.current.onSurface.copy(alpha = 0.5f)
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "No conversations found",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = LuxorColors.current.onSurface.copy(alpha = 0.7f)
                                ),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Start a conversation with a guide or join a travel group",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = LuxorColors.current.onSurface.copy(alpha = 0.5f)
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(filteredConversations) { conversation ->
                    ConversationCard(
                        conversation = conversation,
                        onClick = { /* Navigate to chat detail */ },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = LuxorColors.current.primary.copy(alpha = 0.2f),
                contentColor = LuxorColors.current.primary
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = LuxorColors.current.primary,
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ConversationCard(
    conversation: ChatConversation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val dateFormatter = remember { SimpleDateFormat("MMM dd", Locale.getDefault()) }
    
    val timeText = remember(conversation.lastMessageTime) {
        val now = Date()
        val diffInHours = (now.time - conversation.lastMessageTime.time) / (1000 * 60 * 60)
        
        when {
            diffInHours < 1 -> "${((now.time - conversation.lastMessageTime.time) / (1000 * 60)).toInt()}m ago"
            diffInHours < 24 -> timeFormatter.format(conversation.lastMessageTime)
            else -> dateFormatter.format(conversation.lastMessageTime)
        }
    }
    
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (conversation.unreadCount > 0) 
                LuxorColors.current.primary.copy(alpha = 0.05f) else 
                LuxorColors.current.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box {
                Card(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = when (conversation.type) {
                            ChatType.GUIDE -> LuxorColors.current.primary.copy(alpha = 0.2f)
                            ChatType.GROUP -> LuxorColors.current.secondary.copy(alpha = 0.2f)
                            ChatType.SUPPORT -> LuxorColors.current.tertiary.copy(alpha = 0.2f)
                            ChatType.COMMUNITY -> LuxorColors.current.outline.copy(alpha = 0.2f)
                        }
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = conversation.type.icon,
                            contentDescription = null,
                            tint = when (conversation.type) {
                                ChatType.GUIDE -> LuxorColors.current.primary
                                ChatType.GROUP -> LuxorColors.current.secondary
                                ChatType.SUPPORT -> LuxorColors.current.tertiary
                                ChatType.COMMUNITY -> LuxorColors.current.outline
                            },
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                // Online indicator
                if (conversation.isOnline) {
                    Card(
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.BottomEnd),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = LuxorColors.current.tertiary
                        )
                    ) {}
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
                            color = LuxorColors.current.onSurface
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Text(
                        text = timeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (conversation.unreadCount > 0) 
                                LuxorColors.current.primary else 
                                LuxorColors.current.onSurface.copy(alpha = 0.6f),
                            fontWeight = if (conversation.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.lastMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = LuxorColors.current.onSurface.copy(
                                alpha = if (conversation.unreadCount > 0) 0.9f else 0.7f
                            ),
                            fontWeight = if (conversation.unreadCount > 0) FontWeight.Medium else FontWeight.Normal
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (conversation.unreadCount > 0) {
                        Card(
                            modifier = Modifier.size(20.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = LuxorColors.current.primary
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (conversation.unreadCount > 9) "9+" else conversation.unreadCount.toString(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = LuxorColors.current.onPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Type indicator
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (conversation.type) {
                            ChatType.GUIDE -> LuxorColors.current.primary.copy(alpha = 0.1f)
                            ChatType.GROUP -> LuxorColors.current.secondary.copy(alpha = 0.1f)
                            ChatType.SUPPORT -> LuxorColors.current.tertiary.copy(alpha = 0.1f)
                            ChatType.COMMUNITY -> LuxorColors.current.outline.copy(alpha = 0.1f)
                        }
                    )
                ) {
                    Text(
                        text = conversation.type.displayName,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = when (conversation.type) {
                                ChatType.GUIDE -> LuxorColors.current.primary
                                ChatType.GROUP -> LuxorColors.current.secondary
                                ChatType.SUPPORT -> LuxorColors.current.tertiary
                                ChatType.COMMUNITY -> LuxorColors.current.outline
                            },
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}