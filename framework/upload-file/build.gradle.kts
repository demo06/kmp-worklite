plugins {
    `java-gradle-plugin`
    kotlin("jvm")
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(gradleKotlinDsl())
    compileOnly(libs.gradlePlugin.android)
    compileOnly(libs.gradlePlugin.android.api)
    implementation(libs.okhttp)
    implementation(libs.coroutines.core)
    implementation(libs.larksuite.oapi)
}

gradlePlugin {
    plugins {
        create("upload-file") {
            id = "plugin.release.apk.upload"
            implementationClass = "UploadFilePlugin"
        }
    }
}
