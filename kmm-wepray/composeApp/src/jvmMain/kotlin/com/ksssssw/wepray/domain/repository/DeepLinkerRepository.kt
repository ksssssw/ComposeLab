package com.ksssssw.wepray.domain.repository

import com.ksssssw.wepray.domain.model.DeepLinkCategory
import com.ksssssw.wepray.domain.model.DeepLinkHistoryItem
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Deep Link Tester
 */
interface DeepLinkerRepository {
    
    // Favorite Apps
    fun getFavoriteApps(): Flow<List<String>>
    suspend fun isFavorite(packageName: String): Boolean
    suspend fun addFavoriteApp(packageName: String, appName: String)
    suspend fun removeFavoriteApp(packageName: String)
    
    // Categories
    fun getAllCategories(): Flow<List<DeepLinkCategory>>
    suspend fun getCategoryById(id: String): DeepLinkCategory?
    suspend fun addCategory(category: DeepLinkCategory)
    suspend fun updateCategory(category: DeepLinkCategory)
    suspend fun deleteCategory(id: String)
    
    // History
    fun getAllHistory(): Flow<List<DeepLinkHistoryItem>>
    fun getHistoryByCategory(categoryId: String): Flow<List<DeepLinkHistoryItem>>
    suspend fun getRecentApps(): List<Pair<String, String>> // packageName to appName
    suspend fun addHistory(item: DeepLinkHistoryItem)
    suspend fun updateHistory(item: DeepLinkHistoryItem)
    suspend fun deleteHistory(id: String)
    suspend fun deleteAllHistory()
}

