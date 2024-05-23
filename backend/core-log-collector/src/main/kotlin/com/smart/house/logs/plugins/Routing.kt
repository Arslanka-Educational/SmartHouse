package com.smart.house.logs.plugins

import com.smart.house.logs.dao.LogDao
import com.smart.house.model.LogSaveRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactor.awaitSingle
import org.redisson.Redisson
import org.redisson.api.RedissonReactiveClient
import org.redisson.config.Config

lateinit var client: RedissonReactiveClient
val IODispatcher = Dispatchers.IO
val coroutineScope = CoroutineScope(IODispatcher)

fun Application.configureRouting() {
    val topic = environment.config.property("storage.redis.topic").getString()

    routing {
        post("/log") {
            client.getTopic(topic).publish(call.receive<LogSaveRequest>()).awaitSingle()
            call.respond(HttpStatusCode.Created)
        }
    }
}

fun Application.configureRedis() {
    val logDao = LogDao(environment.config.property("storage.clickhouse.url").getString())

    val config = Config()
    config.useSingleServer().address = environment.config.property("storage.redis.url").getString()
    client = Redisson.create(config).reactive()

    val topic = environment.config.property("storage.redis.topic").getString()

    client.getTopic(topic).addListener(LogSaveRequest::class.java) { _, msg ->
        coroutineScope.launch {
            logDao.saveLogEntity(msg)
        }
    }.block()
}
