plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlinter)
	alias(libs.plugins.test.logger)
}

group = "com.challenge"
version = "0.0.1-SNAPSHOT"

val Project.fullName: String get() = (parent?.fullName?.plus("-") ?: "") + name

subprojects {
	if (file("src/main/kotlin").isDirectory || file("src/main/resources").isDirectory) {
		apply {
			plugin("org.jetbrains.kotlin.jvm")
			plugin("org.jmailen.kotlinter")
			plugin("com.adarshr.test-logger")
			plugin("jacoco")
			plugin("java-library")
		}

		tasks.findByName("test")?.let { testTask ->
			testTask as Test
			testTask.useJUnitPlatform()
		}

		dependencies {
			implementation(platform(rootProject.libs.log4j.bom))

			implementation(rootProject.libs.kotlin.reflect)
			implementation(rootProject.libs.kotlinx.datetime)
			implementation(rootProject.libs.kotlinx.coroutines.core)
			implementation(rootProject.libs.kotlinx.collections.immutable)

			testImplementation(rootProject.libs.mockk)
			testImplementation(rootProject.libs.kotest.runner.junit5)
			testImplementation(rootProject.libs.kotest.assertions.core)

		}

		configurations.all {
			exclude(group = "org.mockito")
			exclude(group = "javax.validation")
			exclude(module = "hibernate-validator")
			exclude(module = "jakarta.validation-api")
		}

		tasks {
			jar {
				archiveBaseName.set(project.fullName.replaceFirst("${rootProject.name}-", ""))
				archiveVersion.set("")
			}

			compileKotlin {
				kotlinOptions {
					apiVersion = rootProject.libs.versions.compiler.kotlin.get()
					languageVersion = rootProject.libs.versions.compiler.kotlin.get()
					jvmTarget = rootProject.libs.versions.compiler.java.get()
					freeCompilerArgs += listOf(
						"-Xjdk-release=${rootProject.libs.versions.compiler.java.get()}",
						"-Xjsr305=strict",
						"-Xjvm-default=all",
						"-opt-in=kotlin.time.ExperimentalTime"
					)
				}
			}

			compileTestKotlin {
				kotlinOptions {
					apiVersion = rootProject.libs.versions.compiler.kotlin.get()
					languageVersion = rootProject.libs.versions.compiler.kotlin.get()
					jvmTarget = rootProject.libs.versions.compiler.java.get()
					freeCompilerArgs += listOf(
						"-Xjdk-release=${rootProject.libs.versions.compiler.java.get()}",
						"-Xjsr305=strict",
						"-Xjvm-default=all",
						"-opt-in=kotlin.time.ExperimentalTime"
					)
				}
			}

			test {
				ignoreFailures = true
				useJUnitPlatform()
				jvmArgs(
					"--add-opens=java.base/java.lang=ALL-UNNAMED",
					"--add-opens=java.base/java.util=ALL-UNNAMED"
				)
			}
		}
	}
}
