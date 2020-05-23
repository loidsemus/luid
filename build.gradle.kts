plugins {
    kotlin("jvm") version "1.3.72"
    id("application")
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "me.loidsemus"
version = "1.0.0"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("net.dv8tion:JDA:4.1.1_154") {
        exclude(module = "opus-java")
    }
    implementation("org.apache.logging.log4j:log4j-core:2.13.3")
    implementation("com.jagrosh:jda-utilities:3.0.3")
    implementation("net.dean.jraw:JRAW:1.1.0")
}

tasks {
    application {
        mainClassName = "me.loidsemus.luid.Main"
    }

    shadowJar {
        archiveClassifier.set("")
        minimize()
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}