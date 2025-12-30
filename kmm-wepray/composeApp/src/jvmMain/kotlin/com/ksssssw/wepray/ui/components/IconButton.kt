package com.ksssssw.wepray.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Icon Button
 * Based on HTML design: hover:bg-slate-50 dark:hover:bg-[#212631] rounded-md p-1.5
 */
@Composable
fun WePrayIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: IconButtonSize = IconButtonSize.Default,
    variant: IconButtonVariant = IconButtonVariant.Default
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    val iconSize = when (size) {
        IconButtonSize.Small -> WePrayTheme.iconSize.button
        IconButtonSize.Default -> WePrayTheme.iconSize.default
        IconButtonSize.Large -> WePrayTheme.iconSize.md
    }
    
    val padding = when (size) {
        IconButtonSize.Small -> WePrayTheme.spacing.xs
        IconButtonSize.Default -> WePrayTheme.spacing.sm
        IconButtonSize.Large -> WePrayTheme.spacing.md
    }
    
    val (backgroundColor, iconColor) = when (variant) {
        IconButtonVariant.Default -> {
            val bg = if (isHovered && enabled) WePrayTheme.colors.surfaceElevated else Color.Transparent
            val ic = if (enabled) WePrayTheme.colors.textSecondary else WePrayTheme.colors.textDisabled
            bg to ic
        }
        IconButtonVariant.Primary -> {
            val bg = if (isHovered && enabled) WePrayTheme.colors.primaryContainer else Color.Transparent
            val ic = if (enabled) WePrayTheme.colors.primary else WePrayTheme.colors.textDisabled
            bg to ic
        }
        IconButtonVariant.Danger -> {
            val bg = if (isHovered && enabled) WePrayTheme.colors.errorContainer else Color.Transparent
            val ic = if (enabled) WePrayTheme.colors.error else WePrayTheme.colors.textDisabled
            bg to ic
        }
    }
    
    Box(
        modifier = modifier
            .clip(WePrayTheme.shapes.medium)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                enabled = enabled
            )
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize),
            tint = iconColor
        )
    }
}

/**
 * WePray Circular Icon Button
 * For floating action buttons
 */
@Composable
fun WePrayCircularIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = WePrayTheme.colors.primary,
    contentColor: Color = Color.White
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(WePrayTheme.shapes.full)
            .background(
                if (enabled) {
                    if (isHovered) backgroundColor.copy(alpha = 0.9f) else backgroundColor
                } else {
                    backgroundColor.copy(alpha = 0.5f)
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                enabled = enabled
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(WePrayTheme.iconSize.md),
            tint = if (enabled) contentColor else contentColor.copy(alpha = 0.6f)
        )
    }
}

enum class IconButtonSize {
    Small,
    Default,
    Large
}

enum class IconButtonVariant {
    Default,
    Primary,
    Danger
}

@Preview
@Composable
private fun IconButtonPreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .width(800.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xxxl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xxxl)
        ) {
            // Default Icon Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Default Icon Buttons",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WePrayIconButton(
                        icon = Icons.Outlined.Refresh,
                        contentDescription = "Refresh",
                        onClick = {},
                        size = IconButtonSize.Small
                    )
                    WePrayIconButton(
                        icon = Icons.Outlined.Refresh,
                        contentDescription = "Refresh",
                        onClick = {}
                    )
                    WePrayIconButton(
                        icon = Icons.Outlined.Refresh,
                        contentDescription = "Refresh",
                        onClick = {},
                        size = IconButtonSize.Large
                    )
                    WePrayIconButton(
                        icon = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        onClick = {},
                        enabled = false
                    )
                }
            }
            
            // Primary Icon Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Primary Icon Buttons",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WePrayIconButton(
                        icon = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        onClick = {},
                        variant = IconButtonVariant.Primary
                    )
                    WePrayIconButton(
                        icon = Icons.Outlined.MoreVert,
                        contentDescription = "More",
                        onClick = {},
                        variant = IconButtonVariant.Primary
                    )
                }
            }
            
            // Danger Icon Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Danger Icon Buttons",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WePrayIconButton(
                        icon = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        onClick = {},
                        variant = IconButtonVariant.Danger
                    )
                    WePrayIconButton(
                        icon = Icons.Outlined.Close,
                        contentDescription = "Close",
                        onClick = {},
                        variant = IconButtonVariant.Danger
                    )
                }
            }
            
            // Circular Icon Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Circular Icon Buttons",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WePrayCircularIconButton(
                        icon = Icons.Outlined.Refresh,
                        contentDescription = "Refresh",
                        onClick = {}
                    )
                    WePrayCircularIconButton(
                        icon = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        onClick = {},
                        backgroundColor = WePrayTheme.colors.success
                    )
                    WePrayCircularIconButton(
                        icon = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        onClick = {},
                        backgroundColor = WePrayTheme.colors.error
                    )
                    WePrayCircularIconButton(
                        icon = Icons.Outlined.Close,
                        contentDescription = "Close",
                        onClick = {},
                        enabled = false
                    )
                }
            }
            
            // Use case - Action row
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Use Case - File Action Row",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(WePrayTheme.shapes.card)
                        .background(WePrayTheme.colors.surfaceVariant)
                        .padding(WePrayTheme.spacing.xl),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "com.instagram.android.apk",
                        style = WePrayTheme.typography.bodyMedium,
                        color = WePrayTheme.colors.textPrimary
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                    ) {
                        WePrayIconButton(
                            icon = Icons.Outlined.Refresh,
                            contentDescription = "Re-install",
                            onClick = {},
                            variant = IconButtonVariant.Primary
                        )
                        WePrayIconButton(
                            icon = Icons.Outlined.Delete,
                            contentDescription = "Delete",
                            onClick = {},
                            variant = IconButtonVariant.Danger
                        )
                    }
                }
            }
        }
    }
}

