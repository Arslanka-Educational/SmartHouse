package com.smart.house.logs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.smart.house.logs.plugins.configureRedis
import com.smart.house.logs.plugins.configureRouting
import com.smart.house.logs.plugins.configureSerialization
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureRedis()
    configureSerialization()
    configureRouting()
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            enable(SerializationFeature.INDENT_OUTPUT)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        }
    }
}
