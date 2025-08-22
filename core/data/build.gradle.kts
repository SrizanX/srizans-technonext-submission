plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.hilt)
}

android {
    namespace = "com.srizan.technonextcodingassessment.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.dataCache)
    implementation(projects.core.dataDatastore)
    implementation(projects.core.dataNetwork)
    implementation(libs.kotlinx.coroutines.core)
}