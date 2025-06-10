plugins {
    alias(libs.plugins.spacex.android.feature)
    alias(libs.plugins.spacex.android.library.compose)
}

android {
    namespace = "com.example.spacex.feature.rockets"
}

dependencies {

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}