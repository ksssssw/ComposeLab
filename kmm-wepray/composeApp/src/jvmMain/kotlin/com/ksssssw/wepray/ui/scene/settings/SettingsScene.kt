package com.ksssssw.wepray.ui.scene.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ksssssw.wepray.domain.model.AppSettings
import com.ksssssw.wepray.ui.components.*
import com.ksssssw.wepray.ui.theme.WePrayTheme
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable 
internal data object Settings: NavKey

internal fun EntryProviderScope<NavKey>.settingsScene() {
    entry<Settings> {
        SettingsScene()
    }
}

@Composable
fun SettingsScene(
    viewModel: SettingsViewModel = koinViewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val pathSelectionState by viewModel.pathSelectionState.collectAsStateWithLifecycle()
    
    // 경로 선택 결과 처리
    when (pathSelectionState) {
        is PathSelectionState.Success -> {
            println("✅ 스크린샷 경로 변경됨: ${(pathSelectionState as PathSelectionState.Success).path}")
            viewModel.resetPathSelectionState()
        }
        is PathSelectionState.Cancelled -> {
            println("ℹ️ 경로 선택이 취소되었습니다")
            viewModel.resetPathSelectionState()
        }
        is PathSelectionState.Error -> {
            println("❌ 경로 선택 실패: ${(pathSelectionState as PathSelectionState.Error).message}")
            viewModel.resetPathSelectionState()
        }
        else -> { /* Idle or Selecting */ }
    }
    
    SettingsContent(
        settings = settings,
        isSelecting = pathSelectionState is PathSelectionState.Selecting,
        onSelectPath = viewModel::selectScreenshotPath
    )
}

@Composable
private fun SettingsContent(
    settings: AppSettings,
    isSelecting: Boolean,
    onSelectPath: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = WePrayTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xxxl)
    ) {
        // Screenshot Path Setting
        ScreenshotPathSetting(
            currentPath = settings.screenshotSavePath,
            isSelecting = isSelecting,
            onSelectPath = onSelectPath
        )
        
        // App Info Section
        AppInfoSection()
    }
}

@Composable
private fun ScreenshotPathSetting(
    currentPath: String?,
    isSelecting: Boolean,
    onSelectPath: () -> Unit
) {
    WePrayCard {
        Column(
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
        ) {
            Text(
                text = "스크린샷 저장 경로",
                style = WePrayTheme.typography.headlineLarge,
                color = WePrayTheme.colors.textPrimary
            )
            
            Text(
                text = "스크린샷을 저장할 폴더를 선택하세요",
                style = WePrayTheme.typography.bodyMedium,
                color = WePrayTheme.colors.textSecondary
            )
            
            // Current path display
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .background(
                            WePrayTheme.colors.surface,
                            WePrayTheme.shapes.default
                        )
                        .padding(WePrayTheme.spacing.lg)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Folder,
                        contentDescription = null,
                        tint = WePrayTheme.colors.primary
                    )

                    Text(
                        text = currentPath ?: "기본 경로: ${AppSettings.getDefaultScreenshotPath()}",
                        style = WePrayTheme.typography.code,
                        color = if (currentPath != null) {
                            WePrayTheme.colors.textPrimary
                        } else {
                            WePrayTheme.colors.textSecondary
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Change path button
                WePrayPrimaryButton(
                    text = if (isSelecting) "선택 중..." else "경로 변경",
                    onClick = onSelectPath,
                    enabled = !isSelecting,
                )
            }
        }
    }
}

@Composable
private fun AppInfoSection() {
    WePrayCard {
        Column(
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
        ) {
            Text(
                text = "About",
                style = WePrayTheme.typography.headlineLarge,
                color = WePrayTheme.colors.textPrimary
            )
            
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                InfoRow(label = "Application", value = "WePray ADB Manager")
                InfoRow(label = "Version", value = "1.0.0")
                InfoRow(label = "Build", value = "Beta")
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = WePrayTheme.typography.bodyMedium,
            color = WePrayTheme.colors.textSecondary
        )
        Text(
            text = value,
            style = WePrayTheme.typography.bodyMedium,
            color = WePrayTheme.colors.textPrimary
        )
    }
}
