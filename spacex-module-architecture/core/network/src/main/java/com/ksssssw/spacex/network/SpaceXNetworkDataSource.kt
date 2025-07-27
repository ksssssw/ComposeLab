package com.ksssssw.spacex.network

import com.ksssssw.spacex.network.model.NetworkRocket

interface SpaceXNetworkDataSource {
    suspend fun getRockets(): List<NetworkRocket>
}