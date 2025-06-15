package com.example.crew

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CrewScreen(
    onNavigationToCompany: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Crew Screen"
        )

        Button(
            onClick = onNavigationToCompany
        ) {
            Text("Go to Company")
        }
    }
}

@Preview
@Composable
fun CrewScreenPreview() {
    CrewScreen(
        onNavigationToCompany = { }
    )
}