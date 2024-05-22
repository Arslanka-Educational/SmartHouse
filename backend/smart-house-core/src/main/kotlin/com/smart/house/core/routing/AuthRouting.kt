package com.smart.house.core.routing

import com.smart.house.model.auth.LoginResponse
import com.smart.house.model.auth.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.authRoute(authClient: HttpClient, authHost: String) {
    post("/register") {
        val user = call.receive<User>()

        val httpResponse = authClient.post("$authHost/register") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }

        call.respond(httpResponse.status)
    }

    post("/login") {
        val user = call.receive<User>()

        val httpResponse = authClient.post("$authHost/login") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }

        val loginResponse = httpResponse.body<LoginResponse>()

        call.respond(HttpStatusCode.OK, loginResponse)
    }

    get("/user") {
        val user = validateTokenAndGetUser(call, authClient, authHost)

        call.respond(user)
    }
}

suspend fun validateTokenAndGetUser(call: ApplicationCall, authClient: HttpClient, authHost: String): User {
    val token =
        call.request.headers["Authorization"]

    if (token == null) {
        call.respond(HttpStatusCode.Unauthorized, "Authorization header must be present")
        throw IllegalArgumentException("Authorization header must be present")
    }

    val user: User = authClient.get("$authHost/user") {
        headers.append("Authorization", token)
    }.body()

    return user
}
