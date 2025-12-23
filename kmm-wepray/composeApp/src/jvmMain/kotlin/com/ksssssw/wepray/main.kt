package com.ksssssw.wepray

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.ksssssw.wepray.di.appModules
import org.koin.core.context.startKoin

fun main() {
    // Koin 초기화 - Application 시작 전에 먼저 초기화
    startKoin {
        modules(appModules)
    }
    
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "WePray",
            state = WindowState(
                size = DpSize(1300.dp, 800.dp)
            ),
        ) {
            window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
            window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
            window.rootPane.putClientProperty("apple.awt.windowTitleVisible", false)

            App()
        }
    }
}
