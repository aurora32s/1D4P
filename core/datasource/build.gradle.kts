plugins {
    id("haroo.android.library")
    id("haroo.android.hilt")
}

android {
    namespace = "com.core.datasource"
}

dependencies {
    implementation(project(":core:database"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.paging.runtime)
}