package com.ksssssw.wepray.ui.scene.deeplinker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksssssw.wepray.data.adb.AdbManager
import com.ksssssw.wepray.domain.model.DeepLinkCategory
import com.ksssssw.wepray.domain.model.DeepLinkHistoryItem
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.repository.DeepLinkerRepository
import com.ksssssw.wepray.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Deep Linker 화면의 ViewModel
 * NowInAndroid 스타일의 아키텍처 패턴을 따릅니다.
 *
 * - StateFlow로 UI 상태 관리
 * - Single source of truth (Repository)
 * - Unidirectional data flow
 * - 관련 상태를 그룹화하여 관리
 *
 * @property deepLinkerRepository Deep Linker Repository (Single Source of Truth)
 * @property deviceRepository Device Repository (Single Source of Truth)
 * @property adbManager ADB Manager (딥링크 전송 및 앱 목록 조회)
 */
class DeepLinkerViewModel(
    private val deepLinkerRepository: DeepLinkerRepository,
    private val deviceRepository: DeviceRepository,
    private val adbManager: AdbManager
) : ViewModel() {
    
    init {
        // 앱 시작 시 초기화 작업
        initializeDefaultCategories()
        loadRecentApps()
    }
    
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
    
    /** 즐겨찾기 앱 목록 (Repository에서 관찰) */
    private val favoritePackageNames: StateFlow<List<String>> = 
        deepLinkerRepository.getFavoriteApps()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    
    /** 카테고리 목록 (Repository에서 관찰) */
    private val categories: StateFlow<List<DeepLinkCategory>> = 
        deepLinkerRepository.getAllCategories()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    
    /** 히스토리 목록 (Repository에서 관찰) */
    private val history: StateFlow<List<DeepLinkHistoryItem>> = 
        deepLinkerRepository.getAllHistory()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    
    // ============================================
    // Local UI State (ViewModel에서 관리)
    // ============================================
    
    /** 앱 관련 상태 그룹 */
    private val _appState = MutableStateFlow(AppState())
    
    /** 작업 상태 그룹 */
    private val _operationState = MutableStateFlow(OperationState())
    
    /**
     * DeepLinkerUiState: 모든 관련 상태를 하나로 묶음
     * Repository의 Flow와 로컬 상태를 combine하여 통합 UI 상태 생성
     * 
     * Note: combine은 최대 5개까지만 지원하므로 중첩 combine 사용
     */
    val uiState: StateFlow<DeepLinkerUiState> = combine(
        combine(
            selectedDevice,
            favoritePackageNames,
            categories,
            history
        ) { device, favorites, categoryList, historyList ->
            RepositoryState(device, favorites, categoryList, historyList)
        },
        _appState,
        _operationState
    ) { repoState, appState, operationState ->
        
        // 설치된 앱 목록 생성 (즐겨찾기 및 최근 사용 정보 포함)
        val apps = appState.installedApps.map { app ->
            InstalledAppInfo(
                packageName = app.packageName,
                appName = app.appName,
                isFavorite = repoState.favoritePackageNames.contains(app.packageName),
                isRecent = appState.recentApps.any { it.first == app.packageName }
            )
        }
        
        DeepLinkerUiState.Success(
            selectedDevice = repoState.selectedDevice,
            apps = apps,
            categories = repoState.categories,
            history = repoState.history,
            isLoading = operationState.isLoading,
            errorMessage = operationState.errorMessage,
            successMessage = operationState.successMessage,
            recentApps = appState.recentApps
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DeepLinkerUiState.Loading
    )
    
    // ============================================
    // Initialization
    // ============================================
    
    /**
     * 기본 카테고리 초기화
     */
    private fun initializeDefaultCategories() {
        viewModelScope.launch {
            categories.collect { categoriesList ->
                if (categoriesList.isEmpty()) {
                    getDefaultCategories().forEach { category ->
                        deepLinkerRepository.addCategory(category)
                    }
                }
            }
        }
    }
    
    // ============================================
    // User Actions
    // ============================================
    
    /**
     * 설치된 앱 목록을 로드합니다.
     */
    fun loadInstalledApps() {
        viewModelScope.launch {
            val device = selectedDevice.value
            if (device == null) {
                _operationState.value = _operationState.value.copy(
                    errorMessage = "No device selected"
                )
                return@launch
            }
            
            _operationState.value = _operationState.value.copy(
                isLoading = true,
                errorMessage = null
            )
            
            try {
                val result = adbManager.getInstalledApps(device.serialNumber)
                result.onSuccess { packages ->
                    val apps = packages.map { packageName ->
                        // Get app name (simplified - in real app, use PackageManager)
                        val appName = packageName.split(".").lastOrNull()
                            ?.replaceFirstChar { it.uppercase() } 
                            ?: packageName
                        InstalledApp(packageName, appName)
                    }.sortedBy { it.appName }
                    
                    _appState.value = _appState.value.copy(installedApps = apps)
                }.onFailure { e ->
                    _operationState.value = _operationState.value.copy(
                        errorMessage = "Failed to load apps: ${e.message}"
                    )
                }
            } finally {
                _operationState.value = _operationState.value.copy(isLoading = false)
            }
        }
    }
    
    /**
     * 딥링크를 디바이스로 전송합니다.
     * 
     * @param url 딥링크 URL
     * @param packageName 대상 앱 패키지명
     * @param appName 대상 앱 이름
     * @param categoryId 카테고리 ID (선택사항)
     */
    fun sendDeepLink(url: String, packageName: String, appName: String, categoryId: String?) {
        viewModelScope.launch {
            val device = selectedDevice.value
            if (device == null) {
                _operationState.value = _operationState.value.copy(
                    errorMessage = "No device selected"
                )
                return@launch
            }
            
            if (url.isBlank()) {
                _operationState.value = _operationState.value.copy(
                    errorMessage = "URL cannot be empty"
                )
                return@launch
            }
            
            _operationState.value = _operationState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )
            
            try {
                val result = adbManager.sendDeepLink(device.serialNumber, url)
                result.onSuccess {
                    // Add to history
                    val historyItem = DeepLinkHistoryItem(
                        id = UUID.randomUUID().toString(),
                        url = url,
                        packageName = packageName,
                        appName = appName,
                        categoryId = categoryId,
                        timestamp = System.currentTimeMillis()
                    )
                    deepLinkerRepository.addHistory(historyItem)
                    
                    _operationState.value = _operationState.value.copy(
                        successMessage = "Deep link sent successfully"
                    )
                    loadRecentApps()
                }.onFailure { e ->
                    _operationState.value = _operationState.value.copy(
                        errorMessage = "Failed to send deep link: ${e.message}"
                    )
                }
            } finally {
                _operationState.value = _operationState.value.copy(isLoading = false)
            }
        }
    }
    
    /**
     * 앱을 즐겨찾기에 추가/제거합니다.
     * 
     * @param packageName 앱 패키지명
     * @param appName 앱 이름
     */
    fun toggleFavorite(packageName: String, appName: String) {
        viewModelScope.launch {
            try {
                val isFavorite = deepLinkerRepository.isFavorite(packageName)
                if (isFavorite) {
                    deepLinkerRepository.removeFavoriteApp(packageName)
                } else {
                    deepLinkerRepository.addFavoriteApp(packageName, appName)
                }
            } catch (e: Exception) {
                _operationState.value = _operationState.value.copy(
                    errorMessage = "Failed to update favorite: ${e.message}"
                )
            }
        }
    }
    
    /**
     * 카테고리를 추가합니다.
     * 
     * @param name 카테고리 이름
     * @param colorValue 카테고리 색상 (Long ARGB 값)
     */
    fun addCategory(name: String, colorValue: Long) {
        viewModelScope.launch {
            try {
                val category = DeepLinkCategory(
                    id = "cat_${System.currentTimeMillis()}",
                    name = name,
                    colorValue = colorValue
                )
                deepLinkerRepository.addCategory(category)
            } catch (e: Exception) {
                _operationState.value = _operationState.value.copy(
                    errorMessage = "Failed to add category: ${e.message}"
                )
            }
        }
    }
    
    /**
     * 카테고리를 수정합니다.
     */
    fun updateCategory(category: DeepLinkCategory) {
        viewModelScope.launch {
            try {
                deepLinkerRepository.updateCategory(category)
            } catch (e: Exception) {
                _operationState.value = _operationState.value.copy(
                    errorMessage = "Failed to update category: ${e.message}"
                )
            }
        }
    }
    
    /**
     * 카테고리를 삭제합니다.
     * 
     * @param id 카테고리 ID
     */
    fun deleteCategory(id: String) {
        viewModelScope.launch {
            try {
                deepLinkerRepository.deleteCategory(id)
            } catch (e: Exception) {
                _operationState.value = _operationState.value.copy(
                    errorMessage = "Failed to delete category: ${e.message}"
                )
            }
        }
    }
    
    /**
     * 히스토리 항목을 수정합니다.
     */
    fun updateHistory(item: DeepLinkHistoryItem) {
        viewModelScope.launch {
            try {
                deepLinkerRepository.updateHistory(item)
            } catch (e: Exception) {
                _operationState.value = _operationState.value.copy(
                    errorMessage = "Failed to update history: ${e.message}"
                )
            }
        }
    }
    
    /**
     * 히스토리 항목을 삭제합니다.
     * 
     * @param id 히스토리 ID
     */
    fun deleteHistory(id: String) {
        viewModelScope.launch {
            try {
                deepLinkerRepository.deleteHistory(id)
            } catch (e: Exception) {
                _operationState.value = _operationState.value.copy(
                    errorMessage = "Failed to delete history: ${e.message}"
                )
            }
        }
    }
    
    /**
     * 모든 히스토리를 삭제합니다.
     */
    fun clearAllHistory() {
        viewModelScope.launch {
            try {
                deepLinkerRepository.deleteAllHistory()
                _operationState.value = _operationState.value.copy(
                    successMessage = "History cleared"
                )
            } catch (e: Exception) {
                _operationState.value = _operationState.value.copy(
                    errorMessage = "Failed to clear history: ${e.message}"
                )
            }
        }
    }
    
    /**
     * 최근 사용 앱 목록을 로드합니다.
     */
    private fun loadRecentApps() {
        viewModelScope.launch {
            try {
                val recentApps = deepLinkerRepository.getRecentApps()
                _appState.value = _appState.value.copy(recentApps = recentApps)
            } catch (e: Exception) {
                // Silent failure for recent apps
            }
        }
    }
    
    /**
     * 에러 메시지를 초기화합니다.
     */
    fun clearError() {
        _operationState.value = _operationState.value.copy(errorMessage = null)
    }
    
    /**
     * 성공 메시지를 초기화합니다.
     */
    fun clearSuccess() {
        _operationState.value = _operationState.value.copy(successMessage = null)
    }
    
    // ============================================
    // Utility Functions
    // ============================================
    
    companion object {
        /**
         * 기본 카테고리 목록
         */
        private fun getDefaultCategories() = listOf(
            DeepLinkCategory("home", "Home", 0xFF3B82F6),
            DeepLinkCategory("detail", "Detail", 0xFF10B981),
            DeepLinkCategory("settings", "Settings", 0xFFF59E0B),
            DeepLinkCategory("profile", "Profile", 0xFF8B5CF6),
            DeepLinkCategory("notification", "Notification", 0xFFEF4444),
        )
    }
}

