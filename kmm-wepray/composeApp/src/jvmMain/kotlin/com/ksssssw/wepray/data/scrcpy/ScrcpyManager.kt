package com.ksssssw.wepray.data.scrcpy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * scrcpy 실행을 관리하는 매니저 클래스
 * 
 * CPU 아키텍처(Intel/Apple Silicon)를 자동 감지하여 적절한 scrcpy 바이너리를 사용합니다.
 * 프로젝트 내부 리소스의 scrcpy만 사용하도록 설계되었습니다.
 * 
 * 성능 최적화:
 * - 아키텍처 감지 결과 캐싱
 * - 디렉토리 생성 중복 제거
 * - 파일 초기화 한 번에 처리
 */
class ScrcpyManager {
    // 아키텍처를 한 번만 감지하고 캐싱
    private val architecture: String by lazy {
        detectArchitecture()
    }
    
    // 작업 디렉토리를 한 번만 생성하고 캐싱
    private val workingDirectory: File by lazy {
        File(System.getProperty("user.home"), ".wepray/scrcpy/$architecture").apply {
            mkdirs()
        }
    }
    
    private val scrcpyPath: String by lazy {
        initializeScrcpy()
    }
    
    private val scrcpyServerPath: String by lazy {
        initializeScrcpyServer()
    }
    
    private val adbPath: String by lazy {
        initializeAdb()
    }
    
    /**
     * CPU 아키텍처를 감지하여 적절한 scrcpy 바이너리를 초기화합니다.
     * 
     * @return scrcpy 실행 파일 절대 경로
     */
    private fun initializeScrcpy(): String {
        val resourcePath = "/adb/macos/scrcpy-macos-$architecture-v3.3.4/scrcpy"
        val scrcpyFile = File(workingDirectory, "scrcpy")
        
        // 기존 파일이 있고 실행 가능하면 재사용 (빠른 경로)
        if (scrcpyFile.exists() && scrcpyFile.canExecute()) {
            return scrcpyFile.absolutePath
        }
        
        // 리소스에서 파일 복사
        val inputStream = this::class.java.getResourceAsStream(resourcePath)
            ?: throw IllegalStateException("scrcpy binary not found in resources: $resourcePath")
        
        inputStream.use { input ->
            scrcpyFile.outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }
        
        // 실행 권한 부여 (Java API 사용 - 더 빠름)
        if (!scrcpyFile.setExecutable(true)) {
            throw IllegalStateException("Failed to set execute permission for scrcpy")
        }
        
        return scrcpyFile.absolutePath
    }
    
    /**
     * scrcpy-server 파일을 초기화합니다.
     * scrcpy가 디바이스에 설치할 서버 파일입니다.
     * 
     * @return scrcpy-server 파일 절대 경로
     */
    private fun initializeScrcpyServer(): String {
        val resourcePath = "/adb/macos/scrcpy-macos-$architecture-v3.3.4/scrcpy-server"
        val serverFile = File(workingDirectory, "scrcpy-server")
        
        // 기존 파일이 있으면 재사용 (빠른 경로)
        if (serverFile.exists() && serverFile.length() > 0) {
            return serverFile.absolutePath
        }
        
        // 리소스에서 파일 복사
        val inputStream = this::class.java.getResourceAsStream(resourcePath)
            ?: throw IllegalStateException("scrcpy-server not found in resources: $resourcePath")
        
        inputStream.use { input ->
            serverFile.outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }
        
        return serverFile.absolutePath
    }
    
    /**
     * adb 바이너리를 초기화합니다.
     * scrcpy가 디바이스와 통신하기 위해 필요합니다.
     * 
     * @return adb 실행 파일 절대 경로
     */
    private fun initializeAdb(): String {
        val resourcePath = "/adb/macos/scrcpy-macos-$architecture-v3.3.4/adb"
        val adbFile = File(workingDirectory, "adb")
        
        // 기존 파일이 있고 실행 가능하면 재사용 (빠른 경로)
        if (adbFile.exists() && adbFile.canExecute()) {
            return adbFile.absolutePath
        }
        
        // 리소스에서 파일 복사
        val inputStream = this::class.java.getResourceAsStream(resourcePath)
            ?: throw IllegalStateException("adb binary not found in resources: $resourcePath")
        
        inputStream.use { input ->
            adbFile.outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }
        
        // 실행 권한 부여 (Java API 사용 - 더 빠름)
        if (!adbFile.setExecutable(true)) {
            throw IllegalStateException("Failed to set execute permission for adb")
        }
        
        return adbFile.absolutePath
    }
    
    /**
     * CPU 아키텍처를 감지합니다.
     * 
     * @return "aarch64" (Apple Silicon) 또는 "x86_64" (Intel)
     */
    private fun detectArchitecture(): String {
        val osArch = System.getProperty("os.arch").lowercase()
        return when {
            osArch.contains("aarch64") || osArch.contains("arm") -> "aarch64"
            osArch.contains("x86_64") || osArch.contains("amd64") -> "x86_64"
            else -> {
                println("⚠️ Unknown architecture: $osArch, defaulting to x86_64")
                "x86_64"
            }
        }
    }
    
    /**
     * scrcpy 명령을 실행하여 미러링을 시작합니다.
     * 
     * 이 함수는 프로세스를 백그라운드에서 실행하고 즉시 반환됩니다.
     * scrcpy 윈도우를 닫으면 프로세스가 자동으로 종료됩니다.
     * 
     * @param command scrcpy 명령
     * @return 성공 시 Process 객체, 실패 시 예외와 함께 Result.failure
     */
    suspend fun startMirroring(command: ScrcpyCommand): Result<Process> = withContext(Dispatchers.IO) {
        try {
            val args = command.toCommandArguments()
            
            println("🔧 Starting scrcpy with arguments: $args")
            
            // 프로세스 빌더 설정 (환경 변수 한 번에 설정)
            val processBuilder = ProcessBuilder(listOf(scrcpyPath) + args).apply {
                environment().apply {
                    put("SCRCPY_SERVER_PATH", scrcpyServerPath)
                    put("ADB", adbPath)
                }
                // 출력을 부모 프로세스로 리다이렉트
                redirectOutput(ProcessBuilder.Redirect.INHERIT)
                redirectError(ProcessBuilder.Redirect.INHERIT)
            }
            
            // 백그라운드에서 실행
            val process = processBuilder.start()
            
            // 프로세스 시작 확인 (비블로킹 - 짧은 대기만)
            kotlinx.coroutines.delay(300)  // Thread.sleep 대신 코루틴 사용
            
            if (!process.isAlive) {
                val exitCode = process.exitValue()
                return@withContext Result.failure(
                    Exception("scrcpy failed to start (exit code: $exitCode)")
                )
            }
            
            println("✅ scrcpy started successfully (PID: ${process.pid()})")
            Result.success(process)
        } catch (e: Exception) {
            println("❌ Failed to start scrcpy: ${e.message}")
            Result.failure(e)
        }
    }
    
    /**
     * 실행 중인 scrcpy 프로세스를 종료합니다.
     * 
     * @param process 종료할 프로세스
     */
    suspend fun stopMirroring(process: Process) = withContext(Dispatchers.IO) {
        try {
            if (process.isAlive) {
                process.destroy()
                
                // 정상 종료 대기 (최대 2초)
                if (!process.waitFor(2, java.util.concurrent.TimeUnit.SECONDS)) {
                    process.destroyForcibly()
                }
                
                println("✅ scrcpy stopped successfully")
            }
        } catch (e: Exception) {
            println("⚠️ Error stopping scrcpy: ${e.message}")
        }
    }
}

