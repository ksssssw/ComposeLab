package com.ksssssw.spacex.data

import com.ksssssw.spacex.model.Rocket
import kotlinx.coroutines.flow.Flow

interface RocketRepository {
    suspend fun getRockets(): Flow<List<Rocket>>
}