package com.ksssssw.spacex

import android.app.Application
import com.ksssssw.rockets.di.rocketsModule
import com.ksssssw.spacex.data.di.dataModule
import com.ksssssw.spacex.network.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SpaceXApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@SpaceXApplication)
            modules(
                // TODO: 모듈 추가
                networkModule,
                dataModule,
                rocketsModule
            )
        }
    }
}