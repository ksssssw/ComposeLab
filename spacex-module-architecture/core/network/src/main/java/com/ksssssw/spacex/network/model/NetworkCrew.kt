package com.ksssssw.spacex.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

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

@Serializable
data class CrewQueryRequest(
    val query: Map<String, JsonElement> = emptyMap(),
    val options: CrewQueryOptions = CrewQueryOptions()
)

@Serializable
data class CrewQueryOptions(
    val page: Int = 1,
    val limit: Int = 10,
    val sort: Map<String, String> = mapOf("name" to "asc")
)