package com.ksssssw.wepray.ui.theme.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * WePray Border Radius Scale
 * Rounded corner shapes for various UI components
 */

object WePrayShapes {
    val small = RoundedCornerShape(6.dp)
    val medium = RoundedCornerShape(8.dp)
    val default = RoundedCornerShape(12.dp)
    val large = RoundedCornerShape(16.dp)

    // Component-specific shapes
    val button = medium
    val input = medium
    val card = default
    val dialog = default
    val iconButton = small
    val badge = RoundedCornerShape(12.dp)
    val dragDropZone = default
}
