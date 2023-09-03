import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.internal.tasks.testing.DecoratingTestDescriptor

plugins {
    kotlin("jvm") version "1.6.21"
}

group = "org.afterburner"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<Test> {
    useJUnitPlatform()

    afterSuite(KotlinClosure2<DecoratingTestDescriptor, TestResult, Unit>({ desc, result ->
        if (desc.parent == null) {
            val output = "Test Results: ${result.resultType} (${result.testCount} tests, " +
                "${result.successfulTestCount} successes, " +
                "${result.failedTestCount} failures, " +
                "${result.skippedTestCount} skipped)"
            val startItem = "|  "
            val endItem = "  |"
            val repeatLength = startItem.length + output.length + endItem.length
            println(
                '\n' + ("-".repeat(repeatLength)) + '\n' +
                    startItem + output + endItem + '\n' + ("-".repeat(repeatLength))
            )
        }
    }))
}