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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme
import kotlin.math.max
import kotlin.math.min

/**
 * WePray Color Picker
 * Simple color selector with predefined colors
 */
@Composable
fun WePrayColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
    colors: List<Color> = defaultColors
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
    ) {
        // Color grid (6 columns x 4 rows)
        Row(
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            colors.chunked(4).forEach { columnColors ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    columnColors.forEach { color ->
                        ColorOption(
                            color = color,
                            isSelected = color == selectedColor,
                            onClick = { onColorSelected(color) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorOption(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(WePrayTheme.shapes.medium)
            .background(color)
            .border(
                width = if (isSelected) 3.dp else if (isHovered) 2.dp else 1.dp,
                color = if (isSelected) WePrayTheme.colors.primary else WePrayTheme.colors.border,
                shape = WePrayTheme.shapes.medium
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = "Selected",
                tint = getContrastColor(color),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Get contrasting text color based on background luminance
 */
fun getContrastColor(backgroundColor: Color): Color {
    val luminance = backgroundColor.luminance()
    return if (luminance > 0.5f) Color.Black else Color.White
}

/**
 * Adjust color brightness
 */
fun adjustBrightness(color: Color, factor: Float): Color {
    return Color(
        red = max(0f, min(1f, color.red * factor)),
        green = max(0f, min(1f, color.green * factor)),
        blue = max(0f, min(1f, color.blue * factor)),
        alpha = color.alpha
    )
}

private val defaultColors = listOf(
    // Primary colors
    Color(0xFF3B82F6), // Blue
    Color(0xFF10B981), // Green
    Color(0xFFF59E0B), // Amber
    Color(0xFFEF4444), // Red
    Color(0xFF8B5CF6), // Purple
    Color(0xFFEC4899), // Pink
    
    // Secondary colors
    Color(0xFF06B6D4), // Cyan
    Color(0xFF84CC16), // Lime
    Color(0xFFF97316), // Orange
    Color(0xFFDC2626), // Deep Red
    Color(0xFF7C3AED), // Violet
    Color(0xFFDB2777), // Deep Pink
    
    // Tertiary colors
    Color(0xFF0EA5E9), // Light Blue
    Color(0xFF22C55E), // Light Green
    Color(0xFFFBBF24), // Yellow
    Color(0xFFF87171), // Light Red
    Color(0xFFA78BFA), // Light Purple
    Color(0xFFF472B6), // Light Pink
    
    // Neutral colors
    Color(0xFF6B7280), // Gray
    Color(0xFF374151), // Dark Gray
    Color(0xFF1F2937), // Darker Gray
    Color(0xFF64748B), // Slate
    Color(0xFF475569), // Dark Slate
    Color(0xFF334155), // Darker Slate
)

