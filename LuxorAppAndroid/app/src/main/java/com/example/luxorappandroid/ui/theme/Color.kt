package com.example.luxorappandroid.ui.theme

import androidx.compose.ui.graphics.Color

// MARK: - Historical Luxor Colors
object LuxorColors {
    val pureGold = Color(0xFFFFD700)
    val richGold = Color(0xFFD9A441)
    val deepGold = Color(0xFFB8860B)
    val bronzeGold = Color(0xFFCC8020)

    val egyptianBlue = Color(0xFF1052BA)
    val papyrusBeige = Color(0xFFF5DEB3)
    val sandstone = Color(0xFFEDD5AE)
    val hieroglyphBrown = Color(0xFF664228)
    val luxorTeal = egyptianBlue
    val luxorGold = pureGold
    val luxorDarkTeal = Color(0xFF0A3D8C)

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

    object Dark {
        val background = Color(0xFF14100A)
        val secondaryBackground = Color(0xFF1F1A14)
        val textPrimary = pureGold
        val textSecondary = Color(0xFFD9C08C)
        val cardBackground = Color(0xFF261E14)
        val shadow = Color.Black.copy(alpha = 0.6f)
        val accent = richGold
        val border = deepGold.copy(alpha = 0.4f)
        val inputBackground = Color(0xFF2E251A)
    }
}

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650A4)
val PurpleGrey40 = Color(0xFF625B71)
val Pink40 = Color(0xFF7D5260)
