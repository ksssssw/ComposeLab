package com.ksssssw.wepray.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme
import com.ksssssw.wepray.ui.theme.tokens.BorderColor
import com.ksssssw.wepray.ui.theme.tokens.WePrayGradients

/**
 * WePray Basic Card
 * 
 * 디자인 가이드 스펙:
 * - Background: rgba(26, 26, 26, 0.8)
 * - Border: 1px solid rgba(255, 255, 255, 0.1)
 * - Border Radius: 12px
 * - Padding: 25px
 * - Shadow (Hover): 0 8px 25px rgba(74, 158, 224, 0.2)
 * - Transform: translateY(-3px) on hover
 */
@Composable
fun WePrayBasicCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    Column(
        modifier = modifier
            .shadow(
                elevation = if (isHovered) WePrayTheme.elevation.lg else WePrayTheme.elevation.none,
                shape = WePrayTheme.shapes.card,
                spotColor = WePrayTheme.colors.primary.copy(alpha = 0.2f)
            )
            .clip(WePrayTheme.shapes.card)
            .background(WePrayTheme.colors.cardBackground)
            .border(
                width = 1.dp,
                color = BorderColor,
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
 * WePray Device Card
 * 
 * 디자인 가이드 스펙:
 * - Background: rgba(26, 26, 26, 0.8)
 * - Border: 2px solid rgba(255, 255, 255, 0.1)
 * - Border (Hover): 2px solid #4A9EE0
 * - Border (Selected): 2px solid #4A9EE0
 * - Background (Selected): rgba(74, 158, 224, 0.1)
 * - Border Radius: 12px
 * - Padding: 20px
 * - Display: Flex with gap 20px
 * 
 * Structure: [Icon 50x50] [Device Info Flex-1] [Status Badge Auto]
 */
@Composable
fun WePrayDeviceCard(
    deviceModel: String,
    deviceManufacturer: String,
    serialNumber: String,
    androidVersion: String,
    apiLevel: String,
    resolution: String,
    isSelected: Boolean = false,
    isConnected: Boolean = true,
    icon: ImageVector = Icons.Outlined.Smartphone,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    onMirroringClick: () -> Unit = {},
    onScreenshotClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    Box(
        modifier = modifier
            .size(width = 360.dp, height = 320.dp)
            .clip(WePrayTheme.shapes.card)
            .background(
                if (isSelected) 
                    WePrayTheme.colors.hoverEffect 
                else 
                    WePrayTheme.colors.cardBackground
            )
            .border(
                width = 2.dp,
                color = when {
                    isSelected -> WePrayTheme.colors.primary
                    isHovered -> WePrayTheme.colors.primary
                    else -> BorderColor
                },
                shape = WePrayTheme.shapes.card
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onCardClick
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WePrayTheme.spacing.xl),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(WePrayTheme.shapes.card)
                    .background(
                        WePrayGradients.Rainbow
                    ),
                contentAlignment = Alignment.Center
            ) {
                // 디바이스 아이콘 (50x50)
                Icon(
                    imageVector = icon,
                    contentDescription = "Device Icon",
                    modifier = Modifier.size(32.dp),
                    tint = WePrayTheme.colors.onSurface
                )
            }

            // 디바이스 정보 (Flex-1)
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                // Model Name (Title)
                Text(
                    text = deviceModel,
                    style = WePrayTheme.typography.bodyLarge,
                    color = WePrayTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // 2-Column Grid for Device Details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
                ) {
                    // Left Column
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                    ) {
                        InfoItem(
                            label = "제조사",
                            value = deviceManufacturer
                        )
                        InfoItem(
                            label = "Serial",
                            value = serialNumber
                        )
                    }

                    // Right Column
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                    ) {
                        InfoItem(
                            label = "Android",
                            value = "$androidVersion (API ${apiLevel})"
                        )
                        InfoItem(
                            label = "해상도",
                            value = resolution
                        )
                    }
                }
            }

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
private fun InfoItem(
    label: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = WePrayTheme.typography.labelMedium,
            color = WePrayTheme.colors.onSurfaceVariant
        )

        Text(
            text = value,
            style = WePrayTheme.typography.labelLarge,
            color = WePrayTheme.colors.onSurface,
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
                .width(600.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl)
        ) {
            // Basic Card
            WePrayBasicCard(
                onClick = { println("Card clicked") }
            ) {
                Text(
                    text = "Basic Card",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onSurface
                )
                
                Spacer(modifier = Modifier.height(WePrayTheme.spacing.md))
                
                Text(
                    text = "카드 내용이 여기에 표시됩니다. 호버 시 그림자 효과가 나타납니다.",
                    style = WePrayTheme.typography.bodyMedium,
                    color = WePrayTheme.colors.onSurfaceVariant
                )
            }
            
            // Device Card - Connected
            WePrayDeviceCard(
                deviceModel = "SM-S918N",
                deviceManufacturer = "Samsung",
                serialNumber = "1234567890",
                apiLevel = "33",
                resolution = "1080 x 2408",
                androidVersion = "Android 14 (API 34)",
                isSelected = false,
                icon = Icons.Outlined.Smartphone,
                onCardClick = { println("Device selected") },
                onMirroringClick = { println("Mirroring clicked") },
                onScreenshotClick = { println("Screenshot clicked") }
            )
            
            // Device Card - Selected
            WePrayDeviceCard(
                deviceModel = "SM-S918N",
                deviceManufacturer = "Samsung",
                serialNumber = "1234567890",
                apiLevel = "33",
                resolution = "1080 x 2408",
                androidVersion = "Android 14 (API 34)",
                isSelected = true,
                icon = Icons.Outlined.Android,
                onCardClick = { println("Device selected") }
            )
            
            // Device Card - Disconnected
            WePrayDeviceCard(
                deviceModel = "SM-S918N",
                deviceManufacturer = "Samsung",
                serialNumber = "1234567890",
                apiLevel = "33",
                resolution = "1080 x 2408",
                androidVersion = "Android 14 (API 34)",
                isSelected = false,
                onCardClick = { println("Device selected") }
            )
        }
    }
}
