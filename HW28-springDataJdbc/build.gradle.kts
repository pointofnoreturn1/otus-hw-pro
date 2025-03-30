plugins {
    id ("org.springframework.boot")
}

dependencies {
    implementation ("org.projectlombok:lombok")
    implementation("ch.qos.logback:logback-classic")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    annotationProcessor ("org.projectlombok:lombok")
}
