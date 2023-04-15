plugins {
    id("haroo.android.library")
    id("haroo.android.library.compose")
    id("haroo.android.hilt")
}

android {
    namespace = "com.feature.home"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))

    implementation(libs.paging.compose)
}