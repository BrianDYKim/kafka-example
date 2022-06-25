plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("org.apache.kafka:kafka-clients:2.5.0")
    implementation("org.slf4j:slf4j-simple:1.7.30")
}

tasks.register("prepareKotlinBuildScriptModel") {}