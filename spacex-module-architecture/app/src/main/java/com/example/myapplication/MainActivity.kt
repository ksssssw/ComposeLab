package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.company.CompanyScreen
import com.example.crew.CrewScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.rockets.RocketsScreen
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SpacexBottomNavigationApp()
//                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
//                    SpacexApp()
//                }
            }
        }
    }
}

@Serializable
data object Rockets : NavKey

@Serializable
data object Crew : NavKey

@Serializable
data object Company : NavKey

data class BottomNavigationItem(
    val navKey: NavKey,
    val icon: ImageVector,
    val label: String
)

@Composable
fun SpacexBottomNavigationApp() {
    val bottomNavItems = listOf(
        BottomNavigationItem(
            navKey = Rockets,
            icon = Icons.Default.Home,
            label = "Rockets"
        ),
        BottomNavigationItem(
            navKey = Crew,
            icon = Icons.Default.Person,
            label = "Crew"
        ),
        BottomNavigationItem(
            navKey = Company,
            icon = Icons.Default.Info,
            label = "Company"
        )
    )

    val backStack = rememberNavBackStack(Rockets)
    val currentTab by remember {
        derivedStateOf {
            backStack.lastOrNull() ?: Rockets
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    val selected = item.navKey == currentTab
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
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
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(paddingValues),
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                } else {
                    // 루트에서는 그냥 앱 종료
                }
            },
            entryProvider = entryProvider {
                entry<Rockets> {
                    RocketsScreen()
                }

                entry<Crew> {
                    CrewScreen(
                        onNavigationToCompany = { backStack.add(Company) }
                    )
                }

                entry<Company> {
                    CompanyScreen()
                }
            }
        )
    }
}

@Composable
fun SpacexApp(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(Crew) // start destination

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            entry<Crew> {
                CrewScreen(
                    onNavigationToCompany = { backStack.add(Company) }
                )
            }

            entry<Company> {
                CompanyScreen()
            }
        }
    )
}