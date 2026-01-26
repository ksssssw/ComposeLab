# Koin - 의존성 주입 프레임워크
-keep class org.koin.** { *; }
-keep interface org.koin.** { *; }
-keepclassmembers class * {
    public <init>(...);
}

# Koin DSL - viewModelOf, singleOf 등에 필요
-keepclassmembers class kotlin.Metadata {
    *;
}

# ViewModel 클래스들 - Koin이 생성자를 찾을 수 있도록
-keep class com.ksssssw.wepray.WePrayAppViewModel { *; }
-keep class com.ksssssw.wepray.ui.scene.devices.DevicesViewModel { *; }
-keep class com.ksssssw.wepray.ui.scene.settings.SettingsViewModel { *; }
-keep class com.ksssssw.wepray.ui.scene.installer.InstallerViewModel { *; }

# ViewModel 생성자 파라미터 유지
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Repository 인터페이스 및 구현체
-keep interface com.ksssssw.wepray.domain.repository.** { *; }
-keep class com.ksssssw.wepray.data.repository.** { *; }

# UseCase 클래스들
-keep class com.ksssssw.wepray.domain.usecase.** { *; }

# Manager 클래스들 (AdbManager, AaptManager, ScrcpyManager)
-keep class com.ksssssw.wepray.data.adb.AdbManager { *; }
-keep class com.ksssssw.wepray.data.aapt.AaptManager { *; }
-keep class com.ksssssw.wepray.data.scrcpy.ScrcpyManager { *; }

# Kotlin 리플렉션
-dontwarn kotlin.reflect.**
-keep class kotlin.reflect.** { *; }
-keep class kotlin.Metadata { *; }

# Kotlinx Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Room Database
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.ksssssw.wepray.**$$serializer { *; }
-keepclassmembers class com.ksssssw.wepray.** {
    *** Companion;
}
-keepclasseswithmembers class com.ksssssw.wepray.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# 일반적인 최적화 비활성화 (디버깅용)
-dontoptimize
-dontobfuscate

