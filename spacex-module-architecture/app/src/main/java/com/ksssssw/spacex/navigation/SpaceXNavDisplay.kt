package com.ksssssw.spacex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ksssssw.company.navigation.companyScreen
import com.ksssssw.company.navigation.navigateToCompany
import com.ksssssw.crew.navigation.crewScreen
import com.ksssssw.rockets.navigation.rocketsScreen

@Composable
fun SpaceXNavDisplay(
    backStack: NavBackStack,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            } else {
                // 루트에서는 그냥 앱 종료
            }
        },
        entryProvider = entryProvider {
            rocketsScreen()

            crewScreen(
                onNavigationToCompany = backStack::navigateToCompany
            )

            companyScreen()
        }
    )
}