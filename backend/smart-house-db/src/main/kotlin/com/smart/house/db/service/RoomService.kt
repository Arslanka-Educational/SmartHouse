package com.smart.house.db.service

import com.smart.house.db.repository.RoomRepository
import com.smart.house.model.Room
import io.ktor.server.plugins.NotFoundException
import java.util.UUID

class RoomService(
    private val repository: RoomRepository,
) {
    suspend fun getRoom(id: UUID): Room = repository.findRoom(id)
        ?: throw NotFoundException("Room with id=$id is not found")

    suspend fun saveRoom(room: Room, houseId: UUID? = null): Room =
        repository.saveRoom(room.also { it.houseId = houseId ?: it.houseId })

    suspend fun listRooms(houseId: UUID): List<Room> =
        repository.listRooms(houseId)
}
