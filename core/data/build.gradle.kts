plugins {
    id ("haroo.android.library")
}

android {
    namespace = "com.core.data"
}

dependencies {
    implementation(libs.paging.runtime)
}