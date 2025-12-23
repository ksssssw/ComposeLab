package com.ksssssw.wepray.data.repository

import com.ksssssw.wepray.data.adb.AdbCommand
import com.ksssssw.wepray.data.adb.AdbManager
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.repository.DeviceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * DeviceRepository의 구현체
 * AdbManager를 사용하여 실제 디바이스 정보를 조회합니다.
 * 
 * @property adbManager ADB 명령을 실행하는 매니저
 */
class DeviceRepositoryImpl(
    private val adbManager: AdbManager
) : DeviceRepository {
    
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
}
