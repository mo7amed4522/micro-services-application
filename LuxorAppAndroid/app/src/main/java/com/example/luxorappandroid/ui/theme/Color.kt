package com.example.luxorappandroid.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Luxor brand colors (from logo)
val LuxorTeal = Color(0xFF008080)
val LuxorGold = Color(0xFFCC9933)
val LuxorDarkTeal = Color(0xFF004D4D)

// Light mode colors
object LightColors {
    val Background = Color.White
    val SecondaryBackground = Color(0xFFF2F2F7)
    val TextPrimary = Color.Black
    val TextSecondary = Color.Gray
    val CardBackground = Color.White
    val Shadow = Color.Black.copy(alpha = 0.1f)
    val Border = Color.Gray.copy(alpha = 0.2f)
    val InputBackground = Color.White
}

// Enhanced Dark mode colors - Much more visible!
object DarkColors {
    val Background = Color(0xFF141420) // Dark blue-gray instead of pure black
    val SecondaryBackground = Color(0xFF1E1E2E) // Lighter secondary
    val TextPrimary = Color.White
    val TextSecondary = Color(0xFFCCCCD9) // Much lighter gray
    val CardBackground = Color(0xFF262636) // Visible card background
    val Shadow = Color.Black.copy(alpha = 0.4f) // Stronger shadow for depth
    val Border = Color(0xFF404058) // Visible borders
    val InputBackground = Color(0xFF2E2E40) // Input field background
}

// Legacy colors for compatibility
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)