package com.ksssssw.spacex.data

import com.ksssssw.spacex.network.KtorSpaceXNetworkDataSource
import com.ksssssw.spacex.network.SpaceXNetworkDataSource
import com.ksssssw.spacex.network.model.NetworkCrew

internal class CrewRepositoryImpl(
    private val networkDataSource: SpaceXNetworkDataSource
) : CrewRepository {
    override suspend fun getCrew(page: Int, limit: Int): Result<List<NetworkCrew>> {
        return try {
            val response = networkDataSource.getCrew(page, limit)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}