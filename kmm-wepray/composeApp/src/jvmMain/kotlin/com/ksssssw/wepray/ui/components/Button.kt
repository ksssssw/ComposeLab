package com.ksssssw.wepray.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Usb
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Primary Button - Filled Style
 * Based on HTML design: bg-primary hover:bg-blue-600
 */
@Composable
fun WePrayPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ButtonSize = ButtonSize.Default,
    icon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val (horizontalPadding, verticalPadding, textStyle, iconSize) = when (size) {
        ButtonSize.Small -> ButtonSizeSpec(
            horizontal = WePrayTheme.spacing.lg,
            vertical = WePrayTheme.spacing.sm,
            textStyle = WePrayTheme.typography.button,
            iconSize = WePrayTheme.iconSize.button
        )
        ButtonSize.Default -> ButtonSizeSpec(
            horizontal = WePrayTheme.spacing.buttonPaddingHorizontal,
            vertical = WePrayTheme.spacing.buttonPaddingVertical + WePrayTheme.spacing.xs,
            textStyle = WePrayTheme.typography.button,
            iconSize = WePrayTheme.iconSize.button
        )
        ButtonSize.Large -> ButtonSizeSpec(
            horizontal = WePrayTheme.spacing.xxl,
            vertical = WePrayTheme.spacing.buttonPaddingLarge,
            textStyle = WePrayTheme.typography.buttonLarge,
            iconSize = WePrayTheme.iconSize.default
        )
    }

    Box(
        modifier = modifier
            .shadow(
                elevation = if (isHovered && enabled) WePrayTheme.elevation.button else 0.dp,
                shape = WePrayTheme.shapes.button,
                spotColor = WePrayTheme.colors.primary.copy(alpha = 0.3f)
            )
            .clip(WePrayTheme.shapes.button)
            .background(
                if (enabled) {
                    if (isHovered) WePrayTheme.colors.primaryHover else WePrayTheme.colors.primary
                } else {
                    WePrayTheme.colors.primary.copy(alpha = 0.5f)
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                enabled = enabled
            )
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = Color.White
                )
            }
            Text(
                text = text,
                style = textStyle,
                color = if (enabled) Color.White else Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

/**
 * WePray Secondary Button - Outlined Style
 * Based on HTML design: border with bg-white dark:bg-[#282e39]
 */
@Composable
fun WePraySecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ButtonSize = ButtonSize.Default,
    icon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val (horizontalPadding, verticalPadding, textStyle, iconSize) = when (size) {
        ButtonSize.Small -> ButtonSizeSpec(
            horizontal = WePrayTheme.spacing.lg,
            vertical = WePrayTheme.spacing.sm,
            textStyle = WePrayTheme.typography.button,
            iconSize = WePrayTheme.iconSize.button
        )
        ButtonSize.Default -> ButtonSizeSpec(
            horizontal = WePrayTheme.spacing.buttonPaddingHorizontal,
            vertical = WePrayTheme.spacing.buttonPaddingVertical + WePrayTheme.spacing.xs,
            textStyle = WePrayTheme.typography.button,
            iconSize = WePrayTheme.iconSize.button
        )
        ButtonSize.Large -> ButtonSizeSpec(
            horizontal = WePrayTheme.spacing.xxl,
            vertical = WePrayTheme.spacing.buttonPaddingLarge,
            textStyle = WePrayTheme.typography.buttonLarge,
            iconSize = WePrayTheme.iconSize.default
        )
    }

    Box(
        modifier = modifier
            .clip(WePrayTheme.shapes.button)
            .background(
                if (enabled) {
                    if (isHovered) WePrayTheme.colors.surfaceElevated else WePrayTheme.colors.surfaceVariant
                } else {
                    WePrayTheme.colors.surfaceVariant.copy(alpha = 0.5f)
                }
            )
            .border(
                width = 1.dp,
                color = if (enabled) WePrayTheme.colors.borderSecondary else WePrayTheme.colors.borderSecondary.copy(alpha = 0.5f),
                shape = WePrayTheme.shapes.button
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                enabled = enabled
            )
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = if (enabled) WePrayTheme.colors.textPrimary else WePrayTheme.colors.textDisabled
                )
            }
            if (text.isNotEmpty()) {
                Text(
                    text = text,
                    style = textStyle.copy(fontWeight = FontWeight.Medium),
                    color = if (enabled) WePrayTheme.colors.textPrimary else WePrayTheme.colors.textDisabled
                )
            }
        }
    }
}

/**
 * WePray Ghost Button - Text only with hover effect
 * Minimal button style
 */
