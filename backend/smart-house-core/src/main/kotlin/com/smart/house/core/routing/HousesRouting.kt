package com.smart.house.core.routing

import com.smart.house.model.House
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

fun Route.housesRoute(dbClient: HttpClient, dbHost: String, authClient: HttpClient, authHost: String) {
    val uri = "/houses"
    val requestUri = "$dbHost$uri"

    route(uri) {
        get("/{houseId}") {
            validateTokenAndGetUser(call, authClient, authHost)

            val houseId = call.parameters["houseId"]

            val house: House = dbClient.get("$requestUri/$houseId").body()
            call.respond(house)
        }

        post("") {
            val user = validateTokenAndGetUser(call, authClient, authHost)

            val houseToSave = call.receive<House>().copy(userId = user.id.toString())

            val house: House = dbClient.post(requestUri) {
                contentType(ContentType.Application.Json)
                setBody(houseToSave)
            }.body()
            call.respond(house)
        }

        get("/{userId}") {
            validateTokenAndGetUser(call, authClient, authHost)

            val userId = call.parameters["userId"]!!

            val houses: List<House> = dbClient.get("$requestUri/$userId").body()

            call.respond(houses)
        }
    }
}
