package com.example.data.model

data class UserProfile(
    val username: String = "alex_creator_in",
    val fullName: String = "Alex Rivera",
    val bio: String = "Digital Creator | Tech & Travel 🚀 | Mumbai, IN 📍",
    val avatarUrl: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400",
    val postsCount: Int = 248,
    val followersCount: Int = 184200,
    val followingCount: Int = 412,
    val isVerified: Boolean = true,
    val isPrivate: Boolean = false,
    val globalRank: Int = 14890,
    val indiaRank: Int = 1420,
    val rankChange: Int = +42,
    val engagementRate: Float = 4.85f,
    val totalReach: Int = 1240500
)

data class FollowerUser(
    val id: String,
    val username: String,
    val fullName: String,
    val avatarUrl: String,
    val followedDate: String,
    val unfollowedDate: String? = null,
    val isFollowingBack: Boolean,
    val isGhostFollower: Boolean = false,
    val isBlocked: Boolean = false,
    val activityScore: Int = 85,
    val category: FollowerCategory = FollowerCategory.MUTUAL
)

enum class FollowerCategory {
    NEW_THIS_MONTH,
    UNFOLLOWED_THIS_WEEK,
    UNFOLLOWED_THIS_MONTH,
    NOT_FOLLOWING_BACK,
    GHOST_INACTIVE,
    MUTUAL,
    BLOCKED_YOU
}

data class ReachStats(
    val period: String, // "7D", "30D", "90D"
    val accountsReached: Int,
    val reachGrowthPercent: Float,
    val viewsReels: Int,
    val viewsPosts: Int,
    val impressions: Int,
    val profileVisits: Int,
    val websiteClicks: Int,
    val dailyReachPoints: List<Int>,
    val reelViewsDaily: List<Int>,
    val postViewsDaily: List<Int>,
    val labels: List<String>
)

data class ProfileVisitor(
    val id: String,
    val username: String,
    val fullName: String,
    val avatarUrl: String,
    val visitCount: Int,
    val lastVisitedTime: String,
    val isAnonymous: Boolean = true,
    val secretScore: Int = 92 // High secret admirer rating
)

data class AutoCommentItem(
    val id: Long = 0,
    val targetPostTitle: String,
    val commentText: String,
    val scheduledTimeMs: Long,
    val scheduledTimeFormatted: String,
    val triggerKeyword: String = "Collab",
    val isReel: Boolean = true,
    val status: String = "Scheduled" // "Scheduled", "Executed", "Paused"
)

data class SearchHistoryItem(
    val id: Long = 0,
    val handle: String,
    val fullName: String,
    val isPrivate: Boolean,
    val searchedAt: Long = System.currentTimeMillis()
)

data class MediaItem(
    val id: String,
    val type: MediaType,
    val thumbnailUrl: String,
    val caption: String,
    val likesCount: Int,
    val commentsCount: Int,
    val viewsCount: Int,
    val timestamp: String,
    val isPrivateBypassed: Boolean = false
)

enum class MediaType {
    POST, REEL, STORY, HIGHLIGHT
}

data class TargetAccount(
    val handle: String,
    val fullName: String,
    val bio: String,
    val avatarUrl: String,
    val isPrivate: Boolean,
    val isBypassedByProxy: Boolean = true,
    val followersCount: Int,
    val followingCount: Int,
    val postsCount: Int,
    val posts: List<MediaItem>,
    val reels: List<MediaItem>,
    val stories: List<MediaItem>
)
