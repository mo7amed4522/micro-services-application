package com.example.luxorappandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.theme.*

/**
 * Language Selection View that allows users to choose from supported languages
 * Matches the iOS LanguageSelectionView implementation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionView(
    languageManager: LanguageManager,
    themeManager: ThemeManager,
    onLanguageSelected: () -> Unit = {}
) {
    val context = LocalContext.current
    val currentLanguage = languageManager.currentLanguage
    val availableLanguages = languageManager.getAllLanguages()
    
    // Language flag emojis
    val languageFlags = mapOf(
        SupportedLanguage.ENGLISH to "🇺🇸",
        SupportedLanguage.ARABIC to "🇪🇬",
        SupportedLanguage.FRENCH to "🇫🇷",
        SupportedLanguage.CHINESE to "🇨🇳"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (themeManager.isDarkMode) LuxorColors.Dark.background
                else LuxorColors.Light.background
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = LocalizedStrings.selectLanguage(currentLanguage),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = if (themeManager.isDarkMode) LuxorColors.Dark.textPrimary
                   else LuxorColors.Light.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 24.dp)
        )
        
        // Language cards
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(availableLanguages) { language ->
                LanguageCard(
                    language = language,
                    flag = languageFlags[language] ?: "🌐",
                    isSelected = language == currentLanguage,
                    themeManager = themeManager,
                    onClick = {
                        languageManager.setLanguage(language)
                        onLanguageSelected()
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Success message when language changes
        if (currentLanguage != SupportedLanguage.ENGLISH) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (themeManager.isDarkMode) 
                        LuxorColors.richGold.copy(alpha = 0.2f)
                    else LuxorColors.pureGold.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = LocalizedStrings.languageChanged(currentLanguage),
                    modifier = Modifier.padding(16.dp),
                    color = if (themeManager.isDarkMode) LuxorColors.pureGold
                           else LuxorColors.hieroglyphBrown,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun LanguageCard(
    language: SupportedLanguage,
    flag: String,
    isSelected: Boolean,
    themeManager: ThemeManager,
    onClick: () -> Unit
) {
    val cardBackgroundColor = if (themeManager.isDarkMode) {
        if (isSelected) LuxorColors.Dark.cardBackground.copy(alpha = 0.9f)
        else LuxorColors.Dark.cardBackground
    } else {
        if (isSelected) LuxorColors.Light.cardBackground
        else LuxorColors.Light.cardBackground.copy(alpha = 0.7f)
    }
    
    val borderColor = if (isSelected) {
        if (themeManager.isDarkMode) LuxorColors.pureGold
        else LuxorColors.Dark.accent
    } else {
        Color.Transparent
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                } else Modifier
            ),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Flag emoji
                Text(
                    text = flag,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (themeManager.isDarkMode) LuxorColors.Dark.inputBackground
                            else LuxorColors.Light.inputBackground
                        )
                        .wrapContentSize(Alignment.Center)
                )
                
                // Language name
                Text(
                    text = language.displayName,
                    fontSize = 18.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (themeManager.isDarkMode) LuxorColors.Dark.textPrimary
                           else LuxorColors.Light.textPrimary
                )
            }
            
            // Selection indicator
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = if (themeManager.isDarkMode) LuxorColors.pureGold
                           else LuxorColors.Dark.accent,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}