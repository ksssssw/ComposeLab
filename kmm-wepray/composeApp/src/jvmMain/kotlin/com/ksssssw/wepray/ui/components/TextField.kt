package com.ksssssw.wepray.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Text Input
 * Based on HTML design with icon support
 */
@Composable
fun WePrayTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    isSuccess: Boolean = false,
    singleLine: Boolean = true
) {
    var isFocused by remember { mutableStateOf(false) }
    
    val borderColor = when {
        !enabled -> WePrayTheme.colors.border.copy(alpha = 0.5f)
        isError -> WePrayTheme.colors.error
        isSuccess -> WePrayTheme.colors.success
        isFocused -> WePrayTheme.colors.primary
        else -> WePrayTheme.colors.border
    }
    
    val backgroundColor = when {
        !enabled -> WePrayTheme.colors.surfaceVariant.copy(alpha = 0.5f)
        else -> WePrayTheme.colors.surface
    }
    
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isFocused) WePrayTheme.elevation.sm else 0.dp,
                shape = WePrayTheme.shapes.input
            )
            .clip(WePrayTheme.shapes.input)
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = WePrayTheme.shapes.input
            )
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        enabled = enabled,
        singleLine = singleLine,
        textStyle = WePrayTheme.typography.bodyMedium.copy(
            color = if (enabled) WePrayTheme.colors.textPrimary else WePrayTheme.colors.textDisabled
        ),
        cursorBrush = SolidColor(WePrayTheme.colors.primary),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WePrayTheme.spacing.inputPadding),
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading icon
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(WePrayTheme.iconSize.default),
                        tint = if (isFocused) {
                            WePrayTheme.colors.primary
                        } else {
                            WePrayTheme.colors.textSecondary
                        }
                    )
                }
                
                // Text field with placeholder
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = WePrayTheme.typography.bodyMedium,
                            color = WePrayTheme.colors.textSecondary
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

/**
 * WePray TextField with Label
 * For forms with field labels
 */
@Composable
fun WePrayLabeledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    isSuccess: Boolean = false,
    helperText: String? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
    ) {
        // Label
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = WePrayTheme.typography.overline,
                color = when {
                    isError -> WePrayTheme.colors.error
                    isSuccess -> WePrayTheme.colors.success
                    else -> WePrayTheme.colors.textSecondary
                }
            )
        }
        
        // Text field
        WePrayTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            enabled = enabled,
            isError = isError,
            isSuccess = isSuccess
        )
        
        // Helper text
        if (helperText != null) {
            Text(
                text = helperText,
                style = WePrayTheme.typography.bodySmall,
                color = when {
                    isError -> WePrayTheme.colors.error
                    isSuccess -> WePrayTheme.colors.success
                    else -> WePrayTheme.colors.textTertiary
                }
            )
        }
    }
}

@Preview
@Composable
private fun TextFieldPreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .width(700.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xxxl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xxxl)
        ) {
            Text(
                text = "Text Input States",
                style = WePrayTheme.typography.displayMedium,
                color = WePrayTheme.colors.onBackground
            )
            
            // Default
            var defaultText by remember { mutableStateOf("") }
            WePrayLabeledTextField(
                value = defaultText,
                onValueChange = { defaultText = it },
                label = "Default",
                placeholder = "Enter text here..."
            )
            
            // With Icon
            var searchText by remember { mutableStateOf("") }
            WePrayLabeledTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = "Search Input",
                placeholder = "Filter APKs by name...",
                leadingIcon = Icons.Outlined.Search
            )
            
            // With Icon and Value
            var pathText by remember { mutableStateOf("C:/Users/Developer/Downloads/APKs/Release_v2") }
            WePrayLabeledTextField(
                value = pathText,
                onValueChange = { pathText = it },
                label = "Local Source Directory",
                placeholder = "Enter path...",
                leadingIcon = Icons.Outlined.FolderOpen
            )
            
            // Error
            var errorText by remember { mutableStateOf("") }
            WePrayLabeledTextField(
                value = errorText,
                onValueChange = { errorText = it },
                label = "Error State",
                placeholder = "Invalid input",
                isError = true,
                helperText = "This field is required"
            )
            
            // Success
            var successText by remember { mutableStateOf("Valid input") }
            WePrayLabeledTextField(
                value = successText,
                onValueChange = { successText = it },
                label = "Success State",
                isSuccess = true,
                helperText = "Looks good!"
            )
            
            // Disabled
            WePrayLabeledTextField(
                value = "Disabled field",
                onValueChange = {},
                label = "Disabled",
                enabled = false
            )
        }
    }
}
