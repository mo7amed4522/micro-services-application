package com.example.luxorappandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.components.LuxorEmailTextField
import com.example.luxorappandroid.ui.components.LuxorPasswordTextField
import com.example.luxorappandroid.ui.theme.*
import com.example.luxorappandroid.ui.theme.ThemeManager
import com.example.luxorappandroid.ui.theme.LanguageManager
import androidx.compose.foundation.border
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun RegisterView(
    themeManager: ThemeManager,
    languageManager: LanguageManager,
    onBackPressed: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showImagePicker by remember { mutableStateOf(false) }
    var profileImage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 40.dp)
    ) {
        item {
            // Custom App Bar
            CustomAppBar(
                themeManager = themeManager,
                languageManager = languageManager,
                onBackPressed = onBackPressed
            )
        }
        
        item {
            // Profile Photo Section
            ProfilePhotoSection(
                profileImage = profileImage,
                onImageClick = { showImagePicker = true },
                themeManager = themeManager,
                languageManager = languageManager
            )
        }
        
        item {
            // Registration Form
            RegistrationForm(
                fullName = fullName,
                onFullNameChange = { fullName = it },
                email = email,
                onEmailChange = { email = it },
                phoneNumber = phoneNumber,
                onPhoneNumberChange = { phoneNumber = it },
                password = password,
                onPasswordChange = { password = it },
                isLoading = isLoading,
                onRegister = {
                    isLoading = true
                    // Simulate registration
                    coroutineScope.launch {
                        delay(2000)
                        isLoading = false
                        onRegisterSuccess()
                    }
                },
                themeManager = themeManager,
                languageManager = languageManager
            )
        }
    }

    // Image Picker Dialog (simplified)
    if (showImagePicker) {
        ImagePickerDialog(
            onImageSelected = { imageUrl ->
                profileImage = imageUrl
                showImagePicker = false
            },
            onDismiss = { showImagePicker = false },
            themeManager = themeManager,
            languageManager = languageManager
        )
    }
}

@Composable
fun CustomAppBar(
    themeManager: ThemeManager,
    languageManager: LanguageManager,
    onBackPressed: () -> Unit
) {
    val colors = if (themeManager.isDarkMode) LuxorColors.Dark else LuxorColors.Light
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = colors.cardBackground,
                    shape = CircleShape
                )
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    spotColor = colors.shadow
                )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = LocalizedStrings.back(languageManager.currentLanguage),
                tint = colors.textPrimary
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Title
        Text(
            text = LocalizedStrings.createAccount(languageManager.currentLanguage),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Logo
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(colors.primary, colors.secondary)
                    ),
                    shape = CircleShape
                )
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    spotColor = colors.primary.copy(alpha = 0.3f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Luxor Logo",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun ProfilePhotoSection(
    profileImage: String?,
    onImageClick: () -> Unit,
    themeManager: ThemeManager,
    languageManager: LanguageManager
) {
    val colors = if (themeManager.isDarkMode) LuxorColors.Dark else LuxorColors.Light
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colors.cardBackground,
                            colors.inputBackground
                        )
                    ),
                    shape = CircleShape
                )
                .border(
                    width = 3.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(colors.primary, colors.secondary)
                    ),
                    shape = CircleShape
                )
                .shadow(
                    elevation = 12.dp,
                    shape = CircleShape,
                    spotColor = colors.shadow
                )
                .clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (profileImage != null) {
                // Profile image would be displayed here
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = LocalizedStrings.profile(languageManager.currentLanguage),
                    tint = colors.primary,
                    modifier = Modifier.size(40.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = LocalizedStrings.addProfilePhoto(languageManager.currentLanguage),
                    tint = colors.textSecondary,
                    modifier = Modifier.size(40.dp)
                )
            }
            
            // Camera Button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(36.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(colors.primary, colors.secondary)
                        ),
                        shape = CircleShape
                    )
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        spotColor = colors.primary.copy(alpha = 0.4f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = LocalizedStrings.camera(languageManager.currentLanguage),
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = LocalizedStrings.addProfilePhoto(languageManager.currentLanguage),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = colors.textSecondary
        )
    }
}

