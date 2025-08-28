package com.example.luxorappandroid.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.components.LiquidBottomNavBar
import com.example.luxorappandroid.ui.settings.SettingsScreen
import com.example.luxorappandroid.ui.theme.LuxorColors

data class TabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val content: @Composable () -> Unit
)

@Composable
fun MainScreen(
    onLogout: () -> Unit = {}
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    val tabs = listOf(
        TabItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            content = { HomeContentView() }
        ),
        TabItem(
            title = "Places",
            selectedIcon = Icons.Filled.Place,
            unselectedIcon = Icons.Outlined.Place,
            content = { PlacesView() }
        ),
        TabItem(
            title = "Chat",
            selectedIcon = Icons.AutoMirrored.Filled.Chat,
            unselectedIcon = Icons.AutoMirrored.Outlined.Chat,
            content = { ChatView() }
        ),
        TabItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            content = { SettingsView(onLogout = onLogout) }
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LuxorColors.Light.background,
                        LuxorColors.papyrusBeige
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Content area
            Box(
                modifier = Modifier.weight(1f)
            ) {
                tabs[selectedTabIndex].content()
            }
            
            // Custom Bottom Navigation
            LiquidBottomNavBar(
                selectedTab = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}



@Composable
fun HomeContentView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = LuxorColors.Light.cardBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to Luxor",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = LuxorColors.Light.textPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Discover the ancient wonders of Egypt",
                    fontSize = 16.sp,
                    color = LuxorColors.Light.textSecondary
                )
            }
        }
    }
}

@Composable
fun PlacesView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = LuxorColors.Light.cardBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Places",
                    modifier = Modifier.size(48.dp),
                    tint = LuxorColors.pureGold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Explore Places",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = LuxorColors.Light.textPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Discover amazing locations in Luxor",
                    fontSize = 16.sp,
                    color = LuxorColors.Light.textSecondary
                )
            }
        }
    }
}

@Composable
fun ChatView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = LuxorColors.Light.cardBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = "Chat",
                    modifier = Modifier.size(48.dp),
                    tint = LuxorColors.pureGold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Chat with Guides",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = LuxorColors.Light.textPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Connect with local tour guides",
                    fontSize = 16.sp,
                    color = LuxorColors.Light.textSecondary
                )
            }
        }
    }
}

@Composable
fun SettingsView(
    onLogout: () -> Unit = {}
) {
    SettingsScreen(
        onLogout = onLogout
    )
}