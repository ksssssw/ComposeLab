package com.ksssssw.wepray.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme
import com.ksssssw.wepray.ui.theme.tokens.BorderColor

/**
 * WePray Top Bar
 * 
 * 디자인 가이드 스펙:
 * - Background: rgba(26, 26, 26, 0.95)
 * - Border: 1px solid rgba(255, 255, 255, 0.1)
 * - Border Radius: 12px
 * - Padding: 20px 30px
 * - Display: Flex space-between
 * 
 * Structure: [Device Icon + Info] [Actions]
 */
@Composable
fun WePrayTopBar(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    deviceInfo: DeviceInfo? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(WePrayTheme.shapes.default)
            .background(WePrayTheme.colors.surface.copy(alpha = 0.95f))
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = WePrayTheme.shapes.default
            )
            .padding(
                horizontal = WePrayTheme.spacing.xl + WePrayTheme.spacing.lg,
                vertical = WePrayTheme.spacing.xl
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 디바이스 정보
        Column(
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
        ) {
            Text(
                text = title,
                style = WePrayTheme.typography.bodyLarge,
                color = WePrayTheme.colors.onSurface
            )

            Text(
                text = description,
                style = WePrayTheme.typography.bodySmall,
                color = WePrayTheme.colors.onSurfaceVariant
            )
        }

//        if (deviceInfo != null) {
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = deviceInfo.icon,
//                    contentDescription = "Device",
//                    modifier = Modifier.size(WePrayTheme.iconSize.medium),
//                    tint = WePrayTheme.colors.primary
//                )
//
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
//                ) {
//                    Text(
//                        text = deviceInfo.name,
//                        style = WePrayTheme.typography.bodyLarge,
//                        color = WePrayTheme.colors.onSurface
//                    )
//
//                    Text(
//                        text = deviceInfo.model,
//                        style = WePrayTheme.typography.bodySmall,
//                        color = WePrayTheme.colors.onSurfaceVariant
//                    )
//                }
//            }
//        } else {
//            Text(
//                text = "디바이스가 연결되지 않았습니다",
//                style = WePrayTheme.typography.bodyMedium,
//                color = WePrayTheme.colors.onSurfaceVariant
//            )
//        }
        
        // 액션 버튼들
        Row(
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions()
        }
    }
}

/**
 * Device Info Data Class
 */
data class DeviceInfo(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val name: String,
    val model: String
)

@Preview
@Composable
private fun TopBarPreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .width(800.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
        ) {
            // 디바이스 연결됨
            WePrayTopBar(
                title = "Devices",
                description = "연결된 디바이스 목록",
                deviceInfo = DeviceInfo(
                    icon = Icons.Outlined.Smartphone,
                    name = "Galaxy S23",
                    model = "Android 14 (API 34)"
                ),
                actions = {
                    WePrayBadge(
                        text = "연결됨",
                        variant = BadgeVariant.Success
                    )
                }
            )
        }
    }
}
