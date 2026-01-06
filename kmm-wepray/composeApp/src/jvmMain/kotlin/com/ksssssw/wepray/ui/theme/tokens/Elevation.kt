package com.ksssssw.wepray.ui.theme.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * WePray Elevation Scale
 * Shadow and elevation values for depth perception
 * Based on Tailwind shadow system
 */

object WePrayElevation {
    val none: Dp = 0.dp         // shadow-none
    val xs: Dp = 1.dp           // shadow-sm (subtle)
    val sm: Dp = 2.dp           // shadow (default)
    val md: Dp = 4.dp           // shadow-md
    val lg: Dp = 8.dp           // shadow-lg
    val xl: Dp = 12.dp          // shadow-xl
    val xxl: Dp = 16.dp         // shadow-2xl
    val xxxl: Dp = 24.dp        // shadow-2xl (strong)
    
    // Component-specific elevations
    val card: Dp = sm           // Subtle shadow for cards
    val button: Dp = sm         // Button elevation
    val dialog: Dp = xxl        // Strong shadow for dialogs/modals
    val dropdown: Dp = lg       // Dropdown shadow
    val tooltip: Dp = md        // Tooltip shadow
}
