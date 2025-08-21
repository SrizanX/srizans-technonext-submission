package extension

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework

// Define an extension to hold iOS framework configuration
abstract class IOSFrameworkExtension {
    var baseName: String = "ComposeApp"
    var isStatic: Boolean = true
}

fun KotlinMultiplatformExtension.iosTargetBinariesFramework(
    configure: Framework.() -> Unit
) {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework(configure = configure)
    }
}