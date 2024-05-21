package com.smart.house.db.routing

import com.smart.house.db.repository.HouseRepository
import com.smart.house.db.service.HouseService
import com.smart.house.model.House
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

private var houseRepository = HouseRepository()
private var houseService = HouseService(houseRepository)

fun Route.housesRoute() {

    route("/houses") {
        get("/{houseId}") {
            val house = houseService.getHouse(call.parameters["houseId"]!!.toUuid())
            call.respond(house)
        }

        post("/") {
            val houseToSave = call.receive<House>()
            call.respond(houseService.saveHouse(houseToSave))
        }

        get("/{userId}") {
            val userId = call.parameters["userId"]!!
            call.respond(houseService.getUserHouses(userId))
        }
    }
}
