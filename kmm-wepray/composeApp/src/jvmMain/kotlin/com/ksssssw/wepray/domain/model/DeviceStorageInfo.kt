package com.ksssssw.wepray.domain.model

/**
 * 디바이스 스토리지 정보를 나타내는 도메인 모델
 *
 * @property totalGB 전체 용량 (GB)
 * @property usedGB 사용 중인 용량 (GB)
 * @property availableGB 사용 가능한 용량 (GB)
 * @property usedPercentage 사용률 (%)
 * @property appsPercentage 앱이 차지하는 비율 (0.0 ~ 1.0)
 * @property mediaPercentage 미디어가 차지하는 비율 (0.0 ~ 1.0)
 * @property systemPercentage 시스템이 차지하는 비율 (0.0 ~ 1.0)
 */
data class DeviceStorageInfo(
    val totalGB: Double,
    val usedGB: Double,
    val availableGB: Double,
    val usedPercentage: Int,
    val appsPercentage: Float,
    val mediaPercentage: Float,
    val systemPercentage: Float
) {
    /**
     * 포맷된 전체 용량 문자열
     */
    fun getFormattedTotal(): String = String.format("%.0f GB", totalGB)

    /**
     * 포맷된 사용 중인 용량 문자열
     */
    fun getFormattedUsed(): String = String.format("%.1f GB", usedGB)

    /**
     * 포맷된 사용 가능한 용량 문자열
     */
    fun getFormattedAvailable(): String = String.format("%.1f GB", availableGB)

    /**
     * 포맷된 용량 범위 문자열 (예: "70 GB / 128 GB")
     */
    fun getFormattedRange(): String = "${getFormattedUsed()} / ${getFormattedTotal()}"

    /**
     * 포맷된 남은 용량 문자열 (예: "58 GB Free")
     */
    fun getFormattedFree(): String = "${getFormattedAvailable()} Free"
}

