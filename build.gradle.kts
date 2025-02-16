
val testcontainersVersion by extra("1.20.4")
val flywayVersion by extra("11.1.0")
val jimmerVersion by extra("0.9.50")
var springModulith by extra("1.3.2")

plugins {
    jacoco
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.google.devtools.ksp") version "1.9.25-1.0.20"
    id("org.springdoc.openapi-gradle-plugin") version "1.9.0"
    id("pmd")
    id("com.diffplug.spotless") version "7.0.2"
}

group = "com.xermao"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/central")}
    maven { url = uri("https://maven.aliyun.com/repository/public") }
    maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
    maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots") }
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.babyfish.jimmer:jimmer-spring-boot-starter:0.9.50")
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.0")
    implementation("com.auth0:java-jwt:4.4.0")


    implementation("org.springframework.modulith:spring-modulith-bom:$springModulith")
    implementation("org.springframework.modulith:spring-modulith-api:$springModulith")
    implementation("org.springframework.modulith:spring-modulith-starter-core:$springModulith")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test:$springModulith")
    ksp("org.babyfish.jimmer:jimmer-ksp:0.9.50")
    runtimeOnly("org.postgresql:postgresql:42.7.5")
    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.117.Final:osx-aarch_64")
    annotationProcessor("org.projectlombok:lombok")

//    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
//    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
//    testImplementation("org.testcontainers:testcontainers-bom:$testcontainersVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}



tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

jacoco {
    toolVersion = "0.8.12"
    reportsDirectory.set(layout.buildDirectory.dir("reports/jacoco"))
}

pmd {
    sourceSets = listOf(java.sourceSets.findByName("main"))
    isConsoleOutput = true
    toolVersion = "7.9.0"
    rulesMinimumPriority.set(5)
    ruleSetFiles = files("pmd-rules.xml")
}
