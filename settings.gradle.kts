rootProject.name = "creditsimulator"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    "creditsimulator-deployments:creditsimulator-app",
    "creditsimulator-core:adapters:flyway",
    "creditsimulator-core:adapters:messaging",
    "creditsimulator-core:adapters:credit-rules",
    "creditsimulator-core:adapters:http",
    "creditsimulator-core:adapters:r2dbc",
    "creditsimulator-core:domain",
    "creditsimulator-core:application",
)

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}
