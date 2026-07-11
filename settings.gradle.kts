pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "8.5.0" apply false
        id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Calculator"
include(":app")
include(":app-calculator")
include(":core:calculator-engine")
include(":core:unit-converter")
include(":core:currency-converter")
include(":core:mortgage-calculator")
