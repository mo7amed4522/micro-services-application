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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// Composition local for accessing current theme colors
val LocalLuxorColors = staticCompositionLocalOf { LuxorColors.Light }

private val LuxorDarkColorScheme = darkColorScheme(
    primary = LuxorColors.luxorTeal,
    onPrimary = Color.White,
    primaryContainer = LuxorColors.luxorTeal.copy(alpha = 0.2f),
    onPrimaryContainer = LuxorColors.luxorTeal,
    secondary = LuxorColors.richGold,
    onSecondary = LuxorColors.Dark.textPrimary,
    secondaryContainer = LuxorColors.richGold.copy(alpha = 0.2f),
    onSecondaryContainer = LuxorColors.richGold,
    tertiary = LuxorColors.pureGold,
    onTertiary = LuxorColors.hieroglyphBrown,
    tertiaryContainer = LuxorColors.pureGold.copy(alpha = 0.2f),
    onTertiaryContainer = LuxorColors.pureGold,
    background = LuxorColors.Dark.background,
    onBackground = LuxorColors.Dark.textPrimary,
    surface = LuxorColors.Dark.cardBackground,
    onSurface = LuxorColors.Dark.textPrimary,
    surfaceVariant = LuxorColors.Dark.secondaryBackground,
    onSurfaceVariant = LuxorColors.Dark.textSecondary,
    surfaceTint = LuxorColors.luxorTeal,
    inverseSurface = LuxorColors.Light.cardBackground,
    inverseOnSurface = LuxorColors.Light.textPrimary,
    inversePrimary = LuxorColors.luxorTeal,
    outline = LuxorColors.Dark.inputBorder,
    outlineVariant = LuxorColors.Dark.border.copy(alpha = 0.2f),
    scrim = Color.Black.copy(alpha = 0.5f),
    error = LuxorColors.errorRed,
    onError = Color.White,
    errorContainer = LuxorColors.errorRed.copy(alpha = 0.2f),
    onErrorContainer = LuxorColors.errorRed
)

private val LuxorLightColorScheme = lightColorScheme(
    primary = LuxorColors.luxorTeal,
    onPrimary = Color.White,
    primaryContainer = LuxorColors.luxorTeal.copy(alpha = 0.1f),
    onPrimaryContainer = LuxorColors.luxorTeal,
    secondary = LuxorColors.richGold,
    onSecondary = LuxorColors.Light.textPrimary,
    secondaryContainer = LuxorColors.richGold.copy(alpha = 0.1f),
    onSecondaryContainer = LuxorColors.richGold,
    tertiary = LuxorColors.pureGold,
    onTertiary = LuxorColors.hieroglyphBrown,
    tertiaryContainer = LuxorColors.pureGold.copy(alpha = 0.1f),
    onTertiaryContainer = LuxorColors.pureGold,
    background = LuxorColors.Light.background,
    onBackground = LuxorColors.Light.textPrimary,
    surface = LuxorColors.Light.cardBackground,
    onSurface = LuxorColors.Light.textPrimary,
    surfaceVariant = LuxorColors.Light.secondaryBackground,
    onSurfaceVariant = LuxorColors.Light.textSecondary,
    surfaceTint = LuxorColors.luxorTeal,
    inverseSurface = LuxorColors.Dark.cardBackground,
    inverseOnSurface = LuxorColors.Dark.textPrimary,
    inversePrimary = LuxorColors.luxorTeal,
    outline = LuxorColors.Light.inputBorder,
    outlineVariant = LuxorColors.Light.border.copy(alpha = 0.2f),
    scrim = Color.Black.copy(alpha = 0.5f),
    error = LuxorColors.errorRed,
    onError = Color.White,
    errorContainer = LuxorColors.errorRed.copy(alpha = 0.1f),
    onErrorContainer = LuxorColors.errorRed
)

@Composable
fun LuxorAppAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+ but we prefer our custom theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                // Blend dynamic colors with our custom dark scheme
                val dynamicScheme = dynamicDarkColorScheme(context)
                LuxorDarkColorScheme.copy(
                    primary = dynamicScheme.primary,
                    primaryContainer = dynamicScheme.primaryContainer,
                    secondary = dynamicScheme.secondary,
                    secondaryContainer = dynamicScheme.secondaryContainer
                )
            } else {
                // Blend dynamic colors with our custom light scheme
                val dynamicScheme = dynamicLightColorScheme(context)
                LuxorLightColorScheme.copy(
                    primary = dynamicScheme.primary,
                    primaryContainer = dynamicScheme.primaryContainer,
                    secondary = dynamicScheme.secondary,
                    secondaryContainer = dynamicScheme.secondaryContainer
                )
            }
        }
        darkTheme -> LuxorDarkColorScheme
        else -> LuxorLightColorScheme
    }
    
    // Provide current theme colors through composition local
    val luxorColors = if (darkTheme) LuxorColors.Dark else LuxorColors.Light
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
    
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = colorScheme.background,
            darkIcons = !darkTheme
        )
        systemUiController.setNavigationBarColor(
            color = colorScheme.background,
            darkIcons = !darkTheme
        )
    }

    CompositionLocalProvider(LocalLuxorColors provides luxorColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            // typography = Typography,
            content = content
        )
    }
}