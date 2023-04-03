plugins {
    id ("haroo.android.library")
    id ("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.core.database"
}

dependencies {
    implementation(libs.bundles.room)
    implementation(libs.room.paging)
    kapt(libs.room.compiler)
}