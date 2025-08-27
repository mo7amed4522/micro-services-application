package com.example.luxorappandroid.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.R
import com.example.luxorappandroid.ui.theme.LocalLuxorColors
import com.example.luxorappandroid.ui.theme.LocalLuxorThemeManager
import com.example.luxorappandroid.ui.theme.LuxorColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onLogout: () -> Unit
) {
    val settingsManager = rememberSettingsManager()
    val themeManager = LocalLuxorThemeManager.current
    val luxorColors = LocalLuxorColors.current as LuxorColors.Light
    

    val isDarkMode by themeManager?.isDarkMode ?: remember { mutableStateOf(false) }
    val selectedLanguage by settingsManager.selectedLanguage
    val selectedCurrency by settingsManager.selectedCurrency
    val notificationsEnabled by settingsManager.notificationsEnabled
    val locationEnabled by settingsManager.locationEnabled
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        luxorColors.background,
                        LuxorColors.papyrusBeige
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.settings),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = luxorColors.textPrimary,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
            
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = luxorColors.cardBackground
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(LuxorColors.pureGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(30.dp),
                                tint = LuxorColors.pureGold
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "John Doe",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = luxorColors.textPrimary
                            )
                            Text(
                                text = "john.doe@example.com",
                                fontSize = 14.sp,
                                color = luxorColors.textSecondary
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Edit Profile",
                            tint = luxorColors.textSecondary
                        )
                    }
                }
            }
            item {
                SettingsSection(
                    title = "Account",
                    items = listOf(
                        SettingsItem.Row(
                            icon = Icons.Default.Person,
                            title = "Edit Profile",
                            subtitle = "Update your personal information",
                            onClick = { /* Handle edit profile */ }
                        ),
                        SettingsItem.Row(
                            icon = Icons.Default.Security,
                            title = "Change Password",
                            subtitle = "Update your account password",
                            onClick = { /* Handle change password */ }
                        ),
                        SettingsItem.Row(
                            icon = Icons.Default.Payment,
                            title = "Payment Methods",
                            subtitle = "Manage your payment options",
                            onClick = { /* Handle payment methods */ }
                        )
                    )
                )
            }
            item {
                SettingsSection(
                    title = "Preferences",
                    items = listOf(
                        SettingsItem.Toggle(
                            icon = Icons.Default.DarkMode,
                            title = stringResource(R.string.dark_mode),
                            subtitle = stringResource(R.string.switch_theme_subtitle),
                            isChecked = isDarkMode,
                            onToggle = { themeManager?.toggleTheme() }
                        ),
                        SettingsItem.Picker(
                            icon = Icons.Default.Language,
                            title = stringResource(R.string.language),
                            subtitle = selectedLanguage,
                            options = SettingsManager.supportedLanguages,
                            selectedOption = selectedLanguage,
                            onOptionSelected = { settingsManager.setLanguage(it) }
                        ),
                        SettingsItem.Picker(
                            icon = Icons.Default.AttachMoney,
                            title = stringResource(R.string.currency),
                            subtitle = selectedCurrency,
                            options = SettingsManager.supportedCurrencies,
                            selectedOption = selectedCurrency,
                            onOptionSelected = { settingsManager.setCurrency(it) }
                        )
                    )
                )
            }
            item {
                SettingsSection(
                    title = "Privacy & Security",
                    items = listOf(
                        SettingsItem.Toggle(
                            icon = Icons.Default.Notifications,
                            title = stringResource(R.string.push_notifications),
                            subtitle = stringResource(R.string.notifications_subtitle),
                            isChecked = notificationsEnabled,
                            onToggle = { settingsManager.setNotificationsEnabled(it) }
                        ),
                        SettingsItem.Toggle(
                            icon = Icons.Default.LocationOn,
                            title = stringResource(R.string.location_services),
                            subtitle = stringResource(R.string.location_subtitle),
                            isChecked = locationEnabled,
                            onToggle = { settingsManager.setLocationEnabled(it) }
                        ),
                        SettingsItem.Row(
                            icon = Icons.Default.PrivacyTip,
                            title = "Privacy Policy",
                            subtitle = "Read our privacy policy",
                            onClick = { /* Handle privacy policy */ }
                        ),
                        SettingsItem.Row(
                            icon = Icons.Default.Gavel,
                            title = "Terms of Service",
                            subtitle = "Read our terms of service",
                            onClick = { /* Handle terms of service */ }
                        )
                    )
                )
            }
            item {
                SettingsSection(
                    title = "App Settings",
                    items = listOf(
                        SettingsItem.Row(
                            icon = Icons.Default.Storage,
                            title = "Storage",
                            subtitle = "Manage app data and cache",
                            onClick = { /* Handle storage */ }
                        ),
                        SettingsItem.Row(
                            icon = Icons.Default.Download,
                            title = "Downloads",
                            subtitle = "Manage offline content",
                            onClick = { /* Handle downloads */ }
                        ),
                        SettingsItem.Row(
                            icon = Icons.Default.Update,
                            title = "App Updates",
                            subtitle = "Check for updates",
                            onClick = {  }
                        )
                    )
                )
            }
            item {
                SettingsSection(
                    title = "Support",
                    items = listOf(
                        SettingsItem.Row(
                            icon = Icons.AutoMirrored.Filled.Help,
                            title = "Help Center",
                            subtitle = "Get help and support",
                            onClick = { }
                        ),
                        SettingsItem.Row(
                            icon = Icons.Default.Feedback,
                            title = "Send Feedback",
                            subtitle = "Share your thoughts with us",
                            onClick = {  }
                        ),
                        SettingsItem.Row(
                            icon = Icons.Default.Star,
                            title = "Rate App",
                            subtitle = "Rate us on the app store",
                            onClick = { }
                        ),
                        SettingsItem.Row(
                            icon = Icons.Default.Info,
                            title = "About",
                            subtitle = "App version and information",
                            onClick = {  }
                        )
                    )
                )
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { onLogout() },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = LuxorColors.Light.cardBackground
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(24.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Text(
                            text = "Logout",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    items: List<SettingsItem>
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = LuxorColors.Light.textPrimary,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = LuxorColors.Light.cardBackground
            )
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    when (item) {
                        is SettingsItem.Row -> {
                            SettingsRowItem(
                                icon = item.icon,
                                title = item.title,
                                subtitle = item.subtitle,
                                onClick = item.onClick
                            )
                        }
                        is SettingsItem.Toggle -> {
                            SettingsToggleItem(
                                icon = item.icon,
                                title = item.title,
                                subtitle = item.subtitle,
                                isChecked = item.isChecked,
                                onToggle = item.onToggle
                            )
                        }
                        is SettingsItem.Picker -> {
                            SettingsPickerItem(
                                icon = item.icon,
                                title = item.title,
                                subtitle = item.subtitle,
                                options = item.options,
                                selectedOption = item.selectedOption,
                                onOptionSelected = item.onOptionSelected
                            )
                        }
                    }
                    if (index < items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = DividerDefaults.Thickness, color = LuxorColors.Light.textSecondary.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

sealed class SettingsItem {
    data class Row(
        val icon: ImageVector,
        val title: String,
        val subtitle: String,
        val onClick: () -> Unit
    ) : SettingsItem()
    
    data class Toggle(
        val icon: ImageVector,
        val title: String,
        val subtitle: String,
        val isChecked: Boolean,
        val onToggle: (Boolean) -> Unit
    ) : SettingsItem()
    
    data class Picker(
        val icon: ImageVector,
        val title: String,
        val subtitle: String,
        val options: List<String>,
        val selectedOption: String,
        val onOptionSelected: (String) -> Unit
    ) : SettingsItem()
}

@Composable
fun SettingsRowItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = LuxorColors.richGold,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = LuxorColors.Light.textPrimary
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = LuxorColors.Light.textSecondary
            )
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = LuxorColors.Light.textSecondary
        )
    }
}

@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = LuxorColors.richGold,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = LuxorColors.Light.textPrimary
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = LuxorColors.Light.textSecondary
            )
        }
        
        Switch(
            checked = isChecked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = LuxorColors.pureGold,
                checkedTrackColor = LuxorColors.pureGold.copy(alpha = 0.5f),
                uncheckedThumbColor = LuxorColors.Light.textSecondary,
                uncheckedTrackColor = LuxorColors.Light.textSecondary.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
fun SettingsPickerItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = LuxorColors.richGold,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = LuxorColors.Light.textPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = LuxorColors.Light.textSecondary
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Select",
                tint = LuxorColors.Light.textSecondary
            )
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}