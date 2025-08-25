package com.example.luxorappandroid.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Historical Egyptian Color Palette - Matching iOS LuxorColors exactly
object LuxorColors {
    // Gold variations - inspired by ancient Egyptian gold work
    val pureGold = Color(0xFFFFD700)        // Pure gold
    val richGold = Color(0xFFDAA520)        // Rich gold  
    val deepGold = Color(0xFFB8860B)        // Deep gold
    val bronzeGold = Color(0xFFCD7F32)      // Bronze gold
    
    // Egyptian blue - the first synthetic pigment
    val egyptianBlue = Color(0xFF1034A6)
    
    // Natural earth tones
    val papyrusBeige = Color(0xFFF5F5DC)    // Papyrus color
    val sandstone = Color(0xFFD2B48C)       // Sandstone
    val hieroglyphBrown = Color(0xFF8B4513)  // Hieroglyph brown
    
    // Legacy colors for compatibility
    val luxorTeal = Color(0xFF008080)
    val luxorGold = Color(0xFFFFD700)
    val luxorDarkTeal = Color(0xFF006666)
    
    // Gradient definitions matching iOS
    object Gradients {
        val goldGradient = Brush.linearGradient(
            colors = listOf(pureGold, richGold, deepGold)
        )
        
        val splashGradient = Brush.radialGradient(
            colors = listOf(hieroglyphBrown, deepGold, pureGold)
        )
        
        val buttonGradient = Brush.linearGradient(
            colors = listOf(luxorTeal, luxorGold)
        )
        
        val cardGradient = Brush.linearGradient(
            colors = listOf(papyrusBeige.copy(alpha = 0.1f), sandstone.copy(alpha = 0.05f))
        )
        
        val textFieldFocusedGradient = Brush.linearGradient(
            colors = listOf(pureGold, richGold, deepGold)
        )
        
        val textFieldEditingGradient = Brush.linearGradient(
            colors = listOf(pureGold.copy(alpha = 0.3f), richGold.copy(alpha = 0.2f))
        )
    }
    
    // Light theme colors - Enhanced to match iOS
    object Light {
        val background = papyrusBeige
        val surface = Color.White
        val cardBackground = Color.White
        val textPrimary = hieroglyphBrown
        val textSecondary = Color(0xFF666666)
        val accent = pureGold
        val border = sandstone.copy(alpha = 0.3f)
        val shadow = Color.Black.copy(alpha = 0.1f)
        val inputBackground = Color.White
        val inputBorder = bronzeGold.copy(alpha = 0.3f)
        val inputFocusedBorder = pureGold
        val inputErrorBorder = Color.Red
        val buttonPrimary = pureGold
        val buttonSecondary = sandstone
        val divider = sandstone.copy(alpha = 0.2f)
    }
    
    // Dark theme colors - Enhanced to match iOS
    object Dark {
        val background = Color(0xFF121212)
        val surface = Color(0xFF1E1E1E)
        val cardBackground = Color(0xFF2D2D2D)
        val textPrimary = papyrusBeige
        val textSecondary = Color(0xFFB0B0B0)
        val accent = pureGold
        val border = Color(0xFF404040)
        val shadow = Color.Black.copy(alpha = 0.3f)
        val inputBackground = Color(0xFF2D2D2D)
        val inputBorder = bronzeGold.copy(alpha = 0.5f)
        val inputFocusedBorder = pureGold
        val inputErrorBorder = Color.Red
        val buttonPrimary = pureGold
        val buttonSecondary = Color(0xFF404040)
        val divider = Color(0xFF404040)
    }
}

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650A4)
val PurpleGrey40 = Color(0xFF625B71)
val Pink40 = Color(0xFF7D5260)
