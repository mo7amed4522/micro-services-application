package com.example.luxorappandroid.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.theme.*

@Composable
fun LuxorEmailTextField(
    email: String,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false
) {
    var isEditing by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(true) }
    
    val colors = if (isDarkMode) DarkColors else LightColors
    
    val borderGradient = when {
        isEditing -> Brush.linearGradient(listOf(LuxorTeal, LuxorGold))
        !isValid -> Brush.linearGradient(listOf(Color.Red.copy(alpha = 0.6f), Color.Red.copy(alpha = 0.3f)))
        else -> Brush.linearGradient(listOf(colors.Border, colors.Border.copy(alpha = 0.5f)))
    }
    
    val iconColor = when {
        isEditing -> LuxorTeal
        !isValid -> Color.Red
        else -> colors.TextSecondary
    }
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = if (isDarkMode) {
                            listOf(colors.InputBackground, colors.CardBackground)
                        } else {
                            listOf(Color.White, Color(0xFFF2F2F7))
                        }
                    )
                )
                .border(
                    width = if (isEditing) 2.dp else 1.dp,
                    brush = borderGradient,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { isEditing = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            brush = if (isEditing) {
                                Brush.linearGradient(
                                    listOf(
                                        LuxorTeal.copy(alpha = 0.2f),
                                        LuxorGold.copy(alpha = 0.2f)
                                    )
                                )
                            } else {
                                Brush.linearGradient(listOf(Color.Transparent))
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isEditing) {
                            Icons.Default.Email
                        } else {
                            Icons.Default.Email
                        },
                        contentDescription = "Email",
                        tint = iconColor,
                        modifier = Modifier
                            .size(18.dp)
                            .scale(if (isEditing) 1.1f else 1.0f)
                    )
                }
                
                // Text field
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Email Address",
                        fontSize = if (isEditing || email.isNotEmpty()) 11.sp else 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = iconColor,
                        modifier = Modifier.offset(y = if (isEditing || email.isNotEmpty()) (-8).dp else 8.dp)
                    )
                    
                    TextField(
                        value = email,
                        onValueChange = { 
                            onEmailChange(it)
                            validateEmail(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isEditing = it.isFocused },
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.TextPrimary
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }
                
                // Validation icon
                if (email.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = if (isValid) {
                                        listOf(Color.Green.copy(alpha = 0.2f), Color.Green.copy(alpha = 0.1f))
                                    } else {
                                        listOf(Color.Red.copy(alpha = 0.2f), Color.Red.copy(alpha = 0.1f))
                                    }
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isValid) {
                                Icons.Default.CheckCircle
                            } else {
                                Icons.Default.Cancel
                            },
                            contentDescription = "Validation",
                            tint = if (isValid) Color.Green else Color.Red,
                            modifier = Modifier
                                .size(16.dp)
                                .scale(if (isValid) 1.1f else 1.0f)
                        )
                    }
                }
            }
        }
        
        // Error message
        if (!isValid && email.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "Please enter a valid email address",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Red
                )
            }
        }
    }
    
    fun validateEmail(email: String) {
        val emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}".toRegex()
        isValid = email.isEmpty() || email.matches(emailRegex)
    }
}

@Composable
fun LuxorPasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false
) {
    var isEditing by remember { mutableStateOf(false) }
    var isSecure by remember { mutableStateOf(true) }
    var showValidation by remember { mutableStateOf(false) }
    
    val colors = if (isDarkMode) DarkColors else LightColors
    
    val borderGradient = when {
        isEditing -> Brush.linearGradient(listOf(LuxorTeal, LuxorGold))
        else -> Brush.linearGradient(listOf(colors.Border, colors.Border.copy(alpha = 0.5f)))
    }
    
    val iconColor = when {
        isEditing -> LuxorTeal
        else -> colors.TextSecondary
    }
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = if (isDarkMode) {
                            listOf(Color.White.copy(alpha = 0.05f), Color.White.copy(alpha = 0.02f))
                        } else {
                            listOf(Color.White, Color(0xFFF2F2F7))
                        }
                    )
                )
                .border(
                    width = if (isEditing) 2.dp else 1.dp,
                    brush = borderGradient,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { isEditing = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            brush = if (isEditing) {
                                Brush.linearGradient(
                                    listOf(
                                        LuxorTeal.copy(alpha = 0.2f),
                                        LuxorGold.copy(alpha = 0.2f)
                                    )
                                )
                            } else {
                                Brush.linearGradient(listOf(Color.Transparent))
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isEditing) {
                            Icons.Default.LockOpen
                        } else {
                            Icons.Default.Lock
                        },
                        contentDescription = "Password",
                        tint = iconColor,
                        modifier = Modifier
                            .size(18.dp)
                            .scale(if (isEditing) 1.1f else 1.0f)
                    )
                }
                
                // Text field
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Password",
                        fontSize = if (isEditing || password.isNotEmpty()) 11.sp else 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = iconColor,
                        modifier = Modifier.offset(y = if (isEditing || password.isNotEmpty()) (-8).dp else 8.dp)
                    )
                    
                    TextField(
                        value = password,
                        onValueChange = { 
                            onPasswordChange(it)
                            showValidation = it.isNotEmpty()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isEditing = it.isFocused },
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.TextPrimary
                        ),
                        visualTransformation = if (isSecure) PasswordVisualTransformation() else VisualTransformation.None,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }
                
                // Eye toggle
                IconButton(
                    onClick = { isSecure = !isSecure }
                ) {
                    Icon(
                        imageVector = if (isSecure) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = "Toggle password visibility",
                        tint = colors.TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        
        // Password validation
        if (showValidation) {
            PasswordValidationSection(password = password, isDarkMode = isDarkMode)
        }
    }
}

@Composable
private fun PasswordValidationSection(
    password: String,
    isDarkMode: Boolean
) {
    val colors = if (isDarkMode) DarkColors else LightColors
    
    val hasCapitalLetter = password.any { it.isUpperCase() }
    val hasMinimumLength = password.length >= 8
    val hasSpecialCharacter = password.any { !it.isLetterOrDigit() }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = if (isDarkMode) {
                        listOf(Color.White.copy(alpha = 0.03f), Color.White.copy(alpha = 0.01f))
                    } else {
                        listOf(Color(0xFFF2F2F7), Color.White.copy(alpha = 0.8f))
                    }
                )
            )
            .border(
                width = 1.dp,
                color = colors.Border,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Password Requirements",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.TextPrimary
        )
        
        ValidationRow("At least one capital letter", hasCapitalLetter, isDarkMode)
        ValidationRow("At least 8 characters", hasMinimumLength, isDarkMode)
        ValidationRow("At least one special character", hasSpecialCharacter, isDarkMode)
    }
}

@Composable
private fun ValidationRow(
    text: String,
    isValid: Boolean,
    isDarkMode: Boolean
) {
    val colors = if (isDarkMode) DarkColors else LightColors
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(
                    brush = if (isValid) {
                        Brush.linearGradient(listOf(Color.Green, Color.Green.copy(alpha = 0.8f)))
                    } else {
                        Brush.linearGradient(
                            listOf(
                                colors.TextSecondary.copy(alpha = 0.2f),
                                colors.TextSecondary.copy(alpha = 0.1f)
                            )
                        )
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isValid) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Valid",
                    tint = Color.White,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
        
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (isValid) Color.Green else colors.TextSecondary
        )
    }
}
