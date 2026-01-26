package com.ksssssw.wepray.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ksssssw.wepray.data.local.entity.AppSettingsEntity
import kotlinx.coroutines.flow.Flow

/**
 * 앱 설정 관련 데이터베이스 작업을 정의하는 DAO
 */
@Dao
interface AppSettingsDao {
    
    /**
     * 설정 조회
     * Flow로 반환하여 설정 변경사항을 실시간으로 감지
     */
    @Query("SELECT * FROM app_settings WHERE id = 1")
    fun getSettings(): Flow<AppSettingsEntity?>
    
    /**
     * 설정 조회 (일회성)
     */
    @Query("SELECT * FROM app_settings WHERE id = 1")
    suspend fun getSettingsOnce(): AppSettingsEntity?
    
    /**
     * 설정 삽입 (충돌시 교체)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: AppSettingsEntity)
    
    /**
     * 설정 업데이트
     */
    @Update
    suspend fun updateSettings(settings: AppSettingsEntity)
    
    /**
     * 스크린샷 저장 경로만 업데이트
     */
    @Query("UPDATE app_settings SET screenshotSavePath = :path WHERE id = 1")
    suspend fun updateScreenshotSavePath(path: String)
    
    /**
     * APK 폴더 경로만 업데이트
     */
    @Query("UPDATE app_settings SET apkFolderPath = :path WHERE id = 1")
    suspend fun updateApkFolderPath(path: String)
    
    /**
     * 마지막 선택한 탭만 업데이트
     */
    @Query("UPDATE app_settings SET lastSelectedTab = :tab WHERE id = 1")
    suspend fun updateLastSelectedTab(tab: String)
}

