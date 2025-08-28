package com.example.luxorappandroid.ui.components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.luxorappandroid.R
import com.example.luxorappandroid.ui.theme.LuxorColors

@Composable
fun LuxorLogoView(
    size: Dp = 120.dp,
    modifier: Modifier = Modifier,
    animated: Boolean = true
) {
    var isAnimating by remember { mutableStateOf(false) }
    
    val animatedScale by animateFloatAsState(
        targetValue = if (isAnimating) 1.1f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale"
    )
    val rotationAngle by animateFloatAsState(
        targetValue = if (isAnimating) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "logo_rotation"
    )
    LaunchedEffect(animated) {
        if (animated) {
            isAnimating = true
        }
    }
    
    Box(
        modifier = modifier.size(size * 1.4f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(size * 1.4f)
                .scale(animatedScale)
        ) {
            val center = Offset(this.size.width / 2, this.size.height / 2)
            val radius = this.size.width / 2
            
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        LuxorColors.pureGold.copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radius
                ),
                center = center,
                radius = radius
            )
        }
        Box(
            modifier = Modifier
                .size(size)
                .shadow(
                    elevation = 20.dp,
                    shape = CircleShape,
                    ambientColor = LuxorColors.pureGold.copy(alpha = 0.4f),
                    spotColor = LuxorColors.richGold.copy(alpha = 0.6f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(size)
                    .graphicsLayer {
                        rotationZ = rotationAngle
                    }
            ) {
                val center = Offset(this.size.width / 2, this.size.height / 2)
                val radius = this.size.width / 2
                
                drawCircle(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            LuxorColors.pureGold,
                            LuxorColors.richGold,
                            LuxorColors.deepGold,
                            LuxorColors.pureGold
                        ),
                        center = center
                    ),
                    center = center,
                    radius = radius
                )
            }
            Canvas(
                modifier = Modifier.size(size - 12.dp)
            ) {
                val center = Offset(this.size.width / 2, this.size.height / 2)
                val radius = this.size.width / 2
                
                drawCircle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White,
                            Color.White.copy(alpha = 0.95f),
                            LuxorColors.papyrusBeige.copy(alpha = 0.3f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(this.size.width, this.size.height)
                    ),
                    center = center,
                    radius = radius
                )
            }
            Image(
                painter = painterResource(id = R.drawable.luxor_logo),
                contentDescription = "Luxor Logo",
                modifier = Modifier
                    .size(size - 32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Canvas(
                modifier = Modifier.size(size - 20.dp)
            ) {
                val center = Offset(this.size.width / 2, this.size.height / 2)
                val radius = this.size.width / 2
                
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            LuxorColors.pureGold.copy(alpha = 0.1f),
                            Color.Transparent
                        ),
                        center = center,
                        radius = radius * 0.3f
                    ),
                    center = center,
                    radius = radius
                )
            }
        }
    }
}