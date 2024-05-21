package com.smart.house.db

import com.smart.house.db.plugins.DatabaseSingleton
import com.smart.house.db.plugins.configureRouting
import com.smart.house.db.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseSingleton.init(environment)

    configureSerialization()
    configureRouting()
}
