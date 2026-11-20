plugins {
    id("java")
}

group = "io.github.equinoxelectronic.hivemindinstall"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.formdev:flatlaf:3.5.4")
}

tasks.test {
    useJUnitPlatform()
}