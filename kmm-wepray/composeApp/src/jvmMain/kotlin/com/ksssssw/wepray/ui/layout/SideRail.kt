package com.ksssssw.wepray.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.ksssssw.wepray.ui.navigation.TopLevelDestination
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Sidebar Navigation
 * Based on HTML nav design: flex items-center gap-9
 */
@Composable
fun WePraySideRail(
    modifier: Modifier = Modifier,
    topLevelDestination: TopLevelDestination,
    onItemClick: (NavKey) -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(240.dp)
            .fillMaxHeight()
            .padding(
                start = WePrayTheme.spacing.containerPaddingSmall,
                top = WePrayTheme.spacing.containerPaddingSmall,
                bottom = WePrayTheme.spacing.lg
            )
    ) {
        // Sidebar container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(WePrayTheme.shapes.card)
                .background(WePrayTheme.colors.surfaceVariant)
                .border(
                    width = 1.dp,
                    color = WePrayTheme.colors.border,
                    shape = WePrayTheme.shapes.card
                )
                .padding(vertical = WePrayTheme.spacing.xl)
        ) {
            TopLevelDestination.entries.forEach {
                SideRailItemView(
                    item = it,
                    isSelected = it == topLevelDestination,
                    onClick = { onItemClick(it.route) }
                )
            }
        }
    }
}

/**
 * Sidebar Item
 * Based on HTML nav item with hover effects
 */
@Composable
private fun SideRailItemView(
    item: TopLevelDestination,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .background(
                when {
                    isSelected -> WePrayTheme.colors.primaryContainer
                    isHovered -> WePrayTheme.colors.hoverEffect
                    else -> Color.Transparent
                }
            )
            .padding(
                horizontal = WePrayTheme.spacing.xl,
                vertical = WePrayTheme.spacing.lg
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
    ) {
        // Icon
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            modifier = Modifier.size(WePrayTheme.iconSize.default),
            tint = if (isSelected) 
                WePrayTheme.colors.primary
            else if (isHovered)
                WePrayTheme.colors.textPrimary
            else 
                WePrayTheme.colors.textSecondary
        )
        
        // Label
        Text(
            text = item.label,
            style = if (isSelected) 
                WePrayTheme.typography.bodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            else 
                WePrayTheme.typography.bodyMedium,
            color = if (isSelected) 
                WePrayTheme.colors.primary
            else if (isHovered)
                WePrayTheme.colors.textPrimary
            else 
                WePrayTheme.colors.textSecondary
        )
    }
}
