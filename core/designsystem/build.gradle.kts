plugins {
    id ("haroo.android.library")
    id("haroo.android.library.compose")
}

android {
    namespace = "com.core.designsystem"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.bundles.coil)
}