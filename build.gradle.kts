plugins {
    kotlin("jvm") version "1.3.72"
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
    implementation("com.jagrosh:jda-utilities:3.0.3")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}