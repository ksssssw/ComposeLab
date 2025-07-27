package com.ksssssw.rockets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RocketsScreen(
    viewModel: RocketsViewModel = koinViewModel()
) {
    val viewModel = viewModel

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Rockets Screen"
        )
    }
}

@Composable
fun RocketItem(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        // FIXME: 이미지 컴포넌트로 변경해야 함
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = Color(255, 200, 200),
                    shape = RoundedCornerShape(8.dp)
                )
        )

        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text("Falcon 1")

            Text("Falcon 1")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RocketItemPreview() {
    RocketItem()
}

@Preview(showBackground = true)
@Composable
fun RocketsScreenPreview() {
    RocketsScreen()
}