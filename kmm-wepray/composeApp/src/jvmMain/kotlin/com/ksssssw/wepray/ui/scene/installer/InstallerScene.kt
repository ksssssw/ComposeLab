package com.ksssssw.wepray.ui.scene.installer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.FolderOff
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ksssssw.wepray.domain.model.Device
import com.ksssssw.wepray.domain.model.DeviceStorageInfo
import com.ksssssw.wepray.ui.components.BadgeVariant
import com.ksssssw.wepray.ui.components.ButtonSize
import com.ksssssw.wepray.ui.components.IconButtonSize
import com.ksssssw.wepray.ui.components.ProgressSegment
import com.ksssssw.wepray.ui.components.WePrayApkCard
import com.ksssssw.wepray.ui.components.WePrayBadge
import com.ksssssw.wepray.ui.components.WePrayCard
import com.ksssssw.wepray.ui.components.WePrayDropdown
import com.ksssssw.wepray.ui.components.WePrayIconButton
import com.ksssssw.wepray.ui.components.WePrayLabeledTextField
import com.ksssssw.wepray.ui.components.WePrayMultiProgressBar
import com.ksssssw.wepray.ui.components.WePraySearchBar
import com.ksssssw.wepray.ui.components.WePraySecondaryButton
import com.ksssssw.wepray.ui.theme.WePrayTheme
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
internal data object Installer : NavKey

internal fun EntryProviderScope<NavKey>.installerScene() {
    entry<Installer> {
        InstallerScene()
    }
}

@Composable
fun InstallerScene(
    viewModel: InstallerViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is InstallerUiState.Loading -> {
            LoadingState()
        }

        is InstallerUiState.Success -> {
            // 폴더 선택 결과 처리
            LaunchedEffect(state.folderSelectionState) {
                when (state.folderSelectionState) {
                    is FolderSelectionState.Success -> {
                        println("✅ 폴더 선택됨: ${(state.folderSelectionState as FolderSelectionState.Success).path}")
                        viewModel.resetFolderSelectionState()
                    }

                    is FolderSelectionState.Cancelled -> {
                        println("ℹ️ 폴더 선택이 취소되었습니다")
                        viewModel.resetFolderSelectionState()
                    }

                    is FolderSelectionState.Error -> {
                        println("❌ 폴더 선택 실패: ${(state.folderSelectionState as FolderSelectionState.Error).message}")
                        viewModel.resetFolderSelectionState()
                    }

                    else -> { /* Idle or Selecting */
                    }
                }
            }

            InstallerContent(
                uiState = state,
                onFilterTextChange = viewModel::updateFilterText,
                onSortOptionChange = { option ->
                    viewModel.updateSortOption(
                        when (option) {
                            "Name" -> SortOption.NAME
                            "Modified" -> SortOption.MODIFIED_DATE
                            else -> SortOption.MODIFIED_DATE
                        }
                    )
                },
                onSelectFolder = viewModel::selectApkFolder,
                onRefreshList = viewModel::refreshApkList,
                onApkCardClick = viewModel::selectApkFile,
                onInstallClick = viewModel::installApk
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = WePrayTheme.colors.primary
        )
    }
}

