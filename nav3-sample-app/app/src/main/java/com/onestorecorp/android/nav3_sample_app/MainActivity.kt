package com.onestorecorp.android.nav3_sample_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.onestorecorp.android.nav3_sample_app.navigation.animations.fadeAnimatedTransform
import com.onestorecorp.android.nav3_sample_app.navigation.animations.predictFadeAnimatedTransform
import com.onestorecorp.android.nav3_sample_app.navigation.entries.TopLevelDestination
import com.onestorecorp.android.nav3_sample_app.navigation.entries.bottomSheetEntryBuilder
import com.onestorecorp.android.nav3_sample_app.navigation.entries.detailEntryBuilder
import com.onestorecorp.android.nav3_sample_app.navigation.entries.dialogEntryBuilder
import com.onestorecorp.android.nav3_sample_app.navigation.entries.topLevelEntryBuilder
import com.onestorecorp.android.nav3_sample_app.navigation.strategy.rememberBottomNavSceneStrategy
import com.onestorecorp.android.nav3_sample_app.navigation.strategy.rememberBottomSheetSceneStrategy
import com.onestorecorp.android.nav3_sample_app.ui.theme.Nav3SampleAppTheme
import com.onestorecorp.android.nav3_sample_app.ui.theme.PastelYellow

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = rememberNavBackStack(TopLevelDestination.ROOT_VIEW)
            val currentTopLevel = backStack.lastOrNull()

            val bottomNavSceneStrategy = rememberBottomNavSceneStrategy<NavKey>(
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
                })
            val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }
            val bottomSheetStrategy = rememberBottomSheetSceneStrategy<NavKey>()

            Nav3SampleAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(PastelYellow)
                            .padding(innerPadding)
                    ) {
                        NavDisplay(
                            backStack = backStack,
                            onBack = { backStack.removeLastOrNull() },
                            transitionSpec = fadeAnimatedTransform(),
                            popTransitionSpec = fadeAnimatedTransform(),
                            predictivePopTransitionSpec = predictFadeAnimatedTransform(),
                            sceneStrategy = bottomNavSceneStrategy
                                .then(bottomSheetStrategy)
                                .then(dialogStrategy),
                            entryProvider = entryProvider {
                                // Top Level 화면 - BottomNav 표시
                                topLevelEntryBuilder(backStack = backStack)

                                // Detail 화면 - BottomNav 숨김 (metadata 없음)
                                detailEntryBuilder(backStack = backStack)

                                // Dialog 장면
                                dialogEntryBuilder()

                                // BottomSheet 장면
                                bottomSheetEntryBuilder()
                            }
                        )
                    }
                }
            }
        }
    }
}