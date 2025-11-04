package com.ksssssw.spacex.model

data class Rocket(
    val id: String,
    val name: String,
    val description: String,
    val active: Boolean,
    val stages: Int,
    val boosters: Int,
    val costPerLaunch: Long,
    val successRatePct: Int,
    val firstFlight: String,
    val country: String,
    val company: String,
    val height: Dimension,
    val diameter: Dimension,
    val mass: Mass,
    val flickrImages: List<String>,
)

data class Dimension(
    val meters: Double,
    val feet: Double,
)

data class Mass(
    val kg: Long,
    val lb: Long,
)