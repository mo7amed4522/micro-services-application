package com.example.luxorappandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.components.LuxorTextField
import com.example.luxorappandroid.ui.theme.LuxorColors

data class Place(
    val id: String,
    val name: String,
    val description: String,
    val category: PlaceCategory,
    val rating: Float,
    val reviewCount: Int,
    val distance: String,
    val estimatedTime: String,
    val isOpen: Boolean,
    val ticketPrice: String?,
    val highlights: List<String>
)

enum class PlaceCategory(val displayName: String, val icon: ImageVector) {
    TEMPLES("Temples", Icons.Default.AccountBalance),
    TOMBS("Tombs", Icons.Default.Terrain),
    MUSEUMS("Museums", Icons.Default.Museum),
    MARKETS("Markets", Icons.Default.Store),
    RESTAURANTS("Restaurants", Icons.Default.Restaurant),
    HOTELS("Hotels", Icons.Default.Hotel),
    ALL("All", Icons.Default.Apps)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen() {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(PlaceCategory.ALL) }
    var sortBy by remember { mutableStateOf("Rating") }
    
    // Sample data
    val allPlaces = listOf(
        Place(
            id = "1",
            name = "Valley of the Kings",
            description = "Ancient burial ground of pharaohs with magnificent decorated tombs",
            category = PlaceCategory.TOMBS,
            rating = 4.8f,
            reviewCount = 2847,
            distance = "5.2 km",
            estimatedTime = "3-4 hours",
            isOpen = true,
            ticketPrice = "240 EGP",
            highlights = listOf("Tutankhamun's Tomb", "Ramesses VI", "Seti I")
        ),
        Place(
            id = "2",
            name = "Karnak Temple Complex",
            description = "Massive temple complex dedicated to Amun-Ra, one of the largest religious buildings",
            category = PlaceCategory.TEMPLES,
            rating = 4.9f,
            reviewCount = 3521,
            distance = "2.1 km",
            estimatedTime = "2-3 hours",
            isOpen = true,
            ticketPrice = "200 EGP",
            highlights = listOf("Great Hypostyle Hall", "Sacred Lake", "Obelisks")
        ),
        Place(
            id = "3",
            name = "Luxor Museum",
            description = "World-class museum showcasing artifacts from Theban temples and necropolis",
            category = PlaceCategory.MUSEUMS,
            rating = 4.7f,
            reviewCount = 1892,
            distance = "1.8 km",
            estimatedTime = "1-2 hours",
            isOpen = true,
            ticketPrice = "140 EGP",
            highlights = listOf("Royal Mummies", "Tutankhamun Collection", "Akhenaten Statues")
        ),
        Place(
            id = "4",
            name = "Luxor Souk",
            description = "Traditional market with authentic Egyptian crafts, spices, and souvenirs",
            category = PlaceCategory.MARKETS,
            rating = 4.3f,
            reviewCount = 967,
            distance = "0.8 km",
            estimatedTime = "1-2 hours",
            isOpen = true,
            ticketPrice = null,
            highlights = listOf("Handmade Crafts", "Spices", "Jewelry", "Papyrus")
        ),
        Place(
            id = "5",
            name = "Sofitel Winter Palace",
            description = "Historic luxury hotel with stunning Nile views and colonial architecture",
            category = PlaceCategory.HOTELS,
            rating = 4.6f,
            reviewCount = 1234,
            distance = "1.2 km",
            estimatedTime = "Visit anytime",
            isOpen = true,
            ticketPrice = null,
            highlights = listOf("Nile Views", "Historic Architecture", "Fine Dining")
        )
    )
    
