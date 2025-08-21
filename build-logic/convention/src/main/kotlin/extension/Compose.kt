package extension

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

typealias CommonExtensionAndroid = CommonExtension<*, *, *, *, *, *>

internal fun Project.configureAndroidCompose(commonExtension: CommonExtensionAndroid) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }
    }
    dependencies {
        val composeBom = libs.findLibrary("compose-bom").get()
        add("implementation", platform(composeBom))
        add("androidTestImplementation", platform(composeBom))

        add("implementation", libs.findLibrary("compose-ui-tooling-preview").get())
        add("debugImplementation", libs.findLibrary("compose-ui-tooling").get())

        add("debugImplementation", libs.findLibrary("compose-ui-test-manifest").get())
        add("androidTestImplementation", libs.findLibrary("compose-ui-test-junit4").get())
    }
}

fun Project.enableComposeViewBinding(commonExtension: CommonExtensionAndroid) {
    commonExtension.apply {
        buildFeatures {
            viewBinding = true
        }
    }
    dependencies {
        add("implementation", libs.findLibrary("compose-ui-viewbinding").get())
    }
}