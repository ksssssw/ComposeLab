package com.ksssssw.wepray.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import com.ksssssw.wepray.WePrayAppViewModel
import com.ksssssw.wepray.data.aapt.AaptManager
import com.ksssssw.wepray.data.adb.AdbManager
import com.ksssssw.wepray.data.local.WePrayDatabase
import com.ksssssw.wepray.data.scrcpy.ScrcpyManager
import com.ksssssw.wepray.data.repository.DeviceRepositoryImpl
import com.ksssssw.wepray.data.repository.SettingsRepositoryImpl
import com.ksssssw.wepray.domain.repository.DeviceRepository
import com.ksssssw.wepray.domain.repository.SettingsRepository
import com.ksssssw.wepray.domain.usecase.GetDeviceStorageUseCase
import com.ksssssw.wepray.domain.usecase.InstallApkUseCase
import com.ksssssw.wepray.domain.usecase.MirrorDeviceUseCase
import com.ksssssw.wepray.domain.usecase.RefreshDevicesUseCase
import com.ksssssw.wepray.domain.usecase.SelectApkFolderUseCase
import com.ksssssw.wepray.domain.usecase.SelectScreenshotPathUseCase
import com.ksssssw.wepray.domain.usecase.TakeScreenshotUseCase
import com.ksssssw.wepray.ui.scene.devices.DevicesViewModel
import com.ksssssw.wepray.ui.scene.installer.InstallerViewModel
import com.ksssssw.wepray.ui.scene.settings.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File

/**
 * Koin 의존성 주입 모듈 정의
 */

/**
 * Database 마이그레이션: v1 -> v2
 * apkFolderPath와 lastSelectedTab 컬럼 추가
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "ALTER TABLE app_settings ADD COLUMN apkFolderPath TEXT DEFAULT NULL"
        )
        connection.execSQL(
            "ALTER TABLE app_settings ADD COLUMN lastSelectedTab TEXT DEFAULT NULL"
        )
    }
}

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
            .addMigrations(MIGRATION_1_2)
            .build()
    }
    
    // DAOs
    single { get<WePrayDatabase>().appSettingsDao() }
    
    // AdbManager - Singleton
    singleOf(::AdbManager)
    
    // AaptManager - Singleton
    singleOf(::AaptManager)
    
    // ScrcpyManager - Singleton
    singleOf(::ScrcpyManager)
    
    // Repositories
    single<DeviceRepository> {
        DeviceRepositoryImpl(
            adbManager = get(),
            getDeviceStorageUseCase = get()
        )
    }
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
    factory { GetDeviceStorageUseCase(get()) }
    factory { MirrorDeviceUseCase(get()) }
}

/**
 * UI Layer 모듈
 */
val uiModule = module {
    // ViewModels - 명시적 의존성 주입 (Release 빌드 호환성)
    
    // 앱 전역 ViewModel
    viewModel {
        WePrayAppViewModel(
            deviceRepository = get(),
            settingsRepository = get(),
            refreshDevicesUseCase = get()
        )
    }
    
    // Devices 화면 ViewModel
    viewModel {
        DevicesViewModel(
            deviceRepository = get(),
            takeScreenshotUseCase = get(),
            mirrorDeviceUseCase = get()
        )
    }

    // Settings 화면 ViewModel
    viewModel {
        SettingsViewModel(
            settingsRepository = get(),
            selectScreenshotPathUseCase = get()
        )
    }
    
    // Installer 화면 ViewModel
    viewModel {
        InstallerViewModel(
            deviceRepository = get(),
            settingsRepository = get(),
            aaptManager = get(),
            installApkUseCase = get(),
            selectApkFolderUseCase = get()
        )
    }
}

/**
 * 모든 모듈을 포함하는 앱 모듈 리스트
 */
val appModules = listOf(
    dataModule,
    domainModule,
    uiModule
)
