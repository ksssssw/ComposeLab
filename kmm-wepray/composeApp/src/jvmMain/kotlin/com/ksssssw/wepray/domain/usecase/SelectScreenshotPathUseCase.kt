package com.ksssssw.wepray.domain.usecase

import com.ksssssw.wepray.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import kotlin.coroutines.resume

/**
 * 스크린샷 저장 경로를 선택하는 UseCase
 * 
 * JFileChooser를 사용하여 디렉토리를 선택하고 DB에 저장합니다.
 * EDT(Event Dispatch Thread)에서 안전하게 실행됩니다.
 * 
 * @property settingsRepository 설정 저장소
 */
class SelectScreenshotPathUseCase(
    private val settingsRepository: SettingsRepository
) {
    
    /**
     * 파일 선택 다이얼로그를 표시하고 선택한 경로를 저장합니다.
     * 
     * @return 성공 시 선택한 경로, 취소 시 null, 실패 시 에러
     */
    suspend operator fun invoke(): Result<String?> {
        return try {
            // 현재 설정된 경로 가져오기 (IO에서)
            val currentSettings = withContext(Dispatchers.IO) {
                settingsRepository.getSettings()
            }
            val currentPath = currentSettings.screenshotSavePath
            
            // EDT에서 파일 선택 다이얼로그 표시
            val selectedPath = suspendCancellableCoroutine { continuation ->
                SwingUtilities.invokeLater {
                    try {
                        val fileChooser = JFileChooser().apply {
                            // 현재 경로가 있다면 시작 경로로 설정
                            currentDirectory = if (currentPath != null && File(currentPath).exists()) {
                                File(currentPath)
                            } else {
                                // 기본 경로로 시작
                                File(System.getProperty("user.home"))
                            }
                            
                            dialogTitle = "스크린샷 저장 경로 선택"
                            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                            isAcceptAllFileFilterUsed = false
                        }
                        
                        val result = fileChooser.showDialog(null, "선택")
                        
                        if (result == JFileChooser.APPROVE_OPTION) {
                            val path = fileChooser.selectedFile.absolutePath
                            continuation.resume(path)
                        } else {
                            // 사용자가 취소함
                            continuation.resume(null)
                        }
                    } catch (e: Exception) {
                        println("❌ Error showing file chooser: ${e.message}")
                        continuation.resume(null)
                    }
                }
            }
            
            // 선택한 경로가 있으면 DB에 저장 (IO에서)
            if (selectedPath != null) {
                withContext(Dispatchers.IO) {
                    settingsRepository.updateScreenshotSavePath(selectedPath)
                }
                println("✅ Screenshot path selected and saved: $selectedPath")
                Result.success(selectedPath)
            } else {
                println("ℹ️ Screenshot path selection cancelled")
                Result.success(null)
            }
            
        } catch (e: Exception) {
            println("❌ Failed to select screenshot path: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

