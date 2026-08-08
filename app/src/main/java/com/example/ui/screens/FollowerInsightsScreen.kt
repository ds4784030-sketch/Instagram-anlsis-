package com.example.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FollowerCategory
import com.example.data.model.FollowerUser
import com.example.repository.AnalyticsRepository
import com.example.ui.components.StoryAvatar
import androidx.compose.foundation.BorderStroke
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoTextSecondary
import com.example.ui.theme.InstaBlue
import com.example.ui.theme.InstaPink
import com.example.ui.theme.MetricGreen
import com.example.ui.theme.MetricRed
import com.example.ui.theme.OnDarkTextSecondary

@Composable
fun FollowerInsightsScreen(
    repository: AnalyticsRepository,
    modifier: Modifier = Modifier
) {
    val allFollowers = remember { repository.getFollowersList() }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<FollowerCategory?>(null) }

    val newThisMonthCount = allFollowers.count { it.category == FollowerCategory.NEW_THIS_MONTH }
    val unfollowedWeekCount = allFollowers.count { it.category == FollowerCategory.UNFOLLOWED_THIS_WEEK }
    val unfollowedMonthCount = allFollowers.count { it.category == FollowerCategory.UNFOLLOWED_THIS_MONTH || it.category == FollowerCategory.UNFOLLOWED_THIS_WEEK }
    val nonFollowersBackCount = allFollowers.count { it.category == FollowerCategory.NOT_FOLLOWING_BACK }

    val filteredFollowers = remember(searchQuery, selectedCategory) {
        allFollowers.filter { user ->
            val matchesCategory = selectedCategory == null || user.category == selectedCategory
            val matchesSearch = searchQuery.isEmpty() ||
                    user.username.contains(searchQuery, ignoreCase = true) ||
                    user.fullName.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("follower_insights_screen")
    ) {
        Text(
            text = "Follower Insights & Audit",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Track new followers, recent unfollowers, and non-followers back",
            fontSize = 12.sp,
            color = BentoTextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Insight Metric Summary Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            InsightCard(
                title = "New Followers",
                subtitle = "This Month",
                count = "+$newThisMonthCount",
                icon = Icons.Default.PersonAdd,
                accentColor = MetricGreen,
                isSelected = selectedCategory == FollowerCategory.NEW_THIS_MONTH,
                onClick = {
                    selectedCategory = if (selectedCategory == FollowerCategory.NEW_THIS_MONTH) null else FollowerCategory.NEW_THIS_MONTH
                },
                modifier = Modifier.weight(1f)
            )

            InsightCard(
                title = "Unfollowed",
                subtitle = "This Week",
                count = "-$unfollowedWeekCount",
                icon = Icons.Default.PersonRemove,
                accentColor = MetricRed,
                isSelected = selectedCategory == FollowerCategory.UNFOLLOWED_THIS_WEEK,
                onClick = {
                    selectedCategory = if (selectedCategory == FollowerCategory.UNFOLLOWED_THIS_WEEK) null else FollowerCategory.UNFOLLOWED_THIS_WEEK
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            InsightCard(
                title = "Unfollowed",
                subtitle = "This Month",
                count = "-$unfollowedMonthCount",
                icon = Icons.Default.PersonRemove,
                accentColor = InstaPink,
                isSelected = selectedCategory == FollowerCategory.UNFOLLOWED_THIS_MONTH,
                onClick = {
                    selectedCategory = if (selectedCategory == FollowerCategory.UNFOLLOWED_THIS_MONTH) null else FollowerCategory.UNFOLLOWED_THIS_MONTH
                },
                modifier = Modifier.weight(1f)
            )

            InsightCard(
                title = "Don't Follow Back",
                subtitle = "Not Following You",
                count = "$nonFollowersBackCount",
                icon = Icons.Default.Warning,
                accentColor = InstaBlue,
                isSelected = selectedCategory == FollowerCategory.NOT_FOLLOWING_BACK,
                onClick = {
                    selectedCategory = if (selectedCategory == FollowerCategory.NOT_FOLLOWING_BACK) null else FollowerCategory.NOT_FOLLOWING_BACK
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search account or handle...", fontSize = 13.sp, color = OnDarkTextSecondary) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = OnDarkTextSecondary) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("follower_search_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = InstaPink,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CategoryChip(
                    label = "All Accounts",
                    isSelected = selectedCategory == null,
                    onClick = { selectedCategory = null }
                )
            }
            item {
                CategoryChip(
                    label = "New (Current Month)",
                    isSelected = selectedCategory == FollowerCategory.NEW_THIS_MONTH,
                    onClick = { selectedCategory = FollowerCategory.NEW_THIS_MONTH }
                )
            }
            item {
                CategoryChip(
                    label = "Unfollowed (This Week)",
                    isSelected = selectedCategory == FollowerCategory.UNFOLLOWED_THIS_WEEK,
                    onClick = { selectedCategory = FollowerCategory.UNFOLLOWED_THIS_WEEK }
                )
            }
            item {
                CategoryChip(
                    label = "Don't Follow Back",
                    isSelected = selectedCategory == FollowerCategory.NOT_FOLLOWING_BACK,
                    onClick = { selectedCategory = FollowerCategory.NOT_FOLLOWING_BACK }
                )
            }
            item {
                CategoryChip(
                    label = "Ghost / Inactive",
                    isSelected = selectedCategory == FollowerCategory.GHOST_INACTIVE,
                    onClick = { selectedCategory = FollowerCategory.GHOST_INACTIVE }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Followers List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredFollowers) { user ->
                FollowerItemCard(user = user)
            }
        }
    }
}

@Composable
private fun InsightCard(
    title: String,
    subtitle: String,
    count: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: androidx.compose.ui.graphics.Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, if (isSelected) accentColor else BentoBorder),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) accentColor.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = BentoTextSecondary
                )
                Text(
                    text = count,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = BentoTextSecondary
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) InstaPink else MaterialTheme.colorScheme.surface)
            .border(1.dp, if (isSelected) InstaPink else BentoBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) androidx.compose.ui.graphics.Color.White else BentoTextSecondary
        )
    }
}

