package com.example.luxorappandroid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.theme.LuxorColors

@Composable
fun SocialLoginButtons(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onAppleClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        GoogleLoginButton(
            onClick = onGoogleClick,
            enabled = !isLoading
        )
        
        FacebookLoginButton(
            onClick = onFacebookClick,
            enabled = !isLoading
        )
        
        AppleLoginButton(
            onClick = onAppleClick,
            enabled = !isLoading
        )
    }
}

@Composable
fun GoogleLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    SocialLoginButton(
        text = "Continue with Google",
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        backgroundColor = Color.White,
        contentColor = Color.Black,
        borderColor = Color.Gray.copy(alpha = 0.3f),
        icon = {
            // Google icon placeholder - in a real app, you'd use the actual Google icon
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        Color.Red,
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
fun FacebookLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    SocialLoginButton(
        text = "Continue with Facebook",
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        backgroundColor = Color(0xFF1877F2),
        contentColor = Color.White,
        borderColor = Color.Transparent,
        icon = {
            // Facebook icon placeholder - in a real app, you'd use the actual Facebook icon
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        Color.White,
                        RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "f",
                    color = Color(0xFF1877F2),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
fun AppleLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val isDarkTheme = isSystemInDarkTheme()
    
    SocialLoginButton(
        text = "Continue with Apple",
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        backgroundColor = if (isDarkTheme) Color.White else Color.Black,
        contentColor = if (isDarkTheme) Color.Black else Color.White,
        borderColor = Color.Transparent,
        icon = {
            // Apple icon placeholder - in a real app, you'd use the actual Apple icon
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        if (isDarkTheme) Color.Black else Color.White,
                        RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "", // Apple symbol
                    color = if (isDarkTheme) Color.White else Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
fun EmailLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val isDarkTheme = isSystemInDarkTheme()
    
    SocialLoginButton(
        text = "Continue with Email",
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        backgroundColor = if (isDarkTheme) LuxorColors.Dark.cardBackground else LuxorColors.Light.cardBackground,
        contentColor = LuxorColors.pureGold,
        borderColor = LuxorColors.pureGold,
        icon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = LuxorColors.pureGold,
                modifier = Modifier.size(20.dp)
            )
        }
    )
}

@Composable
private fun SocialLoginButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color,
    contentColor: Color,
    borderColor: Color,
    icon: @Composable () -> Unit
) {
    val finalBackgroundColor = if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.6f)
    val finalContentColor = if (enabled) contentColor else contentColor.copy(alpha = 0.6f)
    val finalBorderColor = if (enabled) borderColor else borderColor.copy(alpha = 0.6f)
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(finalBackgroundColor)
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(
                        width = 1.dp,
                        color = finalBorderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else Modifier
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            icon()
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = finalContentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SocialLoginDivider(
    modifier: Modifier = Modifier,
    text: String = "or"
) {
    val isDarkTheme = isSystemInDarkTheme()
    
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            color = if (isDarkTheme) LuxorColors.Dark.border else LuxorColors.Light.border,
            thickness = 1.dp
        )
        
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        
        Divider(
            modifier = Modifier.weight(1f),
            color = if (isDarkTheme) LuxorColors.Dark.border else LuxorColors.Light.border,
            thickness = 1.dp
        )
    }
}

@Composable
fun CompactSocialLoginButtons(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onAppleClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CompactSocialButton(
            onClick = onGoogleClick,
            enabled = !isLoading,
            modifier = Modifier.weight(1f),
            backgroundColor = Color.White,
            borderColor = Color.Gray.copy(alpha = 0.3f)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        Color.Red,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        CompactSocialButton(
            onClick = onFacebookClick,
            enabled = !isLoading,
            modifier = Modifier.weight(1f),
            backgroundColor = Color(0xFF1877F2),
            borderColor = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        Color.White,
                        RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "f",
                    color = Color(0xFF1877F2),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        CompactSocialButton(
            onClick = onAppleClick,
            enabled = !isLoading,
            modifier = Modifier.weight(1f),
            backgroundColor = Color.Black,
            borderColor = Color.Transparent
        ) {
            Text(
                text = "",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CompactSocialButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    borderColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.6f))
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(
                        width = 1.dp,
                        color = if (enabled) borderColor else borderColor.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp)
                    )
                } else Modifier
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}