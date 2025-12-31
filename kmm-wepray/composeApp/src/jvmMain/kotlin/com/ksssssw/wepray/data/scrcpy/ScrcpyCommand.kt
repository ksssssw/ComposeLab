package com.ksssssw.wepray.data.scrcpy

/**
 * scrcpy 명령어를 나타내는 sealed class
 * 디바이스 화면 미러링 기능 제공
 */
sealed class ScrcpyCommand {
    abstract fun toCommandArguments(): List<String>

    /**
     * 기본 미러링 시작
     *
     * @property serialNumber 디바이스 시리얼 번호
     * @property windowTitle 윈도우 제목
     * @property maxSize 최대 해상도 (기본값: 1024, 성능 최적화)
     * @property bitRate 비트레이트 (기본값: 8Mbps)
     * @property stayAwake 화면 켜짐 유지 여부
     * @property turnScreenOff 미러링 시작 시 디바이스 화면 끄기
     * @property showTouches 터치 표시 여부
     */
    data class StartMirroring(
        val serialNumber: String,
        val windowTitle: String,
        val maxSize: Int = 1024,
        val bitRate: Int = 8_000_000,
        val stayAwake: Boolean = true,
        val turnScreenOff: Boolean = false,
        val showTouches: Boolean = false,
    ) : ScrcpyCommand() {
        override fun toCommandArguments(): List<String> {
            return buildList {
                // 디바이스 지정
                add("--serial=$serialNumber")
                
                // 윈도우 제목
                add("--window-title=$windowTitle")
                
                // 최대 해상도 (성능 최적화)
                add("--max-size=$maxSize")
                
                // 비트레이트
                add("--video-bit-rate=$bitRate")
                
                // 화면 켜짐 유지
                if (stayAwake) {
                    add("--stay-awake")
                }
                
                // 미러링 시작 시 디바이스 화면 끄기
                if (turnScreenOff) {
                    add("--turn-screen-off")
                }
                
                // 터치 표시
                if (showTouches) {
                    add("--show-touches")
                }
            }
        }
    }

    /**
     * 고급 미러링 옵션
     *
     * @property serialNumber 디바이스 시리얼 번호
     * @property windowTitle 윈도우 제목
     * @property maxFps 최대 FPS (기본값: 60)
     * @property fullscreen 전체화면 여부
     * @property alwaysOnTop 항상 위 여부
     * @property noBorder 테두리 없음
     * @property disableScreensaver 화면 보호기 비활성화
     */
    data class StartMirroringAdvanced(
        val serialNumber: String,
        val windowTitle: String,
        val maxFps: Int = 60,
        val fullscreen: Boolean = false,
        val alwaysOnTop: Boolean = true,
        val noBorder: Boolean = false,
        val disableScreensaver: Boolean = true,
    ) : ScrcpyCommand() {
        override fun toCommandArguments(): List<String> {
            return buildList {
                add("--serial=$serialNumber")
                add("--window-title=$windowTitle")
                add("--max-fps=$maxFps")
                
                if (fullscreen) {
                    add("--fullscreen")
                }
                
                if (alwaysOnTop) {
                    add("--always-on-top")
                }
                
                if (noBorder) {
                    add("--window-borderless")
                }
                
                if (disableScreensaver) {
                    add("--disable-screensaver")
                }
            }
        }
    }
}

