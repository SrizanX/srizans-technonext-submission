plugins {
    alias(libs.plugins.convention.android.library)
}

android {
    namespace = "com.srizan.technonextcodingassessment.domain"
}

dependencies {
    api(projects.core.domainModel)
    implementation(libs.androidx.datastore.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.common)
}