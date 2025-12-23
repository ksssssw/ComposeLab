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
    currentTopLevelDestination: TopLevelDestination,
    sideRail: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val categories = listOf("홈 화면", "상세 화면", "설정 화면", "프로필 화면")
    var selectedCategory1 by remember { mutableStateOf<String?>(null) }

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
                WePrayTopBar(
                    title = currentTopLevelDestination.label,
                    description = currentTopLevelDestination.description,
                    deviceInfo = DeviceInfo(
                        icon = Icons.Outlined.Smartphone,
                        name = "Galaxy S23",
                        model = "Android 14 (API 34)"
                    ),
                    actions = {
                        WePrayDropdown(
                            modifier = Modifier
                                .width(180.dp),
                            value = selectedCategory1,
                            onValueChange = { selectedCategory1 = it },
                            items = categories,
                            placeholder = "카테고리 선택"
                        )

                        WePrayIconButton(
                            icon = Icons.Outlined.Refresh,
                            contentDescription = "Refresh",
                            onClick = {}
                        )
                    }
                )

                content()
            }
        }
    }
}
