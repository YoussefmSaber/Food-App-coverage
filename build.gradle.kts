plugins {
    kotlin("jvm") version "2.1.10"
    jacoco
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // koin
    implementation("io.insert-koin:koin-core:4.0.3")

    // kotlin date time
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

    // kotest, assertion
    testImplementation("io.kotest:kotest-runner-junit5:5.7.2")
    testImplementation("io.kotest:kotest-assertions-core:5.7.2")

    // google truth
    testImplementation("com.google.truth:truth:1.4.2")

    // mockk
    testImplementation("io.mockk:mockk:1.14.0")

    // junit params
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(20)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    classDirectories.setFrom(
        fileTree("build/classes/kotlin/main") {
            exclude("**/generated/**")
        }
    )
    sourceDirectories.setFrom(files("src/main/kotlin"))
    executionData.setFrom(fileTree(buildDir).include("jacoco/test.exec"))
}

tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    dependsOn(tasks.test)

    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal() // Require 80% line coverage
            }
        }
    }

    classDirectories.setFrom(
        fileTree("build/classes/kotlin/main") {
            exclude("**/generated/**")
        }
    )
    sourceDirectories.setFrom(files("src/main/kotlin"))
    executionData.setFrom(fileTree(buildDir).include("jacoco/test.exec"))
}

tasks.check {
    dependsOn("jacocoTestReport", "jacocoTestCoverageVerification")
}
