package com.dacha.env.event.producer


import com.smart.house.model.EnvStateEvent
import com.smart.house.model.EnvType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.random.Random

lateinit var client: RedissonClient

const val HOUSES_QUEUE = "houses"
const val ENV_EVENT_QUEUE = "env_state_events"
const val CHUNK_SIZE = 100

fun configureRedis() {
    val config = Config()
    // docker run -d --name my-redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack:latest
    config.useSingleServer().address = "redis://localhost:6379"
    client = Redisson.create(config)
}

fun configureScheduler() {
    val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(10)

    executor.scheduleAtFixedRate(
        {
            val houses: List<House> = client.getQueue<House>(HOUSES_QUEUE).poll(CHUNK_SIZE)
            houses.forEach { house ->
                EnvType.values().forEach { envType ->
                    client
                        .getQueue<EnvStateEvent>(ENV_EVENT_QUEUE)
                        .offer(EnvStateEvent(house.id, envType.name, Random.nextInt(envType.from, envType.to)))
                }

                client.getQueue<House>(HOUSES_QUEUE).offer(house)
            }
        },
        1000,
        100,
        TimeUnit.MILLISECONDS
    )
}

fun Routing.houseRouting() {
    post("/houses/{houseId}") {
        val houseId: UUID = UUID.fromString(call.parameters["houseId"])
            ?: throw IllegalArgumentException("houseId must be UUID")

        client.getQueue<House>(HOUSES_QUEUE).offer(House(houseId))

        call.respond(HttpStatusCode.OK)
    }
}

data class House(val id: UUID)
