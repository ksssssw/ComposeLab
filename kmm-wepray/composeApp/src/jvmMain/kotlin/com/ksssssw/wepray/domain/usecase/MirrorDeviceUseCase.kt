package com.ksssssw.wepray.domain.usecase

import com.ksssssw.wepray.data.scrcpy.ScrcpyCommand
import com.ksssssw.wepray.data.scrcpy.ScrcpyManager
import com.ksssssw.wepray.domain.model.Device

/**
 * 디바이스 화면 미러링 UseCase
 * 
 * scrcpy를 사용하여 Android 디바이스 화면을 PC에 미러링합니다.
 * 
 * @property scrcpyManager scrcpy 실행 매니저
 */
class MirrorDeviceUseCase(
    private val scrcpyManager: ScrcpyManager
) {
    /**
     * 디바이스 미러링을 시작합니다.
     * 
     * @param device 미러링할 디바이스
     * @return 성공 시 Process 객체, 실패 시 예외와 함께 Result.failure
     */
    suspend operator fun invoke(device: Device): Result<Process> {
        // 디바이스가 연결되어 있는지 확인
        if (device.status != com.ksssssw.wepray.domain.model.DeviceStatus.CONNECTED) {
            return Result.failure(
                IllegalStateException("디바이스가 연결되어 있지 않습니다: ${device.modelName}")
            )
        }
        
        // 윈도우 타이틀 생성
        val windowTitle = "${device.manufacturer} ${device.modelName} (${device.serialNumber})"
        
        // scrcpy 명령 생성
        val command = ScrcpyCommand.StartMirroring(
            serialNumber = device.serialNumber,
            windowTitle = windowTitle,
            maxSize = 1024,  // 성능 최적화를 위한 해상도 제한
            bitRate = 8_000_000,  // 8Mbps
            stayAwake = true,  // 화면 켜짐 유지
            turnScreenOff = false,  // 디바이스 화면 켜둠
            showTouches = false  // 터치 표시 안 함
        )
        
        // 미러링 시작
        return scrcpyManager.startMirroring(command)
    }
    
    /**
     * 고급 옵션으로 디바이스 미러링을 시작합니다.
     * 
     * @param device 미러링할 디바이스
     * @param maxFps 최대 FPS (기본값: 60)
     * @param fullscreen 전체화면 여부 (기본값: false)
     * @param alwaysOnTop 항상 위 여부 (기본값: true)
     * @return 성공 시 Process 객체, 실패 시 예외와 함께 Result.failure
     */
    suspend fun startAdvanced(
        device: Device,
        maxFps: Int = 60,
        fullscreen: Boolean = false,
        alwaysOnTop: Boolean = true
    ): Result<Process> {
        if (device.status != com.ksssssw.wepray.domain.model.DeviceStatus.CONNECTED) {
            return Result.failure(
                IllegalStateException("디바이스가 연결되어 있지 않습니다: ${device.modelName}")
            )
        }
        
        val windowTitle = "${device.manufacturer} ${device.modelName} (${device.serialNumber})"
        
        val command = ScrcpyCommand.StartMirroringAdvanced(
            serialNumber = device.serialNumber,
            windowTitle = windowTitle,
            maxFps = maxFps,
            fullscreen = fullscreen,
            alwaysOnTop = alwaysOnTop,
            noBorder = false,
            disableScreensaver = true
        )
        
        return scrcpyManager.startMirroring(command)
    }
    
    /**
     * 미러링을 중지합니다.
     * 
     * @param process 중지할 프로세스
     */
    suspend fun stop(process: Process) {
        scrcpyManager.stopMirroring(process)
    }
}

