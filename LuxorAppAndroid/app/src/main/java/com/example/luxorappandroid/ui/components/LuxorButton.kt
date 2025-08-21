package com.example.luxorappandroid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.theme.LuxorColors


enum class LuxorButtonStyle {
    PRIMARY,
    SECONDARY,
    OUTLINE,
    TEXT
}

@Composable
fun LuxorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: LuxorButtonStyle = LuxorButtonStyle.PRIMARY,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isLoading: Boolean = false
) {
    val isDarkTheme = isSystemInDarkTheme()
    
    val (backgroundColor, contentColor, borderColor) = when (style) {
        LuxorButtonStyle.PRIMARY -> {
            if (isDarkTheme) {
                Triple(
                    LuxorColors.pureGold,
                    LuxorColors.egyptianBlue,
                    Color.Transparent
                )
            } else {
                Triple(
                    LuxorColors.pureGold,
                    LuxorColors.egyptianBlue,
                    Color.Transparent
                )
            }
        }
        LuxorButtonStyle.SECONDARY -> {
            if (isDarkTheme) {
                Triple(
                    LuxorColors.Dark.cardBackground,
                    LuxorColors.Dark.textPrimary,
                    LuxorColors.Dark.border
                )
            } else {
                Triple(
                    LuxorColors.Light.cardBackground,
                    LuxorColors.Light.textPrimary,
                    LuxorColors.Light.border
                )
            }
        }
        LuxorButtonStyle.OUTLINE -> {
            if (isDarkTheme) {
                Triple(
                    Color.Transparent,
                    LuxorColors.pureGold,
                    LuxorColors.pureGold
                )
            } else {
                Triple(
                    Color.Transparent,
                    LuxorColors.pureGold,
                    LuxorColors.pureGold
                )
            }
        }
        LuxorButtonStyle.TEXT -> {
            if (isDarkTheme) {
                Triple(
                    Color.Transparent,
                    LuxorColors.pureGold,
                    Color.Transparent
                )
            } else {
                Triple(
                    Color.Transparent,
                    LuxorColors.pureGold,
                    Color.Transparent
                )
            }
        }
    }
    
    val finalBackgroundColor = if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.6f)
    val finalContentColor = if (enabled) contentColor else contentColor.copy(alpha = 0.6f)
    val finalBorderColor = if (enabled) borderColor else borderColor.copy(alpha = 0.6f)
    
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (style == LuxorButtonStyle.OUTLINE) {
                    Modifier.border(
                        width = 2.dp,
                        color = finalBorderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else Modifier
            ),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = finalBackgroundColor,
            contentColor = finalContentColor,
            disabledContainerColor = finalBackgroundColor,
            disabledContentColor = finalContentColor
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (style == LuxorButtonStyle.PRIMARY) 4.dp else 0.dp,
            pressedElevation = if (style == LuxorButtonStyle.PRIMARY) 2.dp else 0.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = finalContentColor,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = finalContentColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = finalContentColor,
                    textAlign = TextAlign.Center
                )
                
                trailingIcon?.let { icon ->
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = finalContentColor
                    )
                }
            }
        }
    }
}

@Composable
fun LuxorPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isLoading: Boolean = false
) {
    LuxorButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        style = LuxorButtonStyle.PRIMARY,
        enabled = enabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isLoading = isLoading
    )
}

@Composable
fun LuxorSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isLoading: Boolean = false
) {
    LuxorButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        style = LuxorButtonStyle.SECONDARY,
        enabled = enabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isLoading = isLoading
    )
}

@Composable
fun LuxorOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isLoading: Boolean = false
) {
    LuxorButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        style = LuxorButtonStyle.OUTLINE,
        enabled = enabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isLoading = isLoading
    )
}

@Composable
fun LuxorTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    LuxorButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        style = LuxorButtonStyle.TEXT,
        enabled = enabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isLoading = false
    )
}

@Composable
fun LuxorGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isLoading: Boolean = false
) {
    val isDarkTheme = isSystemInDarkTheme()
    
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            LuxorColors.pureGold,
            LuxorColors.pureGold,
            LuxorColors.pureGold
        )
    )
    
    val contentColor = LuxorColors.egyptianBlue
    val finalContentColor = if (enabled) contentColor else contentColor.copy(alpha = 0.6f)
    
    Box(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (enabled) gradientBrush else Brush.horizontalGradient(
                colors = listOf(
                    LuxorColors.pureGold.copy(alpha = 0.6f),
                    LuxorColors.luxorGold.copy(alpha = 0.6f),
                    LuxorColors.pureGold.copy(alpha = 0.6f)
                )
            ))
            .clickable(enabled = enabled && !isLoading) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = finalContentColor,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = finalContentColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = finalContentColor,
                    textAlign = TextAlign.Center
                )
                
                trailingIcon?.let { icon ->
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = finalContentColor
                    )
                }
            }
        }
    }
}