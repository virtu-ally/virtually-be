plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.allopen") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.confluent:kafka-avro-serializer:7.5.8") // newer versions are borked for the time being
    implementation("org.bouncycastle:bctls-jdk18on:1.80")
    implementation("org.bouncycastle:bcprov-jdk18on:1.80")
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-client-jsonb")
    implementation("io.quarkus:quarkus-rest-client")
    implementation("io.quarkus:quarkus-rest-client-kotlin-serialization")
    implementation("io.quarkus:quarkus-kafka-client")
    implementation("io.quarkus:quarkus-avro")
    implementation("io.quarkus:quarkus-hibernate-reactive-rest-data-panache")
    implementation("io.quarkus:quarkus-rest-kotlin-serialization")
    implementation("io.quarkus:quarkus-rest-client-jackson")
    implementation("io.quarkus:quarkus-rest-jsonb")
    implementation("io.quarkus:quarkus-hibernate-validator")
    implementation("io.quarkus:quarkus-confluent-registry-avro")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-jackson")
    implementation("io.quarkus:quarkus-kafka-streams")
    implementation("io.quarkus:quarkus-reactive-pg-client")
    implementation("io.quarkus:quarkus-rest-jaxb")
    implementation("io.quarkus:quarkus-reactive-routes")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "com.virtuallyapp"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
        javaParameters = true
    }
}
