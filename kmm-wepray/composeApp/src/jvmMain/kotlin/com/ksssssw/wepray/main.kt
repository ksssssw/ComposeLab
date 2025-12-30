package com.ksssssw.wepray

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.ksssssw.wepray.di.appModules
import org.koin.core.context.startKoin
import java.awt.Dimension

fun main() {
    startKoin {
        modules(appModules)
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "WePray",
            state = rememberWindowState(
                size = DpSize(1300.dp, 800.dp)
            ),
        ) {
            // macOS 투명 타이틀바 설정
            window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
            window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
            window.rootPane.putClientProperty("apple.awt.windowTitleVisible", false)

            // 최소 크기 설정
            window.minimumSize = Dimension(1300, 800)

            WePrayApp()
        }
    }
}