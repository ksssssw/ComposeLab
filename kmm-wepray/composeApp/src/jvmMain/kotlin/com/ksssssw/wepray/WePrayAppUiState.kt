package com.ksssssw.wepray

import com.ksssssw.wepray.domain.model.Device

/**
 * 앱 전역 UI 상태
 * NowInAndroid 아키텍처 패턴 적용
 * 
 * 앱의 모든 화면에서 공유되는 상태를 관리합니다.
 * - 디바이스 목록 (Repository 캐시)
 * - 현재 선택된 디바이스 (모든 탭에서 공유)
 */
data class WePrayAppUiState(
    /**
     * 연결된 디바이스 목록
     */
    val devices: List<Device> = emptyList(),
    
    /**
     * 현재 선택된 디바이스
     * 모든 화면에서 이 디바이스를 대상으로 작업을 수행합니다.
     */
    val selectedDevice: Device? = null
)