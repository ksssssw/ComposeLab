package com.onestorecorp.android.tracer.data

import kotlinx.serialization.Serializable

@Serializable
enum class ServerType {
    PROD, QA, CUSTOM
}

enum class UrlTarget {
    BASE, IMAGE
}

@Serializable
data class UrlConfiguration(
    val type: ServerType = ServerType.PROD,
    val url: String = ""
)

@Serializable
data class ServerConfiguration(
    val base: UrlConfiguration = UrlConfiguration(
        type = ServerType.PROD,
        url = ServerUrls.BASE_PROD
    ),
    val image: UrlConfiguration = UrlConfiguration(
        type = ServerType.PROD,
        url = ServerUrls.IMAGE_PROD
    )
)

object ServerUrls {
    const val BASE_PROD = "https://api.prod.example.com"
    const val IMAGE_PROD = "https://images.prod.example.com"
}