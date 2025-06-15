package com.example.myapplication.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.example.rockets.navigation.RocketsNavKey
import com.example.crew.navigation.CrewNavKey
import com.example.company.navigation.CompanyNavKey
import com.example.ui.icon.SpaceXIcons
import com.example.rockets.R as rocketsR
import com.example.company.R as companyR
import com.example.crew.R as crewR

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconLabelId: Int,
    val navKey: NavKey
) {
    ROCKETS(
        selectedIcon = SpaceXIcons.RocketFilled,
        unselectedIcon = SpaceXIcons.RocketOutlined,
        iconLabelId = rocketsR.string.feature_rockets_label,
        navKey = RocketsNavKey
    ),
    CREW(
        selectedIcon = SpaceXIcons.CrewFilled,
        unselectedIcon = SpaceXIcons.CrewOutlined,
        iconLabelId = crewR.string.feature_crew_label,
        navKey = CrewNavKey
    ),
    COMPANY(
        selectedIcon = SpaceXIcons.CompanyFilled,
        unselectedIcon = SpaceXIcons.CompanyOutlined,
        iconLabelId = companyR.string.feature_company_label,
        navKey = CompanyNavKey
    )
}