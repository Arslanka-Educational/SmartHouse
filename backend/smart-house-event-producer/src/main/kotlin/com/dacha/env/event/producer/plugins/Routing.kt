package com.dacha.env.event.producer.plugins

import com.dacha.env.event.producer.houseRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        houseRouting()
    }
}
