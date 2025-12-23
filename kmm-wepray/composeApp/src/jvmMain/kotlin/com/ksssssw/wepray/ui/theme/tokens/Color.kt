package com.ksssssw.wepray.ui.theme.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * WePray Color Palette
 * Based on Coldplay theme with dark background and neon accent colors
 */

// Background Colors
val Background = Color(0xFF0A0A0A)
val Surface = Color(0xFF1A1A1A)
val SurfaceVariant = Color(0xFF2A2A2E)

// Brand Colors
val Primary = Color(0xFF4A9EE0)      // Soft Blue
val Secondary = Color(0xFFE06B9E)    // Soft Pink
val Accent = Color(0xFFF4C430)       // Soft Gold

// Text Colors
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB3B3B3)
val TextDisabled = Color(0xFF808080)

// Semantic Colors
val Success = Color(0xFF00E676)
val Error = Color(0xFFFF3D00)
val Warning = Color(0xFFFFC107)
val Info = Color(0xFF2196F3)

// Opacity Variants
val BackgroundOverlay = Color(0xFF000000).copy(alpha = 0.7f)
val CardBackground = Color(0xFF1A1A1A).copy(alpha = 0.8f)
val HoverEffect = Color(0xFF4A9EE0).copy(alpha = 0.1f)
val BorderColor = Color(0xFFFFFFFF).copy(alpha = 0.1f)

// Additional UI Colors
val SelectedBackground = Primary.copy(alpha = 0.1f)
val SelectedBorder = Primary

/**
 * WePray Color Scheme
 * Immutable class containing all theme colors
 */
@Immutable
data class WePrayColorScheme(
    // Background colors
    val background: Color = Background,
    val surface: Color = Surface,
    val surfaceVariant: Color = SurfaceVariant,
    
    // Brand colors
    val primary: Color = Primary,
    val secondary: Color = Secondary,
    val tertiary: Color = Accent,
    
    // Text colors
    val onBackground: Color = TextPrimary,
    val onSurface: Color = TextPrimary,
    val onSurfaceVariant: Color = TextSecondary,
    val onPrimary: Color = TextPrimary,
    val onSecondary: Color = TextPrimary,
    val onTertiary: Color = Background,
    
    // Semantic colors
    val success: Color = Success,
    val error: Color = Error,
    val warning: Color = Warning,
    val info: Color = Info,
    
    // Container colors
    val primaryContainer: Color = Primary.copy(alpha = 0.2f),
    val secondaryContainer: Color = Secondary.copy(alpha = 0.2f),
    val tertiaryContainer: Color = Accent.copy(alpha = 0.2f),
    val errorContainer: Color = Error.copy(alpha = 0.2f),
    
    val onPrimaryContainer: Color = Primary,
    val onSecondaryContainer: Color = Secondary,
    val onTertiaryContainer: Color = Accent,
    val onErrorContainer: Color = Error,
    
    // Additional UI colors
    val backgroundOverlay: Color = BackgroundOverlay,
    val cardBackground: Color = CardBackground,
    val hoverEffect: Color = HoverEffect,
    val border: Color = BorderColor,
    val selectedBackground: Color = SelectedBackground,
    val selectedBorder: Color = SelectedBorder,
)

/**
 * Default WePray Dark Color Scheme
 */
val wePrayDarkColorScheme = WePrayColorScheme()

/**
 * CompositionLocal for WePray Colors
 */
val LocalWePrayColors = staticCompositionLocalOf { wePrayDarkColorScheme }
