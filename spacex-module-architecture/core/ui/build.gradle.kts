plugins {
    alias(libs.plugins.spacex.android.library)
    alias(libs.plugins.spacex.android.library.compose)
}

android {
    namespace = "com.ksssssw.core.ui"
}

dependencies {
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
//    api(libs.androidx.compose.material3.navigationSuite)
    api(libs.androidx.compose.ui.util)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}