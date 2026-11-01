// build.gradle.kts – das "Rezept", nach dem Gradle das Projekt baut.
// Vergleich zu Java: Das ist wie eine pom.xml (Maven), nur in Kotlin geschrieben.

plugins {
    kotlin("jvm") version "2.0.21" // Kotlin für die JVM (ohne Android)
    application                    // erlaubt "gradlew run"
}

// Java und Kotlin müssen Bytecode für DIESELBE Java-Version erzeugen.
// Wir legen beide fest auf Java 17 – das läuft auf jedem JDK ab 17 (auch 21, 23 …)
// und ist dieselbe Version, die Android nutzt.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

group = "de.tripbudget"
version = "0.1.0"

repositories {
    mavenCentral() // hier lädt Gradle Bibliotheken herunter
}

dependencies {
    // JUnit 5 nur für Tests ("testImplementation" = nicht Teil der fertigen App)
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
}

application {
    // Kotlin erzeugt aus Main.kt die Klasse "MainKt"
    mainClass.set("de.tripbudget.MainKt")
}

// Sorgt dafür, dass "€" und Umlaute in der Konsole richtig erscheinen (v. a. unter Windows).
tasks.withType<JavaExec> {
    jvmArgs("-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8")
}

tasks.test {
    useJUnitPlatform()
    testLogging { events("passed", "failed") }
}
