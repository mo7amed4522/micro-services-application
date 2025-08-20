package com.example.luxorappandroid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.theme.*
import com.example.luxorappandroid.data.ThemeManager
import com.example.luxorappandroid.data.LanguageManager

@Composable
fun SocialLoginButtons(
    modifier: Modifier = Modifier,
    themeManager: ThemeManager,
    languageManager: LanguageManager
) {
    val colors = if (themeManager.isDarkMode) LuxorColors.Dark else LuxorColors.Light
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = LocalizedStrings.orContinueWith(languageManager.currentLanguage),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = colors.textSecondary
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SocialButton(
                icon = Icons.Default.Apple,
                color = if (themeManager.isDarkMode) Color.White else Color.Black,
                onClick = { /* Apple Sign In */ },
                themeManager = themeManager
            )
            
            SocialButton(
                icon = Icons.Default.Language,
                color = Color.Red,
                onClick = { /* Google Sign In */ },
                themeManager = themeManager
            )
            
            SocialButton(
                icon = Icons.Default.Facebook,
                color = Color.Blue,
                onClick = { /* Facebook Sign In */ },
                themeManager = themeManager
            )
        }
    }
}

@Composable
private fun SocialButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    themeManager: ThemeManager,
    modifier: Modifier = Modifier
) {
    val colors = if (themeManager.isDarkMode) LuxorColors.Dark else LuxorColors.Light
    
    Box(
        modifier = modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(colors.cardBackground, colors.cardBackground)
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Social login",
            tint = color,
            modifier = Modifier.size(20.dp)
        )
    }
}
