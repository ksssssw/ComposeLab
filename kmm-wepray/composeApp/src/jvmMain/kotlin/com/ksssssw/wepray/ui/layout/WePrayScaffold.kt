package com.ksssssw.wepray.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Scaffold
 * Main layout structure: Side rail + Top bar + Content
 * Based on HTML layout with sticky header and sidebar
 */
@Composable
fun WePrayScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    sideRail: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(WePrayTheme.colors.background)
    ) {
        // Left side rail
        sideRail()
        
        // Main content area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // Top bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        WePrayTheme.spacing.containerPaddingSmall
                    )
            ) {
                topBar()
            }
            
            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = WePrayTheme.spacing.containerPaddingSmall,
                        end = WePrayTheme.spacing.containerPaddingSmall,
                        bottom = WePrayTheme.spacing.lg
                    )
            ) {
                content()
            }
        }
    }
}
