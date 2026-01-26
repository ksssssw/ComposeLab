package com.ksssssw.wepray.data.repository

import com.ksssssw.wepray.data.local.dao.CategoryDao
import com.ksssssw.wepray.data.local.dao.DeepLinkHistoryDao
import com.ksssssw.wepray.data.local.dao.FavoriteAppDao
import com.ksssssw.wepray.data.local.entity.CategoryEntity
import com.ksssssw.wepray.data.local.entity.DeepLinkHistoryEntity
import com.ksssssw.wepray.data.local.entity.FavoriteAppEntity
import com.ksssssw.wepray.domain.model.DeepLinkCategory
import com.ksssssw.wepray.domain.model.DeepLinkHistoryItem
import com.ksssssw.wepray.domain.repository.DeepLinkerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of DeepLinkerRepository
 */
class DeepLinkerRepositoryImpl(
    private val favoriteAppDao: FavoriteAppDao,
    private val categoryDao: CategoryDao,
    private val deepLinkHistoryDao: DeepLinkHistoryDao
) : DeepLinkerRepository {
    
    // Favorite Apps
    override fun getFavoriteApps(): Flow<List<String>> {
        return favoriteAppDao.getAllFavoriteApps().map { entities ->
            entities.map { it.packageName }
        }
    }
    
    override suspend fun isFavorite(packageName: String): Boolean {
        return favoriteAppDao.isFavorite(packageName)
    }
    
    override suspend fun addFavoriteApp(packageName: String, appName: String) {
        favoriteAppDao.insertFavoriteApp(
            FavoriteAppEntity(
                packageName = packageName,
                appName = appName
            )
        )
    }
    
    override suspend fun removeFavoriteApp(packageName: String) {
        favoriteAppDao.deleteFavoriteAppByPackageName(packageName)
    }
    
    // Categories
    override fun getAllCategories(): Flow<List<DeepLinkCategory>> {
        return categoryDao.getAllCategories().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun getCategoryById(id: String): DeepLinkCategory? {
        return categoryDao.getCategoryById(id)?.toDomainModel()
    }
    
    override suspend fun addCategory(category: DeepLinkCategory) {
        categoryDao.insertCategory(category.toEntity())
    }
    
    override suspend fun updateCategory(category: DeepLinkCategory) {
        categoryDao.updateCategory(category.toEntity())
    }
    
    override suspend fun deleteCategory(id: String) {
        categoryDao.deleteCategoryById(id)
    }
    
    // History
    override fun getAllHistory(): Flow<List<DeepLinkHistoryItem>> {
        return deepLinkHistoryDao.getAllHistory().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getHistoryByCategory(categoryId: String): Flow<List<DeepLinkHistoryItem>> {
        return deepLinkHistoryDao.getHistoryByCategory(categoryId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun getRecentApps(): List<Pair<String, String>> {
        return deepLinkHistoryDao.getRecentApps().map { 
            it.packageName to it.appName 
        }
    }
    
    override suspend fun addHistory(item: DeepLinkHistoryItem) {
        deepLinkHistoryDao.insertHistory(item.toEntity())
    }
    
    override suspend fun updateHistory(item: DeepLinkHistoryItem) {
        deepLinkHistoryDao.updateHistory(item.toEntity())
    }
    
    override suspend fun deleteHistory(id: String) {
        deepLinkHistoryDao.deleteHistoryById(id)
    }
    
    override suspend fun deleteAllHistory() {
        deepLinkHistoryDao.deleteAllHistory()
    }
}

// Extension functions for mapping between entity and domain model
private fun CategoryEntity.toDomainModel() = DeepLinkCategory(
    id = id,
    name = name,
    colorValue = colorHex.toLongOrNull(16) ?: 0xFF000000L  // Default to black if parsing fails
)

private fun DeepLinkCategory.toEntity() = CategoryEntity(
    id = id,
    name = name,
    colorHex = colorValue.toString(16).uppercase().padStart(8, '0')
)

private fun DeepLinkHistoryEntity.toDomainModel() = DeepLinkHistoryItem(
    id = id,
    url = url,
    packageName = packageName,
    appName = appName,
    categoryId = categoryId,
    timestamp = timestamp
)

private fun DeepLinkHistoryItem.toEntity() = DeepLinkHistoryEntity(
    id = id,
    url = url,
    packageName = packageName,
    appName = appName,
    categoryId = categoryId,
    timestamp = timestamp
)

