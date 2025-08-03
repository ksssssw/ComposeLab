package com.ksssssw.spacex.data

import android.util.Log
import com.ksssssw.spacex.model.CrewPage
import com.ksssssw.spacex.network.SpaceXNetworkDataSource
import com.ksssssw.spacex.network.model.asExternalModel

internal class CrewRepositoryImpl(
    private val networkDataSource: SpaceXNetworkDataSource
) : CrewRepository {

    override suspend fun getCrew(page: Int, limit: Int): CrewPage {
        Log.d("CrewRepository", "getCrew called - page: $page, limit: $limit")
        val networkCrew = networkDataSource.getCrew(page, limit)
        Log.d("CrewRepository", "Network response - docs: ${networkCrew.docs.size}, page: ${networkCrew.page}, hasNext: ${networkCrew.hasNextPage}")
        val result = networkCrew.asExternalModel()
        Log.d("CrewRepository", "Mapped result - members: ${result.crewMembers.size}, page: ${result.page}, hasNext: ${result.hasNextPage}")
        return result
    }
}