group = "com.challenge.adapters"

plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(platform(rootProject.libs.spring.boot.bom))
    implementation(projects.creditsimulatorCore.domain)
    implementation(projects.creditsimulatorCore.application)

    implementation(libs.kotlin.logging)
    implementation(libs.javax.money.api)
    implementation(libs.slf4j.api)
    testImplementation(libs.slf4j.simple)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.kotlinx.coroutines.slf4j)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.spring.boot.starter.webflux)
    testImplementation(libs.spring.boot.starter.test)
}