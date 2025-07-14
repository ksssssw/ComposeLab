package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.myapplication.navigation.SpaceXNavDisplay
import com.example.myapplication.navigation.TopLevelDestination
import com.example.ui.theme.SpaceXTheme
import com.example.rockets.navigation.RocketsNavKey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpaceXTheme {
                SpacexBottomNavigationApp()
            }
        }
    }
}

@Composable
fun SpacexBottomNavigationApp() {
    val topLevelDestination: List<TopLevelDestination> = TopLevelDestination.entries

    val backStack = rememberNavBackStack(RocketsNavKey)
    val currentTab by remember {
        derivedStateOf {
            backStack.lastOrNull() ?: RocketsNavKey
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                topLevelDestination.forEach { item ->
                    val selected = item.navKey == currentTab
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.selectedIcon,
                                contentDescription = stringResource(item.iconLabelId)
                            )
                        },
                        label = { Text(stringResource(item.iconLabelId)) },
                        selected = currentTab == item.navKey,
                        onClick = {
                            if (currentTab != item.navKey) {
                                backStack.clear()
                                backStack.add(item.navKey)
                            } else {
                                if (backStack.size > 1) {
                                    backStack.clear()
                                    backStack.add(item.navKey)
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        SpaceXNavDisplay(
            backStack = backStack,
            modifier = Modifier.padding((paddingValues))
        )
    }
}