package com.onestorecorp.android.nav3_sample_app.navigation.entries

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.onestorecorp.android.nav3_sample_app.navigation.strategy.BottomNavScene.Companion.showNavBar
import com.onestorecorp.android.nav3_sample_app.navigation.strategy.BottomSheetSceneStrategy.Companion.bottomSheet
import com.onestorecorp.android.nav3_sample_app.ui.theme.PastelPurple
import kotlinx.serialization.Serializable

interface BottomSheetNavKey : NavKey

@Serializable
data object BottomSheetContentA : BottomSheetNavKey

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.bottomSheetEntryBuilder() {
    entry<BottomSheetNavKey>(
        key = BottomSheetContentA,
        metadata = bottomSheet()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(PastelPurple)
//                .fillMaxSize()
                .clip(RoundedCornerShape(48.dp))

        ) {
            Text("BottomSheet Content A")
        }
    }
}