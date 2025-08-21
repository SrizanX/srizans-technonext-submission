plugins {
    alias(libs.plugins.convention.android.application.compose)
    alias(libs.plugins.convention.android.feature)
}

android {
    namespace = "com.srizan.technonextcodingassessment"
    defaultConfig {
        applicationId = "com.srizan.technonextcodingassessment"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.core.data)
    with(projects.feature) {
        implementation(signin)
        implementation(signup)
        implementation(posts)
        implementation(favourites)
    }
}