package com.onestorecorp.android.nav3_sample_app.navigation.entries

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.onestorecorp.android.nav3_sample_app.navigation.strategy.BottomNavScene.Companion.showNavBar
import com.onestorecorp.android.nav3_sample_app.ui.contents.TopContent
import com.onestorecorp.android.nav3_sample_app.ui.theme.PastelBlue
import com.onestorecorp.android.nav3_sample_app.ui.theme.PastelGreen
import com.onestorecorp.android.nav3_sample_app.ui.theme.PastelRed
import kotlinx.serialization.Serializable

interface TopLevelNavKey : NavKey

@Serializable
data object TopLevelContentA : TopLevelNavKey

@Serializable
data object TopLevelContentB : TopLevelNavKey

@Serializable
data object TopLevelContentC : TopLevelNavKey

fun EntryProviderScope<NavKey>.topLevelEntryBuilder(
    backStack: NavBackStack<NavKey>,
) {
    entry<TopLevelNavKey>(
        key = TopLevelContentA,
        metadata = showNavBar()
    ) {
        TopContent(
            modifier = Modifier.background(PastelRed),
            content = {
                Text("Top Content A")

                Button(onClick = { backStack.add(DetailContentA) }) {
                    Text("Go to Detail A")
                }
            }
        )
    }

    entry<TopLevelNavKey>(
        key = TopLevelContentB,
        metadata = showNavBar()
    ) {
        TopContent(
            modifier = Modifier.background(PastelGreen),
            content = {
                Text("Top Content B")

                Button(onClick = { backStack.add(DialogContentA) }) {
                    Text("Go to Dialog A")
                }

                Button(onClick = { backStack.add(BottomSheetContentA) }) {
                    Text("Go to BottomSheet A")
                }
            }
        )
    }

    entry<TopLevelNavKey>(
        key = TopLevelContentC,
        metadata = showNavBar()
    ) {
        TopContent(
            modifier = Modifier.background(PastelBlue),
            content = {
                Text("Top Content C")
            }
        )
    }
}

enum class TopLevelDestination(
    val label: String,
    val icon: ImageVector,
    val route: TopLevelNavKey,
) {
    Atab(
        label = "Tab A",
        icon = Icons.Outlined.Home,
        route = TopLevelContentA
    ),
    Btab(
        label = "Tab B",
        icon = Icons.Outlined.Face,
        route = TopLevelContentB
    ),
    Ctab(
        label = "Tab C",
        icon = Icons.Outlined.Favorite,
        route = TopLevelContentC
    );

    companion object {
        val ROOT_VIEW = Atab.route
    }
}