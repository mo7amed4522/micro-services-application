package com.example.luxorappandroid.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.luxorappandroid.data.PlaceModel
import com.example.luxorappandroid.data.SampleData
import com.example.luxorappandroid.ui.theme.*

@Composable
fun PlacesView(
    isNavBarVisible: MutableState<Boolean>,
    isAppBarVisible: MutableState<Boolean>,
    lastScrollOffset: MutableState<Float>,
    isScrolling: MutableState<Boolean>,
    isDarkMode: Boolean
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var animateCards by remember { mutableStateOf(false) }
    
    val categories = SampleData.categories
    val places = SampleData.samplePlaces
    
    val filteredPlaces = remember(selectedCategory, searchText, places) {
        if (selectedCategory == "All") {
            places.filter { searchText.isEmpty() || it.name.contains(searchText, ignoreCase = true) }
        } else {
            places.filter { 
                it.category == selectedCategory && 
                (searchText.isEmpty() || it.name.contains(searchText, ignoreCase = true))
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            // Search Bar Section
            SearchSection(
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                isDarkMode = isDarkMode
            )
        }
        
        item {
            // Categories Horizontal Scroll
            CategoriesSection(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                animateCards = animateCards,
                isDarkMode = isDarkMode
            )
        }
        
        item {
            // Dynamic Text Section
            DynamicTextSection(
                selectedCategory = selectedCategory,
                isDarkMode = isDarkMode
            )
        }
        
        item {
            // Places Grid
            PlacesGridSection(
                places = filteredPlaces,
                animateCards = animateCards,
                isDarkMode = isDarkMode
            )
        }
    }

    LaunchedEffect(Unit) {
        animateCards = true
    }
}

@Composable
fun SearchSection(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    isDarkMode: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isDarkMode) LuxorColors.Dark.cardBackground else LuxorColors.Light.cardBackground,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            LuxorColors.Dark.accent.copy(alpha = 0.3f),
                            LuxorColors.pureGold.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = LuxorColors.Dark.accent,
                modifier = Modifier.size(18.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            TextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                placeholder = {
                    Text(
                        text = "Search amazing places...",
                        color = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
                    )
                },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = LuxorColors.Dark.accent,
                    textColor = if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
            
            if (searchText.isNotEmpty()) {
                IconButton(
                    onClick = { onSearchTextChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = LuxorColors.Dark.accent.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoriesSection(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    animateCards: Boolean,
    isDarkMode: Boolean
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            CategoryCard(
                category = category,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                animateCards = animateCards,
                isDarkMode = isDarkMode
            )
        }
    }
}

@Composable
fun CategoryCard(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    animateCards: Boolean,
    isDarkMode: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (animateCards) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (animateCards) 1f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    Card(
        modifier = Modifier
            .scale(scale)
            .alpha(alpha)
            .clickable { onClick() }
            .shadow(
                elevation = if (isSelected) 8.dp else 0.dp,
                shape = RoundedCornerShape(25.dp),
                spotColor = if (isSelected) LuxorColors.Dark.accent.copy(alpha = 0.3f) else Color.Transparent
            ),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                Color.Transparent
            } else {
                if (isDarkMode) LuxorColors.Dark.cardBackground else LuxorColors.Light.cardBackground
            }
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = if (isSelected) {
                        Brush.horizontalGradient(
                            colors = listOf(LuxorColors.Dark.accent, LuxorColors.pureGold)
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                if (isDarkMode) LuxorColors.Dark.cardBackground else LuxorColors.Light.cardBackground,
                                if (isDarkMode) LuxorColors.Dark.cardBackground else LuxorColors.Light.cardBackground
                            )
                        )
                    }
                )
                .border(
                    width = 1.dp,
                    color = if (isSelected) Color.Transparent else {
                        if (isDarkMode) LuxorColors.Dark.border else LuxorColors.Light.border
                    },
                    shape = RoundedCornerShape(25.dp)
                )
                .padding(horizontal = 20.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category,
                color = if (isSelected) Color.White else {
                    if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun DynamicTextSection(
    selectedCategory: String,
    isDarkMode: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {
        Row {
            Text(
                text = if (selectedCategory == "All") "Discover" else "Explore",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = if (selectedCategory == "All") "Everything" else selectedCategory,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = LuxorColors.Dark.accent
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = getDescriptionText(selectedCategory),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
            maxLines = 2
        )
    }
}

@Composable
fun PlacesGridSection(
    places: List<PlaceModel>,
    animateCards: Boolean,
    isDarkMode: Boolean
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(places) { place ->
            PlaceCard(
                place = place,
                animateCards = animateCards,
                isDarkMode = isDarkMode
            )
        }
    }
}

@Composable
fun PlaceCard(
    place: PlaceModel,
    animateCards: Boolean,
    isDarkMode: Boolean
) {
    var isLiked by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (animateCards) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (animateCards) 1f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    Card(
        modifier = Modifier
            .scale(scale)
            .alpha(alpha)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = if (isDarkMode) LuxorColors.Dark.shadow else LuxorColors.Light.shadow
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) LuxorColors.Dark.cardBackground else LuxorColors.Light.cardBackground
        )
    ) {
        Column {
            // Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                AsyncImage(
                    model = place.imageURL,
                    contentDescription = place.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
                
                // Like Button
                IconButton(
                    onClick = { isLiked = !isLiked },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(32.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            // Content Section
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Likes and Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Likes",
                            tint = Color.Red,
                            modifier = Modifier.size(12.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(4.dp))
                        
                        Text(
                            text = "${place.likes}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = LuxorColors.pureGold,
                            modifier = Modifier.size(10.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(2.dp))
                        
                        Text(
                            text = "%.1f".format(place.rating),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Place Name
                Text(
                    text = place.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Location
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = LuxorColors.Dark.accent,
                        modifier = Modifier.size(10.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                        text = place.location,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

private fun getDescriptionText(category: String): String {
    return when (category) {
        "Restaurants" -> "Taste the finest cuisines and culinary experiences"
        "Hotels" -> "Luxurious stays and comfortable accommodations"
        "Museums" -> "Explore art, history, and cultural treasures"
        "Parks" -> "Natural beauty and outdoor adventures await"
        "Shopping" -> "Premium brands and unique shopping experiences"
        "Cafes" -> "Cozy spots for coffee and delightful conversations"
        else -> "Amazing places waiting to be discovered by you"
    }
}
