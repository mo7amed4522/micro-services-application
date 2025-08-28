package com.example.luxorappandroid.ui.components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.theme.LuxorColors

data class TabItem(
    val icon: ImageVector,
    val title: String,
    val index: Int
)
@Composable
fun LiquidBottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    val tabs = listOf(
        TabItem(Icons.Default.Home, "Home", 0),
        TabItem(Icons.Default.LocationOn, "Places", 1),
        TabItem(Icons.AutoMirrored.Filled.Chat, "Chat", 2),
        TabItem(Icons.Default.Settings, "Settings", 3)
    )
    
    val visibilityAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(300, easing = EaseInOut),
        label = "visibility_alpha"
    )
    val visibilityScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "visibility_scale"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .alpha(visibilityAlpha)
            .scale(visibilityScale)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.1f),
                            Color.White.copy(alpha = 0.05f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(1000f, 1000f)
                    )
                )
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(30.dp),
                    ambientColor = LuxorColors.deepGold.copy(alpha = 0.1f),
                    spotColor = LuxorColors.pureGold.copy(alpha = 0.2f)
                )
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(30.dp))
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width / 4
            drawRoundRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        LuxorColors.pureGold.copy(alpha = 0.05f),
                        LuxorColors.richGold.copy(alpha = 0.02f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radius
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(30.dp.toPx())
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                TabButton(
                    tab = tab,
                    isSelected = selectedTab == tab.index,
                    onTabClick = { onTabSelected(tab.index) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
@Composable
fun TabButton(
    tab: TabItem,
    isSelected: Boolean,
    onTabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showText by remember { mutableStateOf(false) }
    
    val backgroundScale by animateFloatAsState(
        targetValue = if (isSelected) 1.0f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "background_scale"
    )
    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 0.9f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "icon_scale"
    )
    val iconAlpha by animateFloatAsState(
        targetValue = if (isSelected && showText) 0.3f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "icon_alpha"
    )
    val textAlpha by animateFloatAsState(
        targetValue = if (showText) 1.0f else 0.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "text_alpha"
    )
    
    val textScale by animateFloatAsState(
        targetValue = if (showText) 1.0f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "text_scale"
    )
    LaunchedEffect(isSelected) {
        if (isSelected) {
            kotlinx.coroutines.delay(150)
            showText = true
        } else {
            showText = false
        }
    }
    
    Box(
        modifier = modifier
            .size(50.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onTabClick()
            },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .scale(backgroundScale)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                LuxorColors.pureGold,
                                LuxorColors.richGold,
                                LuxorColors.deepGold
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(100f, 100f)
                        )
                    )
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = LuxorColors.pureGold.copy(alpha = 0.3f),
                        spotColor = LuxorColors.richGold.copy(alpha = 0.5f)
                    )
            )
        }
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = tab.title,
                tint = if (isSelected) Color.White else LuxorColors.Light.textSecondary,
                modifier = Modifier
                    .size(if (isSelected) 18.dp else 20.dp)
                    .scale(iconScale)
                    .alpha(iconAlpha)
            )
            if (isSelected) {
                Text(
                    text = tab.title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .alpha(textAlpha)
                        .scale(textScale)
                )
            }
        }
    }
}