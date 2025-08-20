package com.example.luxorappandroid.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.R
import com.example.luxorappandroid.ui.components.LuxorButton
import com.example.luxorappandroid.ui.components.LuxorLogo
import com.example.luxorappandroid.ui.theme.LuxorColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    var selectedLanguage by remember { mutableStateOf("English") }
    var showLanguageDialog by remember { mutableStateOf(false) }
    
    val languages = listOf(
        "English",
        "العربية", // Arabic
        "Français", // French
        "Deutsch", // German
        "Español", // Spanish
        "Italiano", // Italian
        "中文", // Chinese
        "日本語", // Japanese
        "Русский" // Russian
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LuxorColors.current.background,
                        LuxorColors.current.surface,
                        LuxorColors.current.background
                    )
                )
            )
    ) {
        // Language selector at top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = { showLanguageDialog = true },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = LuxorColors.current.primary
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            LuxorColors.current.primary,
                            LuxorColors.current.secondary
                        )
                    )
                ),
                modifier = Modifier.height(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Language",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = selectedLanguage,
                    fontSize = 14.sp
                )
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isTablet) 64.dp else 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(if (isTablet) 80.dp else 40.dp))
            
            // Luxor Logo
            LuxorLogo(
                size = if (isTablet) 200.dp else 150.dp,
                animate = true
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Welcome Text
            Text(
                text = "Welcome to Luxor",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isTablet) 36.sp else 28.sp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            LuxorColors.current.primary,
                            LuxorColors.current.secondary,
                            LuxorColors.current.tertiary
                        )
                    )
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Subtitle
            Text(
                text = "Discover the ancient wonders of Egypt\nExplore temples, tombs, and treasures",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = if (isTablet) 18.sp else 16.sp,
                    color = LuxorColors.current.onSurface.copy(alpha = 0.8f)
                ),
                textAlign = TextAlign.Center,
                lineHeight = if (isTablet) 26.sp else 24.sp
            )
            
            Spacer(modifier = Modifier.height(if (isTablet) 64.dp else 48.dp))
            
            // Get Started Button
            LuxorButton(
                text = "Get Started",
                onClick = onGetStartedClick,
                modifier = Modifier
                    .fillMaxWidth(if (isTablet) 0.6f else 1f)
                    .height(56.dp),
                style = com.example.luxorappandroid.ui.components.LuxorButtonStyle.PRIMARY
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Sign In Button
            LuxorButton(
                text = "Already have an account? Sign In",
                onClick = onSignInClick,
                modifier = Modifier
                    .fillMaxWidth(if (isTablet) 0.6f else 1f)
                    .height(56.dp),
                style = com.example.luxorappandroid.ui.components.LuxorButtonStyle.TEXT
            )
            
            Spacer(modifier = Modifier.height(if (isTablet) 80.dp else 40.dp))
            
            // Features Preview
            Card(
                modifier = Modifier
                    .fillMaxWidth(if (isTablet) 0.8f else 1f)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = LuxorColors.current.surface.copy(alpha = 0.8f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "What awaits you",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = LuxorColors.current.primary
                        ),
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val features = listOf(
                        "🏛️ Explore ancient temples and monuments",
                        "🗺️ Interactive maps and guided tours",
                        "💬 Connect with fellow travelers",
                        "⚙️ Personalized experience settings"
                    )
                    
                    features.forEach { feature ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = feature,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = LuxorColors.current.onSurface
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
    
    // Language Selection Dialog
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = {
                Text(
                    text = "Select Language",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = LuxorColors.current.primary
                    )
                )
            },
            text = {
                LazyColumn {
                    items(languages) { language ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedLanguage == language,
                                onClick = {
                                    selectedLanguage = language
                                    showLanguageDialog = false
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = if (isSystemInDarkTheme()) LuxorColors.Dark.primary else LuxorColors.Light.primary
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = language,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showLanguageDialog = false }
                ) {
                    Text(
                        text = "Cancel",
                        color = if (isSystemInDarkTheme()) LuxorColors.Dark.primary else LuxorColors.Light.primary
                    )
                }
            },
            containerColor = if (isSystemInDarkTheme()) LuxorColors.Dark.surface else LuxorColors.Light.surface,
            tonalElevation = 8.dp
        )
    }
}