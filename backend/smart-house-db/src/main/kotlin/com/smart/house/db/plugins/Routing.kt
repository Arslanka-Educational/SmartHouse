package com.smart.house.db.plugins

import com.smart.house.db.routing.devicesRoute
import com.smart.house.db.routing.housesRoute
import com.smart.house.db.routing.roomsRoute
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        housesRoute()
        roomsRoute()
        devicesRoute()
    }
}
