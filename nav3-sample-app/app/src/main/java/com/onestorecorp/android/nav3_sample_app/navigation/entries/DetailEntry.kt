package com.onestorecorp.android.nav3_sample_app.navigation.entries

import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.onestorecorp.android.nav3_sample_app.navigation.animations.horizontalSlideTransition
import com.onestorecorp.android.nav3_sample_app.ui.contents.DetailContent
import com.onestorecorp.android.nav3_sample_app.ui.theme.PastelOrange
import com.onestorecorp.android.nav3_sample_app.ui.theme.PastelPink
import kotlinx.serialization.Serializable

interface DetailNavKey : NavKey

@Serializable
data object DetailContentA : DetailNavKey

@Serializable
data object DetailContentB : DetailNavKey

fun EntryProviderScope<NavKey>.detailEntryBuilder(
    backStack: NavBackStack<NavKey>,
) {
    entry<DetailNavKey>(
        key = DetailContentA,
        metadata = horizontalSlideTransition()
    ) {
        DetailContent(
            modifier = Modifier.background(PastelOrange),
            content = {
                Text("Detail Content A")

                Button(onClick = { backStack.add(DetailContentB) }) {
                    Text("Go to Detail B")
                }
            }
        )
    }

    entry<DetailNavKey>(
        key = DetailContentB,
        metadata = horizontalSlideTransition()
    ) {
        DetailContent(
            modifier = Modifier.background(PastelPink),
            content = {
                Text("Detail Content B")
            }
        )
    }
}