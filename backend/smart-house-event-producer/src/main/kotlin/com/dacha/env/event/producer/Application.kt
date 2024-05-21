package com.dacha.env.event.producer

import com.dacha.env.event.producer.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    configureRedis()
    configureScheduler()
    embeddedServer(Netty, port = 13001, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
}
