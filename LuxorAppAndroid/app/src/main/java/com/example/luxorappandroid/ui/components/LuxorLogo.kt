package com.example.luxorappandroid.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.luxorappandroid.R
import com.example.luxorappandroid.ui.theme.*

@Composable
fun LuxorLogo(
    size: Float = 120f,
    modifier: Modifier = Modifier,
    themeManager: ThemeManager
) {
    val colors = if (themeManager.isDarkMode) LuxorColors.Dark else LuxorColors.Light
    val infiniteTransition = rememberInfiniteTransition(label = "logo_animation")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_animation"
    )
    
    val opacity by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "opacity_animation"
    )
    
    Box(
        modifier = modifier
            .size((size * 1.4).dp)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow effect
        Box(
            modifier = Modifier
                .size((size * 1.4).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            LuxorColors.luxorTeal.copy(alpha = if (themeManager.isDarkMode) 0.4f else 0.3f),
                            Color.Transparent
                        ),
                        center = androidx.compose.ui.geometry.Offset.Zero,
                        radius = size * 0.8f
                    ),
                    shape = CircleShape
                )
        )
        
        // Main logo circle
        Box(
            modifier = Modifier
                .size(size.dp)
                .background(
                    brush = Brush.angularGradient(
                        colors = if (themeManager.isDarkMode) {
                            listOf(
                                LuxorColors.luxorTeal,
                                LuxorColors.richGold,
                                LuxorColors.egyptianBlue,
                                LuxorColors.deepGold,
                                LuxorColors.luxorTeal
                            )
                        } else {
                            listOf(
                                LuxorColors.luxorTeal,
                                LuxorColors.luxorGold,
                                LuxorColors.luxorTeal.copy(alpha = 0.8f),
                                LuxorColors.luxorGold.copy(alpha = 0.9f),
                                LuxorColors.luxorTeal
                            )
                        }
                    ),
                    shape = CircleShape
                )
                .scale(scale)
        ) {
            // Inner circle
            Box(
                modifier = Modifier
                    .size((size - 12).dp)
                    .align(Alignment.Center)
                    .background(
                        brush = Brush.linearGradient(
                            colors = if (themeManager.isDarkMode) {
                                listOf(
                                    colors.cardBackground,
                                    colors.cardBackground.copy(alpha = 0.95f),
                                    colors.inputBackground.copy(alpha = 0.3f)
                                )
                            } else {
                                listOf(
                                    Color.White,
                                    Color.White.copy(alpha = 0.95f),
                                    colors.secondaryBackground.copy(alpha = 0.3f)
                                )
                            }
                        ),
                        shape = CircleShape
                    )
            ) {
                // Logo image placeholder
                Box(
                    modifier = Modifier
                        .size((size - 32).dp)
                        .align(Alignment.Center)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    LuxorColors.luxorTeal.copy(alpha = if (themeManager.isDarkMode) 0.2f else 0.1f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                ) {
                    // Placeholder for logo image
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Luxor Logo",
                        modifier = Modifier
                            .size((size - 40).dp)
                            .align(Alignment.Center),
                        tint = if (themeManager.isDarkMode) LuxorColors.pureGold else LuxorColors.luxorTeal
                    )
                }
            }
        }
        
        // Highlight effect
        Box(
            modifier = Modifier
                .size((size * 0.4).dp)
                .offset(x = (-size * 0.15).dp, y = (-size * 0.15).dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            (if (themeManager.isDarkMode) LuxorColors.pureGold else Color.White).copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
}
