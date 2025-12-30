package com.ksssssw.wepray.ui.scene.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.repository.DeviceRepository
import com.ksssssw.wepray.domain.usecase.TakeScreenshotUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Devices 화면의 ViewModel
 * NowInAndroid 스타일의 아키텍처 패턴을 따릅니다.
 *
 * - StateFlow로 UI 상태 관리
 * - Single source of truth (Repository)
 * - Unidirectional data flow
 *
 * Repository의 Flow를 구독하여 디바이스 목록과 선택 상태를 관리합니다.
 * ViewModel 간 직접적인 의존성이 없습니다.
 *
 * @property deviceRepository 디바이스 Repository (상태 공유)
 * @property takeScreenshotUseCase 스크린샷 촬영 UseCase
 */
class DevicesViewModel(
    private val deviceRepository: DeviceRepository,
    private val takeScreenshotUseCase: TakeScreenshotUseCase
) : ViewModel() {

    val deviceState: StateFlow<DevicesUiState> = combine(
        deviceRepository.observeCachedDevices(),
        deviceRepository.observeSelectedDevice()
    ) { devices, selectedDevice ->
        DevicesUiState.Success(devices = devices, selectedDevice = selectedDevice)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DevicesUiState.Loading
        )

    // 스크린샷 작업 상태
    private val _screenshotState = MutableStateFlow<ScreenshotState>(ScreenshotState.Idle)
    val screenshotState: StateFlow<ScreenshotState> = _screenshotState.asStateFlow()

    /**
     * 디바이스를 선택합니다.
     * Repository를 통해 선택 상태를 변경하면 모든 구독자에게 자동으로 전파됩니다.
     */
    fun selectDevice(device: Device) {
        viewModelScope.launch {
            deviceRepository.selectDevice(device)
        }
    }

    /**
     * 스크린샷을 촬영합니다.
     * 
     * @param device 대상 디바이스
     */
    fun takeScreenshot(device: Device) {
        viewModelScope.launch {
            _screenshotState.value = ScreenshotState.Loading
            
            val result = takeScreenshotUseCase(device)
            
            _screenshotState.value = when {
                result.isSuccess -> {
                    val filePath = result.getOrNull()
                    if (filePath != null) {
                        ScreenshotState.Success(filePath)
                    } else {
                        // 사용자가 경로 선택을 취소함
                        ScreenshotState.Cancelled
                    }
                }
                else -> {
                    ScreenshotState.Error(
                        result.exceptionOrNull()?.message ?: "알 수 없는 오류가 발생했습니다"
                    )
                }
            }
        }
    }

    /**
     * 스크린샷 상태를 초기화합니다.
     */
    fun resetScreenshotState() {
        _screenshotState.value = ScreenshotState.Idle
    }
}

/**
 * 스크린샷 작업 상태
 */
sealed interface ScreenshotState {
    /** 대기 중 */
    data object Idle : ScreenshotState
    
    /** 촬영 중 */
    data object Loading : ScreenshotState
    
    /** 성공 */
    data class Success(val filePath: String) : ScreenshotState
    
    /** 사용자가 경로 선택 취소 */
    data object Cancelled : ScreenshotState
    
    /** 실패 */
    data class Error(val message: String) : ScreenshotState
}

