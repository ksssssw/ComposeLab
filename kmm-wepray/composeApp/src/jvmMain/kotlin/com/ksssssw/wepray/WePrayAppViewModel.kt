package com.ksssssw.wepray

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.repository.DeviceRepository
import com.ksssssw.wepray.domain.usecase.RefreshDevicesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 앱 전역 ViewModel
 * NowInAndroid 아키텍처 패턴 적용
 *
 * Repository의 Flow를 구독하여 앱 전역 상태를 관리합니다:
 * - 디바이스 목록 (Repository 캐시)
 * - 현재 선택된 디바이스
 *
 * Repository가 Single Source of Truth이며,
 * 이 ViewModel은 Repository의 상태를 UI에 노출하고
 * 디바이스 새로고침 등의 비즈니스 로직을 처리합니다.
 *
 * @property deviceRepository 디바이스 Repository
 * @property getDevicesUseCase 디바이스 목록 조회 UseCase
 */
class WePrayAppViewModel(
    private val deviceRepository: DeviceRepository,
    private val refreshDevicesUseCase: RefreshDevicesUseCase,
) : ViewModel() {

    init {
        // 앱 시작 시 디바이스 목록 로드
        refreshDevices()
    }

    // Repository의 디바이스 목록과 선택된 디바이스를 함께 구독
    val appState: StateFlow<WePrayAppUiState> = combine(
        deviceRepository.observeCachedDevices(),
        deviceRepository.observeSelectedDevice()
    ) { devices, selectedDevice ->
        WePrayAppUiState(
            devices = devices,
            selectedDevice = selectedDevice
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WePrayAppUiState()
    )

    /**
     * 디바이스를 선택합니다.
     * Repository에 변경사항을 전파하면 자동으로 모든 구독자에게 업데이트됩니다.
     *
     * @param device 선택할 디바이스. null이면 선택 해제
     */
    fun selectDevice(device: Device?) {
        viewModelScope.launch {
            deviceRepository.selectDevice(device)
        }
    }

    /**
     * 디바이스 목록을 새로고침합니다.
     * UseCase를 통해 최신 디바이스 목록을 조회하고 Repository를 업데이트합니다.
     */
    fun refreshDevices() {
        viewModelScope.launch {
            refreshDevicesUseCase()
        }
    }
}
