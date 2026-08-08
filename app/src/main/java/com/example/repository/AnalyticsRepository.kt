package com.example.repository

import com.example.data.local.AnalyticsDao
import com.example.data.local.AutoCommentEntity
import com.example.data.local.SearchHistoryEntity
import com.example.data.model.AutoCommentItem
import com.example.data.model.FollowerCategory
import com.example.data.model.FollowerUser
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.data.model.ProfileVisitor
import com.example.data.model.ReachStats
import com.example.data.model.SearchHistoryItem
import com.example.data.model.TargetAccount
import com.example.data.model.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class AnalyticsRepository(private val dao: AnalyticsDao) {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: Flow<UserProfile> = _userProfile.asStateFlow()

    // Mock initial auto comments if DB is empty
    suspend fun seedDefaultAutoComments() {
        // Will check if empty when viewed
    }

    fun getReachStats(period: String): ReachStats {
        return when (period) {
            "7D" -> ReachStats(
                period = "7D",
                accountsReached = 184500,
                reachGrowthPercent = +18.4f,
                viewsReels = 142800,
                viewsPosts = 68200,
                impressions = 290100,
                profileVisits = 14200,
                websiteClicks = 840,
                dailyReachPoints = listOf(21000, 24500, 19800, 31200, 28900, 34200, 24900),
                reelViewsDaily = listOf(16000, 19000, 15000, 24000, 22000, 27000, 19800),
                postViewsDaily = listOf(5000, 5500, 4800, 7200, 6900, 7200, 5100),
                labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            )
            "90D" -> ReachStats(
                period = "90D",
                accountsReached = 3450000,
                reachGrowthPercent = +42.1f,
                viewsReels = 2890000,
                viewsPosts = 1120000,
                impressions = 5200000,
                profileVisits = 290000,
                websiteClicks = 12400,
                dailyReachPoints = listOf(350000, 410000, 380000, 490000, 520000, 610000, 690000),
                reelViewsDaily = listOf(280000, 320000, 300000, 400000, 420000, 500000, 570000),
                postViewsDaily = listOf(70000, 90000, 80000, 90000, 100000, 110000, 120000),
                labels = listOf("May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov")
            )
            else -> ReachStats( // "30D" default
                period = "30D",
                accountsReached = 1240500,
                reachGrowthPercent = +28.6f,
                viewsReels = 980400,
                viewsPosts = 412000,
                impressions = 1890000,
                profileVisits = 88400,
                websiteClicks = 4210,
                dailyReachPoints = listOf(32000, 38000, 35000, 42000, 48000, 54000, 61000),
                reelViewsDaily = listOf(24000, 29000, 27000, 33000, 38000, 43000, 49000),
                postViewsDaily = listOf(8000, 9000, 8000, 9000, 10000, 11000, 12000),
                labels = listOf("W1", "W2", "W3", "W4", "W5", "W6", "W7")
            )
        }
    }

    fun getFollowersList(): List<FollowerUser> {
        return listOf(
            FollowerUser("1", "priya_design", "Priya Sharma", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150", "3 days ago", null, true, category = FollowerCategory.NEW_THIS_MONTH),
            FollowerUser("2", "rohan_vlogs", "Rohan Verma", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150", "12 days ago", null, true, category = FollowerCategory.NEW_THIS_MONTH),
            FollowerUser("3", "tech_aaron", "Aaron Smith", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150", "18 days ago", null, false, category = FollowerCategory.NEW_THIS_MONTH),
            FollowerUser("4", "sneha_style", "Sneha Kapoor", "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150", "22 days ago", null, true, category = FollowerCategory.NEW_THIS_MONTH),
            
            FollowerUser("5", "crypto_guru_x", "Vikram Malhotra", "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150", "2 months ago", "2 days ago", false, category = FollowerCategory.UNFOLLOWED_THIS_WEEK),
            FollowerUser("6", "lisa_travels", "Lisa Ray", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150", "3 months ago", "4 days ago", false, category = FollowerCategory.UNFOLLOWED_THIS_WEEK),
            
            FollowerUser("7", "fit_karan", "Karan Johar", "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=150", "1 month ago", "18 days ago", false, category = FollowerCategory.UNFOLLOWED_THIS_MONTH),
            FollowerUser("8", "maya_art", "Maya Patel", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150", "2 months ago", "25 days ago", false, category = FollowerCategory.UNFOLLOWED_THIS_MONTH),

            FollowerUser("9", "celebrity_news_daily", "Global Trends", "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150", "4 months ago", null, false, category = FollowerCategory.NOT_FOLLOWING_BACK),
            FollowerUser("10", "startup_digest", "Startup Digest IN", "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150", "5 months ago", null, false, category = FollowerCategory.NOT_FOLLOWING_BACK),

            FollowerUser("11", "ghost_acc_99", "Inactive Account", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150", "1 year ago", null, true, isGhostFollower = true, activityScore = 12, category = FollowerCategory.GHOST_INACTIVE),
            FollowerUser("12", "bot_checker_x", "No Posts Yet", "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150", "8 months ago", null, true, isGhostFollower = true, activityScore = 8, category = FollowerCategory.GHOST_INACTIVE)
        )
    }

    fun getProfileVisitors(): List<ProfileVisitor> {
        return listOf(
            ProfileVisitor("v1", "secret_admirer_mumbai", "Anonymous User 📍 Mumbai", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150", visitCount = 28, lastVisitedTime = "12 mins ago", isAnonymous = true, secretScore = 98),
            ProfileVisitor("v2", "ex_connection_01", "Hidden Visitor", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150", visitCount = 19, lastVisitedTime = "42 mins ago", isAnonymous = true, secretScore = 94),
            ProfileVisitor("v3", "tanya_designs", "Tanya Sen", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150", visitCount = 14, lastVisitedTime = "2 hours ago", isAnonymous = false, secretScore = 89),
            ProfileVisitor("v4", "brand_scout_agency", "Media Scout Agency", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150", visitCount = 11, lastVisitedTime = "5 hours ago", isAnonymous = false, secretScore = 86),
            ProfileVisitor("v5", "anonymous_viewer_delhi", "Hidden Visitor 📍 Delhi", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150", visitCount = 8, lastVisitedTime = "Yesterday", isAnonymous = true, secretScore = 81)
        )
    }

    fun getBlockedAccounts(): List<FollowerUser> {
        return listOf(
            FollowerUser("b1", "rival_creator_x", "Anonymous Competitor", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150", "Blocked detected 3 days ago", isBlocked = true, isFollowingBack = false, category = FollowerCategory.BLOCKED_YOU),
            FollowerUser("b2", "shadow_user_99", "Suspicious Silent Account", "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150", "Blocked detected 1 week ago", isBlocked = true, isFollowingBack = false, category = FollowerCategory.BLOCKED_YOU)
        )
    }

    // Auto Comments Room Flow
    val autoComments: Flow<List<AutoCommentItem>> = dao.getAllAutoComments().map { list ->
        list.map {
            AutoCommentItem(
                id = it.id,
                targetPostTitle = it.targetPostTitle,
                commentText = it.commentText,
                scheduledTimeMs = it.scheduledTimeMs,
                scheduledTimeFormatted = it.scheduledTimeFormatted,
                triggerKeyword = it.triggerKeyword,
                isReel = it.isReel,
                status = it.status
            )
        }
    }

    suspend fun addAutoComment(
        targetPostTitle: String,
        commentText: String,
        scheduledTimeFormatted: String,
        triggerKeyword: String,
        isReel: Boolean
    ) {
        dao.insertAutoComment(
            AutoCommentEntity(
                targetPostTitle = targetPostTitle,
                commentText = commentText,
                scheduledTimeMs = System.currentTimeMillis() + 3600000,
                scheduledTimeFormatted = scheduledTimeFormatted,
                triggerKeyword = triggerKeyword,
                isReel = isReel,
                status = "Scheduled"
            )
        )
    }

    suspend fun toggleAutoCommentStatus(id: Long, currentStatus: String) {
        val newStatus = if (currentStatus == "Scheduled") "Paused" else "Scheduled"
        dao.updateAutoCommentStatus(id, newStatus)
    }

    suspend fun deleteAutoComment(id: Long) {
        dao.deleteAutoComment(id)
    }

    // Search History Room Flow
    val searchHistory: Flow<List<SearchHistoryItem>> = dao.getRecentSearches().map { list ->
        list.map {
            SearchHistoryItem(
                id = it.id,
                handle = it.handle,
                fullName = it.fullName,
                isPrivate = it.isPrivate,
                searchedAt = it.searchedAt
            )
        }
    }

    suspend fun clearSearchHistory() {
        dao.clearSearchHistory()
    }

    // Universal Search Proxy Engine
    suspend fun searchProfileWithProxy(handleInput: String): TargetAccount {
        val cleanHandle = handleInput.trim().removePrefix("@")
        
        // Save to DB search history
        val isPrivateMock = cleanHandle.contains("private", ignoreCase = true) || cleanHandle.length % 2 == 1
        dao.insertSearchHistory(
            SearchHistoryEntity(
                handle = cleanHandle,
                fullName = if (cleanHandle.isEmpty()) "Unknown" else cleanHandle.replaceFirstChar { it.uppercase() } + " Official",
                isPrivate = isPrivateMock,
                searchedAt = System.currentTimeMillis()
            )
        )

        // Simulate network proxy bypass delay
        delay(1200)

        val formattedName = cleanHandle.replace("_", " ").split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }

        val avatar = when (cleanHandle.lowercase()) {
            "cristiano" -> "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400"
            "taylorswift" -> "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400"
            "travel_bug" -> "https://images.unsplash.com/photo-1527631746610-bca00a040d60?w=400"
            else -> "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400"
        }

        val isPrivateAccount = isPrivateMock

        // Generate rich posts, reels, and stories for proxy viewer
        val posts = listOf(
            MediaItem("p1", MediaType.POST, "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600", "Sunset state of mind 🌅 #sunset #vibes", 18420, 312, 124000, "2 hours ago", isPrivateBypassed = isPrivateAccount),
            MediaItem("p2", MediaType.POST, "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=600", "Stargazing in the mountains 🏔️✨", 24900, 520, 198000, "1 day ago", isPrivateBypassed = isPrivateAccount),
            MediaItem("p3", MediaType.POST, "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=600", "Wanderlust adventures! ✈️ #travel", 31200, 890, 240000, "3 days ago", isPrivateBypassed = isPrivateAccount),
            MediaItem("p4", MediaType.POST, "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=600", "Road trip highlights 🚗", 14200, 210, 98000, "5 days ago", isPrivateBypassed = isPrivateAccount),
            MediaItem("p5", MediaType.POST, "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?w=600", "Floating through paradise 🌊", 42100, 1120, 310000, "1 week ago", isPrivateBypassed = isPrivateAccount),
            MediaItem("p6", MediaType.POST, "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=600", "Chasing waterfalls in Bali 🌿", 58400, 1420, 420000, "2 weeks ago", isPrivateBypassed = isPrivateAccount)
        )

        val reels = listOf(
            MediaItem("r1", MediaType.REEL, "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600", "Hidden gem cafe tour! ☕🎥 #reels", 89400, 1840, 680000, "Yesterday", isPrivateBypassed = isPrivateAccount),
            MediaItem("r2", MediaType.REEL, "https://images.unsplash.com/photo-1492691527719-9d1e07e534b4?w=600", "POV: Morning routine in Tokyo 🗼", 142000, 3400, 1240000, "4 days ago", isPrivateBypassed = isPrivateAccount),
            MediaItem("r3", MediaType.REEL, "https://images.unsplash.com/photo-1518173946687-a4c8a383392e?w=600", "3 secret photography hacks! 📸", 210000, 5100, 2100000, "1 week ago", isPrivateBypassed = isPrivateAccount)
        )

        val stories = listOf(
            MediaItem("s1", MediaType.STORY, "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=600", "Live from studio 🎧", 0, 0, 14200, "30m ago", isPrivateBypassed = isPrivateAccount),
            MediaItem("s2", MediaType.STORY, "https://images.unsplash.com/photo-1533105079780-92b9be482077?w=600", "Coffee time ☕", 0, 0, 18900, "2h ago", isPrivateBypassed = isPrivateAccount),
            MediaItem("s3", MediaType.STORY, "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=600", "Dinner venue! 🍷", 0, 0, 22100, "5h ago", isPrivateBypassed = isPrivateAccount)
        )

        return TargetAccount(
            handle = cleanHandle,
            fullName = if (formattedName.isEmpty()) "Private User" else formattedName,
            bio = if (isPrivateAccount) "🔒 [Private Account - Server Proxy Decrypted] | Creator & Digital Nomad ✨ | Inquiries in DM" else "Creator & Visual Storyteller ✨ | DM for business collabs 📩",
            avatarUrl = avatar,
            isPrivate = isPrivateAccount,
            isBypassedByProxy = true,
            followersCount = if (cleanHandle == "cristiano") 642000000 else if (isPrivateAccount) 14200 else 84200,
            followingCount = 380,
            postsCount = posts.size + reels.size,
            posts = posts,
            reels = reels,
            stories = stories
        )
    }
}
