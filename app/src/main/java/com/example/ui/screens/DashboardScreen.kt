package com.example.ui.components.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.repository.AnalyticsRepository
import com.example.ui.components.MetricStatCard
import com.example.ui.components.ProfileHeaderCard
import com.example.ui.components.RankingBadge
import com.example.ui.components.ReachCurvedChart
import com.example.ui.components.ViewsBarChart
import com.example.ui.components.formatCount
import androidx.compose.foundation.BorderStroke
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoTextSecondary
import com.example.ui.theme.InstaBlue
import com.example.ui.theme.InstaOrange
import com.example.ui.theme.InstaPink
import com.example.ui.theme.MetricGreen
import com.example.ui.theme.RankGold

@Composable
fun DashboardScreen(
    repository: AnalyticsRepository,
    modifier: Modifier = Modifier
) {
    val userProfile by repository.userProfile.collectAsState(initial = com.example.data.model.UserProfile())
    var selectedPeriod by remember { mutableStateOf("30D") }
    val reachStats = remember(selectedPeriod) { repository.getReachStats(selectedPeriod) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("dashboard_screen")
    ) {
        // App Header Title Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Insta Analytics",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Real-time Account Performance & Insights",
                    fontSize = 12.sp,
                    color = BentoTextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(InstaPink.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "PRO ACTIVE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = InstaPink
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Card
        ProfileHeaderCard(user = userProfile)

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, BentoBorder),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.img_analytics_banner_1786160149418),
                    contentDescription = "Analytics Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                                    androidx.compose.ui.graphics.Color.Transparent
                                )
                            )
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column {
                        Text(
                            text = "Account Growth Peak 🔥",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Your Reels are outperforming 94% of creators in India this month!",
                            fontSize = 12.sp,
                            color = BentoTextSecondary,
                            modifier = Modifier.width(220.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Global & Regional Rankings Section
        Text(
            text = "Account Rankings",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RankingBadge(
                rankTitle = "India Rank 🇮🇳",
                rankNumber = "#${userProfile.indiaRank}",
                iconColor = RankGold,
                modifier = Modifier.weight(1f)
            )

            RankingBadge(
                rankTitle = "Worldwide Rank 🌐",
                rankNumber = "#${formatCount(userProfile.globalRank)}",
                iconColor = InstaBlue,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, BentoBorder),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Up",
                        tint = MetricGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+${userProfile.rankChange} spots gained this week",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MetricGreen
                    )
                }

                Text(
                    text = "Top 1% Creator Tier",
                    fontSize = 11.sp,
                    color = BentoTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Time Period Filter Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Views & Reach Analytics",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(2.dp)
            ) {
                listOf("7D", "30D", "90D").forEach { period ->
                    val isSelected = selectedPeriod == period
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isSelected) InstaPink else androidx.compose.ui.graphics.Color.Transparent)
                            .clickable { selectedPeriod = period }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("period_tab_$period")
                    ) {
                        Text(
                            text = period,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) androidx.compose.ui.graphics.Color.White else BentoTextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Metric Stat Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricStatCard(
                title = "Total Reach",
                value = formatCount(reachStats.accountsReached),
                subtitle = "Accounts reached",
                badgeText = "+${reachStats.reachGrowthPercent}%",
                badgePositive = true,
                modifier = Modifier.weight(1f)
            )

            MetricStatCard(
                title = "Impressions",
                value = formatCount(reachStats.impressions),
                subtitle = "Total feed displays",
                badgeText = "Peak",
                badgePositive = true,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Views Dashboard Bar Chart (Reels vs Standard Posts)
        ViewsBarChart(
            reelsData = reachStats.reelViewsDaily,
            postsData = reachStats.postViewsDaily,
            labels = reachStats.labels
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Reach Performance Curved Chart
        ReachCurvedChart(
            points = reachStats.dailyReachPoints,
            labels = reachStats.labels
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
