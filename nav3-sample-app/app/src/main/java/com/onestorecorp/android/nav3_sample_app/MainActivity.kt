package com.onestorecorp.android.nav3_sample_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.onestorecorp.android.nav3_sample_app.ui.contents.TopContentA
import com.onestorecorp.android.nav3_sample_app.ui.contents.TopContentB
import com.onestorecorp.android.nav3_sample_app.ui.contents.TopContentC
import com.onestorecorp.android.nav3_sample_app.ui.contents.TopLevelNavKey
import com.onestorecorp.android.nav3_sample_app.ui.theme.Nav3sampleappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = rememberNavBackStack(TopContentA)
            val currentTopLevel = backStack.lastOrNull()

            Nav3sampleappTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            TopLevelDestination.entries.forEach { destination ->
                                NavigationBarItem(
                                    selected = currentTopLevel == destination.route,
                                    onClick = { backStack.add(destination.route) },
                                    icon = {
                                        Icon(
                                            imageVector = destination.icon,
                                            contentDescription = destination.label
                                        )
                                    },
                                    label = {
                                        Text(destination.label)
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Yellow)
                            .padding(innerPadding)
                    ) {
                        NavDisplay(
                            backStack = backStack,
                            onBack = { backStack.removeLastOrNull() },
                            entryProvider = entryProvider {
                                entry(TopContentA) { TopContentA() }
                                entry(TopContentB) { TopContentB() }
                                entry(TopContentC) { TopContentC() }
                            }
                        )
                    }
                }
            }
        }
    }
}

enum class TopLevelDestination(
    val label: String,
    val icon: ImageVector,
    val route: TopLevelNavKey,
) {
    Atab(
        label = "Tab A",
        icon = Icons.Outlined.Home,
        route = TopContentA
    ),
    Btab(
        label = "Tab B",
        icon = Icons.Outlined.Face,
        route = TopContentB
    ),
    Ctab(
        label = "Tab C",
        icon = Icons.Outlined.Favorite,
        route = TopContentC
    )
}