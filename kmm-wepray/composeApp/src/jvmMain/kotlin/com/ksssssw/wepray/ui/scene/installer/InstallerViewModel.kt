package com.ksssssw.wepray.ui.scene.installer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.model.DeviceStorageInfo
import com.ksssssw.wepray.domain.repository.DeviceRepository
import com.ksssssw.wepray.domain.usecase.InstallApkUseCase
import com.ksssssw.wepray.domain.usecase.SelectApkFolderUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Installer 화면의 ViewModel
 * NowInAndroid 스타일의 아키텍처 패턴을 따릅니다.
 * 
 * - StateFlow로 UI 상태 관리
 * - Single source of truth (DeviceRepository)
 * - Unidirectional data flow
 * - 관련 상태를 그룹화하여 관리
 * 
 * @property deviceRepository 디바이스 Repository (Single Source of Truth)
 * @property selectApkFolderUseCase 폴더 선택 UseCase
 * @property installApkUseCase APK 설치 UseCase
 */
class InstallerViewModel(
    private val deviceRepository: DeviceRepository,
    private val selectApkFolderUseCase: SelectApkFolderUseCase,
    private val installApkUseCase: InstallApkUseCase
) : ViewModel() {
    
    // ============================================
    // State from Repository (Single Source of Truth)
    // ============================================
    
    /** 선택된 디바이스 (Repository에서 관찰) */
    private val selectedDevice: StateFlow<Device?> = deviceRepository.observeSelectedDevice()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    /** 디바이스 스토리지 정보 (Repository에서 관찰) */
    private val deviceStorageInfo: StateFlow<DeviceStorageInfo?> = 
        deviceRepository.observeSelectedDeviceStorageInfo()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    
    // ============================================
    // Local UI State (ViewModel에서 관리)
    // ============================================
    
    /** 폴더 관련 상태 그룹 */
    private val _folderState = MutableStateFlow(FolderState())
    
    /** 필터 및 정렬 상태 그룹 */
    private val _filterSortState = MutableStateFlow(FilterSortState())
    
    /** APK 파일 관련 상태 그룹 */
    private val _apkState = MutableStateFlow(ApkState())
    
    /**
     * InstallerUiState: 모든 관련 상태를 하나로 묶음
     * 5개의 주요 상태 그룹만 combine하여 가독성과 성능 향상
     */
    val uiState: StateFlow<InstallerUiState> = combine(
        selectedDevice,
        deviceStorageInfo,
        _folderState,
        _filterSortState,
        _apkState
    ) { device: Device?, 
        storageInfo: DeviceStorageInfo?, 
        folderState: FolderState, 
        filterSortState: FilterSortState, 
        apkState: ApkState ->
        
        // 필터링 및 정렬 적용
        val filteredAndSorted = apkState.apkFiles
            .filter { apkFile ->
                if (filterSortState.filterText.isBlank()) true 
                else apkFile.fileName.contains(filterSortState.filterText, ignoreCase = true) 
            }
            .let { files ->
                when (filterSortState.sortOption) {
                    SortOption.NAME -> files.sortedBy { it.fileName.lowercase() }
                    SortOption.MODIFIED_DATE -> files.sortedByDescending { it.modifiedTimestamp }
                }
            }
        
        // 디렉토리 크기 계산
        val totalSize = apkState.apkFiles.sumOf { it.fileSizeBytes }
        
        InstallerUiState.Success(
            selectedDevice = device,
            deviceStorageInfo = storageInfo,
            folderPath = folderState.selectedPath,
            folderSelectionState = folderState.selectionState,
            filterText = filterSortState.filterText,
            sortOption = filterSortState.sortOption,
            apkFiles = filteredAndSorted,
            selectedApkFile = apkState.selectedApk,
            installStates = apkState.installStates,
            totalFilesCount = apkState.apkFiles.size,
            totalDirectorySize = formatFileSize(totalSize)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InstallerUiState.Loading
    )
    
    // ============================================
    // User Actions
    // ============================================
    
    /**
     * APK 폴더를 선택합니다.
     */
    fun selectApkFolder() {
        viewModelScope.launch {
            _folderState.value = _folderState.value.copy(
                selectionState = FolderSelectionState.Selecting
            )
            
            val result = selectApkFolderUseCase(_folderState.value.selectedPath)
            
            val newSelectionState = when {
                result.isSuccess -> {
                    val path = result.getOrNull()
                    if (path != null) {
                        _folderState.value = _folderState.value.copy(selectedPath = path)
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
            
            _folderState.value = _folderState.value.copy(selectionState = newSelectionState)
        }
    }
    
    /**
     * 지정된 폴더에서 APK 파일 목록을 로드합니다.
     */
    private suspend fun loadApkFiles(folderPath: String) = withContext(Dispatchers.IO) {
        try {
            val folder = File(folderPath)
            if (!folder.exists() || !folder.isDirectory) {
                _apkState.value = _apkState.value.copy(apkFiles = emptyList())
                return@withContext
            }
            
            val apkFiles = folder.listFiles { file ->
                file.isFile && file.extension.equals("apk", ignoreCase = true)
            }?.map { file ->
                ApkFileInfo(
                    fileName = file.name,
                    filePath = file.absolutePath,
                    modifiedDate = formatDate(file.lastModified()),
                    modifiedTimestamp = file.lastModified(),
                    fileSize = formatFileSize(file.length()),
                    fileSizeBytes = file.length()
                )
            } ?: emptyList()
            
            _apkState.value = _apkState.value.copy(apkFiles = apkFiles)
            println("✅ Loaded ${apkFiles.size} APK files from $folderPath")
            
        } catch (e: Exception) {
            println("❌ Failed to load APK files: ${e.message}")
            _apkState.value = _apkState.value.copy(apkFiles = emptyList())
        }
    }
    
    /**
     * APK 목록을 새로고침합니다.
     */
    fun refreshApkList() {
        val currentPath = _folderState.value.selectedPath
        if (currentPath != null) {
            viewModelScope.launch {
                loadApkFiles(currentPath)
            }
        }
    }
    
    /**
     * 검색 필터 텍스트를 업데이트합니다.
     */
    fun updateFilterText(text: String) {
        _filterSortState.value = _filterSortState.value.copy(filterText = text)
    }
    
    /**
     * 정렬 옵션을 변경합니다.
     */
    fun updateSortOption(option: SortOption) {
        _filterSortState.value = _filterSortState.value.copy(sortOption = option)
    }
    
    /**
     * APK 파일을 선택합니다.
     */
    fun selectApkFile(apkFile: ApkFileInfo) {
        _apkState.value = _apkState.value.copy(selectedApk = apkFile)
    }
    
    /**
     * 선택된 APK 파일을 현재 선택된 디바이스에 설치합니다.
     */
    fun installSelectedApk() {
        val apk = _apkState.value.selectedApk
        val device = selectedDevice.value
        
        if (apk == null) {
            println("❌ No APK file selected")
            return
        }
        
        if (device == null) {
            println("❌ No device selected")
            return
        }
        
        installApk(device, apk)
    }
    
    /**
     * 특정 APK 파일을 설치합니다.
     */
    fun installApk(apkFile: ApkFileInfo) {
        val device = selectedDevice.value
        
        if (device == null) {
            println("❌ No device selected")
            return
        }
        
        installApk(device, apkFile)
    }
    
    /**
     * APK 파일을 디바이스에 설치합니다 (드래그 앤 드롭).
     */
    fun installApkByPath(apkPath: String) {
        val device = selectedDevice.value
        
        if (device == null) {
            println("❌ No device selected")
            return
        }
        
        val apkFile = ApkFileInfo(
            fileName = File(apkPath).name,
            filePath = apkPath,
            modifiedDate = "",
            modifiedTimestamp = 0L,
            fileSize = "",
            fileSizeBytes = 0L
        )
        
        installApk(device, apkFile)
    }
    
    /**
     * APK 파일을 디바이스에 설치합니다.
     */
    private fun installApk(device: Device, apkFile: ApkFileInfo) {
        viewModelScope.launch {
            // 설치 중 상태로 변경
            val currentStates = _apkState.value.installStates
            _apkState.value = _apkState.value.copy(
                installStates = currentStates + (apkFile.filePath to InstallStatus.Installing)
            )
            
            val result = installApkUseCase(
                device = device,
                apkPath = apkFile.filePath,
                reinstall = true
            )
            
            // 결과에 따라 상태 업데이트
            val newStatus = when {
                result.isSuccess -> InstallStatus.Installed
                else -> InstallStatus.Error(
                    result.exceptionOrNull()?.message ?: "APK 설치에 실패했습니다"
                )
            }
            
            _apkState.value = _apkState.value.copy(
                installStates = currentStates + (apkFile.filePath to newStatus)
            )
            
            // 3초 후 상태 초기화 (성공 또는 실패 메시지 표시 후)
            kotlinx.coroutines.delay(3000)
            _apkState.value = _apkState.value.copy(
                installStates = currentStates - apkFile.filePath
            )
        }
    }
    
    /**
     * 폴더 선택 상태를 초기화합니다.
     */
    fun resetFolderSelectionState() {
        _folderState.value = _folderState.value.copy(
            selectionState = FolderSelectionState.Idle
        )
    }
    
    // ============================================
    // Utility Functions
    // ============================================
    
    /**
     * 타임스탬프를 포맷팅합니다.
     */
    private fun formatDate(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        
        return when {
            seconds < 60 -> "방금 전"
            minutes < 60 -> "${minutes}분 전"
            hours < 24 -> "${hours}시간 전"
            days < 7 -> "${days}일 전"
            else -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                sdf.format(Date(timestamp))
            }
        }
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

// ============================================
// State Groups (내부 상태 관리용)
// ============================================

/**
 * 폴더 관련 상태 그룹
 * 폴더 선택과 관련된 모든 상태를 하나로 묶음
 */
private data class FolderState(
    val selectedPath: String? = null,
    val selectionState: FolderSelectionState = FolderSelectionState.Idle
)

/**
 * 필터 및 정렬 상태 그룹
 * 검색/필터링과 정렬에 관련된 상태를 하나로 묶음
 */
private data class FilterSortState(
    val filterText: String = "",
    val sortOption: SortOption = SortOption.MODIFIED_DATE  // 기본값: 수정순
)

/**
 * APK 파일 관련 상태 그룹
 * APK 파일 목록, 선택, 설치 상태를 하나로 묶음
 */
private data class ApkState(
    val apkFiles: List<ApkFileInfo> = emptyList(),
    val selectedApk: ApkFileInfo? = null,
    val installStates: Map<String, InstallStatus> = emptyMap()
)

// ============================================
// UI State
// ============================================

/**
 * Installer UI 상태
 */
sealed interface InstallerUiState {
    /** 로딩 중 */
    data object Loading : InstallerUiState
    
    /** 성공 */
    data class Success(
        // Device 상태 (Repository에서)
        val selectedDevice: Device?,
        val deviceStorageInfo: DeviceStorageInfo?,
        
        // 폴더 상태
        val folderPath: String?,
        val folderSelectionState: FolderSelectionState,
        
        // 필터/정렬 상태
        val filterText: String,
        val sortOption: SortOption,
        
        // APK 상태
        val apkFiles: List<ApkFileInfo>,
        val selectedApkFile: ApkFileInfo?,
        val installStates: Map<String, InstallStatus>,
        val totalFilesCount: Int,
        val totalDirectorySize: String
    ) : InstallerUiState
}

// ============================================
// Domain Models
// ============================================

/**
 * APK 파일 정보
 */
data class ApkFileInfo(
    val fileName: String,
    val filePath: String,
    val modifiedDate: String,
    val modifiedTimestamp: Long,
    val fileSize: String,
    val fileSizeBytes: Long
)

/**
 * 정렬 옵션
 */
enum class SortOption {
    /** 이름순 */
    NAME,
    
    /** 수정순 */
    MODIFIED_DATE
}

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
 * APK 설치 상태 (파일별)
 */
sealed interface InstallStatus {
    /** 설치 중 */
    data object Installing : InstallStatus
    
    /** 설치됨 */
    data object Installed : InstallStatus
    
    /** 실패 */
    data class Error(val message: String) : InstallStatus
}
