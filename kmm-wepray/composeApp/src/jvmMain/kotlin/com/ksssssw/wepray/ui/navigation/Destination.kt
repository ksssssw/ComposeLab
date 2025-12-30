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
        description = "View and manage connected devices",
        icon = Icons.Outlined.Devices
    ),
    INSTALLER(
        route = Installer,
        label = "APK Install",
        description = "Install APK files to devices",
        icon = Icons.Outlined.InstallMobile
    ),
    DEEP_LINKER(
        route = DeepLinker,
        label = "DeepLinks",
        description = "Test deep links and app intents on your connected device",
        icon = Icons.Outlined.Link
    ),
    SETTINGS(
        route = Settings,
        label = "Settings",
        description = "Configure application settings",
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