import com.example.spacex.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "spacex.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

//            extensions.configure<LibraryExtension> {
//
//            }

            dependencies {
                "implementation"(project(":core:ui"))

                "implementation"(libs.findLibrary("androidx.navigation3.ui").get())
                "implementation"(libs.findLibrary("androidx.navigation3.runtime").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.navigation3").get())
                "implementation"(libs.findLibrary("kotlinx.serialization.core").get())
            }
        }
    }
}