plugins {
    alias(libs.plugins.spacex.android.feature)
    alias(libs.plugins.spacex.android.library.compose)
}

android {
    namespace = "com.ksssssw.feature.company"
}

dependencies {

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}