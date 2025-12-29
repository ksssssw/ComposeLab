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
import com.ksssssw.wepray.ui.theme.tokens.TextDisabled
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

    DevicesContent(
        deviceState = deviceState,
        onDeviceSelect = viewModel::selectDevice
    )
}

@Composable
private fun DevicesContent(
    deviceState: DevicesUiState,
    onDeviceSelect: (Device) -> Unit,
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
                        onDeviceSelect = onDeviceSelect
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = WePrayTheme.colors.primary
            )
            Text(
                text = "디바이스를 검색하는 중...",
                style = WePrayTheme.typography.bodyLarge,
                color = WePrayTheme.colors.onSecondary
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Smartphone,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = TextDisabled
            )
            Text(
                text = "연결된 디바이스가 없습니다",
                style = WePrayTheme.typography.headlineLarge,
                color = WePrayTheme.colors.primary
            )
            Text(
                text = "USB로 Android 디바이스를 연결하고\nUSB 디버깅을 활성화해주세요",
                style = WePrayTheme.typography.bodyLarge,
                color = WePrayTheme.colors.onSurfaceVariant,
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "오류가 발생했습니다",
                style = WePrayTheme.typography.headlineLarge,
                color = WePrayTheme.colors.error
            )
            Text(
                text = message,
                style = WePrayTheme.typography.bodyLarge,
                color = WePrayTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DeviceList(
    devices: List<Device>,
    selectedDevice: Device?,
    onDeviceSelect: (Device) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = WePrayTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl),
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
                    onScreenshotClick = { /* TODO: 스크린샷 기능 구현 */ }
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun DevicesScenePreview() {
//    WePrayTheme {
//        DevicesContent(
//            uiState = DevicesUiState.Success(
//                devices = listOf(
//                    Device(
//                        serialNumber = "1234567890",
//                        modelName = "SM-S918N",
//                        manufacturer = "Samsung",
//                        resolution = "1080x2400",
//                        androidVersion = "14",
//                        sdkVersion = "34",
//                        status = DeviceStatus.CONNECTED
//                    )
//                )
//            ),
//            selectedDevice = null,
//            onEvent = {}
//        )
//    }
//}
