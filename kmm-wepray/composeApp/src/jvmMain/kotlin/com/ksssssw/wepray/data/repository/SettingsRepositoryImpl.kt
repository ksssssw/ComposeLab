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
        val currentSettings = appSettingsDao.getSettingsOnce()
        if (currentSettings == null) {
            // 설정이 없으면 새로 생성
            appSettingsDao.insertSettings(
                AppSettingsEntity(
                    id = 1,
                    screenshotSavePath = path
                )
            )
        } else {
            // 기존 설정 업데이트
            appSettingsDao.updateScreenshotSavePath(path)
        }
    }
    
    /**
     * Entity를 Domain 모델로 변환
     */
    private fun AppSettingsEntity.toDomain(): AppSettings {
        return AppSettings(
            screenshotSavePath = screenshotSavePath
        )
    }
}

