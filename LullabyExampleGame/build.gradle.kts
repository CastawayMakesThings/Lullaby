plugins {
    `java`
    `application`
}

group = "com.github.castawaymakesthings.lullaby"
version = "unspecified"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation(project(":LullabyEngine"))
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.github.CastawayMakesThings:Lullaby:v0.1.0")
}

application {
    mainClass.set("com.github.castawaymakesthings.lullabyexamplegame.Main")
}

tasks.test {
    useJUnitPlatform()
}