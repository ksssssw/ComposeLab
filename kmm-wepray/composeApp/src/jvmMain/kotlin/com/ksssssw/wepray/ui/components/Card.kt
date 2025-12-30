package com.ksssssw.wepray.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Basic Card
 * Based on HTML design: rounded-xl with border and shadow
 */
@Composable
fun WePrayCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Column(
        modifier = modifier
            .shadow(
                elevation = if (isHovered && onClick != null) WePrayTheme.elevation.card else 0.dp,
                shape = WePrayTheme.shapes.card
            )
            .clip(WePrayTheme.shapes.card)
            .background(WePrayTheme.colors.surfaceVariant)
            .border(
                width = 1.dp,
                color = WePrayTheme.colors.border,
                shape = WePrayTheme.shapes.card
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
            .padding(WePrayTheme.spacing.cardPadding)
    ) {
        content()
    }
}

/**
 * WePray APK File Card
 * Based on HTML list item design
 */
@Composable
fun WePrayApkCard(
    fileName: String,
    version: String? = null,
    modifiedTime: String,
    modifier: Modifier = Modifier,
    isInstalling: Boolean = false,
    isInstalled: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    onCardClick: () -> Unit = {},
    onInstallClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val iconColor = when {
        isError -> WePrayTheme.colors.error
        isInstalling -> WePrayTheme.colors.primary
        isInstalled -> WePrayTheme.colors.success
        else -> WePrayTheme.colors.accentEmerald
    }

    val icon = when {
        isError -> Icons.Outlined.Warning
        isInstalling -> Icons.Outlined.Sync
        isInstalled -> Icons.Outlined.CheckCircle
        else -> Icons.Outlined.Android
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(WePrayTheme.shapes.default)
            .background(
                if (isHovered)
                    WePrayTheme.colors.surfaceElevated
                else
                    Color.Transparent
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onCardClick
            )
            .padding(
                horizontal = WePrayTheme.spacing.listItemPaddingHorizontal,
                vertical = WePrayTheme.spacing.listItemPadding
            ),
        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(WePrayTheme.shapes.default)
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(WePrayTheme.iconSize.default),
                tint = iconColor
            )
        }

        // File info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fileName,
                    style = WePrayTheme.typography.bodyMedium,
                    color = WePrayTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                if (version != null) {
                    WePrayBadge(
                        text = version,
                        variant = BadgeVariant.Neutral
                    )
                }
            }

            Text(
                text = when {
                    isError && errorMessage != null -> errorMessage
                    isInstalling -> "Installing..."
                    isInstalled -> "Installed successfully"
                    else -> "Modified: $modifiedTime"
                },
                style = WePrayTheme.typography.bodySmall,
                color = when {
                    isError -> WePrayTheme.colors.error
                    isInstalling -> WePrayTheme.colors.primary
                    isInstalled -> WePrayTheme.colors.success
                    else -> WePrayTheme.colors.textSecondary
                }
            )
        }

        // Actions
        if (isHovered && !isInstalling) {
            WePrayPrimaryButton(
                text = "Install",
                onClick = onInstallClick,
                size = ButtonSize.Default
            )
        }
    }
}

/**
 * WePray Device Card
 * Based on HTML device card design
 */
