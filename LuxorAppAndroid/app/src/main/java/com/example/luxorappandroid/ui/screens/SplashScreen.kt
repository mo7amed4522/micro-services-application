package com.example.luxorappandroid.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.components.LuxorLogo
import com.example.luxorappandroid.ui.components.LuxorParticles
import com.example.luxorappandroid.ui.theme.LuxorColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    
    // Animation states - matching iOS timing
    var backgroundOpacity by remember { mutableStateOf(0f) }
    var logoScale by remember { mutableStateOf(0.5f) }
    var logoOpacity by remember { mutableStateOf(0f) }
    var showGoldenParticles by remember { mutableStateOf(false) }
    
    val animatedBackgroundOpacity by animateFloatAsState(
        targetValue = backgroundOpacity,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "background_opacity"
    )
    
    val animatedLogoScale by animateFloatAsState(
        targetValue = logoScale,
        animationSpec = tween(
            durationMillis = 1200,
            delayMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "logo_scale"
    )
    
    val animatedLogoOpacity by animateFloatAsState(
        targetValue = logoOpacity,
        animationSpec = tween(
            durationMillis = 1200,
            delayMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "logo_opacity"
    )
    
    // iOS-style background gradient
    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            LuxorColors.hieroglyphBrown.copy(alpha = 0.9f),
            LuxorColors.deepGold.copy(alpha = 0.3f),
            LuxorColors.pureGold.copy(alpha = 0.1f)
        ),
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
    )
    
    // Start animation sequence matching iOS
    LaunchedEffect(Unit) {
        // Background fade in
        backgroundOpacity = 1f
        
        delay(300)
        // Logo animation
        logoScale = 1f
        logoOpacity = 1f
        
        delay(800)
        // Particles appear
        showGoldenParticles = true
        
        delay(2500)
        // Fade out
        logoOpacity = 0f
        backgroundOpacity = 0f
        
        delay(500)
        onSplashComplete()
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .alpha(animatedBackgroundOpacity),
        contentAlignment = Alignment.Center
    ) {
        // Golden particles background
        if (showGoldenParticles) {
            LuxorGoldenParticles(
                modifier = Modifier.fillMaxSize(),
                isVisible = showGoldenParticles
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(30.dp)
        ) {
            // Logo with blur effect (iOS style)
            Box {
                // Blurred background logo
                LuxorLogo(
                    modifier = Modifier
                        .scale(animatedLogoScale)
                        .alpha(animatedLogoOpacity * 0.6f),
                    size = 120.dp,
                    animated = false,
                    showParticles = false
                )
                
                // Sharp foreground logo
                LuxorLogo(
                    modifier = Modifier
                        .scale(animatedLogoScale)
                        .alpha(animatedLogoOpacity),
                    size = 120.dp,
                    animated = true,
                    showParticles = false
                )
            }
            
            Spacer(modifier = Modifier.height(30.dp))
            
            // Welcome title with iOS-style gradient text
            Text(
                text = "Welcome to Luxor",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = LuxorColors.pureGold,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(animatedLogoOpacity)
                    .scale(animatedLogoScale * 0.9f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Subtitle
            Text(
                text = "Discover Ancient Wonders",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = LuxorColors.papyrusBeige.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(animatedLogoOpacity)
                    .scale(animatedLogoScale * 0.9f)
            )
        }
    }
}

@Composable
fun LuxorLoadingIndicator(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "loading_rotation"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "loading_scale"
    )
    
    Box(
        modifier = modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer ring
        LuxorLogo(
            modifier = Modifier
                .size(32.dp)
                .scale(scale),
            size = 32.dp,
            animated = true,
            showParticles = false
        )
    }
}

@Composable
fun LuxorSplashWithBranding(
    onSplashComplete: () -> Unit,
    modifier: Modifier = Modifier,
    appVersion: String = "1.0.0"
) {
    val isDarkTheme = isSystemInDarkTheme()
    
    // Animation states
    var startAnimation by remember { mutableStateOf(false) }
    
    val contentAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "content_alpha"
    )
    
    val brandingAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = 1500,
            easing = FastOutSlowInEasing
        ),
        label = "branding_alpha"
    )
    
    // Background gradient
    val backgroundBrush = Brush.verticalGradient(
        colors = if (isDarkTheme) {
            listOf(
                Color.Black,
                LuxorColors.Dark.background,
                LuxorColors.Dark.textPrimary
            )
        } else {
            listOf(
                LuxorColors.papyrusBeige,
                LuxorColors.Light.background,
                LuxorColors.Light.textPrimary
            )
        }
    )
    
    // Start animation and handle completion
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3500) // Extended splash duration for branding
        onSplashComplete()
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Particle background
        LuxorParticles(
            modifier = Modifier.fillMaxSize(),
            particleCount = 25
        )
        
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(contentAlpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo and title
            LuxorLogo(
                size = 120.dp,
                animated = startAnimation,
                showParticles = true
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "LUXOR",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = LuxorColors.pureGold,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Ancient Wonders Await",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        // Branding at bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .alpha(brandingAlpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LuxorLoadingIndicator()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Version $appVersion",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "© 2024 Luxor Travel App",
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}