@Composable
private fun InstallerContent(
    uiState: InstallerUiState.Success,
    onFilterTextChange: (String) -> Unit,
    onSortOptionChange: (String) -> Unit,
    onSelectFolder: () -> Unit,
    onRefreshList: () -> Unit,
    onApkCardClick: (ApkFileInfo) -> Unit,
    onInstallClick: (ApkFileInfo) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = WePrayTheme.spacing.xl)
    ) {
        // 메인 컨텐츠: 좌우 레이아웃
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sectionGap)
        ) {
            // 왼쪽: 폴더 & 검색 & 파일 리스트 (8/12)
            Column(
                modifier = Modifier
                    .weight(0.67f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sectionGap)
            ) {
                // 로컬 소스 디렉토리
                LocalSourceDirectorySection(
                    selectedFolderPath = uiState.folderPath,
                    onSelectFolder = onSelectFolder
                )

                // 검색 & 필터
                SearchAndFiltersSection(
                    filterText = uiState.filterText,
                    sortOption = when (uiState.sortOption) {
                        SortOption.NAME -> "Name"
                        SortOption.MODIFIED_DATE -> "Modified"
                    },
                    onFilterTextChange = onFilterTextChange,
                    onSortOptionChange = onSortOptionChange,
                    onRefreshList = onRefreshList
                )

                // 파일 리스트
                ApkFileListSection(
                    apkFiles = uiState.apkFiles,
                    selectedApkFile = uiState.selectedApkFile,
                    selectedDevice = uiState.selectedDevice,
                    installStates = uiState.installStates,
                    filterText = uiState.filterText,
                    sortOption = uiState.sortOption,
                    onApkCardClick = onApkCardClick,
                    onInstallClick = onInstallClick,
                    modifier = Modifier.weight(1f)
                )
            }

            // 오른쪽: 통계 & 정보 (4/12)
            Column(
                modifier = Modifier
                    .weight(0.33f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sectionGap)
            ) {
                // 통계 카드
                StatsCardsSection(
                    totalFilesFound = uiState.totalFilesCount,
                )

                // 디바이스 스토리지 카드
                DeviceStorageCard(
                    selectedDevice = uiState.selectedDevice,
                    deviceStorageInfo = uiState.deviceStorageInfo
                )

                // APK 상세 정보 섹션
                ApkDetailSection(
                    selectedApkFile = uiState.selectedApkFile,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// ============================================
// 로컬 소스 디렉토리 섹션
// ============================================

@Composable
private fun LocalSourceDirectorySection(
    selectedFolderPath: String?,
    onSelectFolder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WePrayCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
        ) {
            Text(
                text = "LOCAL SOURCE DIRECTORY",
                style = WePrayTheme.typography.overline,
                color = WePrayTheme.colors.textSecondary
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                WePrayLabeledTextField(
                    value = selectedFolderPath ?: "",
                    onValueChange = { /* 직접 입력은 미지원 */ },
                    placeholder = "Enter path...",
                    leadingIcon = Icons.Outlined.FolderOpen,
                    modifier = Modifier.weight(1f),
                    label = "",
                    enabled = false
                )

                WePraySecondaryButton(
                    text = "Browse",
                    onClick = onSelectFolder,
                    size = ButtonSize.Large,
                )
            }
        }
    }
}

// ============================================
// 검색 & 필터 섹션
// ============================================

@Composable
private fun SearchAndFiltersSection(
    filterText: String,
    sortOption: String,
    onFilterTextChange: (String) -> Unit,
    onSortOptionChange: (String) -> Unit,
    onRefreshList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 검색 바
        WePraySearchBar(
            value = filterText,
            onValueChange = onFilterTextChange,
            placeholder = "Filter APKs by name...",
            modifier = Modifier.weight(1f)
        )

        // 정렬
        WePrayDropdown(
            value = sortOption,
            onValueChange = onSortOptionChange,
            items = listOf("Name", "Modified"),
            placeholder = "Sort",
            modifier = Modifier.width(140.dp)
        )

        // 새로고침 버튼
        WePrayIconButton(
            icon = Icons.Outlined.Refresh,
            contentDescription = "Refresh List",
            size = IconButtonSize.Large,
            onClick = onRefreshList
        )
    }
}

// ============================================
// APK 파일 리스트 섹션
// ============================================

