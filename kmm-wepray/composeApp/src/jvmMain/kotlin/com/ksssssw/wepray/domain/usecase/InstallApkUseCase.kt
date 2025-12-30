package com.ksssssw.wepray.domain.usecase

import com.ksssssw.wepray.data.adb.AdbCommand
import com.ksssssw.wepray.data.adb.AdbManager
import com.ksssssw.wepray.domain.model.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * APK를 디바이스에 설치하는 UseCase
 * 
 * @property adbManager ADB 매니저
 */
class InstallApkUseCase(
    private val adbManager: AdbManager
) {
    
    /**
     * APK 파일을 디바이스에 설치합니다.
     * 
     * @param device 대상 디바이스
     * @param apkPath APK 파일 경로
     * @param reinstall 재설치 여부 (기존 앱 덮어쓰기)
     * @return 성공 시 true, 실패 시 에러
     */
    suspend operator fun invoke(
        device: Device,
        apkPath: String,
        reinstall: Boolean = true
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            println("📦 Installing APK: $apkPath to ${device.modelName} (${device.serialNumber})")
            
            // APK 파일 존재 확인
            val apkFile = java.io.File(apkPath)
            if (!apkFile.exists()) {
                return@withContext Result.failure(
                    IllegalArgumentException("APK 파일을 찾을 수 없습니다: $apkPath")
                )
            }
            
            if (!apkFile.extension.equals("apk", ignoreCase = true)) {
                return@withContext Result.failure(
                    IllegalArgumentException("APK 파일이 아닙니다: $apkPath")
                )
            }
            
            // ADB 명령으로 APK 설치
            val command = AdbCommand.InstallApk(
                serialNumber = device.serialNumber,
                apkPath = apkPath,
                reinstall = reinstall
            )
            
            val result = adbManager.executeCommand(command)
            
            if (result.isSuccess) {
                val output = result.getOrNull() ?: ""
                
                // 설치 성공 확인 (adb install 출력에 "Success" 포함)
                if (output.contains("Success", ignoreCase = true)) {
                    println("✅ APK installed successfully")
                    Result.success(true)
                } else {
                    // 실패 메시지 파싱
                    val errorMsg = when {
                        output.contains("INSTALL_FAILED_ALREADY_EXISTS") ->
                            "이미 설치된 앱입니다"
                        output.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE") ->
                            "저장 공간이 부족합니다"
                        output.contains("INSTALL_FAILED_INVALID_APK") ->
                            "올바르지 않은 APK 파일입니다"
                        output.contains("INSTALL_PARSE_FAILED") ->
                            "APK 파일 파싱에 실패했습니다"
                        else ->
                            "APK 설치에 실패했습니다: $output"
                    }
                    println("❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } else {
                val error = result.exceptionOrNull()
                println("❌ APK installation failed: ${error?.message}")
                Result.failure(error ?: Exception("APK 설치에 실패했습니다"))
            }
            
        } catch (e: Exception) {
            println("❌ Exception during APK installation: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

