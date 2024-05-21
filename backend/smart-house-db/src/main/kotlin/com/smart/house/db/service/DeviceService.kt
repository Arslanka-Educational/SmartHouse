package com.smart.house.db.service

import com.smart.house.db.repository.DeviceRepository
import com.smart.house.model.Device
import io.ktor.server.plugins.NotFoundException
import java.util.UUID

class DeviceService(
    private val deviceRepository: DeviceRepository,
    private val roomService: RoomService,
    private val houseService: HouseService,
) {
    suspend fun getDevice(id: UUID): Device = deviceRepository.findDevice(id)
        ?: throw NotFoundException("Device with id=$id is not found")

    suspend fun saveDevice(device: Device, roomId: UUID? = null): Device =
        deviceRepository.saveDevice(device.also { it.roomId = roomId ?: it.roomId })

    suspend fun changeTriggerAmount(deviceId: UUID, amount: Int): Device =
        deviceRepository.changeTriggerAmount(deviceId, amount)

    suspend fun changeRoom(deviceId: UUID, roomId: UUID, houseId: UUID) {
        val house = houseService.getHouse(houseId)
        val targetRoom = roomService.getRoom(roomId)
        require(targetRoom.houseId == houseId) { "Specified room belongs to another house. Require house $houseId" }
        require(roomService.getRoom(getDevice(deviceId).roomId).houseId == houseId) { "Device belongs to another house. Require house $houseId" }
        deviceRepository.changeRoom(deviceId, roomId)
    }

    suspend fun switchStatus(deviceId: UUID) {
        val device = deviceRepository.findDevice(deviceId)
        require(device != null) { "Device by id = $deviceId not found" }
        val room = roomService.getRoom(device.id)
        val newState = device.state.not()
        deviceRepository.saveDevice(device.copy(state = newState))
    }
}
