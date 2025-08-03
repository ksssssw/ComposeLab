package com.ksssssw.crew.navigation

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.ksssssw.crew.CrewRoute
import kotlinx.serialization.Serializable

@Serializable data object CrewNavKey : NavKey

fun EntryProviderBuilder<NavKey>.crewScreen() {
    entry<CrewNavKey> {
        CrewRoute()
    }
}