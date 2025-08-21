package com.example.luxorappandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.luxorappandroid.ui.theme.LuxorColors

data class SettingsSection(
    val title: String,
    val items: List<SettingsItem>
)

data class SettingsItem(
    val title: String,
    val subtitle: String? = null,
    val icon: ImageVector,
    val type: SettingsItemType,
    val value: Any? = null,
    val onClick: (() -> Unit)? = null
)

enum class SettingsItemType {
    NAVIGATION, TOGGLE, SELECTION, INFO
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    
    // Settings state
    var isDarkMode by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var locationEnabled by remember { mutableStateOf(true) }
    var offlineMode by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English") }
    var selectedCurrency by remember { mutableStateOf("EGP") }
    
    val settingsSections = listOf(
        SettingsSection(
            title = "Account",
            items = listOf(
                SettingsItem(
                    title = "Profile",
                    subtitle = "Edit your personal information",
                    icon = Icons.Default.Person,
                    type = SettingsItemType.NAVIGATION,
                    onClick = { /* Navigate to profile */ }
                ),
                SettingsItem(
                    title = "Preferences",
                    subtitle = "Customize your experience",
                    icon = Icons.Default.Tune,
                    type = SettingsItemType.NAVIGATION,
                    onClick = { /* Navigate to preferences */ }
                ),
                SettingsItem(
                    title = "Subscription",
                    subtitle = "Manage your premium features",
                    icon = Icons.Default.Star,
                    type = SettingsItemType.NAVIGATION,
                    onClick = { /* Navigate to subscription */ }
                )
            )
        ),
        SettingsSection(
            title = "App Settings",
            items = listOf(
                SettingsItem(
                    title = "Dark Mode",
                    subtitle = "Switch between light and dark themes",
                    icon = Icons.Default.DarkMode,
                    type = SettingsItemType.TOGGLE,
                    value = isDarkMode,
                    onClick = { isDarkMode = !isDarkMode }
                ),
                SettingsItem(
                    title = "Language",
                    subtitle = selectedLanguage,
                    icon = Icons.Default.Language,
                    type = SettingsItemType.SELECTION,
                    value = selectedLanguage,
                    onClick = { /* Show language selection */ }
                ),
                SettingsItem(
                    title = "Currency",
                    subtitle = selectedCurrency,
                    icon = Icons.Default.AttachMoney,
                    type = SettingsItemType.SELECTION,
                    value = selectedCurrency,
                    onClick = { /* Show currency selection */ }
                ),
                SettingsItem(
                    title = "Offline Mode",
                    subtitle = "Download content for offline use",
                    icon = Icons.Default.CloudOff,
                    type = SettingsItemType.TOGGLE,
                    value = offlineMode,
                    onClick = { offlineMode = !offlineMode }
                )
            )
        ),
        SettingsSection(
            title = "Privacy & Security",
            items = listOf(
                SettingsItem(
                    title = "Notifications",
                    subtitle = "Manage push notifications",
                    icon = Icons.Default.Notifications,
                    type = SettingsItemType.TOGGLE,
                    value = notificationsEnabled,
                    onClick = { notificationsEnabled = !notificationsEnabled }
                ),
                SettingsItem(
                    title = "Location Services",
                    subtitle = "Allow location access for better experience",
                    icon = Icons.Default.LocationOn,
                    type = SettingsItemType.TOGGLE,
                    value = locationEnabled,
                    onClick = { locationEnabled = !locationEnabled }
                ),
                SettingsItem(
                    title = "Privacy Policy",
                    subtitle = "Read our privacy policy",
                    icon = Icons.Default.PrivacyTip,
                    type = SettingsItemType.NAVIGATION,
                    onClick = { /* Open privacy policy */ }
                ),
                SettingsItem(
                    title = "Terms of Service",
                    subtitle = "Read our terms of service",
                    icon = Icons.Default.Description,
                    type = SettingsItemType.NAVIGATION,
                    onClick = { /* Open terms */ }
                )
            )
        ),
        SettingsSection(
            title = "Support",
            items = listOf(
                SettingsItem(
                    title = "Help Center",
                    subtitle = "Get help and support",
                    icon = Icons.Default.Help,
                    type = SettingsItemType.NAVIGATION,
                    onClick = { /* Open help center */ }
                ),
                SettingsItem(
                    title = "Contact Us",
                    subtitle = "Send us your feedback",
                    icon = Icons.Default.ContactSupport,
                    type = SettingsItemType.NAVIGATION,
                    onClick = { /* Open contact form */ }
                ),
                SettingsItem(
                    title = "Rate App",
                    subtitle = "Rate us on the Play Store",
                    icon = Icons.Default.Star,
                    type = SettingsItemType.NAVIGATION,
                    onClick = { /* Open Play Store */ }
                ),
                SettingsItem(
                    title = "Share App",
                    subtitle = "Share Luxor with friends",
                    icon = Icons.Default.Share,
                    type = SettingsItemType.NAVIGATION,
                    onClick = { /* Share app */ }
                )
            )
        ),
        SettingsSection(
            title = "About",
            items = listOf(
                SettingsItem(
                    title = "App Version",
                    subtitle = "1.0.0 (Build 1)",
                    icon = Icons.Default.Info,
                    type = SettingsItemType.INFO
                ),
                SettingsItem(
                    title = "What's New",
                    subtitle = "See latest updates",
                    icon = Icons.Default.NewReleases,
                    type = SettingsItemType.NAVIGATION,
                    onClick = { /* Show changelog */ }
                ),
                SettingsItem(
                    title = "Licenses",
                    subtitle = "Open source licenses",
                    icon = Icons.Default.Copyright,
                    type = SettingsItemType.NAVIGATION,
                    onClick = { /* Show licenses */ }
                )
            )
        )
    )
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LuxorColors.pureGold,
                        LuxorColors.deepGold.copy(alpha = 0.3f),
                        LuxorColors.luxorGold
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
            // Header with User Profile
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = LuxorColors.luxorGold
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Avatar
                    Card(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = LuxorColors.luxorGold.copy(alpha = 0.2f)
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = LuxorColors.luxorGold,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "Welcome, Traveler!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = LuxorColors.luxorGold
                        )
                    )
                    
                    Text(
                        text = "user@example.com",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = LuxorColors.deepGold.copy(alpha = 0.7f)
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Quick Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        QuickStatCard(
                            title = "Visited",
                            value = "12",
                            subtitle = "Places",
                            icon = Icons.Default.Place
                        )
                        
                        QuickStatCard(
                            title = "Saved",
                            value = "8",
                            subtitle = "Favorites",
                            icon = Icons.Default.Favorite
                        )
                        
                        QuickStatCard(
                            title = "Shared",
                            value = "24",
                            subtitle = "Photos",
                            icon = Icons.Default.Photo
                        )
                    }
                }
            }
        }
        
        // Settings Sections
        items(settingsSections) { section ->
            SettingsSectionCard(
                section = section,
                onToggleChange = { item, newValue ->
                    when (item.title) {
                        "Dark Mode" -> isDarkMode = newValue
                        "Notifications" -> notificationsEnabled = newValue
                        "Location Services" -> locationEnabled = newValue
                        "Offline Mode" -> offlineMode = newValue
                    }
                }
            )
        }
        
        item {
            // Sign Out Button
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = LuxorColors.hieroglyphBrown.copy(alpha = 0.1f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Sign Out",
                        tint = LuxorColors.hieroglyphBrown,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Text(
                        text = "Sign Out",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = LuxorColors.hieroglyphBrown
                        )
                    )
                }
            }
        }
        
        item {
            // App Info Footer
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Made with ❤️ for Egypt",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = LuxorColors.luxorGold.copy(alpha = 0.6f)
                    ),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "© 2024 Luxor App. All rights reserved.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = LuxorColors.pureGold.copy(alpha = 0.5f)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun QuickStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = LuxorColors.pureGold,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = LuxorColors.luxorGold
            )
        )
        
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall.copy(
                color = LuxorColors.luxorGold.copy(alpha = 0.7f)
            )
        )
    }
}

