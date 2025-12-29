package com.ksssssw.wepray.domain.usecase

import com.ksssssw.wepray.domain.model.DeviceStatus
import com.ksssssw.wepray.domain.repository.DeviceRepository

/**
 * 연결된 디바이스 목록을 가져오는 Use Case
 * 
 * @property deviceRepository 디바이스 Repository
 */
class RefreshDevicesUseCase(
    private val deviceRepository: DeviceRepository
) {
    /**
     * 현재 연결된 디바이스 목록을 가져옵니다.
     * 
     * @return 디바이스 목록을 담은 Result
     */
    suspend operator fun invoke(): Result<Boolean> {
        deviceRepository.getDevices()
            .onSuccess { devices ->
                // Repository에 디바이스 목록 업데이트
                deviceRepository.updateDevices(devices)

                val currentSelected = deviceRepository.getSelectedDevice()
                val connectedDevices = devices.filter { it.status == DeviceStatus.CONNECTED }

                when {
                    // 선택된 디바이스가 없고 연결된 디바이스가 있으면 첫 번째 디바이스 자동 선택
                    currentSelected == null && connectedDevices.isNotEmpty() -> {
                        deviceRepository.selectDevice(connectedDevices.first())
                    }
                    // 선택된 디바이스가 목록에 없으면 첫 번째 디바이스 재선택
                    currentSelected != null && devices.none { it.serialNumber == currentSelected.serialNumber } -> {
                        if (connectedDevices.isNotEmpty()) {
                            deviceRepository.selectDevice(connectedDevices.first())
                        } else {
                            deviceRepository.selectDevice(null)
                        }
                    }
                }
            }
            .onFailure { exception ->
                return Result.failure(exception)
            }

        return Result.success(true)
    }
}
