package com.ksssssw.wepray.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme
import com.ksssssw.wepray.ui.theme.tokens.Background
import com.ksssssw.wepray.ui.theme.tokens.BorderColor
import com.ksssssw.wepray.ui.theme.tokens.TextDisabled

/**
 * WePray Text Input
 * 
 * 디자인 가이드 스펙:
 * - Background: rgba(10, 10, 10, 0.8)
 * - Border: 2px solid rgba(255, 255, 255, 0.2)
 * - Border (Focus): 2px solid #4A9EE0
 * - Padding: 15px 20px
 * - Border Radius: 8px
 * - Text Color: #FFFFFF
 * - Placeholder: #808080
 * - Font Size: 1rem (16px)
 * 
 * States:
 * - Default: Border rgba(255, 255, 255, 0.2)
 * - Focus: Border #4A9EE0, Glow 0 0 15px rgba(74, 158, 224, 0.3)
 * - Error: Border #FF3D00
 * - Success: Border #00E676
 * - Disabled: Opacity 0.5, cursor not-allowed
 */
@Composable
fun WePrayTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    isError: Boolean = false,
    isSuccess: Boolean = false,
    singleLine: Boolean = true
) {
    var isFocused by remember { mutableStateOf(false) }
    
    val borderColor = when {
        !enabled -> BorderColor.copy(alpha = 0.2f)
        isError -> WePrayTheme.colors.error
        isSuccess -> WePrayTheme.colors.success
        isFocused -> WePrayTheme.colors.primary
        else -> BorderColor.copy(alpha = 0.2f)
    }
    
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isFocused) WePrayTheme.elevation.md else WePrayTheme.elevation.none,
                shape = WePrayTheme.shapes.input,
                spotColor = WePrayTheme.colors.primary.copy(alpha = 0.3f)
            )
            .clip(WePrayTheme.shapes.input)
            .background(Background.copy(alpha = 0.8f))
            .border(
                width = 2.dp,
                color = borderColor,
                shape = WePrayTheme.shapes.input
            )
            .padding(
                horizontal = WePrayTheme.spacing.inputPaddingHorizontal,
                vertical = WePrayTheme.spacing.inputPaddingVertical
            )
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        enabled = enabled,
        singleLine = singleLine,
        textStyle = WePrayTheme.typography.bodyMedium.copy(
            color = if (enabled) WePrayTheme.colors.onBackground else WePrayTheme.colors.onBackground.copy(alpha = 0.5f)
        ),
        cursorBrush = SolidColor(WePrayTheme.colors.primary),
        decorationBox = { innerTextField ->
            Box {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = WePrayTheme.typography.bodyMedium,
                        color = TextDisabled
                    )
                }
                innerTextField()
            }
        }
    )
}

@Preview
@Composable
private fun TextFieldPreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .width(600.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
        ) {
            Text(
                text = "Text Input States",
                style = WePrayTheme.typography.displayMedium,
                color = WePrayTheme.colors.onBackground
            )
            
            // Default
            var defaultText by remember { mutableStateOf("") }
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "Default",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.onSurfaceVariant
                )
                WePrayTextField(
                    value = defaultText,
                    onValueChange = { defaultText = it },
                    placeholder = "Enter text here..."
                )
            }
            
            // With Text
            var filledText by remember { mutableStateOf("Hello WePray") }
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "Filled",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.onSurfaceVariant
                )
                WePrayTextField(
                    value = filledText,
                    onValueChange = { filledText = it }
                )
            }
            
            // Error
            var errorText by remember { mutableStateOf("") }
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "Error State",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.error
                )
                WePrayTextField(
                    value = errorText,
                    onValueChange = { errorText = it },
                    placeholder = "Invalid input",
                    isError = true
                )
            }
            
            // Success
            var successText by remember { mutableStateOf("Valid input") }
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "Success State",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.success
                )
                WePrayTextField(
                    value = successText,
                    onValueChange = { successText = it },
                    isSuccess = true
                )
            }
            
            // Disabled
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "Disabled",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.onSurfaceVariant
                )
                WePrayTextField(
                    value = "Disabled field",
                    onValueChange = {},
                    enabled = false
                )
            }
        }
    }
}
