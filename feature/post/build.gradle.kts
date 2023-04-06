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
    implementation(project(":core:ui"))
    implementation(project(":core:model"))

    implementation(libs.paging.compose)
}