plugins {
    alias(libs.plugins.convention.android.library.compose)
    alias(libs.plugins.convention.android.feature)
}

android {
    namespace = "com.srizan.technonextcodingassessment.posts"
}

dependencies {
    implementation(projects.common)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // Testing dependencies
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.test.mockito.core)
    testImplementation(libs.test.mockito.kotlin)
    testImplementation(libs.test.arch.core.testing)
}