rootProject.name = "otusJava"

include("HW01-gradle")
include("HW03-generics")
include("HW06-annotations")
include("HW08-gc:homework")
include("HW10-byteCodes")
include("HW12-solid")
include("HW15-structuralPatterns")
include("HW16-io")
include("HW18-jdbc:homework")
include("HW21-jpql:homework")
include("HW22-cache")
include("HW24-webServer")
include("HW25-di")
include("HW28-springDataJdbc")

pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val protobufVer: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
        id("com.google.protobuf") version protobufVer
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}
