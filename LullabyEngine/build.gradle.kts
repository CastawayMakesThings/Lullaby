plugins {
    id("java")
}

group = "io.github.equinoxelectronic.lullaby"
version = "v0.0.1-ALPHA"


dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.formdev:flatlaf:3.5.4")
    implementation("com.github.CastawayMakesThings:Lullaby:v1.0.1-ALPHA")
}

tasks.test {
    useJUnitPlatform()
}