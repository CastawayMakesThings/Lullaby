plugins {
    `java-library`
    `maven-publish`
}

group = "io.github.equinoxelectronic.lullaby"
version = "v0.0.2-ALPHA"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    api("com.formdev:flatlaf:3.5.4")
}

tasks.test {
    useJUnitPlatform()
}