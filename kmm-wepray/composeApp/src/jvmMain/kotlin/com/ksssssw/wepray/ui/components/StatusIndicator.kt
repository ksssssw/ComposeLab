package com.ksssssw.wepray.ui.components

import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
 * WePray Status Indicator
 * Based on HTML design: animate-ping with pulsing circle
 */
@Composable
fun WePrayStatusIndicator(
    status: IndicatorStatus,
    modifier: Modifier = Modifier,
    animate: Boolean = true
) {
    val color = when (status) {
        IndicatorStatus.Connected -> WePrayTheme.colors.success
        IndicatorStatus.Connecting -> WePrayTheme.colors.warning
        IndicatorStatus.Disconnected -> WePrayTheme.colors.error
        IndicatorStatus.Idle -> WePrayTheme.colors.textTertiary
    }
    
    Box(
        modifier = modifier.size(10.dp),
        contentAlignment = Alignment.Center
    ) {
        // Ping animation (outer circle)
        if (animate && status == IndicatorStatus.Connected) {
            val infiniteTransition = rememberInfiniteTransition()
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 2.5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = WePrayTheme.animation.PULSE_DURATION, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.75f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = WePrayTheme.animation.PULSE_DURATION, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale)
                    .clip(CircleShape)
                    .background(color.copy(alpha = alpha))
            )
        }
        
        // Inner circle (solid)
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
    }
}

/**
 * WePray Status Badge with Indicator
 * Combines status indicator with text
 */
@Composable
fun WePrayStatusBadge(
    status: IndicatorStatus,
    text: String,
    modifier: Modifier = Modifier,
    animate: Boolean = true
) {
    val containerColor = when (status) {
        IndicatorStatus.Connected -> WePrayTheme.colors.successContainer
        IndicatorStatus.Connecting -> WePrayTheme.colors.warningContainer
        IndicatorStatus.Disconnected -> WePrayTheme.colors.errorContainer
        IndicatorStatus.Idle -> WePrayTheme.colors.surfaceVariant
    }
    
    val textColor = when (status) {
        IndicatorStatus.Connected -> WePrayTheme.colors.success
        IndicatorStatus.Connecting -> WePrayTheme.colors.warning
        IndicatorStatus.Disconnected -> WePrayTheme.colors.error
        IndicatorStatus.Idle -> WePrayTheme.colors.textSecondary
    }
    
    Row(
        modifier = modifier
            .clip(WePrayTheme.shapes.default)
            .background(containerColor)
            .border(
                width = 1.dp,
                color = when (status) {
                    IndicatorStatus.Connected -> WePrayTheme.colors.success.copy(alpha = 0.2f)
                    IndicatorStatus.Connecting -> WePrayTheme.colors.warning.copy(alpha = 0.2f)
                    IndicatorStatus.Disconnected -> WePrayTheme.colors.error.copy(alpha = 0.2f)
                    IndicatorStatus.Idle -> WePrayTheme.colors.border
                },
                shape = WePrayTheme.shapes.default
            )
            .padding(
                horizontal = WePrayTheme.spacing.lg,
                vertical = WePrayTheme.spacing.sm
            ),
        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WePrayStatusIndicator(
            status = status,
            animate = animate
        )
        
        Text(
            text = text,
            style = WePrayTheme.typography.labelMedium,
            color = textColor
        )
    }
}

/**
 * WePray Connection Indicator with Device Info
 * Based on HTML header design
 */
@Composable
fun WePrayConnectionBadge(
    deviceName: String,
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(WePrayTheme.shapes.default)
            .background(
                if (isConnected) 
                    WePrayTheme.colors.successContainer 
                else 
                    WePrayTheme.colors.errorContainer
            )
            .border(
                width = 1.dp,
                color = if (isConnected) 
                    WePrayTheme.colors.success.copy(alpha = 0.2f)
                else 
                    WePrayTheme.colors.error.copy(alpha = 0.2f),
                shape = WePrayTheme.shapes.default
            )
            .padding(
                horizontal = WePrayTheme.spacing.lg,
                vertical = WePrayTheme.spacing.sm + WePrayTheme.spacing.xs
            ),
        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Status indicator with ping animation
        Box(
            modifier = Modifier.size(10.dp),
            contentAlignment = Alignment.Center
        ) {
            // Ping animation for connected state
            if (isConnected) {
                val infiniteTransition = rememberInfiniteTransition()
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 2.5f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 0.75f,
                    targetValue = 0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(scale)
                        .clip(CircleShape)
                        .background(
                            if (isConnected) 
                                WePrayTheme.colors.success.copy(alpha = alpha)
                            else 
                                Color.Transparent
                        )
                )
            }
            
            // Inner solid circle
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (isConnected) 
                            WePrayTheme.colors.success
                        else 
                            WePrayTheme.colors.error
                    )
            )
        }
        
        // Device name
        Text(
            text = if (isConnected) "$deviceName Connected" else "No Device Connected",
            style = WePrayTheme.typography.labelMedium,
            color = if (isConnected) 
                WePrayTheme.colors.success
            else 
                WePrayTheme.colors.error
        )
    }
}

enum class IndicatorStatus {
    Connected,
    Connecting,
    Disconnected,
    Idle
}

@Preview
@Composable
private fun StatusIndicatorPreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .width(800.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xxxl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xxxl)
        ) {
            // Status Indicators
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Status Indicators",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                    ) {
                        WePrayStatusIndicator(status = IndicatorStatus.Connected)
                        Text(
                            text = "Connected",
                            style = WePrayTheme.typography.bodySmall,
                            color = WePrayTheme.colors.textSecondary
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                    ) {
                        WePrayStatusIndicator(status = IndicatorStatus.Connecting, animate = false)
                        Text(
                            text = "Connecting",
                            style = WePrayTheme.typography.bodySmall,
                            color = WePrayTheme.colors.textSecondary
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                    ) {
                        WePrayStatusIndicator(status = IndicatorStatus.Disconnected, animate = false)
                        Text(
                            text = "Disconnected",
                            style = WePrayTheme.typography.bodySmall,
                            color = WePrayTheme.colors.textSecondary
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                    ) {
                        WePrayStatusIndicator(status = IndicatorStatus.Idle, animate = false)
                        Text(
                            text = "Idle",
                            style = WePrayTheme.typography.bodySmall,
                            color = WePrayTheme.colors.textSecondary
                        )
                    }
                }
            }
            
            // Status Badges
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Status Badges",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WePrayStatusBadge(
                        status = IndicatorStatus.Connected,
                        text = "Connected"
                    )
                    WePrayStatusBadge(
                        status = IndicatorStatus.Connecting,
                        text = "Connecting...",
                        animate = false
                    )
                    WePrayStatusBadge(
                        status = IndicatorStatus.Disconnected,
                        text = "Offline",
                        animate = false
                    )
                    WePrayStatusBadge(
                        status = IndicatorStatus.Idle,
                        text = "Idle",
                        animate = false
                    )
                }
            }
            
            // Connection Badges
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Connection Badges (Header Style)",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    WePrayConnectionBadge(
                        deviceName = "Samsung SM-G991B",
                        isConnected = true
                    )
                    
                    WePrayConnectionBadge(
                        deviceName = "",
                        isConnected = false
                    )
                }
            }
        }
    }
}

