package com.ksssssw.wepray.ui.scene.devices

import com.ksssssw.wepray.domain.model.Device

/**
 * Devices 화면의 UI 상태를 나타냅니다.
 * NowInAndroid 스타일의 UiState 패턴 사용
 */
sealed interface DevicesUiState {
    /**
     * 로딩 중 상태
     */
    data object Loading : DevicesUiState
    
    /**
     * 성공 상태
     * 
     * @property devices 연결된 디바이스 목록
     */
    data class Success(
        val devices: List<Device> = emptyList(),
        val selectedDevice: Device? = null
    ) : DevicesUiState
    
    /**
     * 에러 상태
     * 
     * @property message 에러 메시지
     */
    data class Error(
        val message: String
    ) : DevicesUiState
}

/**
 * 디바이스 화면에서 발생할 수 있는 사용자 이벤트
 */
//sealed interface DevicesEvent {
//    /**
//     * 새로고침 버튼 클릭
//     */
//    data object Refresh : DevicesEvent
//
//    /**
//     * 디바이스 선택
//     *
//     * @property device 선택된 디바이스
//     */
//    data class SelectDevice(val device: Device) : DevicesEvent
//}