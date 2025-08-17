package com.example.luxorappandroid.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.luxorappandroid.data.SampleData
import com.example.luxorappandroid.ui.components.LuxorPost
import com.example.luxorappandroid.ui.theme.ThemeManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeContentView(
    isNavBarVisible: MutableState<Boolean>,
    isAppBarVisible: MutableState<Boolean>,
    lastScrollOffset: MutableState<Float>,
    isScrolling: MutableState<Boolean>,
    isDarkMode: Boolean
) {
    val posts = SampleData.samplePosts
    val scope = rememberCoroutineScope()
    var scrollTimer by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        items(posts) { post ->
            LuxorPost(
                post = post,
                isDarkMode = isDarkMode
            )
        }
    }

    // Instagram-style scroll handling
    LaunchedEffect(Unit) {
        while (true) {
            delay(100) // Check scroll state every 100ms
            val currentOffset = lastScrollOffset.value
            val delta = kotlin.math.abs(currentOffset - lastScrollOffset.value)
            
            if (delta > 1.0f) {
                if (!isScrolling.value) {
                    isScrolling.value = true
                    isAppBarVisible.value = false
                    isNavBarVisible.value = false
                }
                
                scrollTimer?.cancel()
                scrollTimer = scope.launch {
                    delay(1500) // 1.5 seconds delay
                    isScrolling.value = false
                    isAppBarVisible.value = true
                    isNavBarVisible.value = true
                }
            }
            
            if (currentOffset >= -10f) {
                scrollTimer?.cancel()
                isScrolling.value = false
                isAppBarVisible.value = true
                isNavBarVisible.value = true
            }
        }
    }
}
