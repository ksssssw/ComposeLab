package com.ksssssw.spacex.data

import com.ksssssw.spacex.model.Dimension
import com.ksssssw.spacex.model.Mass
import com.ksssssw.spacex.model.Rocket
import com.ksssssw.spacex.network.SpaceXNetworkDataSource
import com.ksssssw.spacex.network.model.NetworkRocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class RocketRepositoryImpl(
    private val networkDataSource: SpaceXNetworkDataSource
) : RocketRepository {

    override suspend fun getRockets(): Flow<List<Rocket>> =
        flowOf(Unit)
            .map { networkDataSource.getRockets() }
            .map { networkRockets ->
                networkRockets.map(NetworkRocket::asExternalModel)
            }
            .flowOn(Dispatchers.IO)
}

fun NetworkRocket.asExternalModel(): Rocket = Rocket(
    id = id,
    name = name,
    description = description,
    active = active,
    stages = stages,
    boosters = boosters,
    costPerLaunch = costPerLaunch,
    successRatePct = successRatePct,
    firstFlight = firstFlight,
    country = country,
    company = company,
    height = Dimension(height.meters, height.feet),
    diameter = Dimension(diameter.meters, diameter.feet),
    mass = Mass(mass.kg, mass.lb),
    flickrImages = flickrImages,
)