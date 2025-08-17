package com.example.luxorappandroid.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.data.Chat
import com.example.luxorappandroid.ui.components.LiquidBottomNavBar
import com.example.luxorappandroid.ui.theme.*

@Composable
fun MainTabView(
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    var isNavBarVisible by remember { mutableStateOf(true) }
    var isAppBarVisible by remember { mutableStateOf(true) }
    var lastScrollOffset by remember { mutableStateOf(0f) }
    var isScrolling by remember { mutableStateOf(false) }
    var selectedChat by remember { mutableStateOf<Chat?>(null) }
    var showConversation by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (showConversation && selectedChat != null) {
            // Show Conversation View
            ConversationView(
                chat = selectedChat!!,
                isDarkMode = isDarkMode,
                onBackPressed = {
                    showConversation = false
                    selectedChat = null
                }
            )
        } else {
            // Main Tab Content
            when (selectedTab) {
                0 -> HomeContentView(
                    isNavBarVisible = remember { mutableStateOf(isNavBarVisible) },
                    isAppBarVisible = remember { mutableStateOf(isAppBarVisible) },
                    lastScrollOffset = remember { mutableStateOf(lastScrollOffset) },
                    isScrolling = remember { mutableStateOf(isScrolling) },
                    isDarkMode = isDarkMode
                )
                1 -> PlacesView(
                    isNavBarVisible = remember { mutableStateOf(isNavBarVisible) },
                    isAppBarVisible = remember { mutableStateOf(isAppBarVisible) },
                    lastScrollOffset = remember { mutableStateOf(lastScrollOffset) },
                    isScrolling = remember { mutableStateOf(isScrolling) },
                    isDarkMode = isDarkMode
                )
                2 -> ChatView(
                    isNavBarVisible = remember { mutableStateOf(isNavBarVisible) },
                    isAppBarVisible = remember { mutableStateOf(isAppBarVisible) },
                    lastScrollOffset = remember { mutableStateOf(lastScrollOffset) },
                    isScrolling = remember { mutableStateOf(isScrolling) },
                    isDarkMode = isDarkMode,
                    onChatSelected = { chat ->
                        selectedChat = chat
                        showConversation = true
                    }
                )
                3 -> SettingsView(
                    isNavBarVisible = remember { mutableStateOf(isNavBarVisible) },
                    isAppBarVisible = remember { mutableStateOf(isAppBarVisible) },
                    lastScrollOffset = remember { mutableStateOf(lastScrollOffset) },
                    isScrolling = remember { mutableStateOf(isScrolling) },
                    isDarkMode = isDarkMode,
                    onThemeToggle = onThemeToggle
                )
            }

            // App Bar
            if (isAppBarVisible) {
                AppBarView(
                    selectedTab = selectedTab,
                    isDarkMode = isDarkMode
                )
            }

            // Bottom Navigation Bar
            if (isNavBarVisible) {
                LiquidBottomNavBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    isDarkMode = isDarkMode
                )
            }
        }
    }
}

@Composable
fun AppBarView(
    selectedTab: Int,
    isDarkMode: Boolean
) {
    val tabTitles = listOf("Home", "Places", "Chat", "Settings")
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isDarkMode) DarkColors.CardBackground else LightColors.CardBackground
            )
            .shadow(
                elevation = 8.dp,
                spotColor = if (isDarkMode) DarkColors.Shadow else LightColors.Shadow
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo and Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(LuxorTeal, LuxorGold)
                            ),
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Luxor Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = tabTitles[selectedTab],
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
                )
            }
            
            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = { /* Handle search */ },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = if (isDarkMode) DarkColors.CardBackground else Color.White,
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .shadow(
                            elevation = 4.dp,
                            shape = androidx.compose.foundation.shape.CircleShape,
                            spotColor = if (isDarkMode) DarkColors.Shadow else LightColors.Shadow
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
                    )
                }
                
                IconButton(
                    onClick = { /* Handle notifications */ },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = if (isDarkMode) DarkColors.CardBackground else Color.White,
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .shadow(
                            elevation = 4.dp,
                            shape = androidx.compose.foundation.shape.CircleShape,
                            spotColor = if (isDarkMode) DarkColors.Shadow else LightColors.Shadow
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun HomeContentView(
    isNavBarVisible: Boolean,
    isAppBarVisible: Boolean,
    lastScrollOffset: Float,
    isScrolling: Boolean,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false
) {
    val colors = if (isDarkMode) DarkColors else LightColors
    
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.Background),
        contentPadding = PaddingValues(bottom = 100.dp) // Space for bottom nav
    ) {
        items(SampleData.samplePosts) { post ->
            LuxorPost(
                post = post,
                isDarkMode = isDarkMode
            )
        }
    }
}

@Composable
fun PlacesView(
    isNavBarVisible: Boolean,
    isAppBarVisible: Boolean,
    lastScrollOffset: Float,
    isScrolling: Boolean,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false
) {
    val colors = if (isDarkMode) DarkColors else LightColors
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Places",
                tint = LuxorTeal,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Places",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextPrimary
            )
            Text(
                text = "Coming soon...",
                fontSize = 16.sp,
                color = colors.TextSecondary
            )
        }
    }
}

@Composable
fun ChatView(
    isNavBarVisible: Boolean,
    isAppBarVisible: Boolean,
    lastScrollOffset: Float,
    isScrolling: Boolean,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false,
    onChatSelected: (Chat) -> Unit = {}
) {
    val colors = if (isDarkMode) DarkColors else LightColors
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Chat",
                tint = LuxorTeal,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Chat",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextPrimary
            )
            Text(
                text = "Coming soon...",
                fontSize = 16.sp,
                color = colors.TextSecondary
            )
        }
    }
}

@Composable
fun SettingsView(
    isNavBarVisible: Boolean,
    isAppBarVisible: Boolean,
    lastScrollOffset: Float,
    isScrolling: Boolean,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false,
    onThemeToggle: () -> Unit = {}
) {
    val colors = if (isDarkMode) DarkColors else LightColors
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = LuxorTeal,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextPrimary
            )
            Text(
                text = "Coming soon...",
                fontSize = 16.sp,
                color = colors.TextSecondary
            )
        }
    }
}
