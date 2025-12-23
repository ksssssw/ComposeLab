package com.ksssssw.wepray.di

import com.ksssssw.wepray.data.adb.AdbManager
import com.ksssssw.wepray.data.repository.DeviceRepositoryImpl
import com.ksssssw.wepray.domain.repository.DeviceRepository
import com.ksssssw.wepray.domain.usecase.GetDevicesUseCase
import com.ksssssw.wepray.ui.scene.devices.DevicesViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Koin 의존성 주입 모듈 정의
 */

/**
 * Data Layer 모듈
 */
val dataModule = module {
    // AdbManager - Singleton
    singleOf(::AdbManager)
    
    // DeviceRepository
    singleOf(::DeviceRepositoryImpl) bind DeviceRepository::class
}

/**
 * Domain Layer 모듈
 */
val domainModule = module {
    // Use Cases - Factory (매번 새 인스턴스)
    factory { GetDevicesUseCase(get()) }
}

/**
 * UI Layer 모듈
 */
val uiModule = module {
    // ViewModels
    viewModelOf(::DevicesViewModel)
}

/**
 * 모든 모듈을 포함하는 앱 모듈 리스트
 */
val appModules = listOf(
    dataModule,
    domainModule,
    uiModule
)
