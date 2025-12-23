package com.ksssssw.wepray.domain.usecase

import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.repository.DeviceRepository

/**
 * 연결된 디바이스 목록을 가져오는 Use Case
 * 
 * @property deviceRepository 디바이스 Repository
 */
class GetDevicesUseCase(
    private val deviceRepository: DeviceRepository
) {
    /**
     * 현재 연결된 디바이스 목록을 가져옵니다.
     * 
     * @return 디바이스 목록을 담은 Result
     */
    suspend operator fun invoke(): Result<List<Device>> {
        return deviceRepository.getDevices()
    }
}
