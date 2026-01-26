package com.ksssssw.wepray.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Dialog
 * Modal dialog for desktop applications
 */
@Composable
fun WePrayDialog(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    showCloseButton: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismissRequest
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier
                    .widthIn(max = 600.dp)
                    .heightIn(max = 700.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {} // Prevent closing when clicking inside
                    )
                    .shadow(
                        elevation = WePrayTheme.elevation.dialog,
                        shape = WePrayTheme.shapes.card
                    )
                    .clip(WePrayTheme.shapes.card)
                    .background(WePrayTheme.colors.surface)
                    .border(
                        width = 1.dp,
                        color = WePrayTheme.colors.border,
                        shape = WePrayTheme.shapes.card
                    )
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WePrayTheme.colors.surfaceVariant)
                        .padding(WePrayTheme.spacing.xl),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = WePrayTheme.typography.headlineMedium,
                        color = WePrayTheme.colors.textPrimary
                    )
                    if (showCloseButton) {
                        WePrayIconButton(
                            icon = Icons.Outlined.Close,
                            contentDescription = "Close",
                            onClick = onDismissRequest,
                            size = IconButtonSize.Small
                        )
                    }
                }
                
                // Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .padding(WePrayTheme.spacing.xl),
                    content = content
                )
            }
        }
    }
}

/**
 * WePray Confirmation Dialog
 * Simple dialog with action buttons
 */
@Composable
fun WePrayConfirmDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String,
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    isDangerous: Boolean = false
) {
    WePrayDialog(
        onDismissRequest = onDismissRequest,
        title = title,
        modifier = Modifier.width(450.dp)
    ) {
        Text(
            text = message,
            style = WePrayTheme.typography.bodyMedium,
            color = WePrayTheme.colors.textSecondary,
            modifier = Modifier.padding(bottom = WePrayTheme.spacing.xl)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
        ) {
            WePraySecondaryButton(
                text = cancelText,
                onClick = onDismissRequest,
                modifier = Modifier.weight(1f)
            )
            if (isDangerous) {
                WePrayDangerButton(
                    text = confirmText,
                    onClick = {
                        onConfirm()
                        onDismissRequest()
                    },
                    modifier = Modifier.weight(1f)
                )
            } else {
                WePrayPrimaryButton(
                    text = confirmText,
                    onClick = {
                        onConfirm()
                        onDismissRequest()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

