package com.ksssssw.wepray.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
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
 * WePray Primary Button
 * 
 * 디자인 가이드 스펙:
 * - Background: Transparent → Gradient(#4A9EE0, #3A7FB0)
 * - Border: 2px solid #4A9EE0
 * - Text Color: #4A9EE0 → #FFFFFF
 * - Padding: 12px 28px
 * - Border Radius: 8px
 * - Font Weight: 600
 */
@Composable
fun WePrayPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ButtonSize = ButtonSize.Default
) {
    WePrayButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        size = size,
        borderColor = WePrayTheme.colors.primary,
        textColor = WePrayTheme.colors.primary,
        hoverBackground = WePrayTheme.gradients.Primary,
        hoverTextColor = WePrayTheme.colors.onPrimary
    )
}

/**
 * WePray Secondary Button
 * 
 * 디자인 가이드 스펙:
 * - Background: Transparent → Gradient(#E06B9E, #C05080)
 * - Border: 2px solid #E06B9E
 * - Text Color: #E06B9E → #FFFFFF
 * - Padding: 12px 28px
 * - Border Radius: 8px
 * - Font Weight: 600
 */
@Composable
fun WePraySecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ButtonSize = ButtonSize.Default
) {
    WePrayButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        size = size,
        borderColor = WePrayTheme.colors.secondary,
        textColor = WePrayTheme.colors.secondary,
        hoverBackground = WePrayTheme.gradients.Secondary,
        hoverTextColor = WePrayTheme.colors.onSecondary
    )
}

/**
 * WePray Accent Button
 * 
 * 디자인 가이드 스펙:
 * - Background: Transparent → Gradient(#F4C430, #D4A520)
 * - Border: 2px solid #F4C430
 * - Text Color: #F4C430 → #0A0A0A
 * - Padding: 12px 28px
 * - Border Radius: 8px
 * - Font Weight: 600
 */
@Composable
fun WePrayAccentButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ButtonSize = ButtonSize.Default
) {
    WePrayButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        size = size,
        borderColor = WePrayTheme.colors.tertiary,
        textColor = WePrayTheme.colors.tertiary,
        hoverBackground = WePrayTheme.gradients.Accent,
        hoverTextColor = WePrayTheme.colors.background
    )
}

/**
 * WePray Icon Button
 * 
 * 디자인 가이드 스펙:
 * - Background: Transparent → rgba(255, 255, 255, 0.1)
 * - Border: None
 * - Icon Color: #B3B3B3 → #FFFFFF
 * - Padding: 8px
 * - Border Radius: 6px
 * - Icon Size: 24px
 */
@Composable
fun WePrayIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    Box(
        modifier = modifier
            .clip(WePrayTheme.shapes.small)
            .background(
                if (isHovered && enabled) 
                    Color.White.copy(alpha = 0.1f) 
                else 
                    Color.Transparent
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                enabled = enabled
            )
            .padding(WePrayTheme.spacing.sm),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(WePrayTheme.iconSize.default),
            tint = if (enabled) {
                if (isHovered) WePrayTheme.colors.onSurface else WePrayTheme.colors.onSurfaceVariant
            } else {
                WePrayTheme.colors.onSurfaceVariant.copy(alpha = 0.4f)
            }
        )
    }
}

/**
 * Base Button Component
 */
@Composable
private fun WePrayButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ButtonSize = ButtonSize.Default,
    borderColor: Color,
    textColor: Color,
    hoverBackground: androidx.compose.ui.graphics.Brush,
    hoverTextColor: Color
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val (horizontalPadding, verticalPadding, fontSize) = when (size) {
        ButtonSize.Small -> Triple(
            WePrayTheme.spacing.sm + WePrayTheme.spacing.lg,
            WePrayTheme.spacing.sm,
            WePrayTheme.typography.bodySmall
        )
        ButtonSize.Default -> Triple(
            WePrayTheme.spacing.buttonPaddingHorizontal,
            WePrayTheme.spacing.buttonPaddingVertical,
            WePrayTheme.typography.button
        )
        ButtonSize.Large -> Triple(
            WePrayTheme.spacing.lg + WePrayTheme.spacing.xl,
            WePrayTheme.spacing.lg,
            WePrayTheme.typography.bodyLarge
        )
    }

    Box(
        modifier = modifier
            .clip(WePrayTheme.shapes.button)
            .then(
                if (isHovered && enabled) {
                    Modifier.background(hoverBackground)
                } else {
                    Modifier.background(Color.Transparent)
                }
            )
            .border(
                width = 2.dp,
                color = if (enabled) borderColor else borderColor.copy(alpha = 0.4f),
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
        Text(
            text = text,
            style = fontSize,
            color = if (enabled) {
                if (isHovered) hoverTextColor else textColor
            } else {
                textColor.copy(alpha = 0.4f)
            }
        )
    }
}

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
                .width(600.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl)
        ) {
            // Primary Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
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
            }
            
            // Secondary Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
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
                        text = "Secondary",
                        onClick = {}
                    )
                    WePraySecondaryButton(
                        text = "Disabled",
                        onClick = {},
                        enabled = false
                    )
                }
            }
            
            // Accent Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Accent Buttons",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    WePrayAccentButton(
                        text = "Accent",
                        onClick = {}
                    )
                    WePrayAccentButton(
                        text = "Disabled",
                        onClick = {},
                        enabled = false
                    )
                }
            }
            
            // Icon Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Icon Buttons",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    WePrayIconButton(
                        icon = Icons.Outlined.Refresh,
                        contentDescription = "Refresh",
                        onClick = {}
                    )
                    WePrayIconButton(
                        icon = Icons.Outlined.Refresh,
                        contentDescription = "Refresh",
                        onClick = {},
                        enabled = false
                    )
                }
            }
        }
    }
}
