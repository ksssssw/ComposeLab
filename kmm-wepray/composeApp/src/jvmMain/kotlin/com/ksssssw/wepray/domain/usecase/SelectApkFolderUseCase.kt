package com.ksssssw.wepray.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import kotlin.coroutines.resume

/**
 * APK 폴더를 선택하는 UseCase
 * 
 * JFileChooser를 사용하여 디렉토리를 선택합니다.
 * EDT(Event Dispatch Thread)에서 안전하게 실행됩니다.
 */
class SelectApkFolderUseCase {
    
    /**
     * 폴더 선택 다이얼로그를 표시하고 선택한 경로를 반환합니다.
     * 
     * @param currentPath 현재 선택된 경로 (시작 경로로 사용)
     * @return 성공 시 선택한 경로, 취소 시 null, 실패 시 에러
     */
    suspend operator fun invoke(currentPath: String? = null): Result<String?> {
        return try {
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
                            
                            dialogTitle = "APK 폴더 선택"
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
            
            if (selectedPath != null) {
                println("✅ APK folder selected: $selectedPath")
                Result.success(selectedPath)
            } else {
                println("ℹ️ APK folder selection cancelled")
                Result.success(null)
            }
            
        } catch (e: Exception) {
            println("❌ Failed to select APK folder: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

