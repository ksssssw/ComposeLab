package com.ksssssw.wepray

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ksssssw.wepray.domain.model.DeviceStatus
import com.ksssssw.wepray.ui.components.DeviceInfo
import com.ksssssw.wepray.ui.components.WePrayDropdown
import com.ksssssw.wepray.ui.components.WePrayIconButton
import com.ksssssw.wepray.ui.components.WePrayTopBar
import com.ksssssw.wepray.ui.layout.WePrayScaffold
import com.ksssssw.wepray.ui.layout.WePraySideRail
import com.ksssssw.wepray.ui.navigation.TopLevelDestination
import com.ksssssw.wepray.ui.scene.deeplinker.deepLinkerScene
import com.ksssssw.wepray.ui.scene.devices.devicesScene
import com.ksssssw.wepray.ui.scene.installer.installerScene
import com.ksssssw.wepray.ui.scene.settings.settingsScene
import com.ksssssw.wepray.ui.theme.WePrayTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WePrayApp(
    appViewModel: WePrayAppViewModel = koinViewModel()
) {
    val appUiState by appViewModel.appState.collectAsStateWithLifecycle()
    
    WePrayTheme {
        val backStack = remember { mutableStateListOf(TopLevelDestination.START_DESTINATION.route) }
        val currentDestination = backStack.lastOrNull()
        val topLevelDestination = TopLevelDestination.fromDestination(currentDestination as NavKey)
        
        // 디바이스 목록과 선택된 디바이스 모두 appViewModel에서 가져옴 (Repository 구독)
        val devices = appUiState.devices
        val selectedDevice = appUiState.selectedDevice
        
        // DropDown에 표시할 연결된 디바이스 목록
        val connectedDevices = devices.filter { it.status == DeviceStatus.CONNECTED }
        
        WePrayScaffold(
            topBar = {
                WePrayTopBar(
                    title = topLevelDestination.label,
                    description = topLevelDestination.description,
                    deviceInfo = selectedDevice?.let {
                        DeviceInfo(
                            icon = Icons.Outlined.Smartphone,
                            name = "${it.manufacturer} ${it.modelName}",
                            model = "Android ${it.androidVersion} (API ${it.sdkVersion})"
                        )
                    },
                    actions = {
                        // 디바이스 선택 드롭다운
                        WePrayDropdown(
                            modifier = Modifier.width(250.dp),
                            value = selectedDevice,
                            onValueChange = { device ->
                                appViewModel.selectDevice(device)
                            },
                            items = connectedDevices,
                            placeholder = "디바이스를 선택하세요",
                            enabled = connectedDevices.isNotEmpty(),
                            itemLabel = { device ->
                                "${device.modelName} (${device.serialNumber})"
                            }
                        )

                        // 새로고침 버튼
                        WePrayIconButton(
                            icon = Icons.Outlined.Refresh,
                            contentDescription = "디바이스 새로고침",
                            onClick = {
                                appViewModel.refreshDevices()
                            }
                        )
                    }
                )
            },
            sideRail = {
                WePraySideRail(
                    topLevelDestination = topLevelDestination,
                    onItemClick = {
                        backStack.remove(it)
                        backStack.add(it)
                    }
                )
            },
            content = {
                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = entryProvider {
                        devicesScene()

                        installerScene()

                        deepLinkerScene()

                        settingsScene()
                    }
                )
            }
        )
    }
}