package com.example.luxorappandroid.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.components.LuxorLogoView
import com.example.luxorappandroid.ui.theme.LuxorColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1.0f else 0.3f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = EaseOutBounce
        ),
        label = "logo_scale"
    )
    
    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1.0f else 0.0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = EaseOut
        ),
        label = "logo_alpha"
    )
    
    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1.0f else 0.0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 500,
            easing = EaseOut
        ),
        label = "text_alpha"
    )
    
    val backgroundAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1.0f else 0.8f,
        animationSpec = tween(
            durationMillis = 1500,
            easing = EaseInOut
        ),
        label = "background_alpha"
    )
    
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3000)
        onSplashFinished()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        LuxorColors.papyrusBeige.copy(alpha = backgroundAlpha),
                        LuxorColors.Light.background.copy(alpha = backgroundAlpha),
                        LuxorColors.pureGold.copy(alpha = backgroundAlpha * 0.1f)
                    ),
                    radius = 1000f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LuxorLogoView(
                size = 160.dp,
                animated = true,
                modifier = Modifier
                    .scale(logoScale)
                    .alpha(logoAlpha)
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Luxor",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = LuxorColors.deepGold,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Discover Ancient Wonders",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = LuxorColors.Light.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(3) { index ->
                        val dotAlpha by animateFloatAsState(
                            targetValue = if (startAnimation) 1.0f else 0.3f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(
                                    durationMillis = 600,
                                    delayMillis = index * 200
                                ),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "dot_alpha_$index"
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = LuxorColors.richGold.copy(alpha = dotAlpha),
                                    shape = androidx.compose.foundation.shape.CircleShape
                                )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Loading...",
                    fontSize = 14.sp,
                    color = LuxorColors.Light.textSecondary.copy(alpha = textAlpha),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}