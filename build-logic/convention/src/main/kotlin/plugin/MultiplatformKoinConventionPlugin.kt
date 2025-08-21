package plugin

import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

class MultiplatformKoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            extensions.configure<KotlinMultiplatformExtension> {

                with(sourceSets) {
                    commonMain.dependencies {
                        implementation(libs.findLibrary("koin-core").get())
                        api(libs.findLibrary("koin-annotations").get())
                    }

                    androidMain.dependencies {
                        implementation(libs.findLibrary("koin-android").get())
                    }
                }


                // KSP Common sourceSet
                sourceSets.named("commonMain").configure {
                    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
                }
            }
            dependencies {
                add("kspCommonMainMetadata", libs.findLibrary("koin-ksp-compiler").get())
            }

            project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
                if (name != "kspCommonMainKotlinMetadata") {
                    dependsOn("kspCommonMainKotlinMetadata")
                }
            }
        }
    }
}