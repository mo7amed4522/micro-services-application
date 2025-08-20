package com.example.luxorappandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.luxorappandroid.data.SampleData
import com.example.luxorappandroid.ui.theme.ThemeManager
import com.example.luxorappandroid.ui.theme.LanguageManager
import com.example.luxorappandroid.ui.theme.*

@Composable
fun SettingsView(
    isNavBarVisible: MutableState<Boolean>,
    isAppBarVisible: MutableState<Boolean>,
    lastScrollOffset: MutableState<Float>,
    isScrolling: MutableState<Boolean>,
    themeManager: ThemeManager,
    languageManager: LanguageManager
) {
    val colors = if (themeManager.isDarkMode) LuxorColors.Dark else LuxorColors.Light
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(themeManager.isDarkMode) }
    var locationEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(false) }
    var autoPlayVideos by remember { mutableStateOf(true) }
    var dataSaver by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English") }
    var selectedCurrency by remember { mutableStateOf("USD") }
    var showLanguagePicker by remember { mutableStateOf(false) }
    var showCurrencyPicker by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            // Header Section
            HeaderSection(themeManager.isDarkMode)
        }
        
        item {
            // Account Section
            SettingsSection(
                title = LocalizedStrings.account(languageManager.currentLanguage),
                isDarkMode = themeManager.isDarkMode
            ) {
                SettingsRow(
                    icon = Icons.Default.Person,
                    title = LocalizedStrings.editProfile(languageManager.currentLanguage),
                    subtitle = LocalizedStrings.updatePersonalInfo(languageManager.currentLanguage),
                    onClick = { /* Edit Profile */ },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsRow(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Manage your notification preferences",
                    onClick = { /* Notifications */ },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsRow(
                    icon = Icons.Default.Lock,
                    title = "Privacy",
                    subtitle = "Control your privacy settings",
                    onClick = { /* Privacy */ },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsRow(
                    icon = Icons.Default.Star,
                    title = "Payment Methods",
                    subtitle = "Manage your payment options",
                    onClick = { /* Payment Methods */ },
                    isDarkMode = themeManager.isDarkMode
                )
            }
        }
        
        item {
            // Preferences Section
            SettingsSection(
                title = "Preferences",
                isDarkMode = themeManager.isDarkMode
            ) {
                SettingsToggleRow(
                    icon = Icons.Outlined.MoreVert,
                    title = "Dark Mode",
                    subtitle = "Switch between light and dark themes",
                    isOn = darkModeEnabled,
                    onToggle = {
                        darkModeEnabled = !darkModeEnabled
                        themeManager.toggleTheme()
                    },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsToggleRow(
                    icon = Icons.Default.LocationOn,
                    title = "Location Services",
                    subtitle = "Allow access to your location",
                    isOn = locationEnabled,
                    onToggle = { locationEnabled = !locationEnabled },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsToggleRow(
                    icon = Icons.Default.Fingerprint,
                    title = "Biometric Login",
                    subtitle = "Use Face ID or Touch ID to sign in",
                    isOn = biometricEnabled,
                    onToggle = { biometricEnabled = !biometricEnabled },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsPickerRow(
                    icon = Icons.Default.Info,
                    title = "Language",
                    subtitle = "Choose your preferred language",
                    selection = selectedLanguage,
                    onSelectionChange = { selectedLanguage = it },
                    onShowPicker = { showLanguagePicker = true },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsPickerRow(
                    icon = Icons.Default.AttachMoney,
                    title = "Currency",
                    subtitle = "Select your preferred currency",
                    selection = selectedCurrency,
                    onSelectionChange = { selectedCurrency = it },
                    onShowPicker = { showCurrencyPicker = true },
                    isDarkMode = themeManager.isDarkMode
                )
            }
        }
        
        item {
            // Privacy & Security Section
            SettingsSection(
                title = "Privacy & Security",
                isDarkMode = themeManager.isDarkMode
            ) {
                SettingsRow(
                    icon = Icons.Default.Verified,
                    title = "Two-Factor Authentication",
                    subtitle = "Add an extra layer of security",
                    onClick = { /* 2FA */ },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsRow(
                    icon = Icons.Default.Key,
                    title = "Change Password",
                    subtitle = "Update your account password",
                    onClick = { /* Change Password */ },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsRow(
                    icon = Icons.Default.VisibilityOff,
                    title = "Account Privacy",
                    subtitle = "Control who can see your profile",
                    onClick = { /* Account Privacy */ },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsRow(
                    icon = Icons.Default.Delete,
                    title = "Delete Account",
                    subtitle = "Permanently delete your account",
                    onClick = { /* Delete Account */ },
                    isDarkMode = themeManager.isDarkMode,
                    textColor = Color.Red
                )
            }
        }
        
        item {
            // App Settings Section
            SettingsSection(
                title = "App Settings",
                isDarkMode = themeManager.isDarkMode
            ) {
                SettingsToggleRow(
                    icon = Icons.Default.PlayArrow,
                    title = "Auto-play Videos",
                    subtitle = "Automatically play videos in feed",
                    isOn = autoPlayVideos,
                    onToggle = { autoPlayVideos = !autoPlayVideos },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsToggleRow(
                    icon = Icons.Default.NetworkCheck,
                    title = "Data Saver",
                    subtitle = "Reduce data usage",
                    isOn = dataSaver,
                    onToggle = { dataSaver = !dataSaver },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsRow(
                    icon = Icons.Default.SystemUpdate,
                    title = "Check for Updates",
                    subtitle = "Current version: 1.0.0",
                    onClick = { /* Check Updates */ },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsRow(
                    icon = Icons.Default.Description,
                    title = "Terms of Service",
                    subtitle = "Read our terms and conditions",
                    onClick = { /* Terms */ },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsRow(
                    icon = Icons.Default.Policy,
                    title = "Privacy Policy",
                    subtitle = "Learn about data collection",
                    onClick = { /* Privacy Policy */ },
                    isDarkMode = themeManager.isDarkMode
                )
            }
        }
        
        item {
            // Support Section
            SettingsSection(
                title = "Support",
                isDarkMode = themeManager.isDarkMode
            ) {
                SettingsRow(
                    icon = Icons.Default.Help,
                    title = "Help Center",
                    subtitle = "Get help and find answers",
                    onClick = { /* Help Center */ },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsRow(
                    icon = Icons.Default.Email,
                    title = "Contact Support",
                    subtitle = "Reach out to our support team",
                    onClick = { /* Contact Support */ },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsRow(
                    icon = Icons.Default.Star,
                    title = "Rate App",
                    subtitle = "Share your feedback",
                    onClick = { /* Rate App */ },
                    isDarkMode = themeManager.isDarkMode
                )
                
                SettingsRow(
                    icon = Icons.Default.Share,
                    title = "Share App",
                    subtitle = "Tell friends about Luxor",
                    onClick = { /* Share App */ },
                    isDarkMode = themeManager.isDarkMode
                )
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    // Language Picker Dialog
    if (showLanguagePicker) {
        PickerDialog(
            title = "Language",
            options = SampleData.languages,
            selectedOption = selectedLanguage,
            onOptionSelected = { selectedLanguage = it },
            onDismiss = { showLanguagePicker = false },
            isDarkMode = themeManager.isDarkMode
        )
    }

    // Currency Picker Dialog
    if (showCurrencyPicker) {
        PickerDialog(
            title = "Currency",
            options = SampleData.currencies,
            selectedOption = selectedCurrency,
            onOptionSelected = { selectedCurrency = it },
            onDismiss = { showCurrencyPicker = false },
            isDarkMode = themeManager.isDarkMode
        )
    }
}

@Composable
fun HeaderSection(isDarkMode: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            (if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent).copy(alpha = 0.2f),
                            (if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent).copy(alpha = 0.2f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent,
                modifier = Modifier.size(50.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "John Doe",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
        )
        
        Text(
            text = "john.doe@example.com",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
        )
    }
}

@Composable
fun SettingsSection(
    title: String,
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
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
            content()
        }
    }
}

@Composable
fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDarkMode: Boolean,
    textColor: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = (if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent).copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent,
                modifier = Modifier.size(18.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (textColor != Color.Unspecified) textColor else {
                    if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
                }
            )
            
            Text(
                text = subtitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
            )
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
            modifier = Modifier.size(14.dp)
        )
    }
    
    Divider(
        color = if (isDarkMode) LuxorColors.Dark.border else LuxorColors.Light.border,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun SettingsToggleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isOn: Boolean,
    onToggle: () -> Unit,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = (if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent).copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent,
                modifier = Modifier.size(18.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
            )
            
            Text(
                text = subtitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
            )
        }
        
        Switch(
            checked = isOn,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray
            )
        )
    }
    
    Divider(
        color = if (isDarkMode) LuxorColors.Dark.border else LuxorColors.Light.border,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun SettingsPickerRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    selection: String,
    onSelectionChange: (String) -> Unit,
    onShowPicker: () -> Unit,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onShowPicker() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = (if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent).copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent,
                modifier = Modifier.size(18.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
            )
            
            Text(
                text = subtitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
            )
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selection,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Select",
                tint = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                modifier = Modifier.size(14.dp)
            )
        }
    }
    
    Divider(
        color = if (isDarkMode) LuxorColors.Dark.border else LuxorColors.Light.border,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun PickerDialog(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    isDarkMode: Boolean
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkMode) LuxorColors.Dark.cardBackground else LuxorColors.Light.cardBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
                    )
                    
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Done",
                            color = if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LazyColumn {
                    items(options) { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onOptionSelected(option)
                                    onDismiss()
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            
                            if (selectedOption == option) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = if (isDarkMode) LuxorColors.Dark.accent else LuxorColors.Light.accent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        
                        if (option != options.lastOrNull()) {
                            Divider(
                                color = if (isDarkMode) LuxorColors.Dark.border else LuxorColors.Light.border,
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
}
