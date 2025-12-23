package com.ksssssw.wepray.ui.theme.tokens

/**
 * WePray Animation Configuration
 * Timing and easing values for consistent animations
 */

object WePrayAnimation {
    // Duration in milliseconds
    const val FAST = 150
    const val DEFAULT = 300
    const val SLOW = 500

    // Specific use cases
    const val BUTTON_HOVER = DEFAULT
    const val CARD_HOVER = DEFAULT
    const val INPUT_FOCUS = DEFAULT
    const val SNACKBAR_ENTER = DEFAULT
    const val SNACKBAR_EXIT = 200
    const val DIALOG_ENTER = DEFAULT
    const val DIALOG_EXIT = 200
    const val ICON_STATE_CHANGE = FAST

    // Progress animations
    const val PULSE_DURATION = 2000
    const val SPINNER_DURATION = 1000
}
