package extension

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun BaseAppModuleExtension.enableCoreLibraryDesugaring(
    project: Project, enableMultiDex: Boolean = true
) {
    with(project) {
        defaultConfig {
            multiDexEnabled = enableMultiDex
        }
        compileOptions {
            isCoreLibraryDesugaringEnabled = true
        }
        dependencies {
            add("coreLibraryDesugaring", libs.findLibrary("desugar-jdk-libs").get())
        }
    }
}

fun LibraryExtension.enableCoreLibraryDesugaring(
    project: Project, enableMultiDex: Boolean = true
) {
    with(project) {
        defaultConfig {
            multiDexEnabled = enableMultiDex
        }
        compileOptions {
            isCoreLibraryDesugaringEnabled = true
        }
        dependencies {
            add("coreLibraryDesugaring", libs.findLibrary("desugar-jdk-libs").get())
        }
    }
}