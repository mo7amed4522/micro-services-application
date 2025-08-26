package com.example.luxorappandroid.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.luxorappandroid.ui.theme.LuxorColors
import com.example.luxorappandroid.ui.theme.LuxorThemeManager

data class CountryCode(
    val name: String,
    val code: String,
    val flag: String,
    val dialCode: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuxorPhoneTextField(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Phone Number",

) {
    var isEditing by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(true) }
    var selectedCountry by remember { 
        mutableStateOf(CountryCode("United States", "US", "🇺🇸", "+1"))
    }
    var showDropdown by remember { mutableStateOf(false) }
    
    val countries = remember {
        listOf(
            CountryCode("United States", "US", "🇺🇸", "+1"),
            CountryCode("United Kingdom", "GB", "🇬🇧", "+44"),
            CountryCode("Canada", "CA", "🇨🇦", "+1"),
            CountryCode("Australia", "AU", "🇦🇺", "+61"),
            CountryCode("Germany", "DE", "🇩🇪", "+49"),
            CountryCode("France", "FR", "🇫🇷", "+33"),
            CountryCode("Japan", "JP", "🇯🇵", "+81"),
            CountryCode("Brazil", "BR", "🇧🇷", "+55"),
            CountryCode("Mexico", "MX", "🇲🇽", "+52")
        )
    }
    

    fun validatePhoneNumber(number: String) {
        val cleanedNumber = number.replace(Regex("[^0-9]"), "")
        isValid = number.isEmpty() || (cleanedNumber.length in 7..15)
    }
    

    LaunchedEffect(phoneNumber) {
        validatePhoneNumber(phoneNumber)
    }
    

    val borderWidth by animateFloatAsState(
        targetValue = if (isEditing) 2f else 1f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f),
        label = "borderWidth"
    )
    
    val shadowElevation by animateFloatAsState(
        targetValue = if (isEditing) 12f else 6f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f),
        label = "shadowElevation"
    )
    
    val labelOffset by animateFloatAsState(
        targetValue = if (isEditing || phoneNumber.isNotEmpty()) -8f else 8f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "labelOffset"
    )
    
    val labelSize by animateFloatAsState(
        targetValue = if (isEditing || phoneNumber.isNotEmpty()) 11f else 16f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "labelSize"
    )
    
    val dropdownRotation by animateFloatAsState(
        targetValue = if (showDropdown) 180f else 0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "dropdownRotation"
    )
    

    val backgroundGradient = Brush.linearGradient(
        colors = listOf(
            LuxorColors.Light.cardBackground,
            LuxorColors.Light.cardBackground.copy(alpha = 0.8f)
        )
    )
    

    val borderGradient = when {
        isEditing -> Brush.linearGradient(
            colors = listOf(LuxorColors.pureGold, LuxorColors.richGold)
        )
        !isValid -> Brush.linearGradient(
            colors = listOf(Color.Red.copy(alpha = 0.6f), Color.Red.copy(alpha = 0.3f))
        )
        else -> Brush.linearGradient(
            colors = listOf(
                LuxorColors.Light.textSecondary.copy(alpha = 0.2f),
                LuxorColors.Light.textSecondary.copy(alpha = 0.3f)
            )
        )
    }
    
    Column(modifier = modifier) {
        Box {
            // Main container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(
                        elevation = shadowElevation.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = LuxorColors.Light.shadow,
                        spotColor = LuxorColors.Light.shadow
                    )
                    .background(
                        brush = backgroundGradient,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = borderWidth.dp,
                        brush = borderGradient,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        if (showDropdown) {
                            showDropdown = false
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Country code dropdown button
                    Button(
                        onClick = { showDropdown = !showDropdown },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LuxorColors.Light.cardBackground
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = selectedCountry.flag,
                                fontSize = 20.sp
                            )
                            Text(
                                text = selectedCountry.dialCode,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = LuxorColors.Light.textPrimary
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Dropdown",
                                tint = LuxorColors.Light.textSecondary,
                                modifier = Modifier
                                    .size(12.dp)
                                    .rotate(dropdownRotation)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(LuxorColors.Light.border)
                            .padding(horizontal = 8.dp)
                    )

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = label,
                            fontSize = labelSize.sp,
                            fontWeight = FontWeight.Medium,
                            color = when {
                                isEditing -> LuxorColors.luxorTeal
                                !isValid -> Color.Red
                                else -> LuxorColors.Light.textSecondary
                            },
                            modifier = Modifier
                                .offset(y = labelOffset.dp)
                        )
                        TextField(
                            value = phoneNumber,
                            onValueChange = onPhoneNumberChange,
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = LuxorColors.Light.textPrimary,
                                unfocusedTextColor = LuxorColors.Light.textPrimary,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = LuxorColors.pureGold
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { focusState ->
                                    isEditing = focusState.isFocused
                                    if (focusState.isFocused && showDropdown) {
                                        showDropdown = false
                                    }
                                }
                        )
                    }
                    if (phoneNumber.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isValid) {
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color.Green.copy(alpha = 0.2f),
                                                Color.Green.copy(alpha = 0.1f)
                                            )
                                        )
                                    } else {
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color.Red.copy(alpha = 0.2f),
                                                Color.Red.copy(alpha = 0.1f)
                                            )
                                        )
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isValid) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = if (isValid) "Valid" else "Invalid",
                                tint = if (isValid) Color.Green else Color.Red,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
            if (showDropdown) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 72.dp)
                        .zIndex(1f),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = LuxorColors.Light.cardBackground
                    )
                ) {
                    Column {
                        countries.forEachIndexed { index, country ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedCountry = country
                                        showDropdown = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                                    .background(
                                        if (selectedCountry.code == country.code) {
                                            LuxorColors.pureGold.copy(alpha = 0.1f)
                                        } else Color.Transparent
                                    ),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = country.flag,
                                    fontSize = 20.sp
                                )
                                Column {
                                    Text(
                                        text = country.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = LuxorColors.Light.textPrimary
                                    )
                                    Text(
                                        text = country.dialCode,
                                        fontSize = 12.sp,
                                        color = LuxorColors.Light.textSecondary
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                if (selectedCountry.code == country.code) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = LuxorColors.luxorTeal,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            if (index < countries.size - 1) {
                                HorizontalDivider(
                                    color = LuxorColors.Light.border.copy(alpha = 0.2f)
                                )
                            }
                        }
                    }
                }
            }
        }
        if (!isValid && phoneNumber.isNotEmpty()) {
            Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(12.dp)
                )
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