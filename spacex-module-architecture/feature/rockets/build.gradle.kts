plugins {
    alias(libs.plugins.spacex.android.feature)
    alias(libs.plugins.spacex.android.library.compose)
}

android {
    namespace = "com.example.spacex.feature.rockets"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}