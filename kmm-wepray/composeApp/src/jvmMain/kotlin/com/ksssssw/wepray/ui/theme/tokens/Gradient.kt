package com.ksssssw.wepray.ui.theme.tokens

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * WePray Gradient Patterns
 * Used for buttons, progress bars, and decorative elements
 */

object WePrayGradients {
    /**
     * Primary gradient - Blue theme
     * Used for primary buttons and main actions
     */
    val Primary = Brush.linearGradient(
        colors = listOf(
            Color(0xFF4A9EE0),
            Color(0xFF3A7FB0)
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * Secondary gradient - Pink theme
     * Used for secondary buttons and highlights
     */
    val Secondary = Brush.linearGradient(
        colors = listOf(
            Color(0xFFE06B9E),
            Color(0xFFC05080)
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * Accent gradient - Gold theme
     * Used for accent buttons and warnings
     */
    val Accent = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF4C430),
            Color(0xFFD4A520)
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * Rainbow gradient - Multi-color theme
     * Used for progress bars and loading indicators
     */
    val Rainbow = Brush.linearGradient(
        colors = listOf(
            Color(0xFF4A9EE0),
            Color(0xFFE06B9E),
            Color(0xFFF4C430)
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
}
