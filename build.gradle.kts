import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import io.spring.gradle.dependencymanagement.dsl.ImportsHandler
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.3.31"
    idea
    jacoco
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.31"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.31"
    id("org.springframework.boot") version "2.1.4.RELEASE"
    `build-scan` version "2.2"
}

apply(plugin = "io.spring.dependency-management")

group = "com.github.mikesafonov"
version = "1.1.0"


repositories {
    mavenCentral()
    jcenter()
}

tasks.withType<Wrapper> {
    gradleVersion = "5.4.1"
    distributionType = Wrapper.DistributionType.BIN
}

tasks.getByName<BootJar>("bootJar") {
    launchScript()
}

springBoot {
    buildInfo {
        properties {
            additional = mapOf("version" to project.version)
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}

configure<DependencyManagementExtension> {
    imports(delegateClosureOf<ImportsHandler> {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Greenwich.SR1")
    })
}

tasks.withType<Test> {
    useJUnitPlatform()
}

configurations {
    compile {
        exclude(module = "spring-boot-starter-logging")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.telegram:telegrambots-spring-boot-starter:4.2")

    implementation("org.freemarker:freemarker:2.3.28")
    implementation("no.api.freemarker:freemarker-java8:1.3.0")

    implementation("io.github.microutils:kotlin-logging:1.6.22")
    implementation("org.apache.logging.log4j:log4j-web")

    implementation ("io.micrometer:micrometer-core")
    implementation("io.micrometer:micrometer-registry-prometheus")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")

    implementation("org.flywaydb:flyway-core")
    implementation("com.atlassian.jira:jira-rest-java-client-core:5.1.2-2bd0a62e")
    implementation("com.google.oauth-client:google-oauth-client:1.28.0")
    implementation("com.google.http-client:google-http-client-jackson2:1.28.0")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
    testImplementation("io.mockk:mockk:1.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

jacoco {
    toolVersion = "0.8.3"
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        csv.isEnabled = false
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"

    publishAlways()
}