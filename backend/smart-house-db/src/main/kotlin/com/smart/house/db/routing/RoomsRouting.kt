package com.smart.house.db.routing

import com.smart.house.db.repository.RoomRepository
import com.smart.house.db.service.RoomService
import com.smart.house.model.Room
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

private var roomRepository = RoomRepository()
private var roomService = RoomService(roomRepository)

fun Route.roomsRoute() {

    route("/houses/{houseId}/rooms") {
        get("/{roomId}") {
            val room = roomService.getRoom(
                call.parameters["roomId"]!!.toUuid()
            )
            call.respond(room)
        }

        post("/") {
            val roomToSave = call.receive<Room>()
            call.respond(
                roomService.saveRoom(
                    roomToSave,
                    call.parameters["houseId"]!!.toUuid()
                )
            )
        }

        get("/") {
            call.respond(
                roomService.listRooms(call.parameters["houseId"]!!.toUuid())
            )
        }
    }
}
