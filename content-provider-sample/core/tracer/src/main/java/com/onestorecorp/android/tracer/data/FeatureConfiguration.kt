package com.onestorecorp.android.tracer.data

import kotlinx.serialization.Serializable

@Serializable
data class FeatureConfiguration(
    val isLogEnabled: Boolean = false,
    val isCaptureEnabled: Boolean = false
)

