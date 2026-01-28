package com.onestorecorp.android.nav3_sample_app.navigation.entries

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.DialogSceneStrategy.Companion.dialog
import com.onestorecorp.android.nav3_sample_app.ui.theme.PastelPurple
import kotlinx.serialization.Serializable

interface DialogNavKey : NavKey

@Serializable
data object DialogContentA : DialogNavKey

fun EntryProviderScope<NavKey>.dialogEntryBuilder() {
    entry<DialogNavKey>(
        key = DialogContentA,
        metadata = dialog(DialogProperties(windowTitle = "TEST")),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .size(300.dp)
                .clip(RoundedCornerShape(48.dp))
                .background(PastelPurple)
        ) {
            Text("Dialog Content A")
        }
    }
}