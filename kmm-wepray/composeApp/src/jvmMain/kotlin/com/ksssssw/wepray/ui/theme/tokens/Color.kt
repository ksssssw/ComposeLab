package com.ksssssw.wepray.ui.theme.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * WePray Color Palette
 * Based on modern ADB dashboard theme with clean dark background
 */

// Background Colors
val Background = Color(0xFF101622)        // background-dark
val BackgroundLight = Color(0xFFF6F6F8)   // background-light (for future light mode)
val Surface = Color(0xFF111318)           // Main surface
val SurfaceVariant = Color(0xFF1C1F27)    // Card background
val SurfaceElevated = Color(0xFF212631)   // Elevated surface (list headers)

// Brand Colors
val Primary = Color(0xFF135BEC)           // Primary blue
val PrimaryHover = Color(0xFF0047D3)      // Darker blue for hover

// Border & Divider Colors
val BorderPrimary = Color(0xFF282E39)     // Main border color
val BorderSecondary = Color(0xFF3B4354)   // Secondary border (stronger)

// Text Colors
val TextPrimary = Color(0xFFFFFFFF)       // White text
val TextSecondary = Color(0xFF9DA6B9)     // Muted text
val TextTertiary = Color(0xFF64748B)      // Even more muted
val TextDisabled = Color(0xFF4B5563)      // Disabled text

// Semantic Colors
val Success = Color(0xFF10B981)           // Green
val SuccessLight = Color(0xFF22C55E)      
val Error = Color(0xFFEF4444)             // Red
val ErrorLight = Color(0xFFF87171)
val Warning = Color(0xFFF59E0B)           // Orange/Yellow
val Info = Color(0xFF3B82F6)              // Blue

// Accent Colors (for different states/categories)
val AccentPurple = Color(0xFFA855F7)
val AccentOrange = Color(0xFFF97316)
val AccentEmerald = Color(0xFF10B981)
val AccentBlue = Color(0xFF3B82F6)

// Opacity Variants
val BackgroundOverlay = Color(0xFF000000).copy(alpha = 0.7f)
val HoverEffect = Color(0xFF135BEC).copy(alpha = 0.05f)
val HoverEffectStrong = Color(0xFF135BEC).copy(alpha = 0.1f)

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
    val backgroundLight: Color = BackgroundLight,
    val surface: Color = Surface,
    val surfaceVariant: Color = SurfaceVariant,
    val surfaceElevated: Color = SurfaceElevated,
    
    // Brand colors
    val primary: Color = Primary,
    val primaryHover: Color = PrimaryHover,
    
    // Border colors
    val border: Color = BorderPrimary,
    val borderSecondary: Color = BorderSecondary,
    
    // Text colors
    val onBackground: Color = TextPrimary,
    val onSurface: Color = TextPrimary,
    val onSurfaceVariant: Color = TextSecondary,
    val onSurfaceTertiary: Color = TextTertiary,
    val onPrimary: Color = Color.White,
    val textPrimary: Color = TextPrimary,
    val textSecondary: Color = TextSecondary,
    val textTertiary: Color = TextTertiary,
    val textDisabled: Color = TextDisabled,
    
    // Semantic colors
    val success: Color = Success,
    val successLight: Color = SuccessLight,
    val error: Color = Error,
    val errorLight: Color = ErrorLight,
    val warning: Color = Warning,
    val info: Color = Info,
    
    // Accent colors
    val accentPurple: Color = AccentPurple,
    val accentOrange: Color = AccentOrange,
    val accentEmerald: Color = AccentEmerald,
    val accentBlue: Color = AccentBlue,
    
    // Container colors
    val primaryContainer: Color = Primary.copy(alpha = 0.1f),
    val successContainer: Color = Success.copy(alpha = 0.1f),
    val errorContainer: Color = Error.copy(alpha = 0.1f),
    val warningContainer: Color = Warning.copy(alpha = 0.1f),
    val infoContainer: Color = Info.copy(alpha = 0.1f),
    
    val onPrimaryContainer: Color = Primary,
    val onSuccessContainer: Color = Success,
    val onErrorContainer: Color = Error,
    val onWarningContainer: Color = Warning,
    val onInfoContainer: Color = Info,
    
    // Additional UI colors
    val backgroundOverlay: Color = BackgroundOverlay,
    val hoverEffect: Color = HoverEffect,
    val hoverEffectStrong: Color = HoverEffectStrong,
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
