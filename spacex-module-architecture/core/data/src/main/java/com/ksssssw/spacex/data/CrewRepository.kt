package com.ksssssw.spacex.data

import com.ksssssw.spacex.model.CrewPage

interface CrewRepository {
    suspend fun getCrew(page: Int = 1, limit: Int = 10): CrewPage
}