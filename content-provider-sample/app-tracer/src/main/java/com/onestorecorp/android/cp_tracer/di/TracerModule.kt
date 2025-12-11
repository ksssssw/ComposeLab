package com.onestorecorp.android.cp_tracer.di

import com.onestorecorp.android.cp_tracer.ui.ConfigurationViewModel
import com.onestorecorp.android.tracer.storage.ConfigurationStorage
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val tracerModule = module {
    // Storage - Singleton
    single {
        ConfigurationStorage(androidContext())
    }
    
    // ViewModel
    viewModel {
        ConfigurationViewModel(get())
    }
}