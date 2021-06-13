import java.util.Properties

plugins {
    kotlin("jvm") version "1.4.32"
    id("fabric-loom") version "0.8-SNAPSHOT"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// load props from parent project
val parentProps = rootDir.resolve("gradle.properties").bufferedReader().use {
    Properties().apply {
        load(it)
    }
}

val modId: String by parentProps
val modVersion: String by parentProps
val group: String by parentProps
val minecraftVersion: String by parentProps

base {
    archivesBaseName = modId
}

project.group = group
version = modVersion

repositories {
    maven("https://maven.fabricmc.net/") {
        name = "Fabric"
    }
    maven("https://kotlin.bintray.com/kotlinx") {
        name = "Kotlinx"
    }
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
}

minecraft {
}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = minecraftVersion)
    mappings(group = "net.fabricmc", name = "yarn", version = minecraftVersion + "+build.9", classifier = "v2")

//    modImplementation("com.theoparis:creepinoutils-fabric:1.16-1.0.0")
    modImplementation("net.fabricmc:fabric-loader:0.11.5")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.35.0+1.16")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.6.1+kotlin.1.5.10")
    modImplementation("software.bernie.geckolib:geckolib-fabric-1.16.5:3.0.40:dev")
}

val fabricApiVersion = ""
val kotlinVersion = ""

tasks.getByName<ProcessResources>("processResources") {
    filesMatching("fabric.mod.json") {
        expand(
            mutableMapOf(
                "modid" to modId,
                "version" to modVersion,
                "kotlinVersion" to kotlinVersion,
                "fabricApiVersion" to fabricApiVersion
            )
        )
    }
}
