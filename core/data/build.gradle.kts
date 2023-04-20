plugins {
    id("haroo.android.library")
    id("haroo.android.hilt")
}

android {
    namespace = "com.core.data"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))
    implementation(project(":core:model"))

    implementation(libs.paging.runtime)
}