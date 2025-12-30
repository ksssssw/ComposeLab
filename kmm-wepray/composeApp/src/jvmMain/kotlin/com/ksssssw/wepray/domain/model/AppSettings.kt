package com.ksssssw.wepray.domain.model

/**
 * 앱 설정 도메인 모델
 */
data class AppSettings(
    /**
     * 스크린샷 저장 경로
     * null이면 기본 경로 사용 (사용자 홈 디렉토리/WePray/Screenshots)
     */
    val screenshotSavePath: String? = null
) {
    companion object {
        /**
         * 기본 스크린샷 저장 경로
         */
        fun getDefaultScreenshotPath(): String {
            val userHome = System.getProperty("user.home")
            return "$userHome/WePray/Screenshots"
        }
    }
    
    /**
     * 실제 사용할 스크린샷 저장 경로를 반환
     * null이면 기본 경로 반환
     */
    fun getEffectiveScreenshotPath(): String {
        return screenshotSavePath ?: getDefaultScreenshotPath()
    }
}

