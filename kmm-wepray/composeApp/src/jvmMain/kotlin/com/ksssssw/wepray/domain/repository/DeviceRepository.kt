package com.ksssssw.wepray.domain.repository

import com.ksssssw.wepray.domain.model.Device
import kotlinx.coroutines.flow.Flow

/**
 * 디바이스 관련 데이터 작업을 추상화하는 Repository 인터페이스
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
}