@Composable
fun RegistrationForm(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    onRegister: () -> Unit,
    themeManager: ThemeManager,
    languageManager: LanguageManager
) {
    val colors = if (themeManager.isDarkMode) LuxorColors.Dark else LuxorColors.Light
    val isDarkMode = themeManager.isDarkMode
    val isFormValid = remember(fullName, email, phoneNumber, password) {
        fullName.isNotEmpty() && 
        email.isNotEmpty() && 
        phoneNumber.isNotEmpty() && 
        password.isNotEmpty() &&
        isValidEmail(email) &&
        isValidFullName(fullName) &&
        isValidPassword(password)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Full Name Field
        FullNameTextField(
            fullName = fullName,
            onFullNameChange = onFullNameChange,
            isDarkMode = isDarkMode
        )
        
        // Email Field
        LuxorEmailTextField(
            email = email,
            onEmailChange = onEmailChange,
            themeManager = themeManager,
            languageManager = languageManager
        )
        
        // Phone Number Field
        PhoneNumberTextField(
            phoneNumber = phoneNumber,
            onPhoneNumberChange = onPhoneNumberChange,
            isDarkMode = isDarkMode
        )
        
        // Password Field
        LuxorPasswordTextField(
            password = password,
            onPasswordChange = onPasswordChange,
            themeManager = themeManager,
            languageManager = languageManager
        )
        
        // Register Button
        Button(
            onClick = onRegister,
            enabled = isFormValid && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) {
                    Color.Transparent
                } else {
                    Color.Gray.copy(alpha = 0.5f)
                }
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = if (isFormValid) {
                            Brush.linearGradient(
                                colors = listOf(LuxorColors.Dark.accent, LuxorColors.pureGold)
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.Gray.copy(alpha = 0.5f),
                                    Color.Gray.copy(alpha = 0.3f)
                                )
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Create Account",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
        
        // Terms and Privacy
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "By creating an account, you agree to our",
                fontSize = 12.sp,
                color = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                textAlign = TextAlign.Center
            )
            
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = { /* Show Terms */ }
                ) {
                    Text(
                        text = "Terms of Service",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = LuxorColors.Dark.accent
                    )
                }
                
                Text(
                    text = "and",
                    fontSize = 12.sp,
                    color = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
                )
                
                TextButton(
                    onClick = { /* Show Privacy Policy */ }
                ) {
                    Text(
                        text = "Privacy Policy",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = LuxorColors.Dark.accent
                    )
                }
            }
        }
    }
}

