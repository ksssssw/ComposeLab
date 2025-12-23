package com.ksssssw.wepray.ui.scene.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.usecase.GetDevicesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Devices 화면의 ViewModel
 * NowInAndroid 스타일의 아키텍처 패턴을 따릅니다.
 * 
 * - StateFlow로 UI 상태 관리
 * - Single source of truth
 * - Unidirectional data flow
 * 
 * @property getDevicesUseCase 디바이스 목록 조회 UseCase
 */
class DevicesViewModel(
    private val getDevicesUseCase: GetDevicesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<DevicesUiState>(DevicesUiState.Loading)
    val uiState: StateFlow<DevicesUiState> = _uiState.asStateFlow()
    
    init {
        loadDevices()
    }
    
    /**
     * 사용자 이벤트를 처리합니다.
     */
    fun onEvent(event: DevicesEvent) {
        when (event) {
            is DevicesEvent.Refresh -> loadDevices()
            is DevicesEvent.SelectDevice -> selectDevice(event.device)
        }
    }
    
    /**
     * 디바이스 목록을 로드합니다.
     */
    private fun loadDevices() {
        viewModelScope.launch {
            _uiState.update { DevicesUiState.Loading }
            
            getDevicesUseCase()
                .onSuccess { devices ->
                    _uiState.update { currentState ->
                        // 이전 선택 상태 유지
                        val previousSelection = (currentState as? DevicesUiState.Success)?.selectedDevice
                        val newSelection = if (previousSelection != null && devices.any { it.serialNumber == previousSelection.serialNumber }) {
                            devices.find { it.serialNumber == previousSelection.serialNumber }
                        } else {
                            null
                        }
                        
                        DevicesUiState.Success(
                            devices = devices,
                            selectedDevice = newSelection
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        DevicesUiState.Error(
                            message = exception.message ?: "Failed to load devices"
                        )
                    }
                }
        }
    }
    
    /**
     * 디바이스를 선택합니다.
     */
    private fun selectDevice(device: Device) {
        _uiState.update { currentState ->
            if (currentState is DevicesUiState.Success) {
                currentState.copy(selectedDevice = device)
            } else {
                currentState
            }
        }
    }
}
