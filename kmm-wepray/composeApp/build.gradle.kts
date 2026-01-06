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
    add("kspJvm", libs.room.compiler)
}

compose.desktop {
    application {
        mainClass = "com.ksssssw.wepray.MainKt"
        
        // JVM 메모리 최적화 설정
        jvmArgs += listOf(
            "-Xms128m",                      // 초기 힙 크기 128MB
            "-Xmx256m",                      // 최대 힙 크기 512MB
            "-XX:+UseG1GC",                  // G1 가비지 컬렉터 사용
            "-XX:MaxGCPauseMillis=200",      // GC 일시정지 최대 200ms
            "-XX:+UseStringDeduplication",   // 문자열 중복 제거
            "-Dsun.java2d.opengl=true",      // OpenGL 하드웨어 가속 활성화
            "-Dfile.encoding=UTF-8"          // 파일 인코딩 UTF-8 설정 (한글 경로 지원)
        )

        nativeDistributions {
            val appName = "WePray"
            val appPackageName = "com.ksssssw.wepray"
            val appVersion = "1.0.0"

            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = appName
            packageVersion = appVersion

            val iconsRoot = project.file("icons")
            macOS {
                bundleID = appPackageName
                packageName = appName
                packageVersion = appVersion
                iconFile.set(iconsRoot.resolve("icon-mac.icns"))
            }
            
            // ProGuard 비활성화 (Compose Desktop은 필요 없음)
            buildTypes.release.proguard {
                isEnabled.set(false)
            }
        }
    }
}