rootProject.name = "worklite"

include(":android")
include(":desktop")
include(":common")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    includeBuild("framework")
}