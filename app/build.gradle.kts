plugins {
    application
}

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net")
}

dependencies {
    // HTTP API
    implementation("io.javalin:javalin:5.6.3")
    implementation("io.javalin:javalin-rendering:5.6.2")
    implementation("org.freemarker:freemarker:2.3.32")

    // Database
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.6.0")

    // JSON serialisation
    implementation("com.mojang:datafixerupper:6.0.8")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("io.github.virtualdogbert:logback-groovy-config:1.14.5")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.9.1")
        }
    }
}

application {
    mainClass.set("dev.nucleoid.backend.App")
}
