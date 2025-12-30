package com.ksssssw.wepray.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 앱 설정을 저장하는 Entity
 * 
 * 현재는 스크린샷 저장 경로만 저장하지만,
 * 나중에 다른 설정들도 추가할 수 있습니다.
 */
@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey
    val id: Int = 1, // 단일 설정 레코드
    
    /**
     * 스크린샷 저장 경로
     * null이면 기본 경로 사용
     */
    val screenshotSavePath: String? = null
)

