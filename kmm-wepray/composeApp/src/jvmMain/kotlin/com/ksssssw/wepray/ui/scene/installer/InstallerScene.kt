package com.ksssssw.wepray.ui.scene.installer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ksssssw.wepray.ui.components.WePrayApkFileCard
import com.ksssssw.wepray.ui.components.WePrayIconButton
import com.ksssssw.wepray.ui.components.WePrayTextField
import com.ksssssw.wepray.ui.theme.WePrayTheme
import com.ksssssw.wepray.ui.theme.tokens.TextDisabled
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable internal data object Installer: NavKey

internal fun EntryProviderScope<NavKey>.installerScene() {
    entry<Installer> {
        InstallerScene()
    }
}

@Composable
fun InstallerScene(
    viewModel: InstallerViewModel = koinViewModel()
) {
    val selectedFolderPath by viewModel.selectedFolderPath.collectAsStateWithLifecycle()
    val filterText by viewModel.filterText.collectAsStateWithLifecycle()
    val filteredApkFiles by viewModel.filteredApkFiles.collectAsStateWithLifecycle()
    val selectedApkFile by viewModel.selectedApkFile.collectAsStateWithLifecycle()
    val selectedDevice by viewModel.selectedDevice.collectAsStateWithLifecycle()
    val folderSelectionState by viewModel.folderSelectionState.collectAsStateWithLifecycle()
    val installState by viewModel.installState.collectAsStateWithLifecycle()
    
    // 폴더 선택 결과 처리
    LaunchedEffect(folderSelectionState) {
        when (folderSelectionState) {
            is FolderSelectionState.Success -> {
                println("✅ 폴더 선택됨: ${(folderSelectionState as FolderSelectionState.Success).path}")
                viewModel.resetFolderSelectionState()
            }
            is FolderSelectionState.Cancelled -> {
                println("ℹ️ 폴더 선택이 취소되었습니다")
                viewModel.resetFolderSelectionState()
            }
            is FolderSelectionState.Error -> {
                println("❌ 폴더 선택 실패: ${(folderSelectionState as FolderSelectionState.Error).message}")
                viewModel.resetFolderSelectionState()
            }
            else -> { /* Idle or Selecting */ }
        }
    }
    
    // 설치 상태 처리
    LaunchedEffect(installState) {
        when (installState) {
            is InstallState.Success -> {
                println("✅ APK 설치 성공: ${(installState as InstallState.Success).fileName}")
                viewModel.resetInstallState()
            }
            is InstallState.Error -> {
                println("❌ APK 설치 실패: ${(installState as InstallState.Error).message}")
                viewModel.resetInstallState()
            }
            else -> { /* Idle or Installing */ }
        }
    }
    
    InstallerContent(
        modifier = Modifier,
        selectedFolderPath = selectedFolderPath,
        filterText = filterText,
        filteredApkFiles = filteredApkFiles,
        selectedApkFile = selectedApkFile,
        selectedDevice = selectedDevice,
        installState = installState,
        onFilterTextChange = viewModel::updateFilterText,
        onSelectFolder = viewModel::selectApkFolder,
        onApkCardClick = viewModel::selectApkFile,
        onInstallClick = viewModel::installSelectedApk,
        onApkDrop = viewModel::installApkByPath
    )
}

