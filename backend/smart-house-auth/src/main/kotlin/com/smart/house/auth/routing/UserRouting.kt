package com.smart.house.auth.routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.smart.house.auth.model.User
import com.smart.house.auth.plugins.userService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.mindrot.jbcrypt.BCrypt
import java.time.Instant
import java.time.temporal.ChronoUnit


fun Route.userRoute(jwtAudience: String, jwtIssuer: String, jwtSecret: String) {
    post("/register") {
        try {
            val user = call.receive<User>()
            userService.saveUser(user)
            call.respond(HttpStatusCode.Created)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    post("/login") {
        val user = call.receive<User>()

        val savedUser = userService.findByUsername(user.username)

        if (!BCrypt.checkpw(user.password, savedUser.password)) {
            call.respond(HttpStatusCode.Unauthorized)
        }

        val token = JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .withClaim("username", user.username)
            .withExpiresAt(Instant.now().plus(10, ChronoUnit.DAYS))
            .sign(Algorithm.HMAC256(jwtSecret))
        call.respond(hashMapOf("token" to token))
    }
}
