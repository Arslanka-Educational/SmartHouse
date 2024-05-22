package com.smart.house.core.routing

import com.smart.house.model.Room
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.roomsRoute(dbClient: HttpClient, dbHost: String, authClient: HttpClient, authHost: String) {
    val uri = "/houses/{houseId}/rooms"
    val requestUri = "$dbHost$uri"

    route(uri) {
        get("/{roomId}") {
            validateTokenAndGetUser(call, authClient, authHost)

            val roomId = call.parameters["roomId"]!!

            val room: Room = dbClient.get("$requestUri/$roomId").body()
            call.respond(room)
        }

        post("/") {
            validateTokenAndGetUser(call, authClient, authHost)

            val roomToSave = call.receive<Room>()

            val room: Room = dbClient.post(requestUri) {
                contentType(ContentType.Application.Json)
                setBody(roomToSave)
            }.body()

            call.respond(room)
        }

        get("/") {
            validateTokenAndGetUser(call, authClient, authHost)

            val rooms: List<Room> = dbClient.get(requestUri).body()

            call.respond(rooms)
        }
    }
}
