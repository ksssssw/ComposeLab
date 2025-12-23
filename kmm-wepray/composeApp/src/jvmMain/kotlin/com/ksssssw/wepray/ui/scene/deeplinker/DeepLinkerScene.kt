package com.ksssssw.wepray.ui.scene.deeplinker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ksssssw.wepray.ui.components.WePrayDropdown
import com.ksssssw.wepray.ui.theme.WePrayTheme
import kotlinx.serialization.Serializable

@Serializable internal data object DeepLinker: NavKey

internal fun EntryProviderScope<NavKey>.deepLinkerScene() {
    entry<DeepLinker> {
        DeepLinkerScene()
    }
}

@Composable
fun DeepLinkerScene() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "DeepLinker",
            color = WePrayTheme.colors.primary,
            fontSize = 24.sp
        )

        val categories = listOf("홈 화면", "상세 화면", "설정 화면", "프로필 화면")

        // Default
        var selectedCategory1 by remember { mutableStateOf<String?>(null) }
        Column(
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
        ) {
            androidx.compose.material3.Text(
                text = "Default",
                style = WePrayTheme.typography.labelLarge,
                color = WePrayTheme.colors.onSurfaceVariant
            )
            WePrayDropdown(
                value = selectedCategory1,
                onValueChange = { selectedCategory1 = it },
                items = categories,
                placeholder = "카테고리 선택"
            )
        }
    }
}