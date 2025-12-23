package com.ksssssw.wepray.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Badge
 * 
 * 디자인 가이드 스펙:
 * - Padding: 4px 10px
 * - Border Radius: 12px
 * - Font Size: 0.8rem (12.8px)
 * - Font Weight: 600
 * 
 * Variants:
 *   - Primary: Background rgba(74, 158, 224, 0.2), Color #4A9EE0
 *   - Success: Background rgba(0, 230, 118, 0.2), Color #00E676
 *   - Warning: Background rgba(255, 193, 7, 0.2), Color #FFC107
 *   - Error: Background rgba(255, 61, 0, 0.2), Color #FF3D00
 */
@Composable
fun WePrayBadge(
    text: String,
    variant: BadgeVariant = BadgeVariant.Primary,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (variant) {
        BadgeVariant.Primary -> WePrayTheme.colors.primaryContainer to WePrayTheme.colors.primary
        BadgeVariant.Success -> WePrayTheme.colors.success.copy(alpha = 0.2f) to WePrayTheme.colors.success
        BadgeVariant.Warning -> WePrayTheme.colors.warning.copy(alpha = 0.2f) to WePrayTheme.colors.warning
        BadgeVariant.Error -> WePrayTheme.colors.errorContainer to WePrayTheme.colors.error
    }
    
    Box(
        modifier = modifier
            .clip(WePrayTheme.shapes.badge)
            .background(backgroundColor)
            .padding(
                horizontal = WePrayTheme.spacing.sm + WePrayTheme.spacing.xs,
                vertical = WePrayTheme.spacing.xs
            )
    ) {
        Text(
            text = text,
            style = WePrayTheme.typography.labelMedium,
            color = textColor
        )
    }
}

enum class BadgeVariant {
    Primary,
    Success,
    Warning,
    Error
}

@Preview
@Composable
private fun BadgePreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                WePrayBadge(text = "Primary", variant = BadgeVariant.Primary)
                WePrayBadge(text = "Success", variant = BadgeVariant.Success)
                WePrayBadge(text = "Warning", variant = BadgeVariant.Warning)
                WePrayBadge(text = "Error", variant = BadgeVariant.Error)
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                WePrayBadge(text = "연결됨", variant = BadgeVariant.Success)
                WePrayBadge(text = "설치 중", variant = BadgeVariant.Primary)
                WePrayBadge(text = "대기 중", variant = BadgeVariant.Warning)
                WePrayBadge(text = "실패", variant = BadgeVariant.Error)
            }
        }
    }
}
