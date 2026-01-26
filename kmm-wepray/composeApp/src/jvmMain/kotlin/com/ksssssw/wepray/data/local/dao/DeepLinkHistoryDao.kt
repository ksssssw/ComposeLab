package com.ksssssw.wepray.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ksssssw.wepray.data.local.entity.DeepLinkHistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for deep link history
 */
@Dao
interface DeepLinkHistoryDao {
    
    @Query("SELECT * FROM deep_link_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<DeepLinkHistoryEntity>>
    
    @Query("SELECT * FROM deep_link_history WHERE categoryId = :categoryId ORDER BY timestamp DESC")
    fun getHistoryByCategory(categoryId: String): Flow<List<DeepLinkHistoryEntity>>
    
    @Query("SELECT * FROM deep_link_history WHERE id = :id")
    suspend fun getHistoryById(id: String): DeepLinkHistoryEntity?
    
    @Query("SELECT DISTINCT packageName, appName FROM deep_link_history ORDER BY timestamp DESC LIMIT 3")
    suspend fun getRecentApps(): List<RecentApp>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: DeepLinkHistoryEntity)
    
    @Update
    suspend fun updateHistory(history: DeepLinkHistoryEntity)
    
    @Delete
    suspend fun deleteHistory(history: DeepLinkHistoryEntity)
    
    @Query("DELETE FROM deep_link_history WHERE id = :id")
    suspend fun deleteHistoryById(id: String)
    
    @Query("DELETE FROM deep_link_history")
    suspend fun deleteAllHistory()
}

/**
 * Data class for recent apps query
 */
data class RecentApp(
    val packageName: String,
    val appName: String
)

