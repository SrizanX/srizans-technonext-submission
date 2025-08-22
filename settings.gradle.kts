pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "SrizansTechnoNextSubmission"
include(":app")
include(":common")
// Core modules
include(":core:domain")
include(":core:domain-model")
include(":core:data")
include(":core:data-cache")
include(":core:data-network")
// Features modules
include(":feature:posts")
include(":feature:posts-favourites")
include(":feature:signin")
include(":feature:signup")
include(":core:ui-design-system")
include(":core:ui")
include(":core:data-datastore")
