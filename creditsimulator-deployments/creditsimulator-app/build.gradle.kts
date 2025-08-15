plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
}

group = "com.challenge.creditsimulator-deployments"

dependencies {
    implementation(platform(rootProject.libs.spring.boot.bom))

    implementation(projects.creditsimulatorCore.domain)
    implementation(projects.creditsimulatorCore.application)
    implementation(projects.creditsimulatorCore.adapters.http)
    implementation(projects.creditsimulatorCore.adapters.creditRules)
    implementation(projects.creditsimulatorCore.adapters.r2dbc)
    implementation(projects.creditsimulatorCore.adapters.messaging)

    implementation(libs.jackson.kotlin)

    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.javax.money.api)
    implementation(libs.javamoney.moneta)

    implementation(libs.r2dbc.pool)
    implementation(libs.r2dbc.postgresql)

    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.core)
    implementation(libs.spring.boot.starter.log4j2)
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.boot.starter.data.r2dbc)

    implementation(libs.springdoc.openapi.starter.webflux.ui)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.kotest.extensions.spring)
}

configurations.all {
    exclude(module = "spring-boot-starter-logging")
}
