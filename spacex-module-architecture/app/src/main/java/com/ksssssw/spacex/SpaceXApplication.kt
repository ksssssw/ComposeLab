package com.ksssssw.spacex

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.ksssssw.rockets.di.rocketsModule
import com.ksssssw.spacex.data.di.dataModule
import com.ksssssw.spacex.network.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class SpaceXApplication: Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@SpaceXApplication)
            modules(
                networkModule,
                dataModule,
                rocketsModule,
            )
        }
    }

    override fun newImageLoader(): ImageLoader {
        return GlobalContext.get().get<ImageLoader>()
    }
}