package com.example.luxorappandroid.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.theme.LuxorColors
import kotlin.math.*
import kotlin.random.Random

@Composable
fun LuxorLogo(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    animated: Boolean = true,
    showParticles: Boolean = true
) {
    val isDarkTheme = isSystemInDarkTheme()
    
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "logo_animation")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Particle background
        if (showParticles && animated) {
            LuxorParticles(
                modifier = Modifier.fillMaxSize(),
                particleCount = 20
            )
        }
        
        // Main logo
        Canvas(
            modifier = Modifier
                .size(size * 0.8f)
                .then(if (animated) Modifier.scale(scale) else Modifier)
        ) {
            val center = Offset(this.size.width / 2, this.size.height / 2)
            val radius = minOf(this.size.width, this.size.height) / 2
            
            // Draw outer golden ring
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        LuxorColors.pureGold.copy(alpha = if (animated) glowIntensity else 1f),
                        LuxorColors.egyptianBlue.copy(alpha = if (animated) glowIntensity * 0.8f else 0.8f),
                        LuxorColors.pureGold.copy(alpha = if (animated) glowIntensity * 0.6f else 0.6f)
                    ),
                    radius = radius
                ),
                radius = radius,
                center = center
            )
            
            // Draw inner circle
            drawCircle(
                color = if (isDarkTheme) LuxorColors.Dark.background else LuxorColors.Light.background,
                radius = radius * 0.7f,
                center = center
            )
            
            // Draw Egyptian-style patterns
            if (animated) {
                rotate(rotation, center) {
                    drawEgyptianPattern(center, radius * 0.6f)
                }
            } else {
                drawEgyptianPattern(center, radius * 0.6f)
            }
        }
        
        // Logo text
        Text(
            text = "L",
            fontSize = (size.value * 0.3f).sp,
            fontWeight = FontWeight.Bold,
            color = LuxorColors.pureGold,
            textAlign = TextAlign.Center,
            modifier = Modifier.then(if (animated) Modifier.rotate(rotation * 0.1f) else Modifier)
        )
    }
}

@Composable
fun LuxorLogoWithText(
    modifier: Modifier = Modifier,
    logoSize: Dp = 80.dp,
    animated: Boolean = true,
    showParticles: Boolean = false
) {
    val isDarkTheme = isSystemInDarkTheme()
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LuxorLogo(
            size = logoSize,
            animated = animated,
            showParticles = showParticles
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "LUXOR",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = LuxorColors.pureGold,
            letterSpacing = 4.sp,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Discover Ancient Wonders",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun LuxorParticles(
    modifier: Modifier = Modifier,
    particleCount: Int = 15
) {
    val particles = remember {
        List(particleCount) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 4f + 2f,
                speed = Random.nextFloat() * 0.02f + 0.01f,
                alpha = Random.nextFloat() * 0.8f + 0.2f
            )
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_movement"
    )
    
    Canvas(modifier = modifier) {
        particles.forEach { particle ->
            val currentY = (particle.y + animationProgress * particle.speed) % 1f
            val currentX = particle.x + sin(animationProgress * 2 * PI.toFloat() + particle.x * 10) * 0.1f
            
            drawCircle(
                color = LuxorColors.pureGold.copy(alpha = particle.alpha * (1f - currentY)),
                radius = particle.size,
                center = Offset(
                    currentX * size.width,
                    currentY * size.height
                )
            )
        }
    }
}

private fun DrawScope.drawEgyptianPattern(center: Offset, radius: Float) {
    val patternRadius = radius * 0.8f
    val segments = 8
    
    for (i in 0 until segments) {
        val angle = (i * 2 * PI / segments).toFloat()
        val startAngle = angle - PI.toFloat() / 16
        val endAngle = angle + PI.toFloat() / 16
        
        val startX = center.x + cos(startAngle) * patternRadius
        val startY = center.y + sin(startAngle) * patternRadius
        val endX = center.x + cos(endAngle) * patternRadius
        val endY = center.y + sin(endAngle) * patternRadius
        
        drawLine(
            brush = Brush.linearGradient(
                colors = listOf(
                    LuxorColors.pureGold,
                    LuxorColors.egyptianBlue
                )
            ),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
    
    // Draw inner decorative elements
    for (i in 0 until 4) {
        val angle = (i * PI / 2).toFloat()
        val innerRadius = patternRadius * 0.5f
        
        val x = center.x + cos(angle) * innerRadius
        val y = center.y + sin(angle) * innerRadius
        
        drawCircle(
            color = LuxorColors.pureGold,
            radius = 2.dp.toPx(),
            center = Offset(x, y)
        )
    }
}

private data class Particle(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val alpha: Float
)