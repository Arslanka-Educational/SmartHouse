package com.smart.house.auth.plugins

import com.smart.house.auth.repository.UserRepository
import com.smart.house.auth.routing.userRoute
import com.smart.house.auth.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

val userRepository = UserRepository()
val userService = UserService(userRepository)

fun Application.configureRouting(jwtAudience: String, jwtIssuer: String, jwtSecret: String) {
    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, "Hello World!")
        }
        userRoute(jwtAudience, jwtIssuer, jwtSecret)
    }
}
