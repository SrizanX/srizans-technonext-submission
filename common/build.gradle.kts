plugins {
    alias(libs.plugins.convention.android.library.compose)
}

android {
    namespace = "com.srizan.technonextcodingassessment.common"
}

dependencies {
    implementation(libs.compose.material3)
}