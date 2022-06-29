plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("org.apache.kafka:connect-api:2.5.0")
}

tasks.register("prepareKotlinBuildScriptModel") {}