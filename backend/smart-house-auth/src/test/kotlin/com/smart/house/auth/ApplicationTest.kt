package com.smart.house.auth

import com.smart.house.auth.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
//    @Test
//    fun testRoot() = testApplication {
//        application {
//            val jwtAudience = environment.config.property("jwt.audience").getString()
//            val jwtIssuer = environment.config.property("jwt.issuer").getString()
//            val jwtRealm = environment.config.property("jwt.realm").getString()
//            val jwtSecret = environment.config.property("jwt.secret").getString()
//
//            configureRouting(jwtAudience, jwtIssuer, jwtSecret)
//        }
//        client.get("/").apply {
//            assertEquals(HttpStatusCode.OK, status)
//            assertEquals("Hello World!", bodyAsText())
//        }
//    }
}
