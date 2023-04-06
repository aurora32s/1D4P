plugins {
    id("haroo.android.library")
    id("haroo.android.hilt")
}

android {
    namespace = "com.core.data"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))

    implementation(libs.paging.runtime)
}