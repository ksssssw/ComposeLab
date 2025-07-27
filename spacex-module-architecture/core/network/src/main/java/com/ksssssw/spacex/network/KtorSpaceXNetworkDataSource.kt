package com.ksssssw.spacex.network

import com.ksssssw.spacex.network.model.NetworkRocket
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class KtorSpaceXNetworkDataSource(
    private val httpClient: HttpClient
) : SpaceXNetworkDataSource {
    override suspend fun getRockets(): List<NetworkRocket> =
        httpClient.get("rockets").body()
}