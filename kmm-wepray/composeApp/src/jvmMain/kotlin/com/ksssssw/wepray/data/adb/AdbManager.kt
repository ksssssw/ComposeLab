package com.ksssssw.wepray.data.adb

import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.model.DeviceStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * ADB(Android Debug Bridge) 명령을 실행하고 결과를 파싱하는 매니저 클래스
 * 
 * 프로젝트 내부의 ADB 바이너리만을 사용하도록 설계됨
 * macOS Intel/Apple Silicon 아키텍처 모두 지원
 */
class AdbManager {
    private val adbPath: String by lazy {
        initializeAdb()
    }
    
    /**
     * ADB 바이너리 초기화
     * - 프로젝트 리소스에서 ADB 복사
     * - 실행 권한 부여
     * 
     * @return ADB 바이너리 절대 경로
     */
    private fun initializeAdb(): String {
        val resourcePath = "/adb/macos/adb"
        val inputStream = this::class.java.getResourceAsStream(resourcePath)
            ?: throw IllegalStateException("ADB binary not found in resources: $resourcePath")
        
        // 임시 디렉토리에 ADB 복사
        val tempDir = File(System.getProperty("user.home"), ".wepray/adb")
        tempDir.mkdirs()
        
        val adbFile = File(tempDir, "adb")
        
        // 기존 파일이 있고 실행 가능하면 재사용
        if (adbFile.exists() && adbFile.canExecute()) {
            return adbFile.absolutePath
        }
        
        // 리소스에서 파일 복사
        inputStream.use { input ->
            adbFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        
        // 실행 권한 부여
        val chmodProcess = ProcessBuilder("chmod", "+x", adbFile.absolutePath)
            .start()
        chmodProcess.waitFor()
        
        if (!adbFile.canExecute()) {
            throw IllegalStateException("Failed to set execute permission for ADB")
        }
        
        return adbFile.absolutePath
    }
    
    /**
     * ADB 명령을 실행하고 결과를 반환합니다.
     * 
     * @param command ADB 명령
     * @return 명령 실행 결과
     */
    suspend fun executeCommand(command: AdbCommand): Result<String> = withContext(Dispatchers.IO) {
        var process: Process? = null
        try {
            val commandString = command.toCommandString()
            // 로그 출력 빈도 감소 (메모리 절약)
            if (System.getProperty("wepray.debug") == "true") {
                println("🔧 Executing ADB command: adb $commandString")
            }
            
            val processBuilder = ProcessBuilder(adbPath, *commandString.split(" ").toTypedArray())
            processBuilder.redirectErrorStream(true)
            
            process = processBuilder.start()
            
            val output = BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                reader.readText()
            }
            
            val exitCode = process.waitFor()
            
            if (exitCode == 0) {
                Result.success(output)
            } else {
                val errorMsg = "ADB command failed with exit code: $exitCode"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            // 프로세스 리소스 완전히 정리
            process?.let {
                try {
                    it.inputStream.close()
                    it.outputStream.close()
                    it.errorStream.close()
                    it.destroy()
                    // 강제 종료 대기 (최대 1초)
                    if (!it.waitFor(1, java.util.concurrent.TimeUnit.SECONDS)) {
                        it.destroyForcibly()
                    }
                } catch (e: Exception) {
                    // 정리 중 예외는 무시 (이미 종료된 경우)
                }
            }
        }
    }
    
    /**
     * 연결된 디바이스 목록을 파싱합니다.
     * 
     * @param output adb devices -l 명령의 출력
     * @return 파싱된 디바이스 목록
     */
    suspend fun parseDeviceList(output: String): List<Device> = withContext(Dispatchers.Default) {
        val devices = mutableListOf<Device>()
        val lines = output.trim().split("\n")
        
        // "List of devices attached" 라인 제외
        for (line in lines.drop(1)) {
            if (line.isBlank()) continue
            
            try {
                val device = parseDeviceLine(line)
                if (device != null) {
                    devices.add(device)
                }
            } catch (e: Exception) {
                // 파싱 실패한 라인은 무시
            }
        }
        
        devices
    }
    
    /**
     * 디바이스 라인을 파싱합니다.
     * 
     * 예시 라인:
     * R3CR90XXXXXX       device product:a52sxq model:SM_A528N device:a52s transport_id:1
     * 
     * @param line 디바이스 정보 라인
     * @return 파싱된 Device 객체 또는 null
     */
    private suspend fun parseDeviceLine(line: String): Device? {
        val parts = line.trim().split(Regex("\\s+"))
        if (parts.size < 2) return null
        
        val serialNumber = parts[0]
        val statusString = parts[1]
        val status = parseDeviceStatus(statusString)
        
        // device 상태가 아니면 상세 정보를 가져올 수 없음
        if (status != DeviceStatus.CONNECTED) {
            return Device(
                serialNumber = serialNumber,
                modelName = "Unknown",
                manufacturer = "Unknown",
                resolution = "Unknown",
                androidVersion = "Unknown",
                sdkVersion = "Unknown",
                status = status
            )
        }
        
        // 라인에서 model 정보 추출 (있는 경우)
        val modelFromLine = parts.find { it.startsWith("model:") }
            ?.substringAfter("model:")
            ?.replace("_", " ")
        
        // 상세 정보 조회
        val modelName = modelFromLine ?: getDeviceProperty(serialNumber, "ro.product.model")
        val manufacturer = getDeviceProperty(serialNumber, "ro.product.manufacturer")
        val androidVersion = getDeviceProperty(serialNumber, "ro.build.version.release")
        val sdkVersion = getDeviceProperty(serialNumber, "ro.build.version.sdk")
        val resolution = getDeviceResolution(serialNumber)
        
        return Device(
            serialNumber = serialNumber,
            modelName = modelName,
            manufacturer = manufacturer,
            resolution = resolution,
            androidVersion = androidVersion,
            sdkVersion = sdkVersion,
            status = status
        )
    }
    
    /**
     * 디바이스 상태를 파싱합니다.
     */
    private fun parseDeviceStatus(statusString: String): DeviceStatus {
        return when (statusString.lowercase()) {
            "device" -> DeviceStatus.CONNECTED
            "offline" -> DeviceStatus.OFFLINE
            "unauthorized" -> DeviceStatus.UNAUTHORIZED
            "bootloader" -> DeviceStatus.BOOTLOADER
            "recovery" -> DeviceStatus.RECOVERY
            else -> DeviceStatus.UNKNOWN
        }
    }
    
    /**
     * 디바이스 속성을 조회합니다.
     * 
     * @param serialNumber 디바이스 시리얼 번호
     * @param property 속성명
     * @return 속성 값 (실패 시 "Unknown")
     */
    private suspend fun getDeviceProperty(serialNumber: String, property: String): String {
        val command = AdbCommand.GetProperty(serialNumber, property)
        val result = executeCommand(command)
        return result.getOrNull()?.trim() ?: "Unknown"
    }
    
    /**
     * 디바이스 해상도를 조회합니다.
     * 
     * @param serialNumber 디바이스 시리얼 번호
     * @return 해상도 문자열 (예: 1080x2400)
     */
    private suspend fun getDeviceResolution(serialNumber: String): String {
        val command = AdbCommand.ExecuteShell(serialNumber, "wm size")
        val result = executeCommand(command)
        val output = result.getOrNull() ?: return "Unknown"
        
        // 출력 예시: "Physical size: 1080x2400"
        val regex = Regex("Physical size: (\\d+x\\d+)")
        val match = regex.find(output)
        return match?.groupValues?.get(1) ?: "Unknown"
    }
}
