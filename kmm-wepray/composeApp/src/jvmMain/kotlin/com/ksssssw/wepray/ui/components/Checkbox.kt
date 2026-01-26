package com.ksssssw.wepray.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Checkbox
 */
@Composable
fun WePrayCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { if (enabled) onCheckedChange(!checked) },
                enabled = enabled
            ),
        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox box
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(WePrayTheme.shapes.small)
                .background(
                    when {
                        !enabled -> WePrayTheme.colors.surfaceVariant.copy(alpha = 0.5f)
                        checked -> WePrayTheme.colors.primary
                        isHovered -> WePrayTheme.colors.surfaceElevated
                        else -> WePrayTheme.colors.surface
                    }
                )
                .border(
                    width = 1.dp,
                    color = when {
                        !enabled -> WePrayTheme.colors.border.copy(alpha = 0.5f)
                        checked -> WePrayTheme.colors.primary
                        isHovered -> WePrayTheme.colors.primary
                        else -> WePrayTheme.colors.border
                    },
                    shape = WePrayTheme.shapes.small
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.White
                )
            }
        }
        
        // Label
        if (label != null) {
            Text(
                text = label,
                style = WePrayTheme.typography.bodyMedium,
                color = if (enabled) WePrayTheme.colors.textPrimary else WePrayTheme.colors.textDisabled
            )
        }
    }
}

