plugins {
    alias(libs.plugins.flyway)
}

group = "com.challenge.creditsimulator-core.adapters.flyway"

dependencies {
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.kotest.framework.api)
    runtimeOnly(libs.jdbc.postgresql)
    runtimeClasspath(libs.jdbc.postgresql)
}

buildscript {
    dependencies {
        classpath(libs.flyway.database.postgresql)
    }
}

flyway {
    url = "jdbc:postgresql://localhost:5432/credit"
    user = "admin"
    password = "admin"
    schemas = arrayOf("public")
    defaultSchema = "public"
}