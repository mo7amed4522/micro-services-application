package com.example.luxorappandroid.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.luxorappandroid.ui.theme.LuxorColors
import kotlinx.coroutines.delay
import kotlin.random.Random

data class Particle(
    val id: Int,
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val size: Float,
    val color: Color,
    val duration: Int,
    val delay: Int
)

@Composable
fun LuxorParticles(
    modifier: Modifier = Modifier,
    particleCount: Int = 20,
    isVisible: Boolean = true
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    
    val particles = remember {
        (0 until particleCount).map { index ->
            Particle(
                id = index,
                startX = Random.nextFloat() * screenWidth,
                startY = Random.nextFloat() * screenHeight,
                endX = Random.nextFloat() * screenWidth,
                endY = Random.nextFloat() * screenHeight,
                size = Random.nextFloat() * 4f + 2f, // 2-6dp
                color = LuxorColors.pureGold.copy(alpha = Random.nextFloat() * 0.5f + 0.3f), // 0.3-0.8 alpha
                duration = Random.nextInt(2000) + 2000, // 2-4 seconds
                delay = Random.nextInt(2000) // 0-2 seconds delay
            )
        }
    }
    
    val animatables = remember {
        particles.map { Animatable(0f) }
    }
    
    LaunchedEffect(isVisible) {
        if (isVisible) {
            particles.forEachIndexed { index, particle ->
                kotlinx.coroutines.launch {
                    delay(particle.delay.toLong())
                    animatables[index].animateTo(
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = particle.duration,
                                easing = LinearEasing
                            ),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                }
            }
        }
    }
    
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        if (isVisible) {
            particles.forEachIndexed { index, particle ->
                val progress = animatables[index].value
                
                // Interpolate position
                val currentX = particle.startX + (particle.endX - particle.startX) * progress
                val currentY = particle.startY + (particle.endY - particle.startY) * progress
                
                // Animate opacity with sine wave for smooth fade in/out
                val opacity = kotlin.math.sin(progress * kotlin.math.PI).toFloat()
                val currentColor = particle.color.copy(alpha = particle.color.alpha * opacity)
                
                drawCircle(
                    color = currentColor,
                    radius = particle.size,
                    center = Offset(currentX, currentY)
                )
            }
        }
    }
}

@Composable
fun LuxorGoldenParticles(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    
    val particles = remember {
        (0 until 15).map { index ->
            Particle(
                id = index,
                startX = Random.nextFloat() * screenWidth,
                startY = screenHeight + 50f, // Start below screen
                endX = Random.nextFloat() * screenWidth,
                endY = -50f, // End above screen
                size = Random.nextFloat() * 3f + 2f, // 2-5dp
                color = LuxorColors.pureGold.copy(alpha = Random.nextFloat() * 0.4f + 0.4f), // 0.4-0.8 alpha
                duration = Random.nextInt(3000) + 3000, // 3-6 seconds
                delay = Random.nextInt(2000) // 0-2 seconds delay
            )
        }
    }
    
    val animatables = remember {
        particles.map { Animatable(0f) }
    }
    
    LaunchedEffect(isVisible) {
        if (isVisible) {
            particles.forEachIndexed { index, particle ->
                kotlinx.coroutines.launch {
                    delay(particle.delay.toLong())
                    animatables[index].animateTo(
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = particle.duration,
                                easing = LinearEasing
                            ),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                }
            }
        }
    }
    
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        if (isVisible) {
            particles.forEachIndexed { index, particle ->
                val progress = animatables[index].value
                
                // Vertical movement from bottom to top
                val currentX = particle.startX + kotlin.math.sin(progress * 2 * kotlin.math.PI) * 20f // Add slight horizontal drift
                val currentY = particle.startY + (particle.endY - particle.startY) * progress
                
                // Fade in at start, fade out at end
                val opacity = when {
                    progress < 0.1f -> progress / 0.1f
                    progress > 0.9f -> (1f - progress) / 0.1f
                    else -> 1f
                }
                
                val currentColor = particle.color.copy(alpha = particle.color.alpha * opacity)
                
                drawCircle(
                    color = currentColor,
                    radius = particle.size,
                    center = Offset(currentX, currentY)
                )
            }
        }
    }
}