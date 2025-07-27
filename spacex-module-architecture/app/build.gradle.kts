plugins {
    alias(libs.plugins.spacex.android.application)
    alias(libs.plugins.spacex.android.application.compose)
    alias(libs.plugins.spacex.koin)
    alias(libs.plugins.jetbrains.kotlin.serialization) // Kotlin serialization plugin for Nav3
}

android {
    defaultConfig {
        applicationId = "com.ksssssw.spacex"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    namespace = "com.ksssssw.spacex"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.network)
    implementation(projects.core.data)

    implementation(projects.feature.rockets)
    implementation(projects.feature.crew)
    implementation(projects.feature.company)

    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization.core)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    // 테스트 의존성들
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
}