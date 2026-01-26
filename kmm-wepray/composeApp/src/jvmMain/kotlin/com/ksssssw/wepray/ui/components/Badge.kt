package com.ksssssw.wepray.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Badge
 * Based on HTML design: px-2 py-0.5 rounded text-xs font-mono
 */
@Composable
fun WePrayBadge(
    text: String,
    variant: BadgeVariant? = null,
    backgroundColor: Color? = WePrayTheme.colors.primaryContainer,
    textColor: Color? = WePrayTheme.colors.primary,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (variant) {
        BadgeVariant.Primary -> WePrayTheme.colors.primaryContainer to WePrayTheme.colors.primary
        BadgeVariant.Success -> WePrayTheme.colors.successContainer to WePrayTheme.colors.success
        BadgeVariant.SuccessLight -> WePrayTheme.colors.successContainer to WePrayTheme.colors.successLight
        BadgeVariant.Warning -> WePrayTheme.colors.warningContainer to WePrayTheme.colors.warning
        BadgeVariant.Error -> WePrayTheme.colors.errorContainer to WePrayTheme.colors.error
        BadgeVariant.Info -> WePrayTheme.colors.infoContainer to WePrayTheme.colors.info
        BadgeVariant.Neutral -> WePrayTheme.colors.surfaceVariant to WePrayTheme.colors.textSecondary
        null -> backgroundColor to textColor
    }
    
    Box(
        modifier = modifier
            .clip(WePrayTheme.shapes.medium)
            .background(backgroundColor!!)
            .padding(
                horizontal = WePrayTheme.spacing.md,
                vertical = WePrayTheme.spacing.xs
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = WePrayTheme.typography.code.copy(fontSize = WePrayTheme.typography.labelSmall.fontSize),
            color = textColor!!,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * WePray Pill Badge - Full rounded
 * For status indicators
 */
@Composable
fun WePrayPillBadge(
    text: String,
    variant: BadgeVariant = BadgeVariant.Primary,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (variant) {
        BadgeVariant.Primary -> WePrayTheme.colors.primary to Color.White
        BadgeVariant.Success -> WePrayTheme.colors.success to Color.White
        BadgeVariant.SuccessLight -> WePrayTheme.colors.successLight to Color.White
        BadgeVariant.Warning -> WePrayTheme.colors.warning to Color.White
        BadgeVariant.Error -> WePrayTheme.colors.error to Color.White
        BadgeVariant.Info -> WePrayTheme.colors.info to Color.White
        BadgeVariant.Neutral -> WePrayTheme.colors.surfaceElevated to WePrayTheme.colors.textPrimary
    }
    
    Box(
        modifier = modifier
            .clip(WePrayTheme.shapes.full)
            .background(backgroundColor)
            .padding(
                horizontal = WePrayTheme.spacing.lg,
                vertical = WePrayTheme.spacing.sm
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = WePrayTheme.typography.labelMedium,
            color = textColor
        )
    }
}

/**
 * WePray Outlined Badge
 * For subtle indicators
 */
@Composable
fun WePrayOutlinedBadge(
    text: String,
    variant: BadgeVariant = BadgeVariant.Primary,
    modifier: Modifier = Modifier
) {
    val color = when (variant) {
        BadgeVariant.Primary -> WePrayTheme.colors.primary
        BadgeVariant.Success, BadgeVariant.SuccessLight -> WePrayTheme.colors.success
        BadgeVariant.Warning -> WePrayTheme.colors.warning
        BadgeVariant.Error -> WePrayTheme.colors.error
        BadgeVariant.Info -> WePrayTheme.colors.info
        BadgeVariant.Neutral -> WePrayTheme.colors.textSecondary
    }
    
    Box(
        modifier = modifier
            .clip(WePrayTheme.shapes.medium)
            .background(color.copy(alpha = 0.1f))
            .padding(
                horizontal = WePrayTheme.spacing.md,
                vertical = WePrayTheme.spacing.xs
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = WePrayTheme.typography.labelSmall,
            color = color
        )
    }
}

enum class BadgeVariant {
    Primary,
    Success,
    SuccessLight,
    Warning,
    Error,
    Info,
    Neutral
}

@Preview
@Composable
private fun BadgePreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .width(800.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xxxl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xxxl)
        ) {
            // Standard Badges
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Standard Badges (Monospace)",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WePrayBadge(text = "v294.0.0", variant = BadgeVariant.Neutral)
                    WePrayBadge(text = "v1.0.4-beta", variant = BadgeVariant.Neutral)
                    WePrayBadge(text = "v8.8.0", variant = BadgeVariant.Neutral)
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WePrayBadge(text = "Primary", variant = BadgeVariant.Primary)
                    WePrayBadge(text = "Success", variant = BadgeVariant.Success)
                    WePrayBadge(text = "Warning", variant = BadgeVariant.Warning)
                    WePrayBadge(text = "Error", variant = BadgeVariant.Error)
                    WePrayBadge(text = "Info", variant = BadgeVariant.Info)
                }
            }
            
            // Pill Badges
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Pill Badges (Status)",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WePrayPillBadge(text = "연결됨", variant = BadgeVariant.Success)
                    WePrayPillBadge(text = "설치 중", variant = BadgeVariant.Primary)
                    WePrayPillBadge(text = "대기 중", variant = BadgeVariant.Warning)
                    WePrayPillBadge(text = "실패", variant = BadgeVariant.Error)
                    WePrayPillBadge(text = "중립", variant = BadgeVariant.Neutral)
                }
            }
            
            // Outlined Badges
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Outlined Badges (Subtle)",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WePrayOutlinedBadge(text = "NEW", variant = BadgeVariant.Primary)
                    WePrayOutlinedBadge(text = "INSTALLED", variant = BadgeVariant.Success)
                    WePrayOutlinedBadge(text = "PENDING", variant = BadgeVariant.Warning)
                    WePrayOutlinedBadge(text = "FAILED", variant = BadgeVariant.Error)
                }
            }
            
            // Use cases
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Use Cases",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    // Device status
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Samsung SM-G991B",
                            style = WePrayTheme.typography.bodyLarge,
                            color = WePrayTheme.colors.textPrimary,
                            modifier = Modifier.width(200.dp)
                        )
                        WePrayPillBadge(text = "Connected", variant = BadgeVariant.Success)
                    }
                    
                    // APK version
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "com.instagram.android.apk",
                            style = WePrayTheme.typography.bodyMedium,
                            color = WePrayTheme.colors.textPrimary,
                            modifier = Modifier.width(200.dp)
                        )
                        WePrayBadge(text = "v294.0.0", variant = BadgeVariant.Neutral)
                        WePrayOutlinedBadge(text = "NEW", variant = BadgeVariant.Primary)
                    }
                    
                    // Installation status
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "app-debug-unsigned.apk",
                            style = WePrayTheme.typography.bodyMedium,
                            color = WePrayTheme.colors.textPrimary,
                            modifier = Modifier.width(200.dp)
                        )
                        WePrayBadge(text = "v1.0.4-beta", variant = BadgeVariant.Neutral)
                        WePrayPillBadge(text = "Installing", variant = BadgeVariant.Primary)
                    }
                }
            }
        }
    }
}
