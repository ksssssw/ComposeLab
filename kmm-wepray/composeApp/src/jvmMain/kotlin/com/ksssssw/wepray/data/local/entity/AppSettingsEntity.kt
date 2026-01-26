package com.ksssssw.wepray.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 앱 설정을 저장하는 Entity
 * 
 * 앱의 모든 설정을 저장합니다.
 */
@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey
    val id: Int = 1, // 단일 설정 레코드
    
    /**
     * 스크린샷 저장 경로
     * null이면 기본 경로 사용
     */
    val screenshotSavePath: String? = null,
    
    /**
     * APK 폴더 경로
     * null이면 경로가 선택되지 않은 상태
     */
    val apkFolderPath: String? = null,
    
    /**
     * 마지막으로 선택한 탭
     * null이면 기본 탭(Devices) 사용
     */
    val lastSelectedTab: String? = null
)

