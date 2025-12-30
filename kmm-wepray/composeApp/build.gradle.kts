import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons)
            implementation(libs.compose.ui)
            implementation(libs.compose.components)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.viewmodel.nav3)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.nav3.ui)
            implementation(libs.androidx.material3.adaptive)
            implementation(libs.androidx.material3.adaptive.nav3)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

// Room 설정
room {
    schemaDirectory("$projectDir/schemas")
}

// KSP 설정
dependencies {
    add("kspCommonMainMetadata", libs.room.compiler)
}

compose.desktop {
    application {
        mainClass = "com.ksssssw.wepray.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.ksssssw.wepray"
            packageVersion = "1.0.0"

            val iconsRoot = project.file("icons")
            macOS {
                bundleID = "com.ksssssw.wepray"
                iconFile.set(iconsRoot.resolve("icon-mac.icns"))
            }
        }
    }
}