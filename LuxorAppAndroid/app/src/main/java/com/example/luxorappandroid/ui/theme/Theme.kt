package com.example.luxorappandroid.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Custom Luxor color schemes with historical Egyptian colors
private val LuxorLightColorScheme = lightColorScheme(
    primary = LuxorColors.luxorTeal,
    secondary = LuxorColors.luxorGold,
    tertiary = LuxorColors.luxorDarkTeal,
    background = LuxorColors.Light.background,
    surface = LuxorColors.Light.cardBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = LuxorColors.Light.textPrimary,
    onSurface = LuxorColors.Light.textPrimary,
    surfaceVariant = LuxorColors.Light.secondaryBackground,
    onSurfaceVariant = LuxorColors.Light.textSecondary,
    primaryContainer = LuxorColors.pureGold.copy(alpha = 0.2f),
    onPrimaryContainer = LuxorColors.hieroglyphBrown,
    secondaryContainer = LuxorColors.papyrusBeige,
    onSecondaryContainer = LuxorColors.hieroglyphBrown
)

private val LuxorDarkColorScheme = darkColorScheme(
    primary = LuxorColors.luxorTeal,
    secondary = LuxorColors.luxorGold,
    tertiary = LuxorColors.luxorDarkTeal,
    background = LuxorColors.Dark.background,
    surface = LuxorColors.Dark.cardBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = LuxorColors.Dark.textPrimary,
    onSurface = LuxorColors.Dark.textPrimary,
    surfaceVariant = LuxorColors.Dark.secondaryBackground,
    onSurfaceVariant = LuxorColors.Dark.textSecondary,
    primaryContainer = LuxorColors.richGold.copy(alpha = 0.3f),
    onPrimaryContainer = LuxorColors.pureGold,
    secondaryContainer = LuxorColors.Dark.inputBackground,
    onSecondaryContainer = LuxorColors.Dark.textPrimary
)

// Legacy color schemes for compatibility
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun LuxorAppAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to use custom Luxor colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> LuxorDarkColorScheme
        else -> LuxorLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}