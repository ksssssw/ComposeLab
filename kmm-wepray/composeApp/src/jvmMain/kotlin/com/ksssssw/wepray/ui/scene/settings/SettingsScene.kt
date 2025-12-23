package com.ksssssw.wepray.ui.scene.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ksssssw.wepray.ui.theme.WePrayTheme
import kotlinx.serialization.Serializable

@Serializable internal data object Settings: NavKey

internal fun EntryProviderScope<NavKey>.settingsScene() {
    entry<Settings> {
        SettingsScene()
    }
}

@Composable
fun SettingsScene() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Settings",
            color = WePrayTheme.colors.primary,
            fontSize = 24.sp
        )
    }
}