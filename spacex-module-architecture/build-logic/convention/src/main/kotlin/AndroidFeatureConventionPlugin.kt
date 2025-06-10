import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "spacex.android.library")

//            extensions.configure<LibraryExtension> {
//
//            }

            dependencies {
                // TODO: ui, designSystem 의존성 추가
                "implementation"(project(":core:ui"))
            }
        }
    }
}