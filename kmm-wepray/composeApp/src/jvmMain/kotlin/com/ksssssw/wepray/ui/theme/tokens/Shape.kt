package com.ksssssw.wepray.ui.theme.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * WePray Border Radius Scale
 * Rounded corner shapes for various UI components
 * Based on Tailwind-inspired design system
 */

object WePrayShapes {
    // Base shapes
    val none = RoundedCornerShape(0.dp)
    val small = RoundedCornerShape(4.dp)      // DEFAULT: 0.25rem
    val medium = RoundedCornerShape(6.dp)     // rounded-md
    val default = RoundedCornerShape(8.dp)    // rounded-lg: 0.5rem
    val large = RoundedCornerShape(12.dp)     // rounded-xl: 0.75rem
    val extraLarge = RoundedCornerShape(16.dp)  // rounded-2xl: 1rem
    val full = RoundedCornerShape(9999.dp)    // rounded-full

    // Component-specific shapes (mapped to HTML design)
    val button = default                       // rounded-lg
    val buttonSmall = default                  // rounded-md
    val input = default                        // rounded-lg
    val card = large                          // rounded-xl
    val cardLarge = large                     // rounded-xl
    val dialog = large                        // rounded-xl
    val iconButton = medium                   // rounded-md
    val badge = default                       // rounded (relative to badge size)
    val dragDropZone = large                  // rounded-xl
    val chip = full                           // rounded-full
}
