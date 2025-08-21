plugins {
    alias(libs.plugins.convention.android.library)
}

android {
    namespace = "com.srizan.technonextcodingassessment.domain"
}

dependencies {
    api(projects.core.domainModel)
    implementation(libs.kotlinx.coroutines.core)
}