package com.onestorecorp.android.tracer.data

import kotlinx.serialization.Serializable

@Serializable
data class ServerConfiguration(
    val baseUrl: String = "",
    val imageBaseUrl: String = ""
)

