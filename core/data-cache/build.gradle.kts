plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.hilt)
    alias(libs.plugins.convention.android.room)
}

android {
    namespace = "com.srizan.technonextcodingassessment.cache"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}