package com.ksssssw.wepray.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ksssssw.wepray.data.local.dao.AppSettingsDao
import com.ksssssw.wepray.data.local.entity.AppSettingsEntity

/**
 * WePray 앱의 Room Database
 * 
 * 현재는 앱 설정만 저장하지만,
 * 나중에 다른 테이블들도 추가할 수 있습니다.
 * 
 * Version History:
 * - v1: 초기 버전 (screenshotSavePath만 저장)
 * - v2: apkFolderPath, lastSelectedTab 필드 추가
 */
@Database(
    entities = [
        AppSettingsEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class WePrayDatabase : RoomDatabase() {
    abstract fun appSettingsDao(): AppSettingsDao
}

