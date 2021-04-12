import java.util.Properties

plugins {
    kotlin("jvm") version "1.4.31"
    id("fabric-loom") version "0.6-SNAPSHOT"
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
    maven(url = "http://maven.fabricmc.net/") {
        name = "Fabric"
    }
    maven(url = "https://kotlin.bintray.com/kotlinx") {
        name = "Kotlinx"
    }
    mavenLocal()
    mavenCentral()
    jcenter()
}

minecraft {

}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = minecraftVersion)
    mappings(group = "net.fabricmc", name = "yarn", version = minecraftVersion+"+build.6", classifier = "v2")

    modImplementation("net.fabricmc:fabric-loader:0.11.3")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.32.5+1.16")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.5.0+kotlin.1.4.31")
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
