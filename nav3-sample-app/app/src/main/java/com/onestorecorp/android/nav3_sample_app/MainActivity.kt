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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy.Companion.listPane
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.onestorecorp.android.nav3_sample_app.scene.BottomNavScene.Companion.showNavBar
import com.onestorecorp.android.nav3_sample_app.scene.rememberBottomNavSceneStrategy
import com.onestorecorp.android.nav3_sample_app.ui.contents.DetailContent
import com.onestorecorp.android.nav3_sample_app.ui.contents.DetailContentA
import com.onestorecorp.android.nav3_sample_app.ui.contents.DetailContentB
import com.onestorecorp.android.nav3_sample_app.ui.contents.TopContent
import com.onestorecorp.android.nav3_sample_app.ui.contents.TopContentA
import com.onestorecorp.android.nav3_sample_app.ui.contents.TopContentB
import com.onestorecorp.android.nav3_sample_app.ui.contents.TopContentC
import com.onestorecorp.android.nav3_sample_app.ui.contents.TopLevelNavKey
import com.onestorecorp.android.nav3_sample_app.ui.theme.Nav3sampleappTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = rememberNavBackStack(TopContentA)
            val currentTopLevel = backStack.lastOrNull()

            Nav3sampleappTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
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
                            sceneStrategy = rememberBottomNavSceneStrategy(
                                bottomBarContent = {
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
                                }),
                            entryProvider = entryProvider {
                                // Top Level 화면들 - BottomNav 표시
                                entry(
                                    TopContentA,
                                    metadata = listPane() + showNavBar()
                                ) {
                                    TopContent(
                                        modifier = Modifier.background(Color.Red),
                                        content = {
                                            Text("Top Content A")

                                            Button(onClick = { backStack.add(DetailContentA) }) {
                                                Text("Go to Detail A")
                                            }
                                        }
                                    )
                                }

                                entry(
                                    TopContentB,
                                    metadata = showNavBar()
                                ) {
                                    TopContent(
                                        modifier = Modifier.background(Color.Green),
                                        content = {
                                            Text("Top Content B")
                                        }
                                    )
                                }

                                entry(
                                    TopContentC,
                                    metadata = showNavBar()
                                ) {
                                    TopContent(
                                        modifier = Modifier.background(Color.Blue),
                                        content = {
                                            Text("Top Content C")
                                        }
                                    )
                                }

                                // Detail 화면 - BottomNav 숨김 (metadata 없음)
                                entry(
                                    DetailContentA
                                ) {
                                    DetailContent(
                                        modifier = Modifier.background(Color.White),
                                        content = {
                                            Text("Detail Content A")

                                            Button(onClick = { backStack.add(DetailContentB) }) {
                                                Text("Go to Detail B")
                                            }
                                        }
                                    )
                                }

                                entry(DetailContentB) {
                                    DetailContent(
                                        modifier = Modifier.background(Color.Magenta),
                                        content = {
                                            Text("Detail Content B")
                                        }
                                    )
                                }
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