package com.example.rockets.navigation

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.example.rockets.RocketsScreen
import kotlinx.serialization.Serializable

@Serializable
data object RocketsNavKey : NavKey

fun EntryProviderBuilder<NavKey>.rocketsScreen() {
    entry<RocketsNavKey> {
        RocketsScreen()
    }
}