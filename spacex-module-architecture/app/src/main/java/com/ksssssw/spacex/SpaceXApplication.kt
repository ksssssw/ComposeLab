package com.ksssssw.spacex

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.ksssssw.rockets.di.rocketsModule
import com.ksssssw.spacex.data.di.dataModule
import com.ksssssw.spacex.network.di.networkModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SpaceXApplication: Application(), SingletonImageLoader.Factory {

    private val imageLoader: ImageLoader by inject()
    
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

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return imageLoader
    }
}