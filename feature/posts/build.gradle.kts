plugins {
    alias(libs.plugins.convention.android.library.compose)
    alias(libs.plugins.convention.android.feature)
}

android {
    namespace = "com.srizan.technonextcodingassessment.posts"
}

dependencies {
    implementation(projects.common)
}