@Composable
private fun ApkFileListSection(
    apkFiles: List<ApkFileInfo>,
    selectedApkFile: ApkFileInfo?,
    selectedDevice: com.ksssssw.wepray.domain.model.Device?,
    installStates: Map<String, InstallStatus>,
    filterText: String,
    sortOption: SortOption,
    onApkCardClick: (ApkFileInfo) -> Unit,
    onInstallClick: (ApkFileInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    // 필터나 정렬 옵션이 변경되면 스크롤을 최상단으로 이동
    LaunchedEffect(filterText, sortOption) {
        if (apkFiles.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    WePrayCard(
        modifier = modifier.fillMaxWidth()
    ) {
        when {
            // 파일이 없을 때
            apkFiles.isEmpty() -> {
                EmptyApkListState(
                    selectedDevice = selectedDevice
                )
            }

            // 파일 리스트
            else -> {
                LazyColumn(state = listState) {
                    items(apkFiles, key = { it.filePath }) { apkFile ->
                        val installStatus = installStates[apkFile.filePath]

                        ApkListItem(
                            apkFile = apkFile,
                            isSelected = apkFile == selectedApkFile,
                            installStatus = installStatus,
                            onCardClick = { onApkCardClick(apkFile) },
                            onInstallClick = { onInstallClick(apkFile) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ApkListItem(
    apkFile: ApkFileInfo,
    isSelected: Boolean,
    installStatus: InstallStatus?,
    onCardClick: () -> Unit,
    onInstallClick: () -> Unit,
) {
    val isInstalling = installStatus is InstallStatus.Installing
    val isInstalled = installStatus is InstallStatus.Installed
    val isError = installStatus is InstallStatus.Error
    val errorMessage = (installStatus as? InstallStatus.Error)?.message

    WePrayApkCard(
        fileName = apkFile.fileName,
        packageName = apkFile.packageName ?: "N/A",
        version = apkFile.versionName,
        modifiedTime = apkFile.modifiedDate,
        isSelected = isSelected,
        isInstalling = isInstalling,
        isInstalled = isInstalled,
        isError = isError,
        errorMessage = errorMessage,
        onCardClick = onCardClick,
        onInstallClick = onInstallClick,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun EmptyApkListState(
    selectedDevice: Device?,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WePrayTheme.spacing.xxl),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(WePrayTheme.colors.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.FolderOff,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = WePrayTheme.colors.textDisabled
                )
            }

            Text(
                text = "No APKs Found",
                style = WePrayTheme.typography.headlineLarge,
                color = WePrayTheme.colors.onSurface
            )

            Text(
                text = "There are no APK files in the selected directory.\nTry browsing a different folder.",
                style = WePrayTheme.typography.bodyMedium,
                color = WePrayTheme.colors.textSecondary,
                textAlign = TextAlign.Center
            )

            if (selectedDevice == null) {
                Spacer(modifier = Modifier.height(WePrayTheme.spacing.sm))
                Text(
                    text = "⚠️ Please select a device from the Devices tab first",
                    style = WePrayTheme.typography.bodyMedium,
                    color = WePrayTheme.colors.error,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ============================================
// 통계 카드 섹션
// ============================================

@Composable
private fun StatsCardsSection(
    totalFilesFound: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
    ) {
        // Total Apks Found 카드
        StatsCard(
            icon = Icons.Outlined.Apps,
            iconTint = WePrayTheme.colors.primary,
            label = "Total Apks Found",
            value = totalFilesFound.toString(),
            progress = if (totalFilesFound > 0) 0.75f else 0f,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatsCard(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    subLabel: String? = null,
    progress: Float? = null,
) {
    WePrayCard(
        modifier = modifier
    ) {
        Box {
            // 백그라운드 아이콘 (희미하게)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(WePrayTheme.spacing.md)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = iconTint.copy(alpha = 0.1f)
                )
            }

            // 컨텐츠
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = label,
                    style = WePrayTheme.typography.bodyMedium,
                    color = WePrayTheme.colors.textSecondary
                )

                Text(
                    text = value,
                    style = WePrayTheme.typography.displayLarge,
                    color = WePrayTheme.colors.onSurface
                )

                if (subLabel != null) {
                    Text(
                        text = subLabel,
                        style = WePrayTheme.typography.labelMedium,
                        color = WePrayTheme.colors.textDisabled
                    )
                }

                if (progress != null) {
                    Spacer(modifier = Modifier.height(WePrayTheme.spacing.xs))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(WePrayTheme.shapes.full),
                        color = WePrayTheme.colors.primary,
                        trackColor = WePrayTheme.colors.surfaceVariant,
                    )
                }
            }
        }
    }
}

// ============================================
// 디바이스 스토리지 카드
// ============================================

@Composable
private fun DeviceStorageCard(
    selectedDevice: Device?,
    deviceStorageInfo: DeviceStorageInfo?,
    modifier: Modifier = Modifier,
) {
    WePrayCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
        ) {
            // 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Device Storage",
                    style = WePrayTheme.typography.headlineMedium,
                    color = WePrayTheme.colors.onSurface
                )
                Icon(
                    imageVector = Icons.Outlined.Smartphone,
                    contentDescription = null,
                    tint = WePrayTheme.colors.textDisabled
                )
            }

            if (selectedDevice != null) {
                if (deviceStorageInfo != null) {
                    // 사용률 표시
                    Column(
                        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Used",
                                style = WePrayTheme.typography.bodyMedium,
                                color = WePrayTheme.colors.textSecondary
                            )
                            Text(
                                text = "${deviceStorageInfo.usedPercentage}%",
                                style = WePrayTheme.typography.bodyMedium,
                                color = WePrayTheme.colors.textSecondary
                            )
                        }

                        // 멀티 프로그레스 바
                        WePrayMultiProgressBar(
                            segments = listOf(
                                ProgressSegment(
                                    deviceStorageInfo.appsPercentage,
                                    WePrayTheme.colors.primary,
                                    "Apps"
                                ),
                                ProgressSegment(
                                    deviceStorageInfo.mediaPercentage,
                                    WePrayTheme.colors.accentPurple,
                                    "Media"
                                ),
                                ProgressSegment(
                                    deviceStorageInfo.systemPercentage,
                                    WePrayTheme.colors.success,
                                    "System"
                                )
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = deviceStorageInfo.getFormattedRange(),
                                style = WePrayTheme.typography.labelMedium,
                                color = WePrayTheme.colors.textDisabled
                            )
                            Text(
                                text = deviceStorageInfo.getFormattedFree(),
                                style = WePrayTheme.typography.labelMedium,
                                color = WePrayTheme.colors.success
                            )
                        }
                    }

                    // 범례
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                    ) {
                        StorageLegendItem(
                            color = WePrayTheme.colors.primary,
                            label = "Apps"
                        )
                        StorageLegendItem(
                            color = WePrayTheme.colors.accentPurple,
                            label = "Media"
                        )
                        StorageLegendItem(
                            color = WePrayTheme.colors.success,
                            label = "System"
                        )
                    }
                } else {
                    // 로딩 중
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = WePrayTheme.spacing.lg),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = WePrayTheme.colors.primary
                        )
                    }
                }
            } else {
                Text(
                    text = "No device connected",
                    style = WePrayTheme.typography.bodyMedium,
                    color = WePrayTheme.colors.textDisabled,
                    modifier = Modifier.padding(vertical = WePrayTheme.spacing.md)
                )
            }
        }
    }
}

@Composable
private fun StorageLegendItem(
    color: androidx.compose.ui.graphics.Color,
    label: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            style = WePrayTheme.typography.labelMedium,
            color = WePrayTheme.colors.textDisabled
        )
    }
}

