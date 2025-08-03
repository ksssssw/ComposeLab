plugins {
    alias(libs.plugins.spacex.android.feature)
    alias(libs.plugins.spacex.android.library.compose)
    alias(libs.plugins.spacex.koin)
}

android {
    namespace = "com.ksssssw.feature.crew"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.data)

    implementation(libs.coil.kt.compose)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}