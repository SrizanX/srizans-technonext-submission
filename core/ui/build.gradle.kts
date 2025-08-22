plugins {
    alias(libs.plugins.convention.android.library.compose)
}

android {
    namespace = "com.srizan.technonextcodingassessment.ui"
}

dependencies {
    api(projects.core.uiDesignSystem)
    implementation(projects.core.domainModel)
    implementation(libs.compose.material3)
}