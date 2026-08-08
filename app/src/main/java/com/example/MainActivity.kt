package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.repository.AnalyticsRepository
import com.example.ui.components.screens.AutoCommentScreen
import com.example.ui.components.screens.BlockAndVisitorsScreen
import com.example.ui.components.screens.DashboardScreen
import com.example.ui.components.screens.FollowerInsightsScreen
import com.example.ui.components.screens.ProfileViewerScreen
import com.example.ui.theme.BentoTextSecondary
import com.example.ui.theme.InstaPink
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private lateinit var database: AppDatabase
    private lateinit var repository: AnalyticsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "insta_analytics_db"
        ).fallbackToDestructiveMigration().build()

        repository = AnalyticsRepository(database.analyticsDao())

        setContent {
            MyApplicationTheme {
                MainAppScreen(repository = repository)
            }
        }
    }
}

data class NavTabItem(
    val title: String,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun MainAppScreen(repository: AnalyticsRepository) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        NavTabItem("Dashboard", Icons.Default.Analytics, "nav_dashboard"),
        NavTabItem("Insights", Icons.Default.Group, "nav_insights"),
        NavTabItem("Visitors", Icons.Default.Visibility, "nav_visitors"),
        NavTabItem("Auto-Bot", Icons.Default.Comment, "nav_autobot"),
        NavTabItem("Viewer", Icons.Default.Search, "nav_viewer")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_navigation")
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                tint = if (isSelected) InstaPink else BentoTextSecondary
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) InstaPink else BentoTextSecondary
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = InstaPink.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedTab) {
                0 -> DashboardScreen(repository = repository)
                1 -> FollowerInsightsScreen(repository = repository)
                2 -> BlockAndVisitorsScreen(repository = repository)
                3 -> AutoCommentScreen(repository = repository)
                4 -> ProfileViewerScreen(repository = repository)
            }
        }
    }
}
