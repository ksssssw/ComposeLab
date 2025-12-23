package com.ksssssw.wepray.domain.model

/**
 * Android 디바이스 정보를 나타내는 도메인 모델
 * 
 * @property serialNumber 디바이스 시리얼 넘버 (고유 식별자)
 * @property modelName 디바이스 모델명 (예: SM-A528N)
 * @property manufacturer 제조사 (예: Samsung, Google)
 * @property resolution 화면 해상도 (예: 1080x2400)
 * @property androidVersion Android 버전 (예: 14)
 * @property sdkVersion Android SDK 버전 (예: 34)
 * @property status 디바이스 연결 상태
 */
data class Device(
    val serialNumber: String,
    val modelName: String,
    val manufacturer: String,
    val resolution: String,
    val androidVersion: String,
    val sdkVersion: String,
    val status: DeviceStatus
)

/**
 * 디바이스 연결 상태
 */
enum class DeviceStatus {
    /** 정상 연결됨 */
    CONNECTED,
    
    /** 오프라인 상태 */
    OFFLINE,
    
    /** 권한 없음 (USB 디버깅 승인 대기) */
    UNAUTHORIZED,
    
    /** 부트로더 모드 */
    BOOTLOADER,
    
    /** 복구 모드 */
    RECOVERY,
    
    /** 알 수 없는 상태 */
    UNKNOWN
}