// ============================================
// State Groups (내부 상태 관리용)
// ============================================

/**
 * Repository 상태 그룹
 * Repository에서 오는 모든 상태를 하나로 묶음 (combine 제약 해결용)
 */
private data class RepositoryState(
    val selectedDevice: Device?,
    val favoritePackageNames: List<String>,
    val categories: List<DeepLinkCategory>,
    val history: List<DeepLinkHistoryItem>
)

/**
 * 앱 관련 상태 그룹
 * 설치된 앱과 최근 사용 앱 정보를 하나로 묶음
 */
private data class AppState(
    val installedApps: List<InstalledApp> = emptyList(),
    val recentApps: List<Pair<String, String>> = emptyList()
)

/**
 * 작업 상태 그룹
 * 로딩, 에러, 성공 메시지를 하나로 묶음
 */
private data class OperationState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

// ============================================
// UI State
// ============================================

/**
 * Deep Linker UI 상태
 */
sealed interface DeepLinkerUiState {
    /** 로딩 중 */
    data object Loading : DeepLinkerUiState
    
    /** 성공 */
    data class Success(
        // Device 상태 (Repository에서)
        val selectedDevice: Device?,
        
        // App 상태
        val apps: List<InstalledAppInfo>,
        val recentApps: List<Pair<String, String>>,
        
        // Category 상태 (Repository에서)
        val categories: List<DeepLinkCategory>,
        
        // History 상태 (Repository에서)
        val history: List<DeepLinkHistoryItem>,
        
        // Operation 상태
        val isLoading: Boolean,
        val errorMessage: String?,
        val successMessage: String?
    ) : DeepLinkerUiState
}

// ============================================
// Domain Models
// ============================================

/**
 * 설치된 앱 정보
 */
data class InstalledApp(
    val packageName: String,
    val appName: String
)

/**
 * UI용 설치된 앱 정보 (즐겨찾기 및 최근 사용 정보 포함)
 */
data class InstalledAppInfo(
    val packageName: String,
    val appName: String,
    val isFavorite: Boolean = false,
    val isRecent: Boolean = false
)