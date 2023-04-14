plugins {
    id("haroo.android.library")
    id("haroo.android.library.compose")
}

android {
    namespace = "com.feature.home"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
}