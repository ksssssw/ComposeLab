package com.ksssssw.spacex.network.di

import android.util.Log
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.ksssssw.spacex.network.KtorSpaceXNetworkDataSource
import com.ksssssw.spacex.network.SpaceXNetworkDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
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

            install(Logging) {
                logger = object: Logger {
                    override fun log(message: String) {
                        Log.v("HTTP Client", message)
                    }
                }
                level = LogLevel.BODY
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

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("User-Agent", "Mozilla/5.0")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    // Coil ImageLoader
    single<ImageLoader> {
        ImageLoader.Builder(androidApplication())
            .components {
                add(
                    OkHttpNetworkFetcherFactory(
                        callFactory = {
                            get<OkHttpClient>()
                        }
                    )
                )
            }
            .build()
    }
}