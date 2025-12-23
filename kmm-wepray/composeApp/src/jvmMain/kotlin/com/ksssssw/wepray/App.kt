package com.ksssssw.wepray

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ksssssw.wepray.ui.layout.WePrayScaffold
import com.ksssssw.wepray.ui.layout.WePraySideRail
import com.ksssssw.wepray.ui.navigation.TopLevelDestination
import com.ksssssw.wepray.ui.scene.deeplinker.deepLinkerScene
import com.ksssssw.wepray.ui.scene.devices.devicesScene
import com.ksssssw.wepray.ui.scene.installer.installerScene
import com.ksssssw.wepray.ui.scene.settings.settingsScene
import com.ksssssw.wepray.ui.theme.WePrayTheme

@Composable
fun App() {
    WePrayTheme {
        val backStack = remember { mutableStateListOf(TopLevelDestination.START_DESTINATION.route) }
        val currentDestination = backStack.lastOrNull()
        val topLevelDestination = TopLevelDestination.fromDestination(currentDestination as NavKey)
        
        WePrayScaffold(
            currentTopLevelDestination = topLevelDestination,
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