    val filteredPlaces = allPlaces.filter { place ->
        val matchesSearch = place.name.contains(searchQuery, ignoreCase = true) ||
                place.description.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == PlaceCategory.ALL || place.category == selectedCategory
        matchesSearch && matchesCategory
    }.let { places ->
        when (sortBy) {
            "Rating" -> places.sortedByDescending { it.rating }
            "Distance" -> places.sortedBy { it.distance.replace(" km", "").toFloatOrNull() ?: 0f }
            "Name" -> places.sortedBy { it.name }
            else -> places
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LuxorColors.pureGold,
                        LuxorColors.luxorGold.copy(alpha = 0.3f),
                        LuxorColors.sandstone
                    )
                )
            )
    ) {
        // Header
        Column(
            modifier = Modifier.padding(
                horizontal = if (isTablet) 32.dp else 16.dp,
                vertical = 16.dp
            )
        ) {
            Text(
                text = "Explore Places",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            LuxorColors.pureGold,
                            LuxorColors.luxorGold
                        )
                    )
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Discover ${filteredPlaces.size} amazing places in Luxor",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = LuxorColors.luxorGold.copy(alpha = 0.8f)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Bar
            LuxorTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "Search places...",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Category Filter
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(PlaceCategory.values()) { category ->
                    FilterChip(
                        onClick = { selectedCategory = category },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(category.displayName)
                            }
                        },
                        selected = selectedCategory == category,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = LuxorColors.pureGold,
                            selectedLabelColor = LuxorColors.sandstone,
                            containerColor = LuxorColors.luxorGold,
                            labelColor = LuxorColors.sandstone
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Sort Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredPlaces.size} places found",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = LuxorColors.sandstone.copy(alpha = 0.7f)
                    )
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sort by:",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = LuxorColors.sandstone.copy(alpha = 0.7f)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    var expanded by remember { mutableStateOf(false) }
                    
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = sortBy,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .width(120.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxorColors.luxorGold,
                                unfocusedBorderColor = LuxorColors.bronzeGold
                            )
                        )
                        
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listOf("Rating", "Distance", "Name").forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        sortBy = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Places List
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = if (isTablet) 32.dp else 16.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredPlaces) { place ->
                PlaceCard(
                    place = place,
                    onClick = { /* Navigate to place details */ },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            if (filteredPlaces.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = LuxorColors.sandstone
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = LuxorColors.sandstone.copy(alpha = 0.5f)
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "No places found",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = LuxorColors.sandstone.copy(alpha = 0.7f)
                                ),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Try adjusting your search or filters",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = LuxorColors.sandstone.copy(alpha = 0.5f)
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceCard(
    place: Place,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = LuxorColors.sandstone
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                place.category.icon.let { LuxorColors.luxorGold.copy(alpha = 0.3f) },
                                place.category.icon.let { LuxorColors.pureGold.copy(alpha = 0.5f) }
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = place.category.icon,
                    contentDescription = null,
                    tint = LuxorColors.sandstone.copy(alpha = 0.5f),
                    modifier = Modifier.size(48.dp)
                )
                
                // Status and Price Badges
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (place.ticketPrice != null) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = LuxorColors.sandstone
                            )
                        ) {
                            Text(
                                text = place.ticketPrice,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = LuxorColors.sandstone,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                    
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (place.isOpen) 
                                LuxorColors.papyrusBeige else
                                LuxorColors.sandstone
                        )
                    ) {
                        Text(
                            text = if (place.isOpen) "Open" else "Closed",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = LuxorColors.luxorGold,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
                
                // Category Badge
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = LuxorColors.luxorGold.copy(alpha = 0.9f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = place.category.icon,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = LuxorColors.luxorGold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = place.category.displayName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = LuxorColors.luxorGold,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
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
                            text = place.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = LuxorColors.luxorGold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = LuxorColors.pureGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${place.rating} (${place.reviewCount})",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = LuxorColors.sandstone.copy(alpha = 0.8f)
                                )
                            )
                        }
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = LuxorColors.sandstone,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = place.distance,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = LuxorColors.sandstone
                                )
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(2.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = LuxorColors.sandstone.copy(alpha = 0.6f),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = place.estimatedTime,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = LuxorColors.sandstone.copy(alpha = 0.6f)
                                )
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = place.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = LuxorColors.sandstone.copy(alpha = 0.8f)
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (place.highlights.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(place.highlights.take(3)) { highlight ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = LuxorColors.pureGold.copy(alpha = 0.1f)
                                )
                            ) {
                                Text(
                                    text = highlight,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = LuxorColors.deepGold,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                        
                        if (place.highlights.size > 3) {
                            item {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = LuxorColors.papyrusBeige.copy(alpha = 0.1f)
                                    )
                                ) {
                                    Text(
                                        text = "+${place.highlights.size - 3}",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = LuxorColors.papyrusBeige.copy(alpha = 0.7f),
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}