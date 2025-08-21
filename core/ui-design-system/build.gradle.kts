plugins {
    alias(libs.plugins.convention.android.library.compose)
}

android {
    namespace = "com.srizan.technonextcodingassessment.designsystem"
}

dependencies {
    implementation(libs.compose.material3)
}