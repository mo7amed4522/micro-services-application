package com.example.luxorappandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.components.LuxorButton
import com.example.luxorappandroid.ui.theme.LuxorColors

data class Monument(
    val id: String,
    val name: String,
    val description: String,
    val rating: Float,
    val visitorsToday: Int,
    val category: String,
    val estimatedTime: String,
    val isOpen: Boolean
)

data class QuickAction(
    val title: String,
    val icon: ImageVector,
    val description: String,
    val action: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    
    // Sample data
    val featuredMonuments = listOf(
        Monument(
            id = "1",
            name = "Valley of the Kings",
            description = "Ancient burial ground of pharaohs with magnificent tombs",
            rating = 4.8f,
            visitorsToday = 1247,
            category = "Tombs",
            estimatedTime = "3-4 hours",
            isOpen = true
        ),
        Monument(
            id = "2",
            name = "Karnak Temple",
            description = "Massive temple complex dedicated to Amun-Ra",
            rating = 4.9f,
            visitorsToday = 892,
            category = "Temples",
            estimatedTime = "2-3 hours",
            isOpen = true
        ),
        Monument(
            id = "3",
            name = "Luxor Temple",
            description = "Beautiful temple illuminated at night",
            rating = 4.7f,
            visitorsToday = 654,
            category = "Temples",
            estimatedTime = "1-2 hours",
            isOpen = false
        )
    )
    
    val quickActions = listOf(
        QuickAction(
            title = "Book Tour",
            icon = Icons.Default.Tour,
            description = "Guided tours available",
            action = { /* Handle tour booking */ }
        ),
        QuickAction(
            title = "Audio Guide",
            icon = Icons.Default.Headphones,
            description = "Self-guided experience",
            action = { /* Handle audio guide */ }
        ),
        QuickAction(
            title = "Map",
            icon = Icons.Default.Map,
            description = "Interactive site map",
            action = { /* Handle map */ }
        ),
        QuickAction(
            title = "Tickets",
            icon = Icons.Default.ConfirmationNumber,
            description = "Buy entrance tickets",
            action = { /* Handle tickets */ }
        )
    )
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LuxorColors.current.background,
                        LuxorColors.current.surface.copy(alpha = 0.3f),
                        LuxorColors.current.background
                    )
                )
            ),
        contentPadding = PaddingValues(
            horizontal = if (isTablet) 32.dp else 16.dp,
            vertical = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            // Welcome Header
            Column {
                Text(
                    text = "Welcome to Luxor",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                LuxorColors.current.primary,
                                LuxorColors.current.secondary
                            )
                        )
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Discover ancient wonders and timeless treasures",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = LuxorColors.current.onSurface.copy(alpha = 0.8f)
                    )
                )
            }
        }
        
        item {
            // Quick Actions
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = LuxorColors.current.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = LuxorColors.current.primary
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(quickActions) { action ->
                            QuickActionCard(
                                action = action,
                                modifier = Modifier.width(120.dp)
                            )
                        }
                    }
                }
            }
        }
        
        item {
            // Today's Highlights
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Highlights",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = LuxorColors.current.primary
                        )
                    )
                    
                    TextButton(
                        onClick = { /* Navigate to all monuments */ }
                    ) {
                        Text(
                            text = "See All",
                            color = LuxorColors.current.secondary
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = LuxorColors.current.secondary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        
        items(featuredMonuments) { monument ->
            MonumentCard(
                monument = monument,
                onClick = { /* Navigate to monument details */ },
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        item {
            // Weather & Tips Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = LuxorColors.current.tertiary.copy(alpha = 0.1f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = LuxorColors.current.tertiary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Perfect weather for exploring!",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = LuxorColors.current.tertiary
                            )
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "28°C • Clear skies • Light breeze\nBest time to visit outdoor monuments",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = LuxorColors.current.onSurface.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    action: QuickAction,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = action.action,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = LuxorColors.current.primary.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.title,
                tint = LuxorColors.current.primary,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = action.title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = LuxorColors.current.primary
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = action.description,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = LuxorColors.current.onSurface.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun MonumentCard(
    monument: Monument,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = LuxorColors.current.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            // Monument Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                LuxorColors.current.primary.copy(alpha = 0.3f),
                                LuxorColors.current.secondary.copy(alpha = 0.5f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = LuxorColors.current.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(48.dp)
                )
                
                // Status Badge
                Card(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (monument.isOpen) 
                            LuxorColors.current.tertiary else 
                            LuxorColors.current.error
                    )
                ) {
                    Text(
                        text = if (monument.isOpen) "Open" else "Closed",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = LuxorColors.current.onPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
            
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = monument.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = LuxorColors.current.primary
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = monument.category,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = LuxorColors.current.secondary
                            )
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = LuxorColors.current.tertiary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = monument.rating.toString(),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = monument.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = LuxorColors.current.onSurface.copy(alpha = 0.8f)
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = LuxorColors.current.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = monument.estimatedTime,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = LuxorColors.current.onSurface.copy(alpha = 0.8f)
                            )
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = LuxorColors.current.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${monument.visitorsToday} today",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = LuxorColors.current.onSurface.copy(alpha = 0.8f)
                            )
                        )
                    }
                }
            }
        }
    }
}