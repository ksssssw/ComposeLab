package com.ksssssw.wepray.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ksssssw.wepray.WePrayAppViewModel
import com.ksssssw.wepray.data.adb.AdbManager
import com.ksssssw.wepray.data.local.WePrayDatabase
import com.ksssssw.wepray.data.repository.DeviceRepositoryImpl
import com.ksssssw.wepray.data.repository.SettingsRepositoryImpl
import com.ksssssw.wepray.domain.repository.DeviceRepository
import com.ksssssw.wepray.domain.repository.SettingsRepository
import com.ksssssw.wepray.domain.usecase.InstallApkUseCase
import com.ksssssw.wepray.domain.usecase.RefreshDevicesUseCase
import com.ksssssw.wepray.domain.usecase.SelectApkFolderUseCase
import com.ksssssw.wepray.domain.usecase.SelectScreenshotPathUseCase
import com.ksssssw.wepray.domain.usecase.TakeScreenshotUseCase
import com.ksssssw.wepray.ui.scene.devices.DevicesViewModel
import com.ksssssw.wepray.ui.scene.installer.InstallerViewModel
import com.ksssssw.wepray.ui.scene.settings.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File

/**
 * Koin 의존성 주입 모듈 정의
 */

/**
 * Data Layer 모듈
 */
val dataModule = module {
    // Room Database - Singleton
    single {
        val dbFile = File(System.getProperty("user.home"), ".wepray/database/wepray.db")
        dbFile.parentFile?.mkdirs()
        
        Room.databaseBuilder<WePrayDatabase>(
            name = dbFile.absolutePath,
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    
    // DAOs
    single { get<WePrayDatabase>().appSettingsDao() }
    
    // AdbManager - Singleton
    singleOf(::AdbManager)
    
    // Repositories
    singleOf(::DeviceRepositoryImpl) bind DeviceRepository::class
    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class
}

/**
 * Domain Layer 모듈
 */
val domainModule = module {
    // Use Cases - Factory (매번 새 인스턴스)
    factory { RefreshDevicesUseCase(get()) }
    factory { SelectScreenshotPathUseCase(get()) }
    factory { TakeScreenshotUseCase(get(), get(), get()) }
    factory { SelectApkFolderUseCase() }
    factory { InstallApkUseCase(get()) }
}

/**
 * UI Layer 모듈
 */
val uiModule = module {
    // ViewModels
    // 앱 전역 ViewModel - Repository 주입
    viewModelOf(::WePrayAppViewModel)
    
    // Devices 화면 ViewModel - Repository 주입 (ViewModel 간 의존성 없음)
    viewModelOf(::DevicesViewModel)

    // Settings 화면 ViewModel
    viewModelOf(::SettingsViewModel)
    
    // Installer 화면 ViewModel
    viewModelOf(::InstallerViewModel)
}

/**
 * 모든 모듈을 포함하는 앱 모듈 리스트
 */
val appModules = listOf(
    dataModule,
    domainModule,
    uiModule
)
