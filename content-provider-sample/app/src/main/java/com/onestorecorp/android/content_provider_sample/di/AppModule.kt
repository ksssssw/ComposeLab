package com.onestorecorp.android.content_provider_sample.di

import com.onestorecorp.android.content_provider_sample.data.ConfigurationRepository
import com.onestorecorp.android.content_provider_sample.ui.ConfigurationViewModel
import com.onestorecorp.android.tracer.data.AppConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repository - Singleton으로 생성하고 앱 시작 시 한 번만 초기화
    single {
        val repository = ConfigurationRepository(androidContext())
        // 앱 시작 시 즉시 configuration 로드
        val config = repository.getConfiguration()
        repository to config
    }
    
    // Configuration - 앱 전체에서 동일한 인스턴스 사용
    single<AppConfiguration> {
        val (_, config) = get<Pair<ConfigurationRepository, AppConfiguration>>()
        config
    }
    
    // ViewModel
    viewModel {
        ConfigurationViewModel(get())
    }
}