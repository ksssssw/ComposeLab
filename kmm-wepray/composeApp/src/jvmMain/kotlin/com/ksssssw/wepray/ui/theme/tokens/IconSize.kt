package com.ksssssw.wepray.ui.theme.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * WePray Icon Size Scale
 * Consistent icon sizes for various UI components
 * Aligned with Material Symbols sizing
 */

object WePrayIconSize {
    val xs: Dp = 16.dp        // Small icons
    val sm: Dp = 18.dp        // text-[18px] in HTML
    val default: Dp = 20.dp   // text-[20px] in HTML  
    val md: Dp = 24.dp        // Standard icon size
    val lg: Dp = 32.dp        // Large icons
    val xl: Dp = 40.dp        // text-3xl equivalent
    val xxl: Dp = 48.dp       // Extra large
    val xxxl: Dp = 64.dp      // Huge icons (decorative)

    // Component-specific sizes (from HTML design)
    val navigation: Dp = default        // Navigation icons
    val button: Dp = sm                 // Button icons (18px)
    val buttonLarge: Dp = default       // Larger buttons
    val listIcon: Dp = md              // List item icons
    val deviceIcon: Dp = lg            // Device cards
    val dragDropIcon: Dp = xxxl        // Drag & drop zone (text-3xl)
    val decorative: Dp = xxxl          // Background decorative icons (text-6xl would be ~96dp)
}
