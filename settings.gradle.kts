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
// Core modules
include(":core:data")
include(":core:data-cache")
include(":core:data-datastore")
include(":core:data-network")
include(":core:domain")
include(":core:domain-model")
include(":core:ui")
include(":core:ui-design-system")
// Features modules
include(":feature:signin")
include(":feature:signup")
include(":feature:posts")
include(":feature:posts-favourites")
include(":feature:profile")
