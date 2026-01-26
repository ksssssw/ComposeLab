package com.onestorecorp.android.nav3_sample_app.ui.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface TopLevelNavKey : NavKey

@Serializable data object TopContentA : TopLevelNavKey
@Serializable data object TopContentB : TopLevelNavKey
@Serializable data object TopContentC : TopLevelNavKey

@Composable
fun TopContentA(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Top Content A")
    }
}

@Composable
fun TopContentB(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Green),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Top Content B")
    }
}

@Composable
fun TopContentC(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Top Content C")
    }
}