package com.ksssssw.wepray.domain.repository

import com.ksssssw.wepray.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

/**
 * 앱 설정 관련 데이터 작업을 추상화하는 Repository 인터페이스
 * 
 * Single Source of Truth:
 * - 앱 설정 (스크린샷 경로 등)
 */
interface SettingsRepository {
    
    /**
     * 앱 설정을 실시간으로 관찰합니다.
     * 
     * @return 앱 설정의 Flow
     */
    fun observeSettings(): Flow<AppSettings>
    
    /**
     * 현재 앱 설정을 가져옵니다.
     * 
     * @return 앱 설정
     */
    suspend fun getSettings(): AppSettings
    
    /**
     * 스크린샷 저장 경로를 업데이트합니다.
     * 
     * @param path 새로운 저장 경로
     */
    suspend fun updateScreenshotSavePath(path: String)
}

