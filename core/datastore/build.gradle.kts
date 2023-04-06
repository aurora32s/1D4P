plugins {
    id("haroo.android.library")
    id("haroo.android.hilt")
}

android {
    namespace = "com.example.datastore"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}