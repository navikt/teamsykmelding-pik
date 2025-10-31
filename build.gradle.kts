group = "no.nav.syfo"
version = "1.0.0"

val coroutinesVersion = "1.10.2"
val jacksonVersion = "2.20.1"
val kluentVersion = "1.73"
val ktorVersion = "3.2.1"
val logbackVersion = "1.5.20"
val logstashEncoderVersion = "9.0"
val prometheusVersion = "0.16.0"
val mockkVersion = "1.14.6"
val testContainerKafkaVersion = "1.17.6"
val kotlinVersion = "2.2.21"
val kotestVersion = "6.0.4"
val jsonSchemaValidatorVersion = "2.0.0"
val koinVersion="4.1.1"
val kafkaVersion= "3.9.1"

application {
    mainClass.set("no.nav.syfo.ApplicationKt")
}

plugins {
    kotlin("jvm") version "2.2.21"
    id("io.ktor.plugin") version "3.3.1"
}

repositories {
    mavenCentral()
    maven(url = "https://packages.confluent.io/maven/")
    maven {
        url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
    }
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutinesVersion")
    implementation("io.prometheus:simpleclient_hotspot:$prometheusVersion")
    implementation("io.prometheus:simpleclient_common:$prometheusVersion")
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-jackson-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    testImplementation("org.amshove.kluent:kluent:$kluentVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.ktor:ktor-server-test-host") {
        exclude(group = "org.eclipse.jetty")
    }
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("com.networknt:json-schema-validator:$jsonSchemaValidatorVersion")
}

tasks {

    withType<Test> {
        useJUnitPlatform {
        }
        testLogging {
            events("skipped", "failed")
            showStackTraces = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}
