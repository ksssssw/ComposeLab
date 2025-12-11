package com.onestorecorp.android.tracer.data

import kotlinx.serialization.Serializable

@Serializable
data class AppConfiguration(
    val serverConfiguration: ServerConfiguration? = null,
    val featureConfiguration: FeatureConfiguration? = null
)

