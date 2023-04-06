plugins {
    id ("haroo.android.library")
    id("haroo.android.hilt")
}

android {
    namespace = "com.core.domain"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))

    implementation(libs.paging.runtime)
}