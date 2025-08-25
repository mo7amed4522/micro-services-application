package com.example.luxorappandroid.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.theme.LuxorColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuxorTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorMessage: String = "",
    enabled: Boolean = true
) {
    val isDarkTheme = isSystemInDarkTheme()
    var isFocused by remember { mutableStateOf(false) }
    val hasContent = value.isNotEmpty()
    
    // Animated properties matching iOS behavior
    val animatedScale by animateFloatAsState(
        targetValue = if (isFocused) 1.02f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "scale"
    )
    
    val animatedBorderColor by animateColorAsState(
        targetValue = when {
            isError -> if (isDarkTheme) LuxorColors.Dark.inputErrorBorder else LuxorColors.Light.inputErrorBorder
            isFocused -> if (isDarkTheme) LuxorColors.Dark.inputFocusedBorder else LuxorColors.Light.inputFocusedBorder
            else -> if (isDarkTheme) LuxorColors.Dark.inputBorder else LuxorColors.Light.inputBorder
        },
        animationSpec = tween(durationMillis = 200),
        label = "borderColor"
    )
    
    val backgroundColor = if (isDarkTheme) {
        LuxorColors.Dark.inputBackground
    } else {
        LuxorColors.Light.inputBackground
    }
    
    val textColor = if (isDarkTheme) {
        LuxorColors.Dark.textPrimary
    } else {
        LuxorColors.Light.textPrimary
    }
    
    val labelColor = if (isDarkTheme) {
        LuxorColors.Dark.textSecondary
    } else {
        LuxorColors.Light.textSecondary
    }
    
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .scale(animatedScale)
                .fillMaxWidth()
        ) {
            // Background gradient for focused state (iOS-style)
            if (isFocused && !isError) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = if (isDarkTheme) {
                                LuxorColors.Gradients.textFieldEditingGradient
                            } else {
                                LuxorColors.Gradients.textFieldEditingGradient
                            }
                        )
                )
            }
            
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = {
                    Text(
                        text = label,
                        color = if (isFocused && !isError) {
                            if (isDarkTheme) LuxorColors.pureGold else LuxorColors.richGold
                        } else labelColor,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = leadingIcon?.let { icon ->
                    {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isFocused && !isError) {
                                if (isDarkTheme) LuxorColors.pureGold else LuxorColors.richGold
                            } else labelColor
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor)
                    .border(
                        width = if (isFocused) 2.dp else 1.dp,
                        color = animatedBorderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                keyboardActions = keyboardActions,
                isError = isError,
                enabled = enabled,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    disabledTextColor = textColor.copy(alpha = 0.6f),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
        
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = if (isDarkTheme) LuxorColors.Dark.inputErrorBorder else LuxorColors.Light.inputErrorBorder,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun LuxorEmailTextField(
    email: String,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    LuxorTextField(
        value = email,
        onValueChange = onEmailChange,
        label = "Email",
        leadingIcon = Icons.Default.Email,
        keyboardType = KeyboardType.Email,
        modifier = modifier,
        isError = isError,
        errorMessage = errorMessage
    )
}

@Composable
fun LuxorPasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    isError: Boolean = false,
    errorMessage: String = "",
    showValidation: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val isDarkTheme = isSystemInDarkTheme()
    var isFocused by remember { mutableStateOf(false) }
    
    // Password validation (matching iOS behavior)
    val hasMinLength = password.length >= 8
    val hasCapitalLetter = password.any { it.isUpperCase() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }
    
    val animatedScale by animateFloatAsState(
        targetValue = if (isFocused) 1.02f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "scale"
    )
    
    val animatedBorderColor by animateColorAsState(
        targetValue = when {
            isError -> if (isDarkTheme) LuxorColors.Dark.inputErrorBorder else LuxorColors.Light.inputErrorBorder
            isFocused -> if (isDarkTheme) LuxorColors.Dark.inputFocusedBorder else LuxorColors.Light.inputFocusedBorder
            else -> if (isDarkTheme) LuxorColors.Dark.inputBorder else LuxorColors.Light.inputBorder
        },
        animationSpec = tween(durationMillis = 200),
        label = "borderColor"
    )
    
    val backgroundColor = if (isDarkTheme) {
        LuxorColors.Dark.inputBackground
    } else {
        LuxorColors.Light.inputBackground
    }
    
    val textColor = if (isDarkTheme) {
        LuxorColors.Dark.textPrimary
    } else {
        LuxorColors.Light.textPrimary
    }
    
    val labelColor = if (isDarkTheme) {
        LuxorColors.Dark.textSecondary
    } else {
        LuxorColors.Light.textSecondary
    }
    
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .scale(animatedScale)
                .fillMaxWidth()
        ) {
            // Background gradient for focused state (iOS-style)
            if (isFocused && !isError) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = LuxorColors.Gradients.textFieldEditingGradient
                        )
                )
            }
            
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = {
                    Text(
                        text = label,
                        color = if (isFocused && !isError) {
                            if (isDarkTheme) LuxorColors.pureGold else LuxorColors.richGold
                        } else labelColor,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isFocused && !isError) {
                            if (isDarkTheme) LuxorColors.pureGold else LuxorColors.richGold
                        } else labelColor
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = if (isFocused && !isError) {
                                if (isDarkTheme) LuxorColors.pureGold else LuxorColors.richGold
                            } else labelColor
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor)
                    .border(
                        width = if (isFocused) 2.dp else 1.dp,
                        color = animatedBorderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    disabledTextColor = textColor.copy(alpha = 0.6f),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
        
        // Password validation indicators (iOS-style)
        if (showValidation && password.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            ) {
                PasswordValidationItem(
                    text = "At least 8 characters",
                    isValid = hasMinLength,
                    isDarkTheme = isDarkTheme
                )
                PasswordValidationItem(
                    text = "One capital letter",
                    isValid = hasCapitalLetter,
                    isDarkTheme = isDarkTheme
                )
                PasswordValidationItem(
                    text = "One special character",
                    isValid = hasSpecialChar,
                    isDarkTheme = isDarkTheme
                )
            }
        }
        
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = if (isDarkTheme) LuxorColors.Dark.inputErrorBorder else LuxorColors.Light.inputErrorBorder,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
private fun PasswordValidationItem(
    text: String,
    isValid: Boolean,
    isDarkTheme: Boolean
) {
    val color = if (isValid) {
        if (isDarkTheme) LuxorColors.pureGold else LuxorColors.richGold
    } else {
        if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
    }
    
    Text(
        text = "${if (isValid) "✓" else "○"} $text",
        color = color,
        fontSize = 12.sp,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

@Composable
fun LuxorFullNameTextField(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    LuxorTextField(
        value = fullName,
        onValueChange = onFullNameChange,
        label = "Full Name",
        leadingIcon = Icons.Default.Person,
        keyboardType = KeyboardType.Text,
        modifier = modifier,
        isError = isError,
        errorMessage = errorMessage
    )
}

@Composable
fun LuxorPhoneTextField(
    phone: String,
    onPhoneChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    LuxorTextField(
        value = phone,
        onValueChange = onPhoneChange,
        label = "Phone Number",
        leadingIcon = Icons.Default.Phone,
        keyboardType = KeyboardType.Phone,
        modifier = modifier,
        isError = isError,
        errorMessage = errorMessage
    )
}