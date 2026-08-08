package com.example.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MediaItem
import com.example.data.model.TargetAccount
import com.example.repository.AnalyticsRepository
import com.example.ui.components.MediaLightboxDialog
import com.example.ui.components.StoryAvatar
import com.example.ui.components.formatCount
import com.example.ui.theme.InstaBlue
import com.example.ui.theme.InstaPink
import com.example.ui.theme.MetricGreen
import com.example.ui.theme.OnDarkTextSecondary
import androidx.compose.foundation.BorderStroke
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoTextSecondary
import kotlinx.coroutines.launch

@Composable
fun ProfileViewerScreen(
    repository: AnalyticsRepository,
    modifier: Modifier = Modifier
) {
    var searchInput by remember { mutableStateOf("") }
    var searchedAccount by remember { mutableStateOf<TargetAccount?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var selectedMediaForLightbox by remember { mutableStateOf<MediaItem?>(null) }
    var mediaTab by remember { mutableStateOf(0) } // 0 = Posts, 1 = Reels, 2 = Stories

    val searchHistoryList by repository.searchHistory.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    fun performSearch(handle: String) {
        if (handle.isBlank()) return
        scope.launch {
            isLoading = true
            searchedAccount = repository.searchProfileWithProxy(handle)
            isLoading = false
        }
    }

    // Default search on initial load
    remember {
        performSearch("travel_bug")
        true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("profile_viewer_screen")
    ) {
        // Top Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Universal Profile Viewer 🔍",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "View Posts, Reels & Stories for any public or private account",
                    fontSize = 12.sp,
                    color = OnDarkTextSecondary
                )
            }

            // Proxy Indicator Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MetricGreen.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Proxy Active",
                        tint = MetricGreen,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Proxy Relay Active",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MetricGreen
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Input Bar
        OutlinedTextField(
            value = searchInput,
            onValueChange = { searchInput = it },
            placeholder = { Text("Enter Instagram ID (e.g. @cristiano, @private_user99)", fontSize = 12.sp, color = OnDarkTextSecondary) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = InstaPink,
                    modifier = Modifier.clickable { performSearch(searchInput) }
                )
            },
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = InstaPink,
                        strokeWidth = 2.dp
                    )
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("universal_profile_search_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = InstaPink,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Recent Searches Chips
        if (searchHistoryList.isNotEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History",
                    tint = OnDarkTextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Recent:", fontSize = 11.sp, color = OnDarkTextSecondary)

                Spacer(modifier = Modifier.width(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(searchHistoryList) { history ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable {
                                    searchInput = history.handle
                                    performSearch(history.handle)
                                }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "@${history.handle}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Target Account View
        val target = searchedAccount
        if (target != null) {
            Card(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BentoBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StoryAvatar(
                            imageUrl = target.avatarUrl,
                            size = 68.dp,
                            hasStory = target.stories.isNotEmpty()
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "@${target.handle}",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                if (target.isPrivate) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(InstaPink.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Lock,
                                                contentDescription = "Private",
                                                tint = InstaPink,
                                                modifier = Modifier.size(10.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "PRIVATE (BYPASSED)",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = InstaPink
                                            )
                                        }
                                    }
                                }
                            }

                            Text(
                                text = target.fullName,
                                fontSize = 12.sp,
                                color = OnDarkTextSecondary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                StatMini("Posts", "${target.postsCount}")
                                StatMini("Followers", formatCount(target.followersCount))
                                StatMini("Following", "${target.followingCount}")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = target.bio,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Media Tab Selector (Posts, Reels, Stories)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(4.dp)
            ) {
                TabButton(
                    title = "Posts (${target.posts.size})",
                    icon = Icons.Default.GridOn,
                    isSelected = mediaTab == 0,
                    onClick = { mediaTab = 0 },
                    modifier = Modifier.weight(1f)
                )
                TabButton(
                    title = "Reels (${target.reels.size})",
                    icon = Icons.Default.VideoLibrary,
                    isSelected = mediaTab == 1,
                    onClick = { mediaTab = 1 },
                    modifier = Modifier.weight(1f)
                )
                TabButton(
                    title = "Stories (${target.stories.size})",
                    icon = Icons.Default.PlayArrow,
                    isSelected = mediaTab == 2,
                    onClick = { mediaTab = 2 },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val currentMediaList = when (mediaTab) {
                1 -> target.reels
                2 -> target.stories
                else -> target.posts
            }

            // Media Grid Viewer
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(currentMediaList) { mediaItem ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(if (mediaTab == 1) 0.65f else 1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { selectedMediaForLightbox = mediaItem }
                            .testTag("media_grid_item_${mediaItem.id}")
                    ) {
                        AsyncImage(
                            model = mediaItem.thumbnailUrl,
                            contentDescription = mediaItem.caption,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        if (mediaItem.type == com.example.data.model.MediaType.REEL) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.6f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Reel",
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Lightbox Dialog
        val activeMedia = selectedMediaForLightbox
        if (activeMedia != null && target != null) {
            MediaLightboxDialog(
                media = activeMedia,
                accountHandle = target.handle,
                onDismiss = { selectedMediaForLightbox = null }
            )
        }
    }
}

@Composable
private fun StatMini(label: String, valText: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(valText, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Text(label, fontSize = 10.sp, color = OnDarkTextSecondary)
    }
}

@Composable
private fun TabButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) InstaPink else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) Color.White else OnDarkTextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else OnDarkTextSecondary
            )
        }
    }
}
