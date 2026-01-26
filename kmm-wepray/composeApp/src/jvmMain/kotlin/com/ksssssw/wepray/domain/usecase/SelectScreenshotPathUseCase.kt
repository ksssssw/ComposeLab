package com.ksssssw.wepray.domain.usecase

import com.ksssssw.wepray.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.swing.SwingUtilities
import kotlin.coroutines.resume

/**
 * 스크린샷 저장 경로를 선택하는 UseCase
 * 
 * 네이티브 macOS FileDialog를 사용하여 디렉토리를 선택하고 DB에 저장합니다.
 * - JFileChooser보다 훨씬 모던한 UI
 * - 한글 경로 처리 완벽 지원
 * - macOS 시스템 다이얼로그 사용
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
                        // macOS 네이티브 폴더 선택 다이얼로그 설정
                        System.setProperty("apple.awt.fileDialogForDirectories", "true")
                        
                        val fileDialog = FileDialog(null as Frame?, "스크린샷 저장 경로 선택", FileDialog.LOAD).apply {
                            // 시작 경로 설정
                            if (currentPath != null && File(currentPath).exists()) {
                                directory = currentPath
                            } else {
                                directory = System.getProperty("user.home")
                            }
                        }
                        
                        fileDialog.isVisible = true
                        
                        // 다이얼로그가 닫힌 후
                        System.setProperty("apple.awt.fileDialogForDirectories", "false")
                        
                        val selectedDir = fileDialog.directory
                        val selectedFile = fileDialog.file
                        
                        if (selectedDir != null && selectedFile != null) {
                            // FileDialog는 directory와 file을 따로 반환
                            val path = File(selectedDir, selectedFile).absolutePath
                            continuation.resume(path)
                        } else {
                            // 사용자가 취소함
                            continuation.resume(null)
                        }
                    } catch (e: Exception) {
                        println("❌ Error showing file dialog: ${e.message}")
                        System.setProperty("apple.awt.fileDialogForDirectories", "false")
                        continuation.resume(null)
                    }
                }
            }
            
            // 선택한 경로가 있으면 DB에 저장 (IO에서)
            if (selectedPath != null) {
                // 경로 정규화 및 실제 파일 시스템 경로로 변환
                val normalizedPath = File(selectedPath).canonicalPath
                withContext(Dispatchers.IO) {
                    settingsRepository.updateScreenshotSavePath(normalizedPath)
                }
                println("✅ Screenshot path selected and saved: $normalizedPath")
                Result.success(normalizedPath)
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
