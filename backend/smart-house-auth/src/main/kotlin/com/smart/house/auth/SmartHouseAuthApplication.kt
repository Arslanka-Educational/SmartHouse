package com.smart.house.auth

import com.smart.house.auth.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseSingleton.init(environment)

    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtIssuer = environment.config.property("jwt.issuer").getString()
    val jwtRealm = environment.config.property("jwt.realm").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    configureSecurity(jwtRealm, jwtAudience, jwtIssuer, jwtSecret)
    configureSerialization()
    configureRouting(jwtAudience, jwtIssuer, jwtSecret)
}
