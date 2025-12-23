package com.ksssssw.wepray

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
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