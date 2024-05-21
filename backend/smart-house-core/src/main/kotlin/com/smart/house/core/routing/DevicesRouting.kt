package com.smart.house.core.routing

import com.smart.house.model.Device
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.devicesRoute(dbClient: HttpClient, dbHost: String, authClient: HttpClient, authHost: String) {
    val uri = "/houses/{houseId}/rooms/{roomId}/devices"
    val requestUri = "$dbHost$uri"

    route(uri) {
        get("/") {

        }

        post("/") {
            validateTokenAndGetUser(call, authClient, authHost)

            val deviceToSave = call.receive<Device>()

            val savedDevice: Device = dbClient.post(requestUri) {
                contentType(ContentType.Application.Json)
                setBody(deviceToSave)
            }.body()

            call.respond(savedDevice)
        }

        get("/{deviceId}") {
            validateTokenAndGetUser(call, authClient, authHost)

            val deviceId = call.parameters["deviceId"]

            val device: Device = dbClient.get("$requestUri/$deviceId").body()
            call.respond(device)
        }

        delete("/{deviceId}") {
            validateTokenAndGetUser(call, authClient, authHost)

            val deviceId = call.parameters["deviceId"]

            val httpResponse = dbClient.delete("$requestUri/$deviceId")
            call.respond(httpResponse.status)
        }

        put("/{deviceId}/trigger") {
            validateTokenAndGetUser(call, authClient, authHost)

            val deviceId = call.parameters["deviceId"]!!
            val amount = call.request.queryParameters["amount"]!!

            val httpResponse = dbClient.put("$requestUri/$deviceId/trigger") {
                url { parameters.append("amount", amount) }
            }

            val device: Device = httpResponse.body()

            call.respond(device)
        }

        put("/{deviceId}/status") {
            validateTokenAndGetUser(call, authClient, authHost)

            val deviceId = call.parameters["deviceId"]!!

            val httpResponse = dbClient.put("$requestUri/$deviceId/trigger")

            call.respond(httpResponse.status)
        }
    }

    route("/houses/{houseId}/devices/{deviceId}/room/{roomId}") {
        patch("/") {
            validateTokenAndGetUser(call, authClient, authHost)

            val deviceId = call.parameters["deviceId"]!!
            val roomId = call.parameters["roomId"]!!
            val houseId = call.parameters["houseId"]!!

            val httpResponse = dbClient.patch("$requestUri/houses/$houseId/devices/$deviceId/room/$roomId")
            call.respond(httpResponse.status)
        }
    }
}
