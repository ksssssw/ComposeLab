plugins {
    alias(libs.plugins.spacex.android.library)
    alias(libs.plugins.spacex.android.library.compose)
}

android {
    namespace = "com.example.ui"
}

dependencies {
    api(libs.androidx.compose.material3)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}