package com.ksssssw.wepray.ui.components

import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Drag & Drop Zone
 * Based on HTML design: border-dashed with upload icon
 */
@Composable
fun WePrayDragDropZone(
    onFilesDropped: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Drop files here",
    subtitle: String = "or click to browse",
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    // Scale animation for icon on hover
    val scale by animateFloatAsState(
        targetValue = if (isHovered && enabled) 1.1f else 1f,
        animationSpec = tween(
            durationMillis = WePrayTheme.animation.DEFAULT,
            easing = FastOutSlowInEasing
        )
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
            .clip(WePrayTheme.shapes.card)
            .background(
                if (isHovered && enabled) 
                    WePrayTheme.colors.surfaceVariant
                else 
                    Color.Transparent
            )
            .border(
                width = 2.dp,
                color = if (isHovered && enabled) 
                    WePrayTheme.colors.primary
                else 
                    WePrayTheme.colors.borderSecondary,
                shape = WePrayTheme.shapes.card
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    // TODO: Open file picker
                    println("Browse files clicked")
                },
                enabled = enabled
            )
            .padding(WePrayTheme.spacing.xxxxl),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
        ) {
            // Upload icon with animation
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .scale(scale)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(
                        if (isHovered && enabled)
                            WePrayTheme.colors.primaryContainer
                        else
                            WePrayTheme.colors.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.UploadFile,
                    contentDescription = "Upload",
                    modifier = Modifier.size(WePrayTheme.iconSize.decorative),
                    tint = if (isHovered && enabled) 
                        WePrayTheme.colors.primary
                    else 
                        WePrayTheme.colors.textSecondary
                )
            }
            
            // Text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = title,
                    style = WePrayTheme.typography.headlineMedium,
                    color = if (enabled) 
                        WePrayTheme.colors.textPrimary
                    else 
                        WePrayTheme.colors.textDisabled
                )
                
                Text(
                    text = subtitle,
                    style = WePrayTheme.typography.bodySmall,
                    color = if (enabled)
                        WePrayTheme.colors.textSecondary
                    else
                        WePrayTheme.colors.textDisabled
                )
            }
        }
    }
}

/**
 * WePray Compact Drag & Drop Zone
 * Smaller version for inline use
 */
@Composable
fun WePrayCompactDragDropZone(
    onFilesDropped: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Drop APKs here or click to browse",
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(WePrayTheme.shapes.default)
            .background(
                if (isHovered && enabled) 
                    WePrayTheme.colors.surfaceVariant
                else 
                    Color.Transparent
            )
            .border(
                width = 2.dp,
                color = if (isHovered && enabled) 
                    WePrayTheme.colors.primary
                else 
                    WePrayTheme.colors.borderSecondary,
                shape = WePrayTheme.shapes.default
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    // TODO: Open file picker
                    println("Browse files clicked")
                },
                enabled = enabled
            )
            .padding(WePrayTheme.spacing.xl),
        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.UploadFile,
            contentDescription = "Upload",
            modifier = Modifier.size(WePrayTheme.iconSize.lg),
            tint = if (isHovered && enabled) 
                WePrayTheme.colors.primary
            else 
                WePrayTheme.colors.textSecondary
        )
        
        Text(
            text = text,
            style = WePrayTheme.typography.bodyMedium,
            color = if (enabled) 
                WePrayTheme.colors.textPrimary
            else 
                WePrayTheme.colors.textDisabled
        )
    }
}

/**
 * WePray Drag & Drop Zone with File Info
 * Shows upload progress or file count
 */
@Composable
fun WePrayDragDropZoneWithInfo(
    fileCount: Int,
    onFilesDropped: (List<String>) -> Unit,
    onBrowseClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
    ) {
        // Drag & Drop Zone
        WePrayDragDropZone(
            onFilesDropped = onFilesDropped,
            enabled = enabled
        )
        
        // File info
        if (fileCount > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$fileCount file${if (fileCount > 1) "s" else ""} ready to upload",
                    style = WePrayTheme.typography.bodySmall,
                    color = WePrayTheme.colors.textSecondary
                )
                
                WePrayGhostButton(
                    text = "Clear all",
                    onClick = { /* Clear files */ },
                    size = ButtonSize.Small,
                    color = WePrayTheme.colors.error
                )
            }
        }
    }
}

@Preview
@Composable
private fun DragDropZonePreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .width(800.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xxxl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xxxl)
        ) {
            Text(
                text = "Drag & Drop Zones",
                style = WePrayTheme.typography.displayMedium,
                color = WePrayTheme.colors.onBackground
            )
            
            // Standard Drag & Drop Zone
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Standard Zone",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                WePrayDragDropZone(
                    onFilesDropped = { files ->
                        println("Files dropped: $files")
                    }
                )
            }
            
            // Custom text
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Custom Text",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                WePrayDragDropZone(
                    onFilesDropped = { files ->
                        println("APKs dropped: $files")
                    },
                    title = "Drop APKs here",
                    subtitle = "or click to select from your computer"
                )
            }
            
            // Compact version
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Compact Zone",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                WePrayCompactDragDropZone(
                    onFilesDropped = { files ->
                        println("Files dropped: $files")
                    }
                )
            }
            
            // Disabled
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Disabled",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                WePrayDragDropZone(
                    onFilesDropped = {},
                    enabled = false,
                    title = "Upload disabled",
                    subtitle = "Please connect a device first"
                )
            }
            
            // With file info
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "With File Info",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                WePrayDragDropZoneWithInfo(
                    fileCount = 3,
                    onFilesDropped = {},
                    onBrowseClick = {}
                )
            }
        }
    }
}

