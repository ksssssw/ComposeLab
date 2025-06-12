plugins {
    alias(libs.plugins.spacex.android.feature)
    alias(libs.plugins.spacex.android.library.compose)
}

android {
    namespace = "com.example.crew"
}

dependencies {

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}