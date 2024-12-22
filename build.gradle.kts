plugins {
    kotlin("jvm") version "2.1.0"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
    test {
        kotlin.srcDir("test")
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0-RC")
    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.11.1")
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}
tasks.test {
    useJUnitPlatform()
}
