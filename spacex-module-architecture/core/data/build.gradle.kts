plugins {
    alias(libs.plugins.spacex.android.library)
    alias(libs.plugins.spacex.koin)
    id("kotlinx-serialization")
}

android {
    namespace = "com.ksssssw.spacex.data"
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)
    api(projects.core.network)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}