group = "com.challenge.adapters"

plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(platform(rootProject.libs.spring.boot.bom))
    implementation(projects.creditsimulatorCore.domain)

    implementation(libs.spring.boot.starter.data.r2dbc)
    implementation(libs.r2dbc.pool)
    implementation(libs.r2dbc.postgresql)

    implementation(libs.javax.money.api)
    implementation(libs.javamoney.moneta)
    implementation(libs.kotlin.logging)

    implementation(libs.kotlinx.coroutines.reactor)

    testImplementation(libs.spring.boot.starter.test)
}