package com.smart.house.core

import com.dacha.core.listener.configureScheduler
import com.smart.house.core.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting(environment)
    configureScheduler()
}
