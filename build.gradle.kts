import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("com.netflix.dgs.codegen") version "5.1.17"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

tasks.generateJava {
    schemaPaths = mutableListOf("${projectDir}/src/main/resources/schema")
    packageName = "gql"
    generateClient = true

    typeMapping = mutableMapOf(Pair("Show", "com.example.gqldeptest.application.dgs.adaptor.ShowResponse"))
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    // dgs boot 2.3.x ->  dgs 4.9.x
    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:4.9.25"))
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
    // starter-gql
//    implementation("org.springframework.boot:spring-boot-starter-graphql")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("org.springframework:spring-webflux")
//    testImplementation("org.springframework.graphql:spring-graphql-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
