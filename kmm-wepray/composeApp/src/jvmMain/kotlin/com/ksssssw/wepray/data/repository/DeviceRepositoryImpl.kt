package com.ksssssw.wepray.data.repository

import com.ksssssw.wepray.data.adb.AdbCommand
import com.ksssssw.wepray.data.adb.AdbManager
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.model.DeviceStorageInfo
import com.ksssssw.wepray.domain.repository.DeviceRepository
import com.ksssssw.wepray.domain.usecase.GetDeviceStorageUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * DeviceRepository의 구현체
 * AdbManager를 사용하여 실제 디바이스 정보를 조회합니다.
 * 
 * Single Source of Truth:
 * - 디바이스 목록 (StateFlow로 캐싱)
 * - 선택된 디바이스 (StateFlow)
 * - 디바이스 스토리지 정보 (선택된 디바이스 기반 자동 계산)
 * 
 * @property adbManager ADB 명령을 실행하는 매니저
 * @property getDeviceStorageUseCase 디바이스 스토리지 정보 조회 UseCase
 */
class DeviceRepositoryImpl(
    private val adbManager: AdbManager,
    private val getDeviceStorageUseCase: GetDeviceStorageUseCase
) : DeviceRepository {
    
    // 캐시된 디바이스 목록 (앱 전역)
    private val _cachedDevices = MutableStateFlow<List<Device>>(emptyList())
    
    // 선택된 디바이스 상태 (앱 전역)
    private val _selectedDevice = MutableStateFlow<Device?>(null)
    
    // 스토리지 정보 캐시 (디바이스 시리얼 넘버 -> 스토리지 정보)
    private val storageInfoCache = mutableMapOf<String, Pair<DeviceStorageInfo, Long>>()
    private val CACHE_DURATION_MS = 30_000L // 30초 캐시
    
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
    
    override fun observeSelectedDeviceStorageInfo(): Flow<DeviceStorageInfo?> {
        return _selectedDevice.asStateFlow()
            .distinctUntilChanged { old, new -> old?.serialNumber == new?.serialNumber }
            .map { device ->
                if (device != null) {
                    // 캐시 확인
                    val cached = storageInfoCache[device.serialNumber]
                    val currentTime = System.currentTimeMillis()
                    
                    if (cached != null && (currentTime - cached.second) < CACHE_DURATION_MS) {
                        // 캐시가 유효한 경우
                        cached.first
                    } else {
                        // 캐시가 없거나 만료된 경우 새로 조회
                        val result = getDeviceStorageUseCase(device)
                        if (result.isSuccess) {
                            val storage = result.getOrNull()
                            if (storage != null) {
                                // 캐시 저장
                                storageInfoCache[device.serialNumber] = storage to currentTime
                                // 캐시 크기 제한 (최대 10개)
                                if (storageInfoCache.size > 10) {
                                    val oldestKey = storageInfoCache.minByOrNull { it.value.second }?.key
                                    oldestKey?.let { storageInfoCache.remove(it) }
                                }
                            }
                            storage
                        } else {
                            null
                        }
                    }
                } else {
                    null
                }
            }
    }
}
