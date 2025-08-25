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
    implementation(libs.androidx.paging.common)

    // Testing dependencies
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.test.mockito.kotlin)
    testImplementation(libs.test.robolectric)
}