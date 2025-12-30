package com.ksssssw.wepray.ui.scene.installer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.repository.DeviceRepository
import com.ksssssw.wepray.domain.usecase.InstallApkUseCase
import com.ksssssw.wepray.domain.usecase.SelectApkFolderUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Installer 화면의 ViewModel
 * 
 * APK 파일 목록 관리, 필터링, 설치 기능을 담당합니다.
 * 
 * @property deviceRepository 디바이스 Repository
 * @property selectApkFolderUseCase 폴더 선택 UseCase
 * @property installApkUseCase APK 설치 UseCase
 */
class InstallerViewModel(
    private val deviceRepository: DeviceRepository,
    private val selectApkFolderUseCase: SelectApkFolderUseCase,
    private val installApkUseCase: InstallApkUseCase
) : ViewModel() {
    
    // 현재 선택된 디바이스
    val selectedDevice: StateFlow<Device?> = deviceRepository.observeSelectedDevice()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    // 선택한 폴더 경로
    private val _selectedFolderPath = MutableStateFlow<String?>(null)
    val selectedFolderPath: StateFlow<String?> = _selectedFolderPath.asStateFlow()
    
    // 검색 필터 텍스트
    private val _filterText = MutableStateFlow("")
    val filterText: StateFlow<String> = _filterText.asStateFlow()
    
    // 원본 APK 파일 목록
    private val _apkFiles = MutableStateFlow<List<ApkFileInfo>>(emptyList())
    
    // 필터링된 APK 파일 목록
    val filteredApkFiles: StateFlow<List<ApkFileInfo>> = combine(
        _apkFiles,
        _filterText
    ) { files, filter ->
        if (filter.isBlank()) {
            files
        } else {
            files.filter { it.fileName.contains(filter, ignoreCase = true) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    // 선택된 APK 파일
    private val _selectedApkFile = MutableStateFlow<ApkFileInfo?>(null)
    val selectedApkFile: StateFlow<ApkFileInfo?> = _selectedApkFile.asStateFlow()
    
    // 폴더 선택 상태
    private val _folderSelectionState = MutableStateFlow<FolderSelectionState>(FolderSelectionState.Idle)
    val folderSelectionState: StateFlow<FolderSelectionState> = _folderSelectionState.asStateFlow()
    
    // APK 설치 상태
    private val _installState = MutableStateFlow<InstallState>(InstallState.Idle)
    val installState: StateFlow<InstallState> = _installState.asStateFlow()
    
    /**
     * APK 폴더를 선택합니다.
     */
    fun selectApkFolder() {
        viewModelScope.launch {
            _folderSelectionState.value = FolderSelectionState.Selecting
            
            val result = selectApkFolderUseCase(_selectedFolderPath.value)
            
            _folderSelectionState.value = when {
                result.isSuccess -> {
                    val path = result.getOrNull()
                    if (path != null) {
                        _selectedFolderPath.value = path
                        loadApkFiles(path)
                        FolderSelectionState.Success(path)
                    } else {
                        FolderSelectionState.Cancelled
                    }
                }
                else -> {
                    FolderSelectionState.Error(
                        result.exceptionOrNull()?.message ?: "폴더 선택에 실패했습니다"
                    )
                }
            }
        }
    }
    
    /**
     * 지정된 폴더에서 APK 파일 목록을 로드합니다.
     */
    private suspend fun loadApkFiles(folderPath: String) = withContext(Dispatchers.IO) {
        try {
            val folder = File(folderPath)
            if (!folder.exists() || !folder.isDirectory) {
                _apkFiles.value = emptyList()
                return@withContext
            }
            
            val apkFiles = folder.listFiles { file ->
                file.isFile && file.extension.equals("apk", ignoreCase = true)
            }?.map { file ->
                ApkFileInfo(
                    fileName = file.name,
                    filePath = file.absolutePath,
                    modifiedDate = formatDate(file.lastModified()),
                    fileSize = formatFileSize(file.length())
                )
            }?.sortedByDescending { it.fileName } ?: emptyList()
            
            _apkFiles.value = apkFiles
            println("✅ Loaded ${apkFiles.size} APK files from $folderPath")
            
        } catch (e: Exception) {
            println("❌ Failed to load APK files: ${e.message}")
            _apkFiles.value = emptyList()
        }
    }
    
    /**
     * 검색 필터 텍스트를 업데이트합니다.
     */
    fun updateFilterText(text: String) {
        _filterText.value = text
    }
    
    /**
     * APK 파일을 선택합니다.
     */
    fun selectApkFile(apkFile: ApkFileInfo) {
        _selectedApkFile.value = apkFile
    }
    
    /**
     * 선택된 APK 파일을 현재 선택된 디바이스에 설치합니다.
     */
    fun installSelectedApk() {
        val apk = _selectedApkFile.value
        val device = selectedDevice.value
        
        if (apk == null) {
            println("❌ No APK file selected")
            return
        }
        
        if (device == null) {
            _installState.value = InstallState.Error("디바이스를 선택해주세요")
            return
        }
        
        installApk(device, apk)
    }
    
    /**
     * APK 파일을 디바이스에 설치합니다 (드래그 앤 드롭).
     */
    fun installApkByPath(apkPath: String) {
        val device = selectedDevice.value
        
        if (device == null) {
            _installState.value = InstallState.Error("디바이스를 선택해주세요")
            return
        }
        
        val apkFile = ApkFileInfo(
            fileName = File(apkPath).name,
            filePath = apkPath,
            modifiedDate = "",
            fileSize = ""
        )
        
        installApk(device, apkFile)
    }
    
    /**
     * APK 파일을 디바이스에 설치합니다.
     */
    private fun installApk(device: Device, apkFile: ApkFileInfo) {
        viewModelScope.launch {
            _installState.value = InstallState.Installing(apkFile.fileName)
            
            val result = installApkUseCase(
                device = device,
                apkPath = apkFile.filePath,
                reinstall = true
            )
            
            _installState.value = when {
                result.isSuccess -> {
                    InstallState.Success(apkFile.fileName)
                }
                else -> {
                    InstallState.Error(
                        result.exceptionOrNull()?.message ?: "APK 설치에 실패했습니다"
                    )
                }
            }
        }
    }
    
    /**
     * 폴더 선택 상태를 초기화합니다.
     */
    fun resetFolderSelectionState() {
        _folderSelectionState.value = FolderSelectionState.Idle
    }
    
    /**
     * 설치 상태를 초기화합니다.
     */
    fun resetInstallState() {
        _installState.value = InstallState.Idle
    }
    
    /**
     * 타임스탬프를 포맷팅합니다.
     */
    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    /**
     * 파일 크기를 포맷팅합니다.
     */
    private fun formatFileSize(size: Long): String {
        val kb = size / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0
        
        return when {
            gb >= 1 -> String.format("%.2f GB", gb)
            mb >= 1 -> String.format("%.2f MB", mb)
            kb >= 1 -> String.format("%.2f KB", kb)
            else -> "$size B"
        }
    }
}

/**
 * APK 파일 정보
 */
data class ApkFileInfo(
    val fileName: String,
    val filePath: String,
    val modifiedDate: String,
    val fileSize: String
)

/**
 * 폴더 선택 상태
 */
sealed interface FolderSelectionState {
    /** 대기 중 */
    data object Idle : FolderSelectionState
    
    /** 선택 중 */
    data object Selecting : FolderSelectionState
    
    /** 성공 */
    data class Success(val path: String) : FolderSelectionState
    
    /** 취소 */
    data object Cancelled : FolderSelectionState
    
    /** 실패 */
    data class Error(val message: String) : FolderSelectionState
}

/**
 * APK 설치 상태
 */
sealed interface InstallState {
    /** 대기 중 */
    data object Idle : InstallState
    
    /** 설치 중 */
    data class Installing(val fileName: String) : InstallState
    
    /** 성공 */
    data class Success(val fileName: String) : InstallState
    
    /** 실패 */
    data class Error(val message: String) : InstallState
}
