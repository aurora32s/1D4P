plugins {
    id ("haroo.android.library")
    id("haroo.android.library.compose")
}

android {
    namespace = "com.feature.monthly"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
}