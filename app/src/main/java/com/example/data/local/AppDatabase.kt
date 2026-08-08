package com.example.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "auto_comments")
data class AutoCommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val targetPostTitle: String,
    val commentText: String,
    val scheduledTimeMs: Long,
    val scheduledTimeFormatted: String,
    val triggerKeyword: String,
    val isReel: Boolean,
    val status: String
)

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val handle: String,
    val fullName: String,
    val isPrivate: Boolean,
    val searchedAt: Long
)

@Dao
interface AnalyticsDao {
    @Query("SELECT * FROM auto_comments ORDER BY scheduledTimeMs ASC")
    fun getAllAutoComments(): Flow<List<AutoCommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutoComment(comment: AutoCommentEntity): Long

    @Query("UPDATE auto_comments SET status = :status WHERE id = :id")
    suspend fun updateAutoCommentStatus(id: Long, status: String)

    @Query("DELETE FROM auto_comments WHERE id = :id")
    suspend fun deleteAutoComment(id: Long)

    @Query("SELECT * FROM search_history ORDER BY searchedAt DESC LIMIT 10")
    fun getRecentSearches(): Flow<List<SearchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(search: SearchHistoryEntity)

    @Query("DELETE FROM search_history WHERE id = :id")
    suspend fun deleteSearchHistory(id: Long)

    @Query("DELETE FROM search_history")
    suspend fun clearSearchHistory()
}

@Database(
    entities = [AutoCommentEntity::class, SearchHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun analyticsDao(): AnalyticsDao
}
