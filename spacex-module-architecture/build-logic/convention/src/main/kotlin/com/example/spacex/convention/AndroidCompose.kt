package com.example.spacex.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))
            
            // UI 관련 의존성들
            "implementation"(libs.findLibrary("androidx-ui-tooling-preview").get())
            "debugImplementation"(libs.findLibrary("androidx-ui-tooling").get())
            
            // 추가적인 Compose 핵심 의존성들
//            "implementation"(libs.findLibrary("androidx-ui").get())
//            "implementation"(libs.findLibrary("androidx-ui-graphics").get())
//            "implementation"(libs.findLibrary("androidx-material3").get())
//            "implementation"(libs.findLibrary("androidx-activity-compose").get())
        }
    }
}