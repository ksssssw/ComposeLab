plugins {
    alias(libs.plugins.spacex.android.library)
}

android {
    namespace = "com.ksssssw.spacex.model"
}

dependencies {

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
}