plugins {
    id("haroo.android.library")
    id("haroo.android.room")
}

android {
    namespace = "com.core.database"
}

dependencies {
    implementation(libs.room.paging)
}