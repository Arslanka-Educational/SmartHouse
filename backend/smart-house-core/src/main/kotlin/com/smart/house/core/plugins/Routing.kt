package com.smart.house.core.plugins

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.smart.house.core.routing.authRoute
import com.smart.house.core.routing.devicesRoute
import com.smart.house.core.routing.housesRoute
import com.smart.house.core.routing.roomsRoute
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.jackson.jackson
import io.ktor.server.routing.routing

fun Application.configureRouting(environment: ApplicationEnvironment) {
    val authHost = environment.config.property("ktor.client.auth.host").getString()
    val authClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }
    }

    val dbHost = environment.config.property("ktor.client.db.host").getString()
    val dbClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }
    }

    routing {
        authRoute(authClient, authHost)

        devicesRoute(dbClient, dbHost, authClient, authHost)
        housesRoute(dbClient, dbHost, authClient, authHost)
        roomsRoute(dbClient, dbHost, authClient, authHost)
    }
}
