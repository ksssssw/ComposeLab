package com.ksssssw.wepray.ui.scene.devices

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ksssssw.wepray.ui.components.WePrayBasicCard
import com.ksssssw.wepray.ui.components.WePrayDeviceCard
import com.ksssssw.wepray.ui.theme.WePrayTheme
import kotlinx.serialization.Serializable

@Serializable
internal data object Devices : NavKey

internal fun EntryProviderScope<NavKey>.devicesScene() {
    entry<Devices> {
        DevicesScene()
    }
}

@Composable
fun DevicesScene() {
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
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg),
            itemVerticalAlignment = Alignment.CenterVertically
        ) {
            WePrayDeviceCard(
                modifier = Modifier
                    .size(width = 350.dp, height = 230.dp),
                deviceName = "Galaxy S23 Ultra",
                deviceModel = "SM-S918N",
                androidVersion = "Android 14 (API 34)",
                isSelected = false,
                isConnected = true,
                icon = Icons.Outlined.Smartphone,
                onClick = { println("Device selected") }
            )

            WePrayDeviceCard(
                modifier = Modifier
                    .size(width = 350.dp, height = 230.dp),
                deviceName = "Galaxy S23 Ultra",
                deviceModel = "SM-S918N",
                androidVersion = "Android 14 (API 34)",
                isSelected = false,
                isConnected = true,
                icon = Icons.Outlined.Smartphone,
                onClick = { println("Device selected") }
            )

            WePrayDeviceCard(
                modifier = Modifier
                    .size(width = 350.dp, height = 230.dp),
                deviceName = "Galaxy S23 Ultra",
                deviceModel = "SM-S918N",
                androidVersion = "Android 14 (API 34)",
                isSelected = false,
                isConnected = true,
                icon = Icons.Outlined.Smartphone,
                onClick = { println("Device selected") }
            )
        }
    }
}

@Preview
@Composable
fun BasicCardPreview() {
    WePrayTheme {
        DevicesScene()
    }
}
