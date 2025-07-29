package com.ksssssw.spacex.data

import com.ksssssw.spacex.network.model.NetworkCrew

interface CrewRepository {
    suspend fun getCrew(page: Int = 1, limit: Int = 10): Result<List<NetworkCrew>>
}