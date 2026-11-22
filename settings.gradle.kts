rootProject.name = "Lullaby"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

include("LullabyEngine")

val isJitPack = System.getenv("JITPACK")?.equals("true", ignoreCase = true) == true ||
    providers.gradleProperty("jitpack").orNull?.equals("true", ignoreCase = true) == true

if (!isJitPack) {
    include("LullabyExampleGame")
}