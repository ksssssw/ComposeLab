package com.ksssssw.wepray.data.aapt

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * AAPT(Android Asset Packaging Tool) 명령을 실행하고 결과를 파싱하는 매니저 클래스
 * 
 * 프로젝트 내부의 AAPT 바이너리만을 사용하도록 설계됨
 * macOS Intel/Apple Silicon 아키텍처 모두 지원
 */
class AaptManager {
    private val aaptPath: String by lazy {
        initializeAapt()
    }
    
    /**
     * AAPT 바이너리 초기화
     * - 프로젝트 리소스에서 AAPT 복사
     * - 실행 권한 부여
     * 
     * @return AAPT 바이너리 절대 경로
     */
    private fun initializeAapt(): String {
        val resourcePath = "/adb/macos/aapt2"
        val inputStream = this::class.java.getResourceAsStream(resourcePath)
            ?: throw IllegalStateException("AAPT binary not found in resources: $resourcePath")
        
        // 임시 디렉토리에 AAPT 복사
        val tempDir = File(System.getProperty("user.home"), ".wepray/adb")
        tempDir.mkdirs()
        
        val aaptFile = File(tempDir, "aapt2")
        
        // 기존 파일이 있고 실행 가능하면 재사용
        if (aaptFile.exists() && aaptFile.canExecute()) {
            return aaptFile.absolutePath
        }
        
        // 리소스에서 파일 복사
        inputStream.use { input ->
            aaptFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        
        // 실행 권한 부여
        val chmodProcess = ProcessBuilder("chmod", "+x", aaptFile.absolutePath)
            .start()
        chmodProcess.waitFor()
        
        if (!aaptFile.canExecute()) {
            throw IllegalStateException("Failed to set execute permission for AAPT")
        }
        
        return aaptFile.absolutePath
    }
    
    /**
     * AAPT 명령을 실행하고 결과를 반환합니다.
     * 
     * @param command AAPT 명령
     * @return 명령 실행 결과
     */
    suspend fun executeCommand(command: AaptCommand): Result<String> = withContext(Dispatchers.IO) {
        var process: Process? = null
        try {
            val commandArgs = command.toCommandArgs()
            val processBuilder = ProcessBuilder(listOf(aaptPath) + commandArgs)
            processBuilder.redirectErrorStream(true)
            
            process = processBuilder.start()
            
            val output = BufferedReader(InputStreamReader(process.inputStream, Charsets.UTF_8)).use { reader ->
                reader.readText()
            }
            
            val exitCode = process.waitFor()
            
            if (exitCode == 0) {
                Result.success(output)
            } else {
                val errorMsg = "AAPT command failed with exit code: $exitCode"
                println("❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            println("❌ AAPT execution error: ${e.message}")
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
     * APK 파일에서 상세 정보를 추출합니다.
     * 
     * @param apkPath APK 파일 경로
     * @return APK 정보 (ApkInfo)
     */
    suspend fun extractApkInfo(apkPath: String): Result<ApkInfo> = withContext(Dispatchers.IO) {
        try {
            val command = AaptCommand.DumpBadging(apkPath)
            val result = executeCommand(command)
            
            if (result.isFailure) {
                return@withContext Result.failure(result.exceptionOrNull() ?: Exception("Failed to execute aapt"))
            }
            
            val output = result.getOrNull() ?: return@withContext Result.failure(Exception("Empty output"))
            val apkInfo = parseBadgingOutput(output)
            
            Result.success(apkInfo)
        } catch (e: Exception) {
            println("❌ APK parsing error: ${e.message}")
            Result.failure(e)
        }
    }
    
    /**
     * aapt dump badging 출력을 파싱합니다.
     * 
     * @param output aapt dump badging 명령의 출력
     * @return 파싱된 APK 정보
     */
    private fun parseBadgingOutput(output: String): ApkInfo {
        val lines = output.split("\n")
        
        var appName: String? = null
        var packageName: String? = null
        var versionCode: String? = null
        var versionName: String? = null
        var iconPath: String? = null
        var minSdkVersion: String? = null
        var targetSdkVersion: String? = null
        var compileSdkVersion: String? = null
        var signingKey: String? = null
        
        for (line in lines) {
            val trimmedLine = line.trim()
            
            when {
                // package: name='com.example.app' versionCode='1' versionName='1.0' compileSdkVersion='33' platformBuildVersionName='13'
                trimmedLine.startsWith("package:") -> {
                    packageName = extractValue(trimmedLine, "name")
                    versionCode = extractValue(trimmedLine, "versionCode")
                    versionName = extractValue(trimmedLine, "versionName")
                    compileSdkVersion = extractValue(trimmedLine, "compileSdkVersion")
                        ?: extractValue(trimmedLine, "platformBuildVersionCode")
                }
                // application-label:'AppName' 또는 application-label-ko:'한글앱이름'
                trimmedLine.startsWith("application-label:") && appName == null -> {
                    appName = trimmedLine.substringAfter("application-label:")
                        .trim()
                        .removeSurrounding("'")
                        .removeSurrounding("\"")
                }
                // application-icon-160:'res/mipmap-anydpi-v26/ic_launcher.xml'
                trimmedLine.startsWith("application-icon-") && iconPath == null -> {
                    iconPath = trimmedLine.substringAfter(":")
                        .trim()
                        .removeSurrounding("'")
                        .removeSurrounding("\"")
                }
                // uses-sdk: minSdkVersion='21' targetSdkVersion='33' 또는 uses-sdk:'21'
                trimmedLine.startsWith("uses-sdk:") || trimmedLine.startsWith("uses-sdk ") -> {
                    // 방법 1: minSdkVersion='21' 형태
                    val extractedMin = extractValue(trimmedLine, "minSdkVersion")
                    if (extractedMin != null) {
                        minSdkVersion = extractedMin
                    } else {
                        // 방법 2: uses-sdk:'21' 형태
                        val simpleValue = trimmedLine.substringAfter("uses-sdk:")
                            .substringAfter("uses-sdk ")
                            .trim()
                            .removeSurrounding("'")
                            .removeSurrounding("\"")
                        if (simpleValue.isNotBlank() && simpleValue.all { it.isDigit() }) {
                            minSdkVersion = simpleValue
                        }
                    }
                    
                    if (targetSdkVersion == null) {
                        targetSdkVersion = extractValue(trimmedLine, "targetSdkVersion")
                    }
                }
                // minSdkVersion:'34' 형태
                trimmedLine.startsWith("minSdkVersion:") && minSdkVersion == null -> {
                    minSdkVersion = trimmedLine.substringAfter("minSdkVersion:")
                        .trim()
                        .removeSurrounding("'")
                        .removeSurrounding("\"")
                }
                // sdkVersion:'21' (fallback)
                trimmedLine.startsWith("sdkVersion:") && minSdkVersion == null -> {
                    minSdkVersion = trimmedLine.substringAfter("sdkVersion:")
                        .trim()
                        .removeSurrounding("'")
                        .removeSurrounding("\"")
                }
                // targetSdkVersion:'36' 형태
                trimmedLine.startsWith("targetSdkVersion:") && targetSdkVersion == null -> {
                    targetSdkVersion = trimmedLine.substringAfter("targetSdkVersion:")
                        .trim()
                        .removeSurrounding("'")
                        .removeSurrounding("\"")
                }
                // platformBuildVersionCode (compileSdk)
                trimmedLine.contains("platformBuildVersionCode") && compileSdkVersion == null -> {
                    compileSdkVersion = extractValue(trimmedLine, "platformBuildVersionCode")
                }
            }
        }
        
        return ApkInfo(
            appName = appName,
            packageName = packageName,
            iconPath = iconPath,
            versionCode = versionCode,
            versionName = versionName,
            signingKey = signingKey,
            minSdkVersion = minSdkVersion,
            targetSdkVersion = targetSdkVersion,
            compileSdkVersion = compileSdkVersion
        )
    }
    
    /**
     * aapt 출력 라인에서 특정 키의 값을 추출합니다.
     * 
     * 예: "package: name='com.example' versionCode='1'" 에서
     *     extractValue(line, "name") => "com.example"
     */
    private fun extractValue(line: String, key: String): String? {
        // 작은따옴표로 감싸진 값 찾기
        val singleQuotePattern = "$key='([^']*)'".toRegex()
        val singleQuoteMatch = singleQuotePattern.find(line)
        if (singleQuoteMatch != null) {
            return singleQuoteMatch.groupValues[1]
        }
        
        // 큰따옴표로 감싸진 값 찾기
        val doubleQuotePattern = "$key=\"([^\"]*)\"".toRegex()
        val doubleQuoteMatch = doubleQuotePattern.find(line)
        if (doubleQuoteMatch != null) {
            return doubleQuoteMatch.groupValues[1]
        }
        
        return null
    }
}

/**
 * APK 정보 데이터 클래스
 */
data class ApkInfo(
    val appName: String? = null,
    val packageName: String? = null,
    val iconPath: String? = null,
    val versionCode: String? = null,
    val versionName: String? = null,
    val signingKey: String? = null,
    val minSdkVersion: String? = null,
    val targetSdkVersion: String? = null,
    val compileSdkVersion: String? = null
)

