package com.ksssssw.wepray.ui.scene.devices

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
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
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl),
            itemVerticalAlignment = Alignment.CenterVertically
        ) {
            // FIXME: adb로 획득한 디바이스 목록을 이곳에 표시한다.
            WePrayDeviceCard(
                deviceModel = "SM-S918N",
                deviceManufacturer = "Samsung",
                serialNumber = "1234567890",
                apiLevel = "33",
                resolution = "1080 x 2408",
                androidVersion = "14",
                isSelected = false,
                icon = Icons.Outlined.Smartphone,
                onCardClick = { /** TODO: 카드를 선택했을 때 액션 추가 */ },
                onMirroringClick = { /** TODO: 미러링 버튼을 클릭했을 때 액션 추가 */ },
                onScreenshotClick = { /** TODO: 스크린샷 버튼을 클릭했을 때 액션 추가 */ }
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
