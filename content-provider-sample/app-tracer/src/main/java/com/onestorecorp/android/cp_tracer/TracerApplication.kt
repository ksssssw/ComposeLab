package com.onestorecorp.android.cp_tracer

import android.app.Application
import com.onestorecorp.android.cp_tracer.di.tracerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TracerApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@TracerApplication)
            modules(tracerModule)
        }
    }
}

