plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")

    id("plugin.release.apk.upload")
}

group = "funny.buildapp"
version = "1.0-SNAPSHOT"

android {
    compileSdk = 34
    defaultConfig {
        namespace = "funny.buildapp.worklite"
        applicationId = "funny.buildapp.worklite"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    flavorDimensions("base")
    productFlavors {
        create("staging")//test
        create("dev")
        create("press")
        create("prod")
    }
}

dependencies {
    implementation(project(":common"))
    implementation(libs.androidx.activity.compose)
}

tasks.register("BuildAndRun") {
    doFirst {
        exec {
            workingDir(projectDir.parentFile)
            commandLine("./gradlew", "android:build")
            commandLine("./gradlew", "android:installDebug")
        }
    }
}