package com.ksssssw.wepray.ui.scene.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksssssw.wepray.domain.model.AppSettings
import com.ksssssw.wepray.domain.repository.SettingsRepository
import com.ksssssw.wepray.domain.usecase.SelectScreenshotPathUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Settings 화면의 ViewModel
 * 
 * @property settingsRepository 설정 저장소
 * @property selectScreenshotPathUseCase 경로 선택 UseCase
 */
class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val selectScreenshotPathUseCase: SelectScreenshotPathUseCase
) : ViewModel() {
    
    // 앱 설정 상태
    val settings: StateFlow<AppSettings> = settingsRepository.observeSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )
    
    // 경로 선택 상태
    private val _pathSelectionState = MutableStateFlow<PathSelectionState>(PathSelectionState.Idle)
    val pathSelectionState: StateFlow<PathSelectionState> = _pathSelectionState.asStateFlow()
    
    /**
     * 스크린샷 저장 경로를 선택합니다.
     */
    fun selectScreenshotPath() {
        viewModelScope.launch {
            _pathSelectionState.value = PathSelectionState.Selecting
            
            val result = selectScreenshotPathUseCase()
            
            _pathSelectionState.value = when {
                result.isSuccess -> {
                    val path = result.getOrNull()
                    if (path != null) {
                        PathSelectionState.Success(path)
                    } else {
                        PathSelectionState.Cancelled
                    }
                }
                else -> {
                    PathSelectionState.Error(
                        result.exceptionOrNull()?.message ?: "경로 선택에 실패했습니다"
                    )
                }
            }
        }
    }
    
    /**
     * 경로 선택 상태를 초기화합니다.
     */
    fun resetPathSelectionState() {
        _pathSelectionState.value = PathSelectionState.Idle
    }
}

/**
 * 경로 선택 상태
 */
sealed interface PathSelectionState {
    /** 대기 중 */
    data object Idle : PathSelectionState
    
    /** 선택 중 */
    data object Selecting : PathSelectionState
    
    /** 성공 */
    data class Success(val path: String) : PathSelectionState
    
    /** 취소 */
    data object Cancelled : PathSelectionState
    
    /** 실패 */
    data class Error(val message: String) : PathSelectionState
}