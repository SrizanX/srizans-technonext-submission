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

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.data)
    with(projects.feature) {
        implementation(signin)
        implementation(signup)
        implementation(posts)
        implementation(postsFavourites)
        implementation(profile)
    }
    implementation(libs.timber)

    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.4.0-alpha05")

}