@Composable
fun FullNameTextField(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    isDarkMode: Boolean
) {
    var isEditing by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isDarkMode) LuxorColors.Dark.inputBackground else Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = if (isEditing) 2.dp else 1.dp,
                    brush = if (isEditing) {
                        Brush.linearGradient(
                            colors = listOf(LuxorColors.Dark.accent, LuxorColors.pureGold)
                        )
                    } else if (!isValid) {
                        Brush.linearGradient(
                            colors = listOf(Color.Red.copy(alpha = 0.6f), Color.Red.copy(alpha = 0.3f))
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                if (isDarkMode) LuxorColors.Dark.border else Color.Gray.copy(alpha = 0.2f),
            if (isDarkMode) LuxorColors.Dark.border.copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.1f)
                            )
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .shadow(
                    elevation = if (isEditing) 12.dp else 6.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = if (isDarkMode) LuxorColors.Dark.shadow else LuxorColors.Light.shadow
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = if (isEditing) LuxorColors.Dark.accent.copy(alpha = 0.2f) else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.PersonAdd else Icons.Default.Person,
                        contentDescription = "Full Name",
                        tint = if (isEditing) LuxorColors.Dark.accent else {
                            if (isDarkMode) LuxorColors.Dark.textSecondary else Color.Gray
                        },
                        modifier = Modifier.size(18.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Text Field
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Full Name",
                        fontSize = if (isEditing || fullName.isNotEmpty()) 11.sp else 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isEditing) LuxorColors.Dark.accent else {
                            if (isDarkMode) LuxorColors.Dark.textSecondary else Color.Gray
                        }
                    )
                    
                    TextField(
                        value = fullName,
                        onValueChange = { 
                            onFullNameChange(it)
                            isValid = it.isEmpty() || isValidFullName(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = LuxorColors.Dark.accent,
                            focusedTextColor = if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary,
                            unfocusedTextColor = if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                
                // Validation Icon
                if (fullName.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = if (isValid) Color.Green.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isValid) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = "Validation",
                            tint = if (isValid) Color.Green else Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
        
        // Error Message
        if (!isValid && fullName.isNotEmpty()) {
            Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(12.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Please enter your full name (at least 2 words)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun PhoneNumberTextField(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    isDarkMode: Boolean
) {
    var isEditing by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isDarkMode) LuxorColors.Dark.inputBackground else Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = if (isEditing) 2.dp else 1.dp,
                    brush = if (isEditing) {
                        Brush.linearGradient(
                            colors = listOf(LuxorColors.Dark.accent, LuxorColors.pureGold)
                        )
                    } else if (!isValid) {
                        Brush.linearGradient(
                            colors = listOf(Color.Red.copy(alpha = 0.6f), Color.Red.copy(alpha = 0.3f))
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                if (isDarkMode) LuxorColors.Dark.border else Color.Gray.copy(alpha = 0.2f),
                                if (isDarkMode) LuxorColors.Dark.border.copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.1f)
                            )
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .shadow(
                    elevation = if (isEditing) 12.dp else 6.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = if (isDarkMode) LuxorColors.Dark.shadow else LuxorColors.Light.shadow
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Country Code Button
                Button(
                    onClick = { /* Show country picker */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkMode) LuxorColors.Dark.cardBackground else Color.LightGray
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🇺🇸", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "+1",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select Country",
                            tint = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(30.dp)
                        .background(
                            color = if (isDarkMode) LuxorColors.Dark.border else Color.Gray.copy(alpha = 0.3f)
                        )
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Phone Number Field
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Phone Number",
                        fontSize = if (isEditing || phoneNumber.isNotEmpty()) 11.sp else 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isEditing) LuxorColors.Dark.accent else {
                            if (isDarkMode) LuxorColors.Dark.textSecondary else Color.Gray
                        }
                    )
                    
                    TextField(
                        value = phoneNumber,
                        onValueChange = { newValue -> 
                            onPhoneNumberChange(newValue)
                            isValid = newValue.isEmpty() || isValidPhoneNumber(newValue)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = LuxorColors.Dark.accent,
                            textColor = if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                
                // Validation Icon
                if (phoneNumber.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = if (isValid) Color.Green.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isValid) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = "Validation",
                            tint = if (isValid) Color.Green else Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
        
        // Error Message
        if (!isValid && phoneNumber.isNotEmpty()) {
            Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(12.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Please enter a valid phone number",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun ImagePickerDialog(
    onImageSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    isDarkMode: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Profile Photo",
                color = if (isDarkMode) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
            )
        },
        text = {
            Text(
                text = "Choose how you want to add your profile photo",
                color = if (isDarkMode) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Simulate image selection
                    onImageSelected("https://picsum.photos/200/200")
                }
            ) {
                Text("Camera", color = LuxorColors.Dark.accent)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    // Simulate image selection
                    onImageSelected("https://picsum.photos/200/200")
                }
            ) {
                Text("Gallery", color = LuxorColors.Dark.accent)
            }
        },
        containerColor = if (isDarkMode) LuxorColors.Dark.cardBackground else LuxorColors.Light.cardBackground
    )
}

private fun isValidEmail(email: String): Boolean {
    val emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}".toRegex()
    return emailRegex.matches(email)
}

private fun isValidFullName(name: String): Boolean {
    val trimmedName = name.trim()
    val nameComponents = trimmedName.split("\\s+".toRegex()).filter { it.isNotEmpty() }
    return nameComponents.size >= 2 && trimmedName.length >= 3
}

private fun isValidPassword(password: String): Boolean {
    return password.length >= 8 &&
           password.any { it.isUpperCase() } &&
           password.any { !it.isLetterOrDigit() }
}

private fun isValidPhoneNumber(number: String): Boolean {
    val cleanedNumber = number.replace(Regex("[^0-9]"), "")
    return cleanedNumber.length >= 7 && cleanedNumber.length <= 15
}
