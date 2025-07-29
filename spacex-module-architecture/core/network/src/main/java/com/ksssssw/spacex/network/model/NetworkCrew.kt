package com.ksssssw.spacex.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCrew(
    val docs: List<NetworkCrewMember>,
    val totalDocs: Int,
    val offset: Int,
    val limit: Int,
    val totalPages: Int,
    val page: Int,
    val pagingCounter: Int,
    val hasPrevPage: Boolean,
    val hasNextPage: Boolean,
    val prevPage: Int?,
    val nextPage: Int?
)

@Serializable
data class NetworkCrewMember(
    val id: String,
    val name: String,
    val agency: String,
    val image: String,
    val wikipedia: String,
    val launches: List<String>,
    val status: String
)