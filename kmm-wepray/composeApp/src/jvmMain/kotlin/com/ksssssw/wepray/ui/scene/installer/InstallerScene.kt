package com.ksssssw.wepray.ui.scene.installer

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

@Serializable internal data object Installer: NavKey

internal fun EntryProviderScope<NavKey>.installerScene() {
    entry<Installer> {
        InstallerScene()
    }
}

@Composable
fun InstallerScene() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "APK 설치 화면",
            color = WePrayTheme.colors.primary,
            fontSize = 24.sp
        )
    }
}