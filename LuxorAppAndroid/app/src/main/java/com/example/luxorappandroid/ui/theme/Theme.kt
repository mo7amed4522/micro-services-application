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
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LuxorDarkColorScheme = darkColorScheme(
    primary = LuxorColors.luxorTeal,
    onPrimary = Color.White,
    secondary = LuxorColors.richGold,
    onSecondary = LuxorColors.Dark.textPrimary,
    tertiary = LuxorColors.pureGold,
    onTertiary = LuxorColors.hieroglyphBrown,
    background = LuxorColors.Dark.background,
    onBackground = LuxorColors.Dark.textPrimary,
    surface = LuxorColors.Dark.cardBackground,
    onSurface = LuxorColors.Dark.textPrimary,
    surfaceVariant = LuxorColors.Dark.secondaryBackground,
    onSurfaceVariant = LuxorColors.Dark.textSecondary,
    outline = LuxorColors.Dark.border,
    outlineVariant = LuxorColors.Dark.border.copy(alpha = 0.2f)
)

private val LuxorLightColorScheme = lightColorScheme(
    primary = LuxorColors.luxorTeal,
    onPrimary = Color.White,
    secondary = LuxorColors.richGold,
    onSecondary = LuxorColors.Light.textPrimary,
    tertiary = LuxorColors.pureGold,
    onTertiary = LuxorColors.hieroglyphBrown,
    background = LuxorColors.Light.background,
    onBackground = LuxorColors.Light.textPrimary,
    surface = LuxorColors.Light.cardBackground,
    onSurface = LuxorColors.Light.textPrimary,
    surfaceVariant = LuxorColors.Light.secondaryBackground,
    onSurfaceVariant = LuxorColors.Light.textSecondary,
    outline = LuxorColors.Light.border,
    outlineVariant = LuxorColors.Light.border.copy(alpha = 0.2f)
)

@Composable
fun LuxorAppAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+ but we'll use our custom theme
    dynamicColor: Boolean = false,
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
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = colorScheme.background,
            darkIcons = !darkTheme
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
       //typography = Typography,
        content = content
    )
}