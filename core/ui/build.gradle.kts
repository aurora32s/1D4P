plugins {
    id("haroo.android.library")
    id("haroo.android.library.compose")
}

android {
    namespace = "com.core.ui"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.bundles.coil)
    implementation(libs.paging.compose)
}