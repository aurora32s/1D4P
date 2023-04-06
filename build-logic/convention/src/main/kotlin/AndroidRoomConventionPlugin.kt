import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            pluginManager.apply("org.jetbrains.kotlin.kapt")

            extensions.configure<LibraryExtension> {
                dependencies {
                    "implementation"(libs.findBundle("room").get())
                    "kapt"(libs.findLibrary("room.compiler").get())
                }
            }
        }
    }
}