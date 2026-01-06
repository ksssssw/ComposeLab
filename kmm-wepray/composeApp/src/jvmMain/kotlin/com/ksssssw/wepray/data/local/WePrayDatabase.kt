package com.ksssssw.wepray.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ksssssw.wepray.data.local.dao.AppSettingsDao
import com.ksssssw.wepray.data.local.dao.CategoryDao
import com.ksssssw.wepray.data.local.dao.DeepLinkHistoryDao
import com.ksssssw.wepray.data.local.dao.FavoriteAppDao
import com.ksssssw.wepray.data.local.entity.AppSettingsEntity
import com.ksssssw.wepray.data.local.entity.CategoryEntity
import com.ksssssw.wepray.data.local.entity.DeepLinkHistoryEntity
import com.ksssssw.wepray.data.local.entity.FavoriteAppEntity

/**
 * WePray 앱의 Room Database
 * 
 * Version History:
 * - v1: 초기 버전 (screenshotSavePath만 저장)
 * - v2: apkFolderPath, lastSelectedTab 필드 추가
 * - v3: Deep Link Tester 테이블 추가 (favorite_apps, categories, deep_link_history)
 */
@Database(
    entities = [
        AppSettingsEntity::class,
        FavoriteAppEntity::class,
        CategoryEntity::class,
        DeepLinkHistoryEntity::class
    ],
    version = 3,
    exportSchema = true
)
abstract class WePrayDatabase : RoomDatabase() {
    abstract fun appSettingsDao(): AppSettingsDao
    abstract fun favoriteAppDao(): FavoriteAppDao
    abstract fun categoryDao(): CategoryDao
    abstract fun deepLinkHistoryDao(): DeepLinkHistoryDao
}

