package com.example.luxorappandroid.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.theme.*
import com.google.ai.client.generativeai.Chat
import com.google.firebase.annotations.concurrent.Background

data class TabItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val index: Int
)

@Composable
fun LiquidBottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false
) {
    val colors = if (isDarkMode) DarkColors else LightColors
    
    val tabs = listOf(
        TabItem(Icons.Default.Home, "Home", 0),
        TabItem(Icons.Default.LocationOn, "Places", 1),
        TabItem(Icons.Default.Chat, "Chat", 2),
        TabItem(Icons.Default.Settings, "Settings", 3)
    )
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colors.Background.copy(alpha = 0.1f),
                        colors.Background.copy(alpha = 0.1f)
                    )
                )
            )
            .border(
                width = 0.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        colors.Border.copy(alpha = 0.1f),
                        colors.Border.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(30.dp)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            TabButton(
                tab = tab,
                isSelected = selectedTab == tab.index,
                onClick = { onTabSelected(tab.index) },
                isDarkMode = isDarkMode
            )
        }
    }
}

@Composable
private fun TabButton(
    tab: TabItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = if (isDarkMode) DarkColors else LightColors
    
    val iconColor = if (isSelected) {
        Color.White
    } else {
        colors.TextSecondary
    }
    
    Box(
        modifier = modifier
            .size(50.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Background circle for selected state
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(LuxorTeal, LuxorGold)
                        )
                    )
                    .scale(
                        animateFloatAsState(
                            targetValue = if (isSelected) 1.0f else 0.8f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "scale_animation"
                        ).value
                    )
            )
        }
        
        // Icon
        Icon(
            imageVector = tab.icon,
            contentDescription = tab.title,
            tint = iconColor,
            modifier = Modifier
                .size(if (isSelected) 18.dp else 20.dp)
                .scale(
                    animateFloatAsState(
                        targetValue = if (isSelected) 0.9f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "icon_scale_animation"
                    ).value
                )
        )
        
        // Text overlay for selected state
        if (isSelected) {
            Text(
                text = tab.title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .offset(y = 20.dp)
                    .scale(
                        animateFloatAsState(
                            targetValue = 1.0f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "text_scale_animation"
                        ).value
                    )
            )
        }
    }
}
