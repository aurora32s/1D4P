plugins {
    id("haroo.android.library")
    id("haroo.android.room")
}

android {
    namespace = "com.core.database"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.room.paging)
}