plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.cmp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "convention.android.application"
            implementationClass = "plugin.AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "convention.android.application.compose"
            implementationClass = "plugin.AndroidApplicationComposeConventionPlugin"
        }

        register("androidLibrary") {
            id = "convention.android.library"
            implementationClass = "plugin.AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "convention.android.library.compose"
            implementationClass = "plugin.AndroidLibraryComposeConventionPlugin"
        }

        register("androidFeature") {
            id = "convention.android.feature"
            implementationClass = "plugin.AndroidFeatureConventionPlugin"
        }

        register("androidNavigationCompose") {
            id = "convention.android.navigation.compose"
            implementationClass = "plugin.AndroidNavigationConventionPlugin"
        }

        register("multiplatformApplication") {
            id = "convention.multiplatform.application"
            implementationClass = "plugin.MultiplatformApplicationConventionPlugin"
        }

        register("multiplatformLibrary") {
            id = "convention.multiplatform.library"
            implementationClass = "plugin.MultiplatformLibraryConventionPlugin"
        }

        register("multiplatformCompose") {
            id = "convention.multiplatform.compose"
            implementationClass = "plugin.MultiplatformComposeConventionPlugin"
        }

        register("androidHilt") {
            id = "convention.android.hilt"
            implementationClass = "plugin.HiltConventionPlugin"
        }
        register("androidRoom") {
            id = "convention.android.room"
            implementationClass = "plugin.AndroidRoomConventionPlugin"
        }

        register("multiplatformKoin") {
            id = "convention.multiplatform.koin"
            implementationClass = "plugin.MultiplatformKoinConventionPlugin"
        }
    }
}

tasks.register("generateVersionCatalogExtensions") {
    val tomlFile = file("../../gradle/libs.versions.toml") // Adjust if needed
    val outputDir = file("$projectDir/src/main/kotlin/extension/generated")
    val versionCatalogClass = "VersionCatalogExtensions"
    val packageName = "extension.generated"

    doLast {
        if (!tomlFile.exists()) {
            throw IllegalStateException("libs.versions.toml not found!")
        }

        val lines = tomlFile.readLines()
        val dependencies = mutableListOf<String>()

        var insideLibraries = false
        for (line in lines) {
            println("Line: $line \n")
            when {
                line.startsWith("[libraries]") -> insideLibraries = true
                line.startsWith("[") -> insideLibraries = false // Exit section
                insideLibraries && "=" in line -> {
                    val key = line.split("=")[0].trim()
                    dependencies.add(key)
                }
            }
        }

        // Generate Kotlin file
        val generatedCode = buildString {
            appendLine("package $packageName")
            appendLine("")
            appendLine("import org.gradle.api.artifacts.VersionCatalog")
            appendLine("")

            dependencies.forEach { dep ->
                val propertyName = dep.replace("-", "_") // Convert to valid Kotlin property name
                appendLine("val VersionCatalog.$propertyName")
                appendLine("    get() = findLibrary(\"$dep\").get()")
                appendLine("")
            }
        }

        // Write to file
        val outputFile = File(outputDir, "$versionCatalogClass.kt")
        outputFile.parentFile.mkdirs()
        outputFile.writeText(generatedCode)

        println("Generated: ${outputFile.absolutePath}")
    }
}