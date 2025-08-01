plugins {
    alias(libs.plugins.spacex.android.library)
    alias(libs.plugins.spacex.koin)
    id("kotlinx-serialization")
}

android {
    namespace = "com.ksssssw.spacex.network"
}

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.serialization)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.network.ktor)
    implementation(libs.coil.kt.compose)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}