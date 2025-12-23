package com.ksssssw.wepray.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.InstallMobile
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.ksssssw.wepray.ui.scene.deeplinker.DeepLinker
import com.ksssssw.wepray.ui.scene.devices.Devices
import com.ksssssw.wepray.ui.scene.installer.Installer
import com.ksssssw.wepray.ui.scene.settings.Settings

enum class TopLevelDestination(
    val route: NavKey,
    val label: String,
    val description: String,
    val icon: ImageVector,
) {
    DEVICES(
        route = Devices,
        label = "Devices",
        description = "연결된 디바이스 목록",
        icon = Icons.Outlined.Devices
    ),
    INSTALLER(
        route = Installer,
        label = "APK Install",
        description = "APK 설치",
        icon = Icons.Outlined.InstallMobile
    ),
    DEEP_LINKER(
        route = DeepLinker,
        label = "DeepLinks",
        description = "DeepLinks 테스트",
        icon = Icons.Outlined.Link
    ),
    SETTINGS(
        route = Settings,
        label = "Settings",
        description = "설정",
        icon = Icons.Outlined.Settings
    )
    ;

    companion object {
        val START_DESTINATION = DEVICES

        fun fromDestination(destination: NavKey?): TopLevelDestination {
            return entries.find { it.route::class == destination?.let { r -> r::class } }
                ?: START_DESTINATION
        }
    }
}