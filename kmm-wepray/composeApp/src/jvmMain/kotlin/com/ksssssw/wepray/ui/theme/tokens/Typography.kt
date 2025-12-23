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
 */

@Immutable
data class WePrayTypography(
    // Heading 1 - Page Title (48px)
    val displayLarge: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 57.6.sp,
        letterSpacing = 0.sp
    ),

    // Heading 2 - Section Title (28.8px)
    val displayMedium: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.8.sp,
        lineHeight = 34.56.sp,
        letterSpacing = 0.sp
    ),

    // Heading 3 - Subsection Title (24px)
    val displaySmall: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 28.8.sp,
        letterSpacing = 0.sp
    ),

    // Heading 4 - Card Title (20.8px)
    val headlineLarge: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.8.sp,
        lineHeight = 24.96.sp,
        letterSpacing = 0.sp
    ),

    // Body Large - Emphasized Text (17.6px)
    val bodyLarge: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 17.6.sp,
        lineHeight = 26.4.sp,
        letterSpacing = 0.5.sp
    ),

    // Body - Default Text (16px)
    val bodyMedium: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Body Small - Secondary Text (14.4px)
    val bodySmall: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.4.sp,
        lineHeight = 21.6.sp,
        letterSpacing = 0.25.sp
    ),

    // Caption - Meta Information (13.6px)
    val labelLarge: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 13.6.sp,
        lineHeight = 20.4.sp,
        letterSpacing = 0.1.sp
    ),

    // Label Small - Labels, Badges (12.8px)
    val labelMedium: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.8.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

    // Label Smallest - Tiny labels (11px)
    val labelSmall: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

    // Button text
    val button: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),

    // Code / Monospace text
    val code: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp
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
