package com.ksssssw.wepray.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ksssssw.wepray.data.local.entity.FavoriteAppEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for favorite apps
 */
@Dao
interface FavoriteAppDao {
    
    @Query("SELECT * FROM favorite_apps ORDER BY addedAt DESC")
    fun getAllFavoriteApps(): Flow<List<FavoriteAppEntity>>
    
    @Query("SELECT * FROM favorite_apps WHERE packageName = :packageName")
    suspend fun getFavoriteApp(packageName: String): FavoriteAppEntity?
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_apps WHERE packageName = :packageName)")
    suspend fun isFavorite(packageName: String): Boolean
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteApp(app: FavoriteAppEntity)
    
    @Delete
    suspend fun deleteFavoriteApp(app: FavoriteAppEntity)
    
    @Query("DELETE FROM favorite_apps WHERE packageName = :packageName")
    suspend fun deleteFavoriteAppByPackageName(packageName: String)
}

