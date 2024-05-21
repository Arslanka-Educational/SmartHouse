package com.smart.house.db.routing

import com.smart.house.db.repository.DeviceRepository
import com.smart.house.db.repository.HouseRepository
import com.smart.house.db.repository.RoomRepository
import com.smart.house.db.service.DeviceService
import com.smart.house.db.service.HouseService
import com.smart.house.db.service.RoomService
import com.smart.house.model.Device
import io.ktor.http.HttpStatusCode
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

private var deviceRepository = DeviceRepository()
private var roomRepository = RoomRepository()
private var roomService = RoomService(roomRepository)
private var houseRepository = HouseRepository()
private var houseService = HouseService(houseRepository)
private var deviceService = DeviceService(deviceRepository, roomService, houseService)

fun Route.devicesRoute() {

    route("/houses/{houseId}/rooms/{roomId}/devices") {
        get("/") {

        }

        post("/") {
            val deviceToSave = call.receive<Device>()
            call.respond(
                deviceService.saveDevice(
                    deviceToSave,
                    call.parameters["roomId"]!!.toUuid(),
                )
            )
        }

        get("/{deviceId}") {
            call.respond(
                deviceService.getDevice(
                    call.parameters["deviceId"]!!.toUuid(),
                )
            )
        }

        delete("/{deviceId}") {

        }

        put("/{deviceId}/trigger") {
            call.respond(
                deviceService.changeTriggerAmount(
                    call.parameters["deviceId"]!!.toUuid(),
                    call.request.queryParameters["amount"]!!.toInt(),
                )
            )
        }

        put("/{deviceId}/status") {
            call.respond(
                deviceService.switchStatus(
                    call.parameters["deviceId"]!!.toUuid(),
                )
            )
        }
    }

    route("/houses/{houseId}/devices/{deviceId}/room/{roomId}") {
        patch("/") {
            deviceService.changeRoom(
                call.parameters["deviceId"]!!.toUuid(),
                call.parameters["roomId"]!!.toUuid(),
                call.parameters["houseId"]!!.toUuid(),
            )
            call.respond(HttpStatusCode.OK)
        }
    }
}
