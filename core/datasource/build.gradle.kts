plugins {
    id("haroo.android.library")
    id("haroo.android.hilt")
}

android {
    namespace = "com.core.datasource"
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.paging.runtime)
}