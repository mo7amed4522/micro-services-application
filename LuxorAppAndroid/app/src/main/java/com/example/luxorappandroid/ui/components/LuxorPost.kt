package com.example.luxorappandroid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.luxorappandroid.data.PostModel
import com.example.luxorappandroid.ui.theme.*

@Composable
fun LuxorPost(
    post: PostModel,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false
) {
    val colors = if (isDarkMode) DarkColors else LightColors
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf(post.likeCount) }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.Background)
    ) {
        // Header with author info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Profile picture
            AsyncImage(
                model = post.authorProfileImage,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.SecondaryBackground),
                contentScale = ContentScale.Crop,
                error = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colors.SecondaryBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile placeholder",
                            tint = colors.TextSecondary
                        )
                    }
                }
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = post.authorName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )
                
                post.location?.let { location ->
                    Text(
                        text = location,
                        fontSize = 12.sp,
                        color = colors.TextSecondary
                    )
                }
            }
            
            IconButton(
                onClick = { /* More options */ }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = colors.TextPrimary
                )
            }
        }
        
        // Main post image
        AsyncImage(
            model = post.imageUrl,
            contentDescription = "Post image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(colors.SecondaryBackground),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = LuxorTeal
                    )
                }
            }
        )
        
        // Action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    isLiked = !isLiked
                    likeCount += if (isLiked) 1 else -1
                }
            ) {
                Icon(
                    imageVector = if (isLiked) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = "Like",
                    tint = if (isLiked) Color.Red else colors.TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            IconButton(
                onClick = { /* Comment */ }
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "Comment",
                    tint = colors.TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            IconButton(
                onClick = { /* Share */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Share",
                    tint = colors.TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            IconButton(
                onClick = { /* Bookmark */ }
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = colors.TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        // Like count
        if (likeCount > 0) {
            Text(
                text = "$likeCount likes",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.TextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        
        // Caption
        if (post.caption.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = post.authorName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )
                Text(
                    text = post.caption,
                    fontSize = 14.sp,
                    color = colors.TextPrimary
                )
            }
        }
        
        // Time ago
        Text(
            text = post.timeAgo,
            fontSize = 12.sp,
            color = colors.TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
