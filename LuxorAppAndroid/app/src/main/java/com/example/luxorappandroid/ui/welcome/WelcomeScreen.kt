package com.example.luxorappandroid.ui.welcome

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.luxorappandroid.ui.theme.LuxorColors
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    val pages = WelcomePages.getPages()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    var showContent by remember { mutableStateOf(false) }
    var backgroundScale by remember { mutableStateOf(1.0f) }
    
    LaunchedEffect(Unit) {
        showContent = true
    }
    
    LaunchedEffect(pagerState.currentPage) {
        backgroundScale = 0.95f
        kotlinx.coroutines.delay(300)
        backgroundScale = 1.0f
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            WelcomePage(
                pageData = pages[page],
                pageIndex = page,
                totalPages = pages.size,
                backgroundScale = backgroundScale
            )
        }
        
        // Skip button (top right)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 50.dp)
                .zIndex(1f),
            contentAlignment = Alignment.TopEnd
        ) {
            val skipAlpha by animateFloatAsState(
                targetValue = if (showContent) 1f else 0f,
                animationSpec = spring(dampingRatio = 0.8f, stiffness = 600f),
                label = "skipAlpha"
            )
            
            Button(
                onClick = onSkipClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .graphicsLayer { alpha = skipAlpha }
                    .scale(if (showContent) 1f else 0.8f)
            ) {
                Text(
                    text = "Next",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        // Bottom controls
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Page indicators
            val indicatorAlpha by animateFloatAsState(
                targetValue = if (showContent) 1f else 0f,
                animationSpec = spring(dampingRatio = 0.8f, stiffness = 600f),
                label = "indicatorAlpha"
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .graphicsLayer { alpha = indicatorAlpha }
            ) {
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    val animatedWidth by animateIntAsState(
                        targetValue = if (isSelected) 12 else 8,
                        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
                        label = "indicatorWidth"
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(width = animatedWidth.dp, height = animatedWidth.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) LuxorColors.pureGold
                                else Color.White.copy(alpha = 0.4f)
                            )
                    )
                }
            }
            
            // Next/Get Started button
            val buttonAlpha by animateFloatAsState(
                targetValue = if (showContent) 1f else 0f,
                animationSpec = spring(dampingRatio = 0.8f, stiffness = 600f),
                label = "buttonAlpha"
            )
            
            var isAnimating by remember { mutableStateOf(false) }
            
            Button(
                onClick = {
                    isAnimating = true
                    scope.launch {
                        kotlinx.coroutines.delay(100)
                        if (pagerState.currentPage < pages.size - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            onGetStartedClick()
                        }
                        isAnimating = false
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .height(50.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                LuxorColors.pureGold,
                                LuxorColors.richGold
                            )
                        ),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .graphicsLayer { alpha = buttonAlpha }
                    .scale(if (isAnimating) 0.95f else 1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (pagerState.currentPage < pages.size - 1) "Next" else "Get Started",
                        color = LuxorColors.hieroglyphBrown,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (pagerState.currentPage < pages.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(android.R.drawable.ic_media_play),
                            contentDescription = null,
                            tint = LuxorColors.hieroglyphBrown,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomePage(
    pageData: WelcomePageData,
    pageIndex: Int,
    totalPages: Int,
    backgroundScale: Float = 1.0f
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    
    var showContent by remember { mutableStateOf(false) }
    
    LaunchedEffect(pageIndex) {
        showContent = false
        kotlinx.coroutines.delay(200)
        showContent = true
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Full-screen background image
        Image(
            painter = painterResource(id = pageData.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .scale(backgroundScale)
                .graphicsLayer {
                    scaleX = 1.1f
                    scaleY = 1.1f
                },
            contentScale = ContentScale.Crop
        )
        
        // Dark overlay for better text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )
        
        // Animated particles effect
        AnimatedParticles(
            modifier = Modifier.fillMaxSize(),
            particleCount = 20
        )
        
        // Content overlay
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .padding(bottom = 200.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            val contentAlpha by animateFloatAsState(
                targetValue = if (showContent) 1f else 0f,
                animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
                label = "contentAlpha"
            )
            
            val contentOffset by animateFloatAsState(
                targetValue = if (showContent) 0f else 50f,
                animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
                label = "contentOffset"
            )
            
            Column(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = contentAlpha
                        translationY = contentOffset
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = pageData.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    lineHeight = 38.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = pageData.subtitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = LuxorColors.pureGold,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    lineHeight = 26.sp
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = pageData.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }
}

@Composable
fun AnimatedParticles(
    modifier: Modifier = Modifier,
    particleCount: Int = 20
) {
    val particles = remember {
        (0 until particleCount).map {
            ParticleState(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 4f + 2f,
                speed = Random.nextFloat() * 0.5f + 0.1f,
                alpha = Random.nextFloat() * 0.6f + 0.2f
            )
        }
    }
    
    var animationTime by remember { mutableStateOf(0f) }
    
    LaunchedEffect(Unit) {
        while (true) {
            animationTime += 0.016f // ~60fps
            kotlinx.coroutines.delay(16)
        }
    }
    
    Canvas(modifier = modifier) {
        particles.forEach { particle ->
            val currentY = (particle.y + animationTime * particle.speed) % 1f
            val currentX = particle.x + sin(animationTime * 2f + particle.y * 10f) * 0.1f
            
            drawCircle(
                color = LuxorColors.pureGold.copy(alpha = particle.alpha),
                radius = particle.size,
                center = Offset(
                    x = currentX * size.width,
                    y = currentY * size.height
                )
            )
        }
    }
}

data class ParticleState(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val alpha: Float
)