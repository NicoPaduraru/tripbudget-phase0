// settings.gradle.kts – sagt Gradle, wie das Projekt heißt und wo es Plugins findet.
rootProject.name = "tripbudget-phase0"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
