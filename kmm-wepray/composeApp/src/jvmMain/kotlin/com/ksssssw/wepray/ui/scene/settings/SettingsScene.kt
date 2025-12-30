package com.ksssssw.wepray.ui.scene.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Card
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
import com.ksssssw.wepray.ui.components.WePrayPrimaryButton
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WePrayTheme.spacing.xl)
    ) {
        Text(
            text = "설정",
            style = WePrayTheme.typography.displayLarge,
            color = WePrayTheme.colors.primary
        )
        
        Spacer(modifier = Modifier.height(WePrayTheme.spacing.xl))
        
        // 스크린샷 저장 경로 설정
        ScreenshotPathSetting(
            currentPath = settings.screenshotSavePath,
            isSelecting = isSelecting,
            onSelectPath = onSelectPath
        )
    }
}

@Composable
private fun ScreenshotPathSetting(
    currentPath: String?,
    isSelecting: Boolean,
    onSelectPath: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = WePrayTheme.spacing.md)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WePrayTheme.spacing.lg)
        ) {
            Text(
                text = "스크린샷 저장 경로",
                style = WePrayTheme.typography.headlineLarge,
                color = WePrayTheme.colors.primary
            )
            
            Spacer(modifier = Modifier.height(WePrayTheme.spacing.sm))
            
            Text(
                text = "스크린샷을 저장할 폴더를 선택하세요",
                style = WePrayTheme.typography.bodyMedium,
                color = WePrayTheme.colors.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(WePrayTheme.spacing.md))
            
            // 현재 경로 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Folder,
                    contentDescription = null,
                    tint = WePrayTheme.colors.onSurfaceVariant
                )
                
                Text(
                    text = currentPath ?: "기본 경로: ${AppSettings.getDefaultScreenshotPath()}",
                    style = WePrayTheme.typography.bodyLarge,
                    color = if (currentPath != null) {
                        WePrayTheme.colors.onSurface
                    } else {
                        WePrayTheme.colors.onSurfaceVariant
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(WePrayTheme.spacing.md))
            
            // 경로 변경 버튼
            WePrayPrimaryButton(
                text = if (isSelecting) "선택 중..." else "경로 변경",
                onClick = onSelectPath,
                enabled = !isSelecting
            )
        }
    }
}
