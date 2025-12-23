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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.ksssssw.wepray.ui.navigation.TopLevelDestination
import com.ksssssw.wepray.ui.theme.WePrayTheme
import com.ksssssw.wepray.ui.theme.tokens.BorderColor

/**
 * WePray Sidebar Navigation
 * 
 * 디자인 가이드 스펙:
 * - Background: rgba(26, 26, 26, 0.8)
 * - Width: 240px
 * - Border Radius: 12px
 * - Padding: 20px 0
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
//            .padding(WePrayTheme.spacing.containerPadding)
    ) {
        // 사이드바 컨테이너
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(WePrayTheme.shapes.default)
                .background(WePrayTheme.colors.cardBackground)
                .border(
                    width = 1.dp,
                    color = BorderColor,
                    shape = WePrayTheme.shapes.default
                )
                .padding(vertical = WePrayTheme.spacing.xxxl)
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
 * 
 * 디자인 가이드 스펙:
 * - Padding: 15px 25px
 * - Display: Flex gap 15px
 * - Border Left: 3px solid transparent
 * - Text Color: #B3B3B3
 * - Icon Size: 24px
 * 
 * Hover:
 *   - Background: rgba(74, 158, 224, 0.1)
 *   - Text Color: #FFFFFF
 * 
 * Active:
 *   - Background: rgba(74, 158, 224, 0.15)
 *   - Border Left: #4A9EE0
 *   - Text Color: #FFFFFF
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
                    isSelected -> WePrayTheme.colors.hoverEffect.copy(alpha = 0.15f)
                    isHovered -> WePrayTheme.colors.hoverEffect
                    else -> Color.Transparent
                }
            )
            .padding(
                start = WePrayTheme.spacing.xl + WePrayTheme.spacing.sm,
                end = WePrayTheme.spacing.xl + WePrayTheme.spacing.sm,
                top = WePrayTheme.spacing.md + WePrayTheme.spacing.xs,
                bottom = WePrayTheme.spacing.md + WePrayTheme.spacing.xs
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md + WePrayTheme.spacing.xs)
    ) {
        // 아이콘
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            modifier = Modifier.size(WePrayTheme.iconSize.navigation),
            tint = if (isSelected || isHovered) 
                WePrayTheme.colors.onSurface 
            else 
                WePrayTheme.colors.onSurfaceVariant
        )
        
        // 라벨
        Text(
            text = item.label,
            style = WePrayTheme.typography.bodyMedium,
            color = if (isSelected || isHovered) 
                WePrayTheme.colors.onSurface 
            else 
                WePrayTheme.colors.onSurfaceVariant
        )
    }
}

/**
 * Side Rail Item Data Class
 */
data class SideRailItem(
    val id: String,
    val label: String,
    val icon: ImageVector
)
