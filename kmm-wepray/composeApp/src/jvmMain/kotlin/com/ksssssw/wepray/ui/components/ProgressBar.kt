package com.ksssssw.wepray.ui.components

import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme
import kotlinx.coroutines.delay

/**
 * WePray Linear Progress Bar
 * 
 * 디자인 가이드 스펙:
 * - Container Background: rgba(255, 255, 255, 0.1)
 * - Fill: Gradient(#4A9EE0, #E06B9E, #F4C430)
 * - Height: 8px
 * - Border Radius: 4px
 * - Animation: Pulse effect for indeterminate
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
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
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
                    color = WePrayTheme.colors.onSurfaceVariant
                )
                
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = WePrayTheme.typography.bodySmall,
                    color = WePrayTheme.colors.onSurfaceVariant
                )
            }
        }
        
        // Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                .background(Color.White.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                    .background(WePrayTheme.gradients.Rainbow)
            )
        }
    }
}

/**
 * WePray Indeterminate Progress Bar
 * 
 * 디자인 가이드 스펙:
 * - Animation: Pulse effect (opacity animation)
 * - Duration: 2s ease-in-out infinite
 */
@Composable
fun WePrayIndeterminateProgressBar(
    modifier: Modifier = Modifier,
    showLabel: Boolean = false,
    labelText: String = "로딩 중..."
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
    ) {
        // Label
        if (showLabel) {
            Text(
                text = labelText,
                style = WePrayTheme.typography.bodySmall,
                color = WePrayTheme.colors.onSurfaceVariant
            )
        }
        
        // Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                .background(Color.White.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
//                    .background(
//                        WePrayTheme.gradients.Rainbow.copy(alpha = alpha)
//                    )
            )
        }
    }
}

/**
 * Rainbow Gradient with alpha
 */
//private fun Brush.copy(alpha: Float): Brush {
//    return when (this) {
//        is Brush.LinearGradient -> Brush.linearGradient(
//            colors = colors.map { it.copy(alpha = alpha) },
//            start = start,
//            end = end
//        )
//        else -> this
//    }
//}

/**
 * Installation Progress - 실제 APK 설치 시나리오
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
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
    ) {
        // 파일 정보
        Column(
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
        ) {
            Text(
                text = fileName,
                style = WePrayTheme.typography.bodyLarge,
                color = WePrayTheme.colors.onSurface
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

@Preview
@Composable
private fun ProgressBarPreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .width(600.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl)
        ) {
            Text(
                text = "Progress Bar States",
                style = WePrayTheme.typography.displayMedium,
                color = WePrayTheme.colors.onBackground
            )
            
            // 0%
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "0% Progress",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.onSurfaceVariant
                )
                WePrayProgressBar(
                    progress = 0f,
                    showLabel = true,
                    labelText = "대기 중"
                )
            }
            
            // 30%
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "30% Progress",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.onSurfaceVariant
                )
                WePrayProgressBar(
                    progress = 0.3f,
                    showLabel = true,
                    labelText = "설치 중"
                )
            }
            
            // 70%
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "70% Progress",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.onSurfaceVariant
                )
                WePrayProgressBar(
                    progress = 0.7f,
                    showLabel = true,
                    labelText = "거의 완료"
                )
            }
            
            // 100%
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "100% Progress",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.onSurfaceVariant
                )
                WePrayProgressBar(
                    progress = 1f,
                    showLabel = true,
                    labelText = "완료"
                )
            }
            
            // Indeterminate
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "Indeterminate Progress",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.onSurfaceVariant
                )
                WePrayIndeterminateProgressBar(
                    showLabel = true,
                    labelText = "준비 중..."
                )
            }
            
            // Installation Progress - Installing
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "Installation Progress - Installing",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.onSurfaceVariant
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
