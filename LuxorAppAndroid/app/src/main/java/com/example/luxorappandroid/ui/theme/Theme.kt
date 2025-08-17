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

// Custom Luxor color schemes
private val LuxorLightColorScheme = lightColorScheme(
    primary = LuxorTeal,
    secondary = LuxorGold,
    tertiary = LuxorDarkTeal,
    background = LightColors.Background,
    surface = LightColors.CardBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = LightColors.TextPrimary,
    onSurface = LightColors.TextPrimary,
    surfaceVariant = LightColors.SecondaryBackground,
    onSurfaceVariant = LightColors.TextSecondary
)

private val LuxorDarkColorScheme = darkColorScheme(
    primary = LuxorTeal,
    secondary = LuxorGold,
    tertiary = LuxorDarkTeal,
    background = DarkColors.Background,
    surface = DarkColors.CardBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = DarkColors.TextPrimary,
    onSurface = DarkColors.TextPrimary,
    surfaceVariant = DarkColors.SecondaryBackground,
    onSurfaceVariant = DarkColors.TextSecondary
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