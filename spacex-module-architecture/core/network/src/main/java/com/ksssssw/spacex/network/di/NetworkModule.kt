package com.ksssssw.spacex.network.di

import com.ksssssw.spacex.network.KtorSpaceXNetworkDataSource
import com.ksssssw.spacex.network.SpaceXNetworkDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }

    single {
        HttpClient(Android) {
            defaultRequest {
                url("https://api.spacexdata.com/v4/")
            }

            install(ContentNegotiation) {
                json(get<Json>())
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 10_000
            }
        }
    }

    single<SpaceXNetworkDataSource> {
        KtorSpaceXNetworkDataSource(get())
    }
}