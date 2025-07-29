package com.ksssssw.spacex.network

import com.ksssssw.spacex.network.model.CrewQueryOptions
import com.ksssssw.spacex.network.model.CrewQueryRequest
import com.ksssssw.spacex.network.model.NetworkCrew
import com.ksssssw.spacex.network.model.NetworkRocket
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class KtorSpaceXNetworkDataSource(
    private val httpClient: HttpClient
) : SpaceXNetworkDataSource {
    override suspend fun getRockets(): List<NetworkRocket> =
        httpClient.get("rockets").body()

    override suspend fun getCrew(page: Int, limit: Int): List<NetworkCrew> =
        httpClient.post("crew/query") {
            contentType(ContentType.Application.Json)
            setBody(
                CrewQueryRequest(
                    options = CrewQueryOptions(
                        page = page,
                        limit = limit
                    )
                )
            )
        }.body()
}