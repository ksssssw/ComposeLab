package com.ksssssw.spacex.model

data class Crew(
    val id: String,
    val name: String,
    val agency: String,
    val image: String,
    val wikipedia: String,
    val launches: List<String>,
    val status: String,
)

data class CrewPage(
    val crewMembers: List<Crew>,
    val totalDocs: Int,
    val page: Int,
    val totalPages: Int,
    val hasNextPage: Boolean,
    val hasPrevPage: Boolean,
)