package com.ksssssw.wepray.domain.usecase

import com.ksssssw.wepray.data.adb.AdbCommand
import com.ksssssw.wepray.data.adb.AdbManager
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 스크린샷 촬영 UseCase
 *
 * 1. 저장 경로 확인 (없으면 선택 요청)
 * 2. 디바이스에서 스크린샷 촬영
 * 3. PC로 파일 복사
 * 4. 디바이스에서 임시 파일 삭제
 *
 * @property adbManager ADB 명령 실행 매니저
 * @property settingsRepository 설정 저장소 (스크린샷 경로 관리)
 * @property selectScreenshotPathUseCase 경로 선택 UseCase
 */
class TakeScreenshotUseCase(
    private val adbManager: AdbManager,
    private val settingsRepository: SettingsRepository,
    private val selectScreenshotPathUseCase: SelectScreenshotPathUseCase,
) {

    /**
     * 스크린샷을 촬영하고 PC에 저장합니다.
     *
     * @param device 대상 디바이스
     * @return 성공시 저장된 파일 경로, 경로 미선택시 null, 실패시 에러
     */
    suspend operator fun invoke(device: Device): Result<String?> = withContext(Dispatchers.IO) {
        try {
            // 1. 저장 경로 확인
            val settings = settingsRepository.getSettings()
            var savePath = settings.screenshotSavePath

            // 저장 경로가 없거나 유효하지 않으면 선택 요청
            if (savePath == null || !File(savePath).exists()) {
                println("📂 No screenshot path configured, requesting selection...")

                val selectionResult = selectScreenshotPathUseCase()
                if (selectionResult.isFailure) {
                    return@withContext Result.failure(
                        Exception("경로 선택 실패: ${selectionResult.exceptionOrNull()?.message}")
                    )
                }

                savePath = selectionResult.getOrNull()

                // 사용자가 경로 선택을 취소한 경우
                if (savePath == null) {
                    println("ℹ️ User cancelled path selection")
                    return@withContext Result.success(null)
                }
            }

            // 2. 저장 디렉토리 생성 (필요시)
            val saveDir = File(savePath)
            if (!saveDir.exists()) {
                saveDir.mkdirs()
            }

            // 3. 타임스탬프 기반 파일명 생성
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "${device.serialNumber}_$timestamp.png"
            val localFilePath = File(saveDir, fileName).absolutePath

            // 디바이스 내부 임시 경로
            val devicePath = "/sdcard/wepray_temp_screenshot.png"

            println("📸 Taking screenshot for device: ${device.serialNumber}")

            // 4. 디바이스에서 스크린샷 촬영
            val screenshotResult = adbManager.executeCommand(
                AdbCommand.TakeScreenshot(
                    serialNumber = device.serialNumber,
                    devicePath = devicePath
                )
            )

            if (screenshotResult.isFailure) {
                return@withContext Result.failure(
                    Exception("스크린샷 촬영 실패: ${screenshotResult.exceptionOrNull()?.message}")
                )
            }

            println("✅ Screenshot captured on device")

            // 5. PC로 파일 복사
            val pullResult = adbManager.executeCommand(
                AdbCommand.PullFile(
                    serialNumber = device.serialNumber,
                    modelName = device.modelName,
                    devicePath = devicePath,
                    localPath = localFilePath
                )
            )

            if (pullResult.isFailure) {
                // 실패해도 디바이스에서 임시 파일 삭제 시도
                cleanupDeviceFile(device.serialNumber, devicePath)
                return@withContext Result.failure(
                    Exception("파일 복사 실패: ${pullResult.exceptionOrNull()?.message}")
                )
            }

            println("✅ Screenshot saved to: $localFilePath")

            // 6. 디바이스에서 임시 파일 삭제
            cleanupDeviceFile(device.serialNumber, devicePath)

            // 저장된 파일이 실제로 존재하는지 확인
            val savedFile = File(localFilePath)
            if (!savedFile.exists()) {
                return@withContext Result.failure(
                    Exception("파일이 저장되지 않았습니다: $localFilePath")
                )
            }

            Result.success(localFilePath)

        } catch (e: Exception) {
            println("❌ Screenshot failed: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * 디바이스에서 임시 파일 삭제
     */
    private suspend fun cleanupDeviceFile(serialNumber: String, devicePath: String) {
        try {
            adbManager.executeCommand(
                AdbCommand.ExecuteShell(
                    serialNumber = serialNumber,
                    shellCommand = "rm $devicePath"
                )
            )
            println("🧹 Cleaned up temporary file on device")
        } catch (e: Exception) {
            println("⚠️ Failed to cleanup temporary file: ${e.message}")
        }
    }
}

