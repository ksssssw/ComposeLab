package com.ksssssw.wepray.ui.theme.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * WePray Typography
 * Font sizes, weights, and line heights for text hierarchy
 * Based on modern ADB dashboard design
 */

@Immutable
data class WePrayTypography(
    // Display - Large titles (3xl) - Page Title
    val displayLarge: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,         // text-3xl
        lineHeight = 36.sp,
        letterSpacing = (-0.45).sp  // tracking-tight
    ),

    // Display Medium - Section Title (2xl)
    val displayMedium: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,         // text-2xl
        lineHeight = 32.sp,
        letterSpacing = (-0.36).sp
    ),

    // Display Small - Subsection Title (xl)
    val displaySmall: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,         // text-xl
        lineHeight = 28.sp,
        letterSpacing = (-0.3).sp
    ),

    // Headline Large - Card Title (lg)
    val headlineLarge: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,         // text-lg
        lineHeight = 28.sp,
        letterSpacing = (-0.27).sp
    ),

    // Headline Medium
    val headlineMedium: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    // Body Large - Emphasized Text
    val bodyLarge: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,         // text-base
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    // Body Medium - Default Text (sm)
    val bodyMedium: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,         // text-sm
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    // Body Small - Secondary Text (xs)
    val bodySmall: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,         // text-xs
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),

    // Label Large - Meta Information
    val labelLarge: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,         // text-sm font-medium
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    // Label Medium - Labels, Badges
    val labelMedium: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,         // text-xs font-medium
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),

    // Label Small - Tiny labels
    val labelSmall: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp
    ),

    // Button text
    val button: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,    // font-bold
        fontSize = 14.sp,                // text-sm
        lineHeight = 20.sp,
        letterSpacing = 0.24.sp          // tracking-[0.015em]
    ),

    // Button Large
    val buttonLarge: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.24.sp
    ),

    // Code / Monospace text (font-mono)
    val code: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    // Uppercase labels (tracking-wider)
    val overline: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,                // text-xs
        lineHeight = 16.sp,
        letterSpacing = 0.96.sp          // tracking-wider (0.05em)
    )
)

/**
 * Default WePray Typography
 */
val wePrayTypography = WePrayTypography()

/**
 * CompositionLocal for WePray Typography
 */
val LocalWePrayTypography = staticCompositionLocalOf { wePrayTypography }
