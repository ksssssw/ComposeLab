package com.ksssssw.wepray.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Top Bar
 * Based on HTML sticky header design
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
            .clip(WePrayTheme.shapes.card)
            .background(WePrayTheme.colors.surfaceVariant)
            .border(
                width = 1.dp,
                color = WePrayTheme.colors.border,
                shape = WePrayTheme.shapes.card
            )
            .padding(WePrayTheme.spacing.cardPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title and description
        Column(
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
        ) {
            Text(
                text = title,
                style = WePrayTheme.typography.headlineLarge,
                color = WePrayTheme.colors.textPrimary
            )

            Text(
                text = description,
                style = WePrayTheme.typography.bodySmall,
                color = WePrayTheme.colors.textSecondary
            )
        }
        
        // Action buttons
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
                .width(900.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xxxl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl)
        ) {
            // Basic top bar
            WePrayTopBar(
                title = "APK Installer",
                description = "Manage and install APK files from your local machine",
                actions = {
                    WePrayConnectionBadge(
                        deviceName = "Samsung SM-G991B",
                        isConnected = true
                    )
                }
            )
            
            // Without connection
            WePrayTopBar(
                title = "Devices",
                description = "Connect your Android device via ADB",
                actions = {
                    WePrayConnectionBadge(
                        deviceName = "",
                        isConnected = false
                    )
                }
            )
            
            // With multiple actions
            WePrayTopBar(
                title = "Deep Linker",
                description = "Test deep links and app intents",
                actions = {
                    WePrayPrimaryButton(
                        text = "Test Link",
                        onClick = {},
                        size = ButtonSize.Small
                    )
                    WePrayIconButton(
                        icon = androidx.compose.material.icons.Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        onClick = {}
                    )
                }
            )
        }
    }
}
