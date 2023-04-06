plugins {
    id("haroo.android.library")
    id("haroo.android.library.compose")
    id("haroo.android.hilt")
}

android {
    namespace = "com.feature.post"
}

dependencies {
    implementation(project(":core:domain"))

    implementation(libs.paging.compose)
}