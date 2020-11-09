plugins {
    kotlin("jvm") version "1.3.72"
    application
}

application {
    mainClass.set("uk.gov.justice.laa.architecture.App")
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.structurizr:structurizr-client:1.6.2")
    implementation("com.structurizr:structurizr-core:1.6.2")
}
