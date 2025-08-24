plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.hilt)
    alias(libs.plugins.convention.android.room)
}

android {
    namespace = "com.srizan.technonextcodingassessment.cache"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.common)

    val room_version = "2.7.2"

    implementation("androidx.room:room-paging:${room_version}")

}