package com.ksssssw.wepray.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.components.BadgeVariant
import com.ksssssw.wepray.ui.components.DeviceInfo
import com.ksssssw.wepray.ui.components.WePrayBadge
import com.ksssssw.wepray.ui.components.WePrayDropdown
import com.ksssssw.wepray.ui.components.WePrayIconButton
import com.ksssssw.wepray.ui.components.WePrayTopBar
import com.ksssssw.wepray.ui.navigation.TopLevelDestination
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Scaffold
 * 메인 레이아웃 구조: 사이드 레일 + 메인 컨텐츠
 * 
 * @param sideRail 좌측 사이드 레일 컨텐츠
 * @param content 메인 컨텐츠 영역
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
        // 좌측 사이드 레일
        sideRail()
        
        // 메인 컨텐츠 영역
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = WePrayTheme.spacing.lg, vertical = WePrayTheme.spacing.md)
            ) {
                topBar()

                content()
            }
        }
    }
}
