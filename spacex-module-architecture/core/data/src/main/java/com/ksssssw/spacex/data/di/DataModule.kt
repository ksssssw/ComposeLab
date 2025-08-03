package com.ksssssw.spacex.data.di

import com.ksssssw.spacex.data.CrewRepository
import com.ksssssw.spacex.data.CrewRepositoryImpl
import com.ksssssw.spacex.data.RocketRepository
import com.ksssssw.spacex.data.RocketRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<RocketRepository> {
        RocketRepositoryImpl(
            networkDataSource = get()
        )
    }
    
    single<CrewRepository> {
        CrewRepositoryImpl(
            networkDataSource = get()
        )
    }
}