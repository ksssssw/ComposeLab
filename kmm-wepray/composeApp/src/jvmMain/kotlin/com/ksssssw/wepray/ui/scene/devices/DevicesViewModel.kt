package com.ksssssw.wepray.ui.scene.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
 * @property getDevicesUseCase 디바이스 목록 조회 UseCase
 * @property deviceRepository 디바이스 Repository (상태 공유)
 */
class DevicesViewModel(
    private val deviceRepository: DeviceRepository,
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

    /**
     * 디바이스를 선택합니다.
     * Repository를 통해 선택 상태를 변경하면 모든 구독자에게 자동으로 전파됩니다.
     */
    fun selectDevice(device: Device) {
        viewModelScope.launch {
            deviceRepository.selectDevice(device)
        }
    }
}

