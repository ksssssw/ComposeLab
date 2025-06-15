package com.example.company.navigation

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.example.company.CompanyScreen
import kotlinx.serialization.Serializable

@Serializable data object CompanyNavKey : NavKey

fun NavBackStack.navigateToCompany() = add(CompanyNavKey)

fun EntryProviderBuilder<NavKey>.companyScreen() {
    entry<CompanyNavKey> {
        CompanyScreen()
    }
}