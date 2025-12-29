package com.ksssssw.wepray.domain.repository

import com.ksssssw.wepray.domain.model.Device
import kotlinx.coroutines.flow.Flow

/**
 * 디바이스 관련 데이터 작업을 추상화하는 Repository 인터페이스
 * 
 * Single Source of Truth:
 * - 디바이스 목록 (실시간 업데이트)
 * - 현재 선택된 디바이스
 */
interface DeviceRepository {
    /**
     * 연결된 모든 디바이스 목록을 가져옵니다.
     * 
     * @return 디바이스 목록을 담은 Result
     */
    suspend fun getDevices(): Result<List<Device>>
    
    /**
     * 연결된 디바이스 목록을 실시간으로 관찰합니다.
     * 
     * @return 디바이스 목록의 Flow
     */
    fun observeDevices(): Flow<Result<List<Device>>>
    
    /**
     * 현재 캐시된 디바이스 목록을 관찰합니다.
     * Repository에 저장된 최신 디바이스 목록을 반환합니다.
     * 
     * @return 디바이스 목록의 StateFlow
     */
    fun observeCachedDevices(): Flow<List<Device>>
    
    /**
     * 현재 선택된 디바이스를 관찰합니다.
     * 
     * @return 선택된 디바이스의 Flow (null 가능)
     */
    fun observeSelectedDevice(): Flow<Device?>
    
    /**
     * 디바이스를 선택합니다.
     * 
     * @param device 선택할 디바이스 (null이면 선택 해제)
     */
    suspend fun selectDevice(device: Device?)
    
    /**
     * 현재 선택된 디바이스를 가져옵니다.
     * 
     * @return 선택된 디바이스 (null 가능)
     */
    fun getSelectedDevice(): Device?
    
    /**
     * 디바이스 목록을 업데이트합니다.
     * DevicesViewModel이 새로운 목록을 로드했을 때 호출합니다.
     * 
     * @param devices 새로운 디바이스 목록
     */
    suspend fun updateDevices(devices: List<Device>)
}
