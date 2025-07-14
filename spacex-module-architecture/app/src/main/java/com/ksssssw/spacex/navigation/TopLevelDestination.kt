package com.ksssssw.spacex.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.ksssssw.company.navigation.CompanyNavKey
import com.ksssssw.crew.navigation.CrewNavKey
import com.ksssssw.rockets.navigation.RocketsNavKey
import com.ksssssw.ui.icon.SpaceXIcons
import com.ksssssw.feature.company.R as companyR
import com.ksssssw.feature.crew.R as crewR
import com.ksssssw.feature.rockets.R as rocketsR

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