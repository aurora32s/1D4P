plugins {
    id("haroo.android.library")
    id("haroo.android.library.compose")
}

android {
    namespace = "com.core.ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))

    implementation(libs.paging.compose)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.androidx.constraintlayout.compose)
}