@Composable
fun SettingsSectionCard(
    section: SettingsSection,
    onToggleChange: (SettingsItem, Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = LuxorColors.sandstone
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = section.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = LuxorColors.luxorGold
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            section.items.forEachIndexed { index, item ->
                SettingsItemRow(
                    item = item,
                    onToggleChange = { newValue -> onToggleChange(item, newValue) }
                )
                
                if (index < section.items.size - 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(
                        color = LuxorColors.sandstone.copy(alpha = 0.2f),
                        thickness = 0.5.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun SettingsItemRow(
    item: SettingsItem,
    onToggleChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = LuxorColors.sandstone.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = LuxorColors.sandstone
                )
            )
            
            if (item.subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = LuxorColors.sandstone.copy(alpha = 0.7f)
                    )
                )
            }
        }
        
        when (item.type) {
            SettingsItemType.TOGGLE -> {
                Switch(
                    checked = item.value as? Boolean ?: false,
                    onCheckedChange = onToggleChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = LuxorColors.luxorGold,
                        checkedTrackColor = LuxorColors.luxorGold.copy(alpha = 0.5f),
                        uncheckedThumbColor = LuxorColors.pureGold,
                        uncheckedTrackColor = LuxorColors.deepGold.copy(alpha = 0.3f)
                    )
                )
            }
            
            SettingsItemType.NAVIGATION -> {
                IconButton(
                    onClick = item.onClick ?: {}
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Navigate",
                        tint = LuxorColors.sandstone.copy(alpha = 0.5f)
                    )
                }
            }
            
            SettingsItemType.SELECTION -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.value?.toString() ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = LuxorColors.sandstone,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    
                    IconButton(
                        onClick = item.onClick ?: {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Select",
                            tint = LuxorColors.sandstone.copy(alpha = 0.5f)
                        )
                    }
                }
            }
            
            SettingsItemType.INFO -> {
                // No action for info items
            }
        }
    }
}