package com.example.luxorappandroid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.example.luxorappandroid.ui.components.LuxorTextField
import com.example.luxorappandroid.ui.components.LuxorEmailTextField
import com.example.luxorappandroid.ui.components.LuxorPasswordTextField
import com.example.luxorappandroid.ui.components.LuxorFullNameTextField
import com.example.luxorappandroid.ui.components.LuxorPhoneTextField

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
    
    val backgroundColor = if (isDarkTheme) {
        LuxorColors.Dark.inputBackground
    } else {
        LuxorColors.Light.cardBackground
    }
    
    val borderColor = if (isError) {
        Color.Red
    } else if (isDarkTheme) {
        LuxorColors.Dark.border
    } else {
        LuxorColors.Light.border
    }
    
    val textColor = if (isDarkTheme) {
        LuxorColors.Dark.textPrimary
    } else {
        LuxorColors.Light.textPrimary
    }
    
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    color = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                    fontSize = 14.sp
                )
            },
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                ),
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
        
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
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
    errorMessage: String = ""
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val isDarkTheme = isSystemInDarkTheme()
    
    val backgroundColor = if (isDarkTheme) {
        LuxorColors.Dark.inputBackground
    } else {
        LuxorColors.Light.cardBackground
    }
    
    val borderColor = if (isError) {
        Color.Red
    } else if (isDarkTheme) {
        LuxorColors.Dark.border
    } else {
        LuxorColors.Light.border
    }
    
    val textColor = if (isDarkTheme) {
        LuxorColors.Dark.textPrimary
    } else {
        LuxorColors.Light.textPrimary
    }
    
    Column(modifier = modifier) {
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = {
                Text(
                    text = label,
                    color = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                ),
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
        
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
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