package com.ksssssw.wepray.data.repository

import com.ksssssw.wepray.data.local.dao.AppSettingsDao
import com.ksssssw.wepray.data.local.entity.AppSettingsEntity
import com.ksssssw.wepray.domain.model.AppSettings
import com.ksssssw.wepray.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * SettingsRepository의 구현체
 * Room Database를 사용하여 설정을 저장/조회합니다.
 * 
 * @property appSettingsDao Room DAO
 */
class SettingsRepositoryImpl(
    private val appSettingsDao: AppSettingsDao
) : SettingsRepository {
    
    override fun observeSettings(): Flow<AppSettings> {
        return appSettingsDao.getSettings().map { entity ->
            entity?.toDomain() ?: AppSettings()
        }
    }
    
    override suspend fun getSettings(): AppSettings {
        val entity = appSettingsDao.getSettingsOnce()
        return entity?.toDomain() ?: AppSettings()
    }
    
    override suspend fun updateScreenshotSavePath(path: String) {
        // UPSERT 패턴: INSERT OR REPLACE 사용으로 쿼리 1회로 감소
        ensureSettingsExists()
        appSettingsDao.updateScreenshotSavePath(path)
    }
    
    override suspend fun updateApkFolderPath(path: String) {
        // UPSERT 패턴: INSERT OR REPLACE 사용으로 쿼리 1회로 감소
        ensureSettingsExists()
        appSettingsDao.updateApkFolderPath(path)
    }
    
    override suspend fun updateLastSelectedTab(tab: String) {
        // UPSERT 패턴: INSERT OR REPLACE 사용으로 쿼리 1회로 감소
        ensureSettingsExists()
        appSettingsDao.updateLastSelectedTab(tab)
    }
    
    /**
     * 설정 레코드가 존재하는지 확인하고 없으면 생성
     * 앱 실행 시 한 번만 호출되도록 최적화
     */
    private var settingsInitialized = false
    
    private suspend fun ensureSettingsExists() {
        if (!settingsInitialized) {
            val exists = appSettingsDao.getSettingsOnce() != null
            if (!exists) {
                appSettingsDao.insertSettings(AppSettingsEntity(id = 1))
            }
            settingsInitialized = true
        }
    }
    
    /**
     * Entity를 Domain 모델로 변환
     */
    private fun AppSettingsEntity.toDomain(): AppSettings {
        return AppSettings(
            screenshotSavePath = screenshotSavePath,
            apkFolderPath = apkFolderPath,
            lastSelectedTab = lastSelectedTab
        )
    }
}

