package com.ksssssw.spacex.network

import com.ksssssw.spacex.network.model.NetworkCrew
import com.ksssssw.spacex.network.model.NetworkRocket

interface SpaceXNetworkDataSource {
    suspend fun getRockets(): List<NetworkRocket>
    suspend fun getCrew(page: Int = 1, limit: Int = 10): List<NetworkCrew>
}