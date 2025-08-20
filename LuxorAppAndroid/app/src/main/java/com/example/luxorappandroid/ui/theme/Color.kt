package com.example.luxorappandroid.ui.theme

import androidx.compose.ui.graphics.Color

// MARK: - Historical Luxor Colors
object LuxorColors {
    val pureGold = Color(0xFFFFD700.toULong())
    val richGold = Color(0xFFD9A441.toULong())
    val deepGold = Color(0xFFB8860B.toULong())
    val bronzeGold = Color(0xFFCC8020.toULong())

    val egyptianBlue = Color(0xFF1052BA.toULong())
    val papyrusBeige = Color(0xFFF5DEB3.toULong())
    val sandstone = Color(0xFFEDD5AE.toULong())
    val hieroglyphBrown = Color(0xFF664228.toULong())
    val luxorTeal = egyptianBlue
    val luxorGold = pureGold
    val luxorDarkTeal = Color(0xFF0A3D8C.toULong())

    object Light {
        val background = Color(0xFFFAF2E1.toULong()) // Warm papyrus background
        val secondaryBackground = papyrusBeige
        val textPrimary = hieroglyphBrown
        val textSecondary = Color(0xFF80664D.toULong())
        val cardBackground = Color(0xFFFDF6E6.toULong())
        val shadow = hieroglyphBrown.copy(alpha = 0.15f)
        val accent = pureGold
        val border = richGold.copy(alpha = 0.3f)
    }

    object Dark {
        val background = Color(0xFF14100A.toULong())
        val secondaryBackground = Color(0xFF1F1A14.toULong())
        val textPrimary = pureGold
        val textSecondary = Color(0xFFD9C08C.toULong())
        val cardBackground = Color(0xFF261E14.toULong())
        val shadow = Color.Black.copy(alpha = 0.6f)
        val accent = richGold
        val border = deepGold.copy(alpha = 0.4f)
        val inputBackground = Color(0xFF2E251A.toULong())
    }
}

val Purple80 = Color(0xFFD0BCFF.toULong())
val PurpleGrey80 = Color(0xFFCCC2DC.toULong())
val Pink80 = Color(0xFFEFB8C8.toULong())

val Purple40 = Color(0xFF6650A4.toULong())
val PurpleGrey40 = Color(0xFF625B71.toULong())
val Pink40 = Color(0xFF7D5260.toULong())