// ============================================
// APK 상세 정보 섹션
// ============================================
@Composable
private fun ApkDetailSection(
    selectedApkFile: ApkFileInfo?,
    modifier: Modifier = Modifier,
) {
    WePrayCard(
        modifier = modifier.fillMaxWidth()
    ) {
        if (selectedApkFile != null) {
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                // 헤더
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = WePrayTheme.spacing.md),
                ) {
                    Text(
                        text = "Selected Apk Info",
                        style = WePrayTheme.typography.headlineMedium,
                        color = WePrayTheme.colors.onSurface
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
                ) {
                    // App Icon
                    // selectedApkFile.iconPath에 경로가 있지만, APK 내부에서 추출해야 함
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .clip(WePrayTheme.shapes.default)
                            .background(WePrayTheme.colors.accentEmerald.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Android,
                            contentDescription = "App Icon",
                            modifier = Modifier.size(32.dp),
                            tint = WePrayTheme.colors.accentEmerald
                        )
                    }

                    Column(
                        modifier = modifier,
                        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
                    ) {
                        // App Name & version
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedApkFile.appName ?: selectedApkFile.fileName,
                                style = WePrayTheme.typography.bodyLarge,
                                color = WePrayTheme.colors.onSurface
                            )

                            // App Version Name(Version Code)
                            val versionText = buildString {
                                append(selectedApkFile.versionName ?: "?.?.?")
                                append("(")
                                append(selectedApkFile.versionCode ?: "?")
                                append(")")
                            }
                            WePrayBadge(
                                text = versionText,
                                variant = BadgeVariant.Neutral
                            )
                        }
                        // App PackageName
                        Text(
                            text = selectedApkFile.packageName ?: "Unknown Package",
                            style = WePrayTheme.typography.bodySmall,
                            color = WePrayTheme.colors.textSecondary
                        )
                    }
                }

                // Signing Key
                DetailInfoItem(
                    label = "Signing Key"
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(WePrayTheme.shapes.input)
                            .background(WePrayTheme.colors.surface)
                            .border(
                                width = 1.dp,
                                color = WePrayTheme.colors.border,
                                shape = WePrayTheme.shapes.input
                            )
                            .padding(WePrayTheme.spacing.md)
                    ) {
                        Text(
                            text = selectedApkFile.signingKey ?: "Not available (requires apksigner)",
                            style = WePrayTheme.typography.bodySmall,
                            color = WePrayTheme.colors.textTertiary
                        )
                    }
                }

                // SDK Versions
                DetailInfoItem(
                    label = "SDK Versions"
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = WePrayTheme.spacing.xl),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SdkVersionChip(
                            label = "Min",
                            value = selectedApkFile.minSdkVersion ?: "?"
                        )
                        SdkVersionChip(
                            label = "Target",
                            value = selectedApkFile.targetSdkVersion ?: "?"
                        )
                        SdkVersionChip(
                            label = "Compile",
                            value = selectedApkFile.compileSdkVersion ?: "?"
                        )
                    }
                }
            }
        } else {
            // APK가 선택되지 않은 상태
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WePrayTheme.spacing.xxl),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(WePrayTheme.colors.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Apps,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = WePrayTheme.colors.textDisabled
                        )
                    }

                    Text(
                        text = "No APK Selected",
                        style = WePrayTheme.typography.headlineLarge,
                        color = WePrayTheme.colors.onSurface
                    )

                    Text(
                        text = "Select an APK from the list\nto view its details",
                        style = WePrayTheme.typography.bodyMedium,
                        color = WePrayTheme.colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailInfoItem(
    modifier: Modifier = Modifier,
    label: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
    ) {
        Text(
            text = label,
            style = WePrayTheme.typography.bodyMedium,
            color = WePrayTheme.colors.textSecondary
        )

        content()
    }
}

@Composable
private fun SdkVersionChip(
    label: String,
    value: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
    ) {
        Box(
            modifier = Modifier
                .clip(WePrayTheme.shapes.small)
                .background(WePrayTheme.colors.surfaceVariant)
                .padding(horizontal = WePrayTheme.spacing.md, vertical = WePrayTheme.spacing.xs),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                style = WePrayTheme.typography.bodyLarge,
                color = WePrayTheme.colors.textTertiary
            )
        }
        Text(
            text = label,
            style = WePrayTheme.typography.labelSmall,
            color = WePrayTheme.colors.textDisabled
        )
    }
}