@Composable
fun WePrayGhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ButtonSize = ButtonSize.Default,
    icon: ImageVector? = null,
    color: Color = WePrayTheme.colors.primary
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val (horizontalPadding, verticalPadding, textStyle, iconSize) = when (size) {
        ButtonSize.Small -> ButtonSizeSpec(
            horizontal = WePrayTheme.spacing.md,
            vertical = WePrayTheme.spacing.xs,
            textStyle = WePrayTheme.typography.button,
            iconSize = WePrayTheme.iconSize.button
        )
        ButtonSize.Default -> ButtonSizeSpec(
            horizontal = WePrayTheme.spacing.lg,
            vertical = WePrayTheme.spacing.md,
            textStyle = WePrayTheme.typography.button,
            iconSize = WePrayTheme.iconSize.button
        )
        ButtonSize.Large -> ButtonSizeSpec(
            horizontal = WePrayTheme.spacing.xl,
            vertical = WePrayTheme.spacing.lg,
            textStyle = WePrayTheme.typography.buttonLarge,
            iconSize = WePrayTheme.iconSize.default
        )
    }

    Box(
        modifier = modifier
            .clip(WePrayTheme.shapes.button)
            .background(
                if (isHovered && enabled) 
                    color.copy(alpha = 0.1f) 
                else 
                    Color.Transparent
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                enabled = enabled
            )
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = if (enabled) color else WePrayTheme.colors.textDisabled
                )
            }
            Text(
                text = text,
                style = textStyle.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                color = if (enabled) {
                    if (isHovered) color else WePrayTheme.colors.textSecondary
                } else {
                    WePrayTheme.colors.textDisabled
                }
            )
        }
    }
}

/**
 * WePray Danger Button - For destructive actions
 */
@Composable
fun WePrayDangerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ButtonSize = ButtonSize.Default,
    icon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val (horizontalPadding, verticalPadding, textStyle, iconSize) = when (size) {
        ButtonSize.Small -> ButtonSizeSpec(
            horizontal = WePrayTheme.spacing.lg,
            vertical = WePrayTheme.spacing.sm,
            textStyle = WePrayTheme.typography.button,
            iconSize = WePrayTheme.iconSize.button
        )
        ButtonSize.Default -> ButtonSizeSpec(
            horizontal = WePrayTheme.spacing.buttonPaddingHorizontal,
            vertical = WePrayTheme.spacing.buttonPaddingVertical + WePrayTheme.spacing.xs,
            textStyle = WePrayTheme.typography.button,
            iconSize = WePrayTheme.iconSize.button
        )
        ButtonSize.Large -> ButtonSizeSpec(
            horizontal = WePrayTheme.spacing.xxl,
            vertical = WePrayTheme.spacing.buttonPaddingLarge,
            textStyle = WePrayTheme.typography.buttonLarge,
            iconSize = WePrayTheme.iconSize.default
        )
    }

    Box(
        modifier = modifier
            .clip(WePrayTheme.shapes.button)
            .background(
                if (enabled) {
                    if (isHovered) WePrayTheme.colors.errorLight else WePrayTheme.colors.error
                } else {
                    WePrayTheme.colors.error.copy(alpha = 0.5f)
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                enabled = enabled
            )
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = Color.White
                )
            }
            Text(
                text = text,
                style = textStyle,
                color = if (enabled) Color.White else Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

data class ButtonSizeSpec(
    val horizontal: androidx.compose.ui.unit.Dp,
    val vertical: androidx.compose.ui.unit.Dp,
    val textStyle: androidx.compose.ui.text.TextStyle,
    val iconSize: androidx.compose.ui.unit.Dp
)

enum class ButtonSize {
    Small,
    Default,
    Large
}

@Preview
@Composable
private fun ButtonPreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .width(800.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xxxl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xxxl)
        ) {
            // Primary Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Primary Buttons",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    WePrayPrimaryButton(
                        text = "Small",
                        onClick = {},
                        size = ButtonSize.Small
                    )
                    WePrayPrimaryButton(
                        text = "Default",
                        onClick = {},
                        size = ButtonSize.Default
                    )
                    WePrayPrimaryButton(
                        text = "Large",
                        onClick = {},
                        size = ButtonSize.Large
                    )
                    WePrayPrimaryButton(
                        text = "Disabled",
                        onClick = {},
                        enabled = false
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    WePrayPrimaryButton(
                        text = "Connect Device",
                        onClick = {},
                        icon = Icons.Outlined.Usb
                    )
                    WePrayPrimaryButton(
                        text = "Install",
                        onClick = {},
                        icon = Icons.Outlined.Download,
                        size = ButtonSize.Small
                    )
                }
            }
            
            // Secondary Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Secondary Buttons",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    WePraySecondaryButton(
                        text = "Re-Install",
                        onClick = {}
                    )
                    WePraySecondaryButton(
                        text = "Push OBB",
                        onClick = {}
                    )
                    WePraySecondaryButton(
                        text = "Disabled",
                        onClick = {},
                        enabled = false
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    WePraySecondaryButton(
                        text = "Refresh",
                        onClick = {},
                        icon = Icons.Outlined.Refresh,
                        size = ButtonSize.Small
                    )
                }
            }
            
            // Ghost Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Ghost Buttons",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    WePrayGhostButton(
                        text = "Dashboard",
                        onClick = {}
                    )
                    WePrayGhostButton(
                        text = "File Manager",
                        onClick = {}
                    )
                    WePrayGhostButton(
                        text = "Disabled",
                        onClick = {},
                        enabled = false
                    )
                }
            }
            
            // Danger Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Danger Buttons",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    WePrayDangerButton(
                        text = "Delete",
                        onClick = {}
                    )
                    WePrayDangerButton(
                        text = "Delete All",
                        onClick = {},
                        size = ButtonSize.Small
                    )
                    WePrayDangerButton(
                        text = "Disabled",
                        onClick = {},
                        enabled = false
                    )
                }
            }
        }
    }
}
