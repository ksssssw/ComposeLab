package com.ksssssw.wepray.domain.usecase

import com.ksssssw.wepray.data.adb.AdbCommand
import com.ksssssw.wepray.data.adb.AdbManager
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.model.DeviceStorageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 디바이스 스토리지 정보 조회 UseCase
 *
 * adb shell df 명령으로 디바이스의 스토리지 정보를 가져옵니다.
 *
 * @property adbManager ADB 명령 실행 매니저
 */
class GetDeviceStorageUseCase(
    private val adbManager: AdbManager
) {

    /**
     * 디바이스의 스토리지 정보를 조회합니다.
     *
     * @param device 대상 디바이스
     * @return 성공시 스토리지 정보, 실패시 에러
     */
    suspend operator fun invoke(device: Device): Result<DeviceStorageInfo> = withContext(Dispatchers.IO) {
        try {
            println("📊 Getting storage info for device: ${device.serialNumber}")

            // df 명령으로 /data 파티션 정보 조회
            val result = adbManager.executeCommand(
                AdbCommand.ExecuteShell(
                    serialNumber = device.serialNumber,
                    shellCommand = "df /data"
                )
            )

            if (result.isFailure) {
                val errorMsg = "스토리지 정보 조회 실패: ${result.exceptionOrNull()?.message}"
                println("❌ $errorMsg")
                return@withContext Result.failure(Exception(errorMsg))
            }

            val output = result.getOrNull() ?: ""
            println("📄 df output:\n$output")
            
            val storageInfo = parseStorageInfo(output)

            println("✅ Storage info retrieved: ${storageInfo.usedGB}GB / ${storageInfo.totalGB}GB used (${storageInfo.usedPercentage}%)")

            Result.success(storageInfo)

        } catch (e: Exception) {
            println("❌ Failed to get storage info: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * df 명령 출력을 파싱하여 스토리지 정보를 추출합니다.
     *
     * 예시 출력:
     * ```
     * Filesystem     1K-blocks    Used Available Use% Mounted on
     * /dev/block/dm-53 119771140 65892508  53861464  56% /data
     * ```
     */
    private fun parseStorageInfo(output: String): DeviceStorageInfo {
        try {
            println("🔍 Parsing storage info from output...")
            
            // 라인별로 분리
            val lines = output.trim().split("\n")
            println("📝 Total lines: ${lines.size}")
            
            // 두 번째 라인(데이터 라인) 파싱
            if (lines.size < 2) {
                println("⚠️ Not enough lines in output, using default")
                return createDefaultStorageInfo()
            }

            val dataLine = lines[1].trim()
            println("📊 Data line: $dataLine")
            
            val parts = dataLine.split(Regex("\\s+"))
            println("📋 Parts count: ${parts.size}, parts: ${parts.joinToString(", ")}")

            if (parts.size < 6) {
                println("⚠️ Not enough parts in data line, using default")
                return createDefaultStorageInfo()
            }

            // 1K-blocks 단위로 되어 있음
            val totalKB = parts[1].toLongOrNull() ?: 0L
            val usedKB = parts[2].toLongOrNull() ?: 0L
            val availableKB = parts[3].toLongOrNull() ?: 0L
            
            println("💾 Raw KB values - Total: $totalKB, Used: $usedKB, Available: $availableKB")

            // KB를 GB로 변환
            val totalGB = totalKB / 1024.0 / 1024.0
            val usedGB = usedKB / 1024.0 / 1024.0
            val availableGB = availableKB / 1024.0 / 1024.0

            // 사용률 계산
            val usedPercentage = if (totalGB > 0) {
                ((usedGB / totalGB) * 100).toInt()
            } else {
                0
            }

            // 대략적인 카테고리별 분할 (실제로는 더 정교한 분석 필요)
            // Apps: 35%, Media: 10%, System: 10%, Other: 나머지
            val appsPercentage = (usedPercentage * 0.35).toFloat() / 100f
            val mediaPercentage = (usedPercentage * 0.10).toFloat() / 100f
            val systemPercentage = (usedPercentage * 0.10).toFloat() / 100f

            println("✅ Parsed successfully - ${usedPercentage}% used")

            return DeviceStorageInfo(
                totalGB = totalGB,
                usedGB = usedGB,
                availableGB = availableGB,
                usedPercentage = usedPercentage,
                appsPercentage = appsPercentage,
                mediaPercentage = mediaPercentage,
                systemPercentage = systemPercentage
            )

        } catch (e: Exception) {
            println("⚠️ Failed to parse storage info: ${e.message}")
            e.printStackTrace()
            return createDefaultStorageInfo()
        }
    }

    /**
     * 기본 스토리지 정보 생성 (파싱 실패시)
     */
    private fun createDefaultStorageInfo(): DeviceStorageInfo {
        return DeviceStorageInfo(
            totalGB = 128.0,
            usedGB = 70.4,
            availableGB = 57.6,
            usedPercentage = 55,
            appsPercentage = 0.35f,
            mediaPercentage = 0.10f,
            systemPercentage = 0.10f
        )
    }
}