@Composable
private fun FollowerItemCard(user: FollowerUser) {
    Card(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, BentoBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StoryAvatar(
                imageUrl = user.avatarUrl,
                size = 48.dp,
                hasStory = false
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "@${user.username}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = user.fullName,
                    fontSize = 12.sp,
                    color = BentoTextSecondary
                )

                Spacer(modifier = Modifier.height(2.dp))

                val statusText = when (user.category) {
                    FollowerCategory.NEW_THIS_MONTH -> "Followed ${user.followedDate}"
                    FollowerCategory.UNFOLLOWED_THIS_WEEK -> "Unfollowed ${user.unfollowedDate ?: "recently"}"
                    FollowerCategory.UNFOLLOWED_THIS_MONTH -> "Unfollowed ${user.unfollowedDate ?: "this month"}"
                    FollowerCategory.NOT_FOLLOWING_BACK -> "Doesn't follow you back"
                    FollowerCategory.GHOST_INACTIVE -> "Ghost follower (Activity score: ${user.activityScore}%)"
                    else -> "Mutual follower"
                }

                Text(
                    text = statusText,
                    fontSize = 11.sp,
                    color = when (user.category) {
                        FollowerCategory.NEW_THIS_MONTH -> MetricGreen
                        FollowerCategory.UNFOLLOWED_THIS_WEEK, FollowerCategory.UNFOLLOWED_THIS_MONTH -> MetricRed
                        FollowerCategory.NOT_FOLLOWING_BACK -> InstaPink
                        else -> BentoTextSecondary
                    },
                    fontWeight = FontWeight.Medium
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (user.isFollowingBack) MaterialTheme.colorScheme.surfaceVariant
                        else InstaPink.copy(alpha = 0.15f)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (user.isFollowingBack) "Following" else "Follow Back",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (user.isFollowingBack) BentoTextSecondary else InstaPink
                )
            }
        }
    }
}
