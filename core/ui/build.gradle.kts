plugins {
    alias(libs.plugins.convention.android.library.compose)
}

android {
    namespace = "com.srizan.technonextcodingassessment.ui"
}

dependencies {
    api(projects.core.uiDesignSystem)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}