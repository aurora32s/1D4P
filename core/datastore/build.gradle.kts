plugins {
    id("haroo.android.library")
    id("haroo.android.hilt")
}

android {
    namespace = "com.example.datastore"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.kotlinx.coroutines.core)
}