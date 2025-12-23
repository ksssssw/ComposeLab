package com.ksssssw.wepray.data.adb

/**
 * ADB 명령어를 나타내는 sealed class
 * 확장 가능하도록 설계됨
 */
sealed class AdbCommand {
    abstract fun toCommandString(): String
    
    /**
     * 연결된 디바이스 목록 조회
     */
    data object ListDevices : AdbCommand() {
        override fun toCommandString(): String = "devices -l"
    }
    
    /**
     * 디바이스 속성 조회
     * 
     * @property serialNumber 디바이스 시리얼 번호
     * @property property 조회할 속성 (예: ro.product.model)
     */
    data class GetProperty(
        val serialNumber: String,
        val property: String
    ) : AdbCommand() {
        override fun toCommandString(): String = "-s $serialNumber shell getprop $property"
    }
    
    /**
     * APK 설치
     * 
     * @property serialNumber 디바이스 시리얼 번호
     * @property apkPath APK 파일 경로
     * @property reinstall 재설치 여부 (-r 옵션)
     */
    data class InstallApk(
        val serialNumber: String,
        val apkPath: String,
        val reinstall: Boolean = false
    ) : AdbCommand() {
        override fun toCommandString(): String {
            val reinstallOption = if (reinstall) "-r " else ""
            return "-s $serialNumber install $reinstallOption$apkPath"
        }
    }
    
    /**
     * 앱 제거
     * 
     * @property serialNumber 디바이스 시리얼 번호
     * @property packageName 패키지명
     */
    data class UninstallApp(
        val serialNumber: String,
        val packageName: String
    ) : AdbCommand() {
        override fun toCommandString(): String = "-s $serialNumber uninstall $packageName"
    }
    
    /**
     * 딥링크 전송
     * 
     * @property serialNumber 디바이스 시리얼 번호
     * @property deepLink 딥링크 URL
     */
    data class SendDeepLink(
        val serialNumber: String,
        val deepLink: String
    ) : AdbCommand() {
        override fun toCommandString(): String = 
            "-s $serialNumber shell am start -W -a android.intent.action.VIEW -d \"$deepLink\""
    }
    
    /**
     * Shell 명령 실행
     * 
     * @property serialNumber 디바이스 시리얼 번호
     * @property shellCommand Shell 명령어
     */
    data class ExecuteShell(
        val serialNumber: String,
        val shellCommand: String
    ) : AdbCommand() {
        override fun toCommandString(): String = "-s $serialNumber shell $shellCommand"
    }
}
