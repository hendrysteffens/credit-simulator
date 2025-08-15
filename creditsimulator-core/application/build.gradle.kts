group = "com.challenge.creditsimulator-core.domain"

dependencies {
    implementation(platform(rootProject.libs.spring.boot.bom))
    implementation(projects.creditsimulatorCore.domain)
    implementation(libs.javax.money.api)
    implementation(libs.javamoney.moneta)

    testImplementation(libs.spring.boot.starter.test)
}
