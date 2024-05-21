package com.smart.house.auth.routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.smart.house.auth.plugins.userService
import com.smart.house.model.auth.LoginResponse
import com.smart.house.model.auth.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
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

        JWT.decode(token)
        call.respond(LoginResponse(token))
    }

    authenticate {
        get("/user") {
            val username = call.principal<JWTPrincipal>()?.payload?.getClaim("username")?.asString()

            if (username == null) {
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid")
                throw IllegalArgumentException("Token is not valid")
            }

            call.respond(userService.findByUsername(username))
        }
    }
}
