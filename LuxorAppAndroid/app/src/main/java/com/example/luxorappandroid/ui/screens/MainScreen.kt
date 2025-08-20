package com.example.luxorappandroid.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.example.luxorappandroid.ui.screens.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.luxorappandroid.ui.theme.LuxorColors

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home",
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    
    object Places : BottomNavItem(
        route = "places",
        title = "Places",
        selectedIcon = Icons.Filled.Place,
        unselectedIcon = Icons.Outlined.Place
    )
    
    object Chat : BottomNavItem(
        route = "chat",
        title = "Chat",
        selectedIcon = Icons.Filled.Chat,
        unselectedIcon = Icons.Outlined.Chat
    )
    
    object Settings : BottomNavItem(
        route = "settings",
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val isDarkTheme = isSystemInDarkTheme()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Places,
        BottomNavItem.Chat,
        BottomNavItem.Settings
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    Scaffold(
        modifier = modifier,
        bottomBar = {
            LuxorBottomNavigation(
                items = items,
                currentRoute = currentRoute,
                onItemClick = { item ->
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        containerColor = if (isDarkTheme) LuxorColors.Dark.background else LuxorColors.Light.background
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen()
            }
            composable(BottomNavItem.Places.route) {
                PlacesScreen()
            }
            composable(BottomNavItem.Chat.route) {
                ChatScreen()
            }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen()
            }
        }
    }
}

@Composable
fun LuxorBottomNavigation(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    
    // Background with Egyptian-inspired styling
    val backgroundBrush = Brush.verticalGradient(
        colors = if (isDarkTheme) {
            listOf(
                LuxorColors.pureGold,
                LuxorColors.egyptianBlue
            )
        } else {
            listOf(
                LuxorColors.pureGold,
                LuxorColors.egyptianBlue
            )
        }
    )
    
    NavigationBar(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(backgroundBrush),
        containerColor = Color.Transparent,
        contentColor = if (isDarkTheme) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            
            NavigationBarItem(
                icon = {
                    LuxorNavIcon(
                        icon = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        isSelected = isSelected,
                        label = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) {
                            LuxorColors.pureGold
                        } else {
                            if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
                        }
                    )
                },
                selected = isSelected,
                onClick = { onItemClick(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LuxorColors.pureGold,
                    unselectedIconColor = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                    selectedTextColor = LuxorColors.pureGold,
                    unselectedTextColor = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                    indicatorColor = LuxorColors.pureGold.copy(alpha = 0.1f)
                )
            )
        }
    }
}

@Composable
fun LuxorNavIcon(
    icon: ImageVector,
    isSelected: Boolean,
    label: String,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        ),
        label = "icon_scale"
    )
    
    val infiniteTransition = rememberInfiniteTransition(label = "icon_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Glow effect for selected icon
        if (isSelected) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier
                    .scale(scale * 1.1f),
                tint = LuxorColors.pureGold.copy(alpha = glowAlpha * 0.3f)
            )
        }
        
        // Main icon
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.scale(scale),
            tint = if (isSelected) {
                LuxorColors.pureGold
            } else {
                if (isSystemInDarkTheme()) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
            }
        )
    }
}