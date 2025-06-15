package com.example.crew.navigation

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.example.crew.CrewScreen
import kotlinx.serialization.Serializable

@Serializable data object CrewNavKey : NavKey

fun EntryProviderBuilder<NavKey>.crewScreen(
    onNavigationToCompany: () -> Unit
) {
    entry<CrewNavKey> {
        CrewScreen(
            onNavigationToCompany = onNavigationToCompany
        )
    }
}