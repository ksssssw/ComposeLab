package com.ksssssw.wepray.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.ksssssw.wepray.ui.theme.tokens.*

/**
 * WePray Theme
 * Main theme composable that wraps the app content
 * 
 * @param content The composable content to be themed
 */
@Composable
fun WePrayTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalWePrayColors provides wePrayDarkColorScheme,
        LocalWePrayTypography provides wePrayTypography,
        content = content
    )
}

/**
 * WePray Theme Object
 * Direct access to all theme values
 * 
 * Usage:
 * - WePrayTheme.colors.primary
 * - WePrayTheme.typography.displayLarge
 * - WePrayTheme.spacing.md
 * - WePrayTheme.shapes.card
 * - WePrayTheme.gradients.Primary
 */
object WePrayTheme {
    val colors: WePrayColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalWePrayColors.current
    
    val typography: WePrayTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalWePrayTypography.current
    
    val spacing: WePraySpacing
        get() = WePraySpacing
    
    val shapes: WePrayShapes
        get() = WePrayShapes
    
    val gradients: WePrayGradients
        get() = WePrayGradients
    
    val elevation: WePrayElevation
        get() = WePrayElevation
    
    val animation: WePrayAnimation
        get() = WePrayAnimation
    
    val iconSize: WePrayIconSize
        get() = WePrayIconSize
}
