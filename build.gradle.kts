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
    maven("https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("net.dv8tion:JDA:4.1.1_154") {
        exclude(module = "opus-java")
    }
    implementation("org.apache.logging.log4j:log4j-core:2.13.3")
    implementation("com.jagrosh:jda-utilities:3.0.3")
    implementation("net.dean.jraw:JRAW:1.1.0")
    implementation("co.aikar:idb-core:1.0.0-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.xerial:sqlite-jdbc:3.31.1")
    // For time command
    implementation("com.google.maps:google-maps-services:0.13.0")
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