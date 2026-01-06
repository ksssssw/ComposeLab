package com.ksssssw.wepray.ui.theme.tokens

/**
 * WePray Animation Configuration
 * Timing and easing values for consistent animations
 * Based on modern web transition standards
 */

object WePrayAnimation {
    // Duration in milliseconds (CSS transition-* equivalents)
    const val INSTANT = 100         // transition-all
    const val FAST = 150           // Quick interactions
    const val DEFAULT = 200        // Standard transitions
    const val MEDIUM = 300         // Moderate transitions
    const val SLOW = 500           // Slow, noticeable transitions

    // Specific use cases
    const val BUTTON_HOVER = FAST           // Button hover/press
    const val CARD_HOVER = DEFAULT          // Card hover effects
    const val INPUT_FOCUS = DEFAULT         // Input field focus
    const val COLOR_TRANSITION = DEFAULT    // Color changes
    const val OPACITY_TRANSITION = DEFAULT  // Fade in/out
    
    // Layout animations
    const val SNACKBAR_ENTER = MEDIUM
    const val SNACKBAR_EXIT = DEFAULT
    const val DIALOG_ENTER = MEDIUM
    const val DIALOG_EXIT = DEFAULT
    const val SHEET_SLIDE = MEDIUM
    
    // State changes
    const val ICON_STATE_CHANGE = FAST
    const val EXPAND_COLLAPSE = MEDIUM
    const val SCALE_TRANSFORM = DEFAULT

    // Progress animations
    const val PULSE_DURATION = 2000         // Ping/pulse effect
    const val SPINNER_DURATION = 1000       // Rotating spinner
    const val PROGRESS_UPDATE = DEFAULT     // Progress bar updates
}
