package com.ksssssw.wepray.ui.theme.tokens

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * WePray Gradient Patterns
 * Used for buttons, progress bars, and decorative elements
 * Based on modern ADB dashboard design
 */

object WePrayGradients {
    /**
     * Primary gradient - Blue theme
     * Used for primary buttons and main actions
     */
    val Primary = Brush.linearGradient(
        colors = listOf(
            Color(0xFF135BEC),  // Primary blue
            Color(0xFF0047D3)   // Darker blue
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * Surface gradient - Dark background theme
     * Used for cards and elevated surfaces
     * gradient-to-br from-[#1c1f27] to-[#111318]
     */
    val Surface = Brush.linearGradient(
        colors = listOf(
            Color(0xFF1C1F27),
            Color(0xFF111318)
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * Success gradient - Green theme
     * Used for success states and progress
     */
    val Success = Brush.linearGradient(
        colors = listOf(
            Color(0xFF10B981),  // Emerald-500
            Color(0xFF059669)   // Emerald-600
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * Error gradient - Red theme
     * Used for error states and destructive actions
     */
    val Error = Brush.linearGradient(
        colors = listOf(
            Color(0xFFEF4444),  // Red-500
            Color(0xFFDC2626)   // Red-600
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * Warning gradient - Orange theme
     * Used for warning states
     */
    val Warning = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF59E0B),  // Amber-500
            Color(0xFFD97706)   // Amber-600
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * Multi-color storage gradient
     * Used for storage visualization (Apps, Media, System)
     */
    val Storage = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF3B82F6),  // Blue (Apps)
            Color(0xFFA855F7),  // Purple (Media)
            Color(0xFF10B981)   // Green (System)
        )
    )
}
