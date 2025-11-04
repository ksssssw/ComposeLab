package com.onestorecorp.android.tracer.data

import kotlinx.serialization.Serializable

@Serializable
data class AppConfiguration(
    val serverConfiguration: ServerConfiguration = ServerConfiguration(),
    val featureConfiguration: FeatureConfiguration = FeatureConfiguration()
)

