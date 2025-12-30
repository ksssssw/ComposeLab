package com.ksssssw.wepray.ui.scene.devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.model.DeviceStatus
import com.ksssssw.wepray.ui.components.WePrayDeviceCard
import com.ksssssw.wepray.ui.theme.WePrayTheme
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
internal data object Devices : NavKey

internal fun EntryProviderScope<NavKey>.devicesScene() {
    entry<Devices> {
        DevicesScene()
    }
}

@Composable
fun DevicesScene(
    viewModel: DevicesViewModel = koinViewModel(),
) {
    val deviceState by viewModel.deviceState.collectAsStateWithLifecycle()
    val screenshotState by viewModel.screenshotState.collectAsStateWithLifecycle()

    DevicesContent(
        deviceState = deviceState,
        screenshotState = screenshotState,
        onDeviceSelect = viewModel::selectDevice,
        onScreenshotClick = viewModel::takeScreenshot,
        onResetScreenshotState = viewModel::resetScreenshotState
    )
}

@Composable
private fun DevicesContent(
    deviceState: DevicesUiState,
    screenshotState: ScreenshotState,
    onDeviceSelect: (Device) -> Unit,
    onScreenshotClick: (Device) -> Unit,
    onResetScreenshotState: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // UI 상태에 따른 콘텐츠
        when (deviceState) {
            is DevicesUiState.Loading -> {
                LoadingState()
            }

            is DevicesUiState.Success -> {
                if (deviceState.devices.isEmpty()) {
                    EmptyState()
                } else {
                    DeviceList(
                        devices = deviceState.devices,
                        selectedDevice = deviceState.selectedDevice,
                        screenshotState = screenshotState,
                        onDeviceSelect = onDeviceSelect,
                        onScreenshotClick = onScreenshotClick,
                        onResetScreenshotState = onResetScreenshotState
                    )
                }
            }

            is DevicesUiState.Error -> {
                ErrorState(message = deviceState.message)
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl)
        ) {
            CircularProgressIndicator(
                color = WePrayTheme.colors.primary
            )
            Text(
                text = "디바이스를 검색하는 중...",
                style = WePrayTheme.typography.bodyLarge,
                color = WePrayTheme.colors.textPrimary
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl)
        ) {
            Icon(
                imageVector = Icons.Outlined.Smartphone,
                contentDescription = null,
                modifier = Modifier.size(WePrayTheme.iconSize.decorative),
                tint = WePrayTheme.colors.textSecondary
            )
            Text(
                text = "연결된 디바이스가 없습니다",
                style = WePrayTheme.typography.headlineLarge,
                color = WePrayTheme.colors.textPrimary
            )
            Text(
                text = "USB로 Android 디바이스를 연결하고\nUSB 디버깅을 활성화해주세요",
                style = WePrayTheme.typography.bodyMedium,
                color = WePrayTheme.colors.textSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl)
        ) {
            Text(
                text = "오류가 발생했습니다",
                style = WePrayTheme.typography.headlineLarge,
                color = WePrayTheme.colors.error
            )
            Text(
                text = message,
                style = WePrayTheme.typography.bodyMedium,
                color = WePrayTheme.colors.textSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DeviceList(
    devices: List<Device>,
    selectedDevice: Device?,
    screenshotState: ScreenshotState,
    onDeviceSelect: (Device) -> Unit,
    onScreenshotClick: (Device) -> Unit,
    onResetScreenshotState: () -> Unit,
) {
    val scrollState = rememberScrollState()

    // 스크린샷 결과 스낵바 처리
    when (screenshotState) {
        is ScreenshotState.Success -> {
            // 성공 메시지 표시 (간단한 구현)
            println("✅ 스크린샷 저장됨: ${screenshotState.filePath}")
            onResetScreenshotState()
        }
        is ScreenshotState.Cancelled -> {
            // 취소 메시지 표시
            println("ℹ️ 스크린샷 경로 선택이 취소되었습니다")
            onResetScreenshotState()
        }
        is ScreenshotState.Error -> {
            // 에러 메시지 표시 (간단한 구현)
            println("❌ 스크린샷 실패: ${screenshotState.message}")
            onResetScreenshotState()
        }
        else -> { /* Loading or Idle */ }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = WePrayTheme.spacing.xl),
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xxxl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xxxl),
            itemVerticalAlignment = Alignment.CenterVertically
        ) {
            devices.forEach { device ->
                WePrayDeviceCard(
                    deviceModel = device.modelName,
                    deviceManufacturer = device.manufacturer,
                    serialNumber = device.serialNumber,
                    apiLevel = device.sdkVersion,
                    resolution = device.resolution,
                    androidVersion = device.androidVersion,
                    isSelected = device == selectedDevice,
                    isConnected = device.status == DeviceStatus.CONNECTED,
                    icon = Icons.Outlined.Smartphone,
                    onCardClick = { onDeviceSelect(device) },
                    onMirroringClick = { /* TODO: 미러링 기능 구현 */ },
                    onScreenshotClick = { onScreenshotClick(device) }
                )
            }
        }
    }
}