@Composable
fun WePrayDeviceCard(
    deviceModel: String,
    deviceManufacturer: String,
    serialNumber: String,
    androidVersion: String,
    apiLevel: String,
    resolution: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isConnected: Boolean = true,
    icon: ImageVector = Icons.Outlined.Smartphone,
    onCardClick: () -> Unit = {},
    onMirroringClick: () -> Unit = {},
    onScreenshotClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier = modifier
            .size(width = 360.dp, height = 320.dp)
            .shadow(
                elevation = if (isHovered || isSelected) WePrayTheme.elevation.card else 0.dp,
                shape = WePrayTheme.shapes.card
            )
            .clip(WePrayTheme.shapes.card)
            .background(
                WePrayTheme.colors.surfaceVariant
            )
            .border(
                width = 1.dp,
                color = when {
                    isSelected -> WePrayTheme.colors.primary
                    isHovered -> WePrayTheme.colors.primary
                    else -> WePrayTheme.colors.border
                },
                shape = WePrayTheme.shapes.card
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onCardClick
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WePrayTheme.spacing.cardPaddingLarge),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(WePrayTheme.shapes.card)
                    .background(WePrayTheme.gradients.Primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Device Icon",
                    modifier = Modifier.size(32.dp),
                    tint = androidx.compose.ui.graphics.Color.White
                )
            }

            // Device info
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = deviceModel,
                    style = WePrayTheme.typography.headlineMedium,
                    color = WePrayTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
                ) {
                    // Left column
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                    ) {
                        DeviceInfoItem(
                            label = "제조사",
                            value = deviceManufacturer
                        )
                        DeviceInfoItem(
                            label = "Serial",
                            value = serialNumber
                        )
                    }

                    // Right column
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                    ) {
                        DeviceInfoItem(
                            label = "Android",
                            value = "$androidVersion (API $apiLevel)"
                        )
                        DeviceInfoItem(
                            label = "해상도",
                            value = resolution
                        )
                    }
                }
            }

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
            ) {
                WePrayPrimaryButton(
                    modifier = Modifier.weight(1f),
                    text = "Mirroring",
                    onClick = onMirroringClick,
                    size = ButtonSize.Default,
                    enabled = isConnected
                )

                WePraySecondaryButton(
                    modifier = Modifier.weight(1f),
                    text = "Screenshot",
                    onClick = onScreenshotClick,
                    size = ButtonSize.Default,
                    enabled = isConnected
                )
            }
        }
    }
}

@Composable
private fun DeviceInfoItem(
    label: String,
    value: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
    ) {
        Text(
            text = label,
            style = WePrayTheme.typography.labelSmall,
            color = WePrayTheme.colors.textTertiary
        )

        Text(
            text = value,
            style = WePrayTheme.typography.labelMedium,
            color = WePrayTheme.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun CardPreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .width(900.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xxxl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xxxl)
        ) {
            // Basic Card
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Basic Card",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )

                WePrayCard(
                    onClick = { println("Card clicked") }
                ) {
                    Text(
                        text = "Card Title",
                        style = WePrayTheme.typography.headlineMedium,
                        color = WePrayTheme.colors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(WePrayTheme.spacing.md))

                    Text(
                        text = "Card content goes here. This is a basic card with hover effects.",
                        style = WePrayTheme.typography.bodyMedium,
                        color = WePrayTheme.colors.textSecondary
                    )
                }
            }

            // APK Cards
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "APK File Cards",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(WePrayTheme.shapes.card)
                        .background(WePrayTheme.colors.surfaceVariant)
                        .border(1.dp, WePrayTheme.colors.border, WePrayTheme.shapes.card),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    WePrayApkCard(
                        fileName = "com.instagram.android.apk",
                        version = "v294.0.0",
                        modifiedTime = "2 mins ago",
                        onInstallClick = { println("Install") }
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth().height(1.dp)
                            .background(WePrayTheme.colors.border)
                    )

                    WePrayApkCard(
                        fileName = "app-debug-unsigned.apk",
                        version = "v1.0.4-beta",
                        modifiedTime = "5 mins ago",
                        isInstalling = true
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth().height(1.dp)
                            .background(WePrayTheme.colors.border)
                    )

                    WePrayApkCard(
                        fileName = "spotify-lite.apk",
                        version = "v8.8.0",
                        modifiedTime = "1 hour ago",
                        isInstalled = true
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth().height(1.dp)
                            .background(WePrayTheme.colors.border)
                    )

                    WePrayApkCard(
                        fileName = "corrupt-build.apk",
                        version = null,
                        modifiedTime = "Yesterday",
                        isError = true,
                        errorMessage = "Parse error: Invalid manifest"
                    )
                }
            }

            // Device Cards
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Device Cards",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl)
                ) {
                    WePrayDeviceCard(
                        deviceModel = "SM-S918N",
                        deviceManufacturer = "Samsung",
                        serialNumber = "1234567890",
                        androidVersion = "14",
                        apiLevel = "34",
                        resolution = "1080 x 2408",
                        isSelected = true,
                        icon = Icons.Outlined.Smartphone
                    )

                    WePrayDeviceCard(
                        deviceModel = "Pixel 8 Pro",
                        deviceManufacturer = "Google",
                        serialNumber = "0987654321",
                        androidVersion = "14",
                        apiLevel = "34",
                        resolution = "1344 x 2992"
                    )
                }
            }
        }
    }
}
