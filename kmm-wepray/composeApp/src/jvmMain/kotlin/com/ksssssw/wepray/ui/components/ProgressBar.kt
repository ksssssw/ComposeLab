package com.ksssssw.wepray.ui.components

import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme
import kotlinx.coroutines.delay

/**
 * WePray Linear Progress Bar
 * Based on HTML design with storage visualization colors
 */
@Composable
fun WePrayProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    showLabel: Boolean = false,
    labelText: String = ""
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
    ) {
        // Label
        if (showLabel) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = labelText,
                    style = WePrayTheme.typography.bodySmall,
                    color = WePrayTheme.colors.textSecondary
                )
                
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = WePrayTheme.typography.bodySmall,
                    color = WePrayTheme.colors.textSecondary
                )
            }
        }
        
        // Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp))
                .background(WePrayTheme.colors.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp))
                    .background(WePrayTheme.gradients.Primary)
            )
        }
    }
}

/**
 * WePray Multi-Color Progress Bar
 * For storage/multi-category visualization (like HTML storage bar)
 */
@Composable
fun WePrayMultiProgressBar(
    segments: List<ProgressSegment>,
    modifier: Modifier = Modifier,
    showLabel: Boolean = false,
    labelText: String = ""
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
    ) {
        // Label
        if (showLabel) {
            Text(
                text = labelText,
                style = WePrayTheme.typography.bodySmall,
                color = WePrayTheme.colors.textSecondary
            )
        }
        
        // Multi-segment Progress Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp))
                .background(WePrayTheme.colors.surfaceVariant)
        ) {
            segments.forEach { segment ->
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(segment.percentage)
                        .background(segment.color)
                )
            }
        }
        
        // Legend
        if (segments.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl)
            ) {
                segments.forEach { segment ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(segment.color)
                        )
                        Text(
                            text = segment.label,
                            style = WePrayTheme.typography.bodySmall,
                            color = WePrayTheme.colors.textSecondary
                        )
                    }
                }
            }
        }
    }
}

/**
 * WePray Indeterminate Progress Bar
 * With pulse animation
 */
@Composable
fun WePrayIndeterminateProgressBar(
    modifier: Modifier = Modifier,
    showLabel: Boolean = false,
    labelText: String = "로딩 중..."
) {
    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
    ) {
        // Label
        if (showLabel) {
            Text(
                text = labelText,
                style = WePrayTheme.typography.bodySmall,
                color = WePrayTheme.colors.textSecondary
            )
        }
        
        // Progress Bar with animated gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp))
                .background(WePrayTheme.colors.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f + (offset * 0.1f))
                    .fillMaxHeight()
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp))
                    .background(WePrayTheme.gradients.Primary)
            )
        }
    }
}

/**
 * Installation Progress - APK installation scenario
 */
@Composable
fun WePrayInstallationProgress(
    fileName: String,
    progress: Float,
    isInstalling: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
    ) {
        // File info
        Column(
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
        ) {
            Text(
                text = fileName,
                style = WePrayTheme.typography.bodyLarge,
                color = WePrayTheme.colors.textPrimary
            )
            
            Text(
                text = if (isInstalling) "설치 중..." else "설치 완료",
                style = WePrayTheme.typography.bodySmall,
                color = if (isInstalling) 
                    WePrayTheme.colors.primary 
                else 
                    WePrayTheme.colors.success
            )
        }
        
        // Progress Bar
        if (isInstalling) {
            WePrayProgressBar(
                progress = progress,
                showLabel = true,
                labelText = "설치 진행 중"
            )
        } else {
            WePrayProgressBar(
                progress = 1f,
                showLabel = true,
                labelText = "설치 완료"
            )
        }
    }
}

data class ProgressSegment(
    val percentage: Float,
    val color: androidx.compose.ui.graphics.Color,
    val label: String
)

@Preview
@Composable
private fun ProgressBarPreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .width(800.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xxxl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xxxl)
        ) {
            Text(
                text = "Progress Bar States",
                style = WePrayTheme.typography.displayMedium,
                color = WePrayTheme.colors.onBackground
            )
            
            // 0%
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "0% Progress",
                    style = WePrayTheme.typography.headlineMedium,
                    color = WePrayTheme.colors.onBackground
                )
                WePrayProgressBar(
                    progress = 0f,
                    showLabel = true,
                    labelText = "대기 중"
                )
            }
            
            // 30%
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "30% Progress",
                    style = WePrayTheme.typography.headlineMedium,
                    color = WePrayTheme.colors.onBackground
                )
                WePrayProgressBar(
                    progress = 0.3f,
                    showLabel = true,
                    labelText = "설치 중"
                )
            }
            
            // 70%
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "70% Progress",
                    style = WePrayTheme.typography.headlineMedium,
                    color = WePrayTheme.colors.onBackground
                )
                WePrayProgressBar(
                    progress = 0.7f,
                    showLabel = true,
                    labelText = "거의 완료"
                )
            }
            
            // 100%
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "100% Progress",
                    style = WePrayTheme.typography.headlineMedium,
                    color = WePrayTheme.colors.onBackground
                )
                WePrayProgressBar(
                    progress = 1f,
                    showLabel = true,
                    labelText = "완료"
                )
            }
            
            // Multi-color storage progress
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Storage Progress (Multi-Color)",
                    style = WePrayTheme.typography.headlineMedium,
                    color = WePrayTheme.colors.onBackground
                )
                WePrayMultiProgressBar(
                    segments = listOf(
                        ProgressSegment(0.35f, WePrayTheme.colors.accentBlue, "Apps"),
                        ProgressSegment(0.10f, WePrayTheme.colors.accentPurple, "Media"),
                        ProgressSegment(0.10f, WePrayTheme.colors.accentEmerald, "System")
                    ),
                    showLabel = true,
                    labelText = "Device Storage: 55% Used"
                )
            }
            
            // Indeterminate
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Indeterminate Progress",
                    style = WePrayTheme.typography.headlineMedium,
                    color = WePrayTheme.colors.onBackground
                )
                WePrayIndeterminateProgressBar(
                    showLabel = true,
                    labelText = "준비 중..."
                )
            }
            
            // Installation Progress
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Installation Progress",
                    style = WePrayTheme.typography.headlineMedium,
                    color = WePrayTheme.colors.onBackground
                )
                
                var installProgress by remember { mutableStateOf(0f) }
                
                LaunchedEffect(Unit) {
                    while (installProgress < 1f) {
                        delay(100)
                        installProgress = (installProgress + 0.01f).coerceAtMost(1f)
                    }
                }
                
                WePrayInstallationProgress(
                    fileName = "MyApp-v1.0.apk",
                    progress = installProgress,
                    isInstalling = installProgress < 1f
                )
            }
        }
    }
}
