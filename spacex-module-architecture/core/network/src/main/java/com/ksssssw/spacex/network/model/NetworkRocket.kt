package com.ksssssw.spacex.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRocket(
    val id: String,
    val name: String,
    val description: String,
    val active: Boolean,
    val stages: Int,
    val boosters: Int,
    @SerialName("cost_per_launch")
    val costPerLaunch: Long,
    @SerialName("success_rate_pct")
    val successRatePct: Int,
    @SerialName("first_flight")
    val firstFlight: String,
    val country: String,
    val company: String,
    val height: NetworkDimension,
    val diameter: NetworkDimension,
    val mass: NetworkMass,
    @SerialName("flickr_images")
    val flickrImages: List<String>,
)

@Serializable
data class NetworkDimension(
    val meters: Double,
    val feet: Double,
)

@Serializable
data class NetworkMass(
    val kg: Long,
    val lb: Long,
)