@Composable
private fun InstallerContent(
    modifier: Modifier = Modifier,
    selectedFolderPath: String?,
    filterText: String,
    filteredApkFiles: List<ApkFileInfo>,
    selectedApkFile: ApkFileInfo?,
    selectedDevice: com.ksssssw.wepray.domain.model.Device?,
    installState: InstallState,
    onFilterTextChange: (String) -> Unit,
    onSelectFolder: () -> Unit,
    onApkCardClick: (ApkFileInfo) -> Unit,
    onInstallClick: () -> Unit,
    onApkDrop: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = WePrayTheme.spacing.lg)
    ) {
        // 상단 검색 및 폴더 선택 영역
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = WePrayTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 검색 텍스트 필드
            WePrayTextField(
                modifier = Modifier.weight(1f),
                value = filterText,
                onValueChange = onFilterTextChange,
                placeholder = "APK 파일명 검색...",
//                enabled = selectedFolderPath != null && filteredApkFiles.isNotEmpty()
            )
            
            // 현재 선택된 폴더 경로 표시
            if (selectedFolderPath != null) {
                Text(
                    text = selectedFolderPath,
                    style = WePrayTheme.typography.bodyMedium,
                    color = WePrayTheme.colors.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
            
            // 폴더 선택 버튼
            WePrayIconButton(
                icon = if (selectedFolderPath != null) Icons.Outlined.FolderOpen else Icons.Outlined.Folder,
                contentDescription = "폴더 선택",
                onClick = onSelectFolder
            )
        }
        
        Spacer(modifier = Modifier.height(WePrayTheme.spacing.lg))
        
        // 컨텐츠 영역 (상태에 따라 다르게 표시)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = WePrayTheme.spacing.lg)
        ) {
            when {
                // 설치 중
                installState is InstallState.Installing -> {
                    InstallingState(fileName = installState.fileName)
                }
                
                // 폴더를 선택하지 않았을 경우
                selectedFolderPath == null -> {
                    EmptyFolderState(
                        selectedDevice = selectedDevice,
                        onSelectFolder = onSelectFolder
                    )
                }
                
                // 폴더를 선택했지만 APK 파일이 없을 경우
                filteredApkFiles.isEmpty() -> {
                    NoApkFilesState(
                        selectedDevice = selectedDevice,
                        folderPath = selectedFolderPath,
                        onSelectFolder = onSelectFolder
                    )
                }
                
                // APK 파일 목록 표시
                else -> {
                    ApkFileList(
                        apkFiles = filteredApkFiles,
                        selectedApkFile = selectedApkFile,
                        onApkCardClick = onApkCardClick,
                        onInstallClick = onInstallClick
                    )
                }
            }
        }
    }
}

@Composable
private fun InstallingState(fileName: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = WePrayTheme.colors.primary
            )
            Text(
                text = "APK 설치 중...",
                style = WePrayTheme.typography.headlineLarge,
                color = WePrayTheme.colors.primary
            )
            Text(
                text = fileName,
                style = WePrayTheme.typography.bodyLarge,
                color = WePrayTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EmptyFolderState(
    isDragging: Boolean = false,
    selectedDevice: com.ksssssw.wepray.domain.model.Device?,
    onSelectFolder: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(WePrayTheme.spacing.xl)
        ) {
            Icon(
                imageVector = Icons.Outlined.Folder,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = TextDisabled
            )
            
            Text(
                text = "폴더를 선택해주세요",
                style = WePrayTheme.typography.headlineLarge,
                color = WePrayTheme.colors.onSurface
            )
            
            Text(
                text = "APK 파일이 있는 폴더를 선택하세요",
                style = WePrayTheme.typography.bodyLarge,
                color = WePrayTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            if (selectedDevice == null) {
                Spacer(modifier = Modifier.height(WePrayTheme.spacing.sm))
                Text(
                    text = "⚠️ 먼저 Devices 탭에서 디바이스를 선택해주세요",
                    style = WePrayTheme.typography.bodyMedium,
                    color = WePrayTheme.colors.error,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun NoApkFilesState(
    isDragging: Boolean = false,
    selectedDevice: com.ksssssw.wepray.domain.model.Device?,
    folderPath: String,
    onSelectFolder: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(WePrayTheme.spacing.xl)
        ) {
            Icon(
                imageVector = Icons.Outlined.Android,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = TextDisabled
            )
            
            Text(
                text = "APK 파일이 없습니다",
                style = WePrayTheme.typography.headlineLarge,
                color = WePrayTheme.colors.onSurface
            )
            
            Text(
                text = "이 폴더에는 APK 파일이 없습니다\n다른 폴더를 선택해주세요",
                style = WePrayTheme.typography.bodyLarge,
                color = WePrayTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "현재 폴더: $folderPath",
                style = WePrayTheme.typography.bodySmall,
                color = TextDisabled,
                textAlign = TextAlign.Center
            )
            
            if (selectedDevice == null) {
                Spacer(modifier = Modifier.height(WePrayTheme.spacing.sm))
                Text(
                    text = "⚠️ 먼저 Devices 탭에서 디바이스를 선택해주세요",
                    style = WePrayTheme.typography.bodyMedium,
                    color = WePrayTheme.colors.error,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ApkFileList(
    apkFiles: List<ApkFileInfo>,
    selectedApkFile: ApkFileInfo?,
    onApkCardClick: (ApkFileInfo) -> Unit,
    onInstallClick: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
    ) {
        items(apkFiles, key = { it.filePath }) { apkFile ->
            WePrayApkFileCard(
                fileName = apkFile.fileName,
                modifiedDate = apkFile.modifiedDate,
                isSelected = apkFile == selectedApkFile,
                onCardClick = { onApkCardClick(apkFile) },
                onInstallClick = onInstallClick
            )
        }
    }
}