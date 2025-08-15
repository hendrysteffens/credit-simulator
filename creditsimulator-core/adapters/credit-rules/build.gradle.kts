group = "com.challenge.adapters"

dependencies {
    implementation(platform(rootProject.libs.spring.boot.bom))
    implementation(projects.creditsimulatorCore.domain)

    implementation(libs.kotlinx.coroutines.reactor)
    testImplementation(libs.spring.boot.starter.test)
}