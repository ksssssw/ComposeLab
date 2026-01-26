package com.ksssssw.wepray.ui.theme.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * WePray Spacing Scale
 * Consistent spacing values for layouts and components
 * Based on Tailwind spacing scale (4px base unit)
 */

object WePraySpacing {
    // Base spacing scale (Tailwind-inspired)
    val none: Dp = 0.dp       // 0
    val xs: Dp = 2.dp         // 0.5 (0.125rem)
    val sm: Dp = 4.dp         // 1 (0.25rem)
    val md: Dp = 8.dp         // 2 (0.5rem)
    val lg: Dp = 12.dp        // 3 (0.75rem)
    val xl: Dp = 16.dp        // 4 (1rem)
    val xxl: Dp = 20.dp       // 5 (1.25rem)
    val xxxl: Dp = 24.dp      // 6 (1.5rem)
    val xxxxl: Dp = 32.dp     // 8 (2rem)
    val xxxxxl: Dp = 40.dp    // 10 (2.5rem)
    val xxxxxxl: Dp = 48.dp   // 12 (3rem)

    // Layout specific spacing (from HTML design)
    val containerPadding: Dp = 40.dp           // px-10 (2.5rem)
    val containerPaddingSmall: Dp = 16.dp      // px-4 (1rem)
    val containerPaddingMedium: Dp = 40.dp     // px-10 (2.5rem)
    val containerPaddingLarge: Dp = 80.dp      // px-20 (5rem)
    
    val sectionGap: Dp = 24.dp                 // gap-6 (1.5rem)
    val componentGap: Dp = 16.dp               // gap-4 (1rem)
    val componentGapSmall: Dp = 8.dp           // gap-2 (0.5rem)
    val componentGapLarge: Dp = 24.dp          // gap-6 (1.5rem)
    
    val cardPadding: Dp = 16.dp                // p-5 (1.25rem)
    val cardPaddingLarge: Dp = 20.dp           // p-6 (1.5rem)
    
    val buttonPaddingVertical: Dp = 8.dp       // py-2 (0.5rem)
    val buttonPaddingHorizontal: Dp = 16.dp    // px-4 (1rem)
    val buttonPaddingLarge: Dp = 12.dp         // py-3 (0.75rem)
    
    val inputPadding: Dp = 12.dp               // p-3 (0.75rem)
    val inputPaddingSmall: Dp = 10.dp          // p-2.5 (0.625rem)
    
    val listItemPadding: Dp = 16.dp            // py-4 (1rem)
    val listItemPaddingHorizontal: Dp = 24.dp  // px-6 (1.5rem)
}
