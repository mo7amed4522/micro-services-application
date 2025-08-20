package com.example.luxorappandroid.ui.theme

import androidx.compose.ui.graphics.Color

// MARK: - Historical Luxor Colors
object LuxorColors {
    // Pure Gold (Mica Powder) - Primary historical colors
    val pureGold = Color(0xFFFFD700) // Pure gold
    val richGold = Color(0xFFD9A441) // Rich gold
    val deepGold = Color(0xFFB8860B) // Deep gold
    val bronzeGold = Color(0xFFCC8020) // Bronze gold
    
    // Historical Egyptian colors
    val egyptianBlue = Color(0xFF1052BA) // Lapis lazuli blue
    val papyrusBeige = Color(0xFFF5DEB3) // Papyrus color
    val sandstone = Color(0xFFEDD5AE) // Sandstone
    val hieroglyphBrown = Color(0xFF664228) // Dark brown
    
    // Legacy colors (keeping for compatibility)
    val luxorTeal = egyptianBlue
    val luxorGold = pureGold
    val luxorDarkTeal = Color(0xFF0A3D8C)
    
    // Light mode colors - Historical theme
    object Light {
        val background = Color(0xFFFAF2E1) // Warm papyrus background
        val secondaryBackground = papyrusBeige
        val textPrimary = hieroglyphBrown
        val textSecondary = Color(0xFF80664D)
        val cardBackground = Color(0xFFFDF6E6)
        val shadow = hieroglyphBrown.copy(alpha = 0.15f)
        val accent = pureGold
        val border = richGold.copy(alpha = 0.3f)
    }
    
    // Dark mode colors - Historical theme
    object Dark {
        val background = Color(0xFF14100A) // Dark stone
        val secondaryBackground = Color(0xFF1F1A14)
        val textPrimary = pureGold
        val textSecondary = Color(0xFFD9C08C) // Warm gold text
        val cardBackground = Color(0xFF261E14)
        val shadow = Color.Black.copy(alpha = 0.6f)
        val accent = richGold
        val border = deepGold.copy(alpha = 0.4f)
        val inputBackground = Color(0xFF2E251A)
    }
}

// Legacy colors for compatibility
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)