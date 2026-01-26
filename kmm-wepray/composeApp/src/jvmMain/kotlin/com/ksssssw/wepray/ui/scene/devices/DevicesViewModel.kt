package com.ksssssw.wepray.ui.scene.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.repository.DeviceRepository
import com.ksssssw.wepray.domain.usecase.MirrorDeviceUseCase
import com.ksssssw.wepray.domain.usecase.TakeScreenshotUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

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
 * @property mirrorDeviceUseCase 디바이스 미러링 UseCase
 */
class DevicesViewModel(
    private val deviceRepository: DeviceRepository,
    private val takeScreenshotUseCase: TakeScreenshotUseCase,
    private val mirrorDeviceUseCase: MirrorDeviceUseCase
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

    // 미러링 작업 상태
    private val _mirroringState = MutableStateFlow<MirroringState>(MirroringState.Idle)
    val mirroringState: StateFlow<MirroringState> = _mirroringState.asStateFlow()
    
    // 미러링 프로세스 관리 (디바이스별) - Thread-safe
    private val mirroringProcesses = ConcurrentHashMap<String, Process>()

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

    /**
     * 디바이스 미러링을 시작합니다.
     * 
     * @param device 미러링할 디바이스
     */
    fun startMirroring(device: Device) {
        // 이미 미러링 중인 경우 무시 (빠른 경로)
        if (mirroringProcesses.containsKey(device.serialNumber)) {
            println("⚠️ 디바이스가 이미 미러링 중입니다: ${device.modelName}")
            return
        }
        
        viewModelScope.launch {
            _mirroringState.value = MirroringState.Starting(device)
            
            val result = mirrorDeviceUseCase(device)
            
            result.fold(
                onSuccess = { process ->
                    mirroringProcesses[device.serialNumber] = process
                    
                    // 프로세스 종료 모니터링
                    monitorMirroringProcess(device, process)
                    
                    _mirroringState.value = MirroringState.Active(device, process)
                },
                onFailure = { exception ->
                    _mirroringState.value = MirroringState.Error(
                        device,
                        exception.message ?: "미러링 시작 중 오류가 발생했습니다"
                    )
                }
            )
        }
    }

    /**
     * 디바이스 미러링을 중지합니다.
     * 
     * @param device 미러링을 중지할 디바이스
     */
    fun stopMirroring(device: Device) {
        viewModelScope.launch {
            mirroringProcesses.remove(device.serialNumber)?.let { process ->
                mirrorDeviceUseCase.stop(process)
                _mirroringState.value = MirroringState.Idle
                println("✅ 미러링 중지됨: ${device.modelName}")
            }
        }
    }

    /**
     * 미러링 프로세스를 모니터링하여 종료 시 상태를 업데이트합니다.
     * 
     * 별도의 코루틴에서 블로킹 호출을 수행하여 메인 스레드를 차단하지 않습니다.
     */
    private fun monitorMirroringProcess(device: Device, process: Process) {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.Default) {
            try {
                // 프로세스 종료 대기 (블로킹 호출)
                process.waitFor()
                
                // 프로세스가 종료되면 맵에서 제거
                mirroringProcesses.remove(device.serialNumber)
                _mirroringState.value = MirroringState.Idle
                
                println("✅ 미러링 프로세스 종료됨: ${device.modelName}")
            } catch (e: Exception) {
                println("⚠️ 미러링 프로세스 모니터링 중 오류: ${e.message}")
            }
        }
    }

    /**
     * 미러링 상태를 초기화합니다.
     */
    fun resetMirroringState() {
        _mirroringState.value = MirroringState.Idle
    }

    /**
     * ViewModel 종료 시 모든 미러링 프로세스를 정리합니다.
     * 
     * onCleared()는 viewModelScope가 이미 취소된 후 호출되므로
     * 별도의 방식으로 프로세스를 정리합니다.
     */
    override fun onCleared() {
        super.onCleared()
        
        // viewModelScope를 사용하지 않고 직접 정리
        val processes = mirroringProcesses.values.toList()
        mirroringProcesses.clear()
        
        processes.forEach { process ->
            try {
                if (process.isAlive) {
                    process.destroy()
                }
            } catch (e: Exception) {
                println("⚠️ 미러링 프로세스 정리 중 오류: ${e.message}")
            }
        }
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

/**
 * 미러링 작업 상태
 */
sealed interface MirroringState {
    /** 대기 중 */
    data object Idle : MirroringState
    
    /** 시작 중 */
    data class Starting(val device: Device) : MirroringState
    
    /** 활성화됨 (미러링 중) */
    data class Active(val device: Device, val process: Process) : MirroringState
    
    /** 실패 */
    data class Error(val device: Device, val message: String) : MirroringState
}

