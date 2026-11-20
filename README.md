![Lullaby Logo](Lullaby.png)

# Lullaby
# WARNING, THERE IS A MASSIVE ISSUE IN THE TAGS FOR THE RELEASES ON GITHUB. 

Lullaby is a lightweight Java game engine for text-based games and interactive stories.

## Dependency / JitPack Setup

Lullaby is published using [JitPack](https://jitpack.io/).

[![](https://jitpack.io/v/EquinoxElectronic/Lullaby.svg)](https://jitpack.io/#EquinoxElectronic/Lullaby)

### 1. Add the JitPack repository

#### Gradle (Kotlin DSL - `settings.gradle.kts`)
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

#### Gradle (Groovy DSL - `settings.gradle` or `build.gradle`)
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

#### Maven (`pom.xml`)
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

### 2. Add the dependency

#### Gradle (Kotlin DSL - `build.gradle.kts`)
```kotlin
dependencies {
    implementation("com.github.EquinoxElectronic.Lullaby:LullabyEngine:Tag")
}
```

#### Gradle (Groovy DSL - `build.gradle`)
```groovy
dependencies {
    implementation 'com.github.EquinoxElectronic.Lullaby:LullabyEngine:Tag'
}
```

#### Maven (`pom.xml`)
```xml
<dependency>
    <groupId>com.github.EquinoxElectronic.Lullaby</groupId>
    <artifactId>LullabyEngine</artifactId>
    <version>Tag</version>
</dependency>
```

> Replace `Tag` with the release version tag or commit hash (e.g., `v0.0.1-ALPHA` or `main-SNAPSHOT`).