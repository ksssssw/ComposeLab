package com.ksssssw.wepray.data.repository

import com.ksssssw.wepray.data.adb.AdbCommand
import com.ksssssw.wepray.data.adb.AdbManager
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.repository.DeviceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

/**
 * DeviceRepository의 구현체
 * AdbManager를 사용하여 실제 디바이스 정보를 조회합니다.
 * 
 * Single Source of Truth:
 * - 디바이스 목록 (StateFlow로 캐싱)
 * - 선택된 디바이스 (StateFlow)
 * 
 * @property adbManager ADB 명령을 실행하는 매니저
 */
class DeviceRepositoryImpl(
    private val adbManager: AdbManager
) : DeviceRepository {
    
    // 캐시된 디바이스 목록 (앱 전역)
    private val _cachedDevices = MutableStateFlow<List<Device>>(emptyList())
    
    // 선택된 디바이스 상태 (앱 전역)
    private val _selectedDevice = MutableStateFlow<Device?>(null)
    
    override suspend fun getDevices(): Result<List<Device>> {
        return try {
            val command = AdbCommand.ListDevices
            val result = adbManager.executeCommand(command)
            
            result.fold(
                onSuccess = { output ->
                    val devices = adbManager.parseDeviceList(output)
                    Result.success(devices)
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun observeDevices(): Flow<Result<List<Device>>> = flow {
        while (true) {
            emit(getDevices())
            delay(3000) // 3초마다 폴링
        }
    }
    
    override fun observeCachedDevices(): Flow<List<Device>> {
        return _cachedDevices.asStateFlow()
    }
    
    override fun observeSelectedDevice(): Flow<Device?> {
        return _selectedDevice.asStateFlow()
    }
    
    override suspend fun selectDevice(device: Device?) {
        _selectedDevice.value = device
    }
    
    override fun getSelectedDevice(): Device? {
        return _selectedDevice.value
    }
    
    override suspend fun updateDevices(devices: List<Device>) {
        _cachedDevices.value = devices
    }
}
