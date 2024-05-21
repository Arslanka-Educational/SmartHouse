package com.smart.house.db.repository.mapper

import com.smart.house.db.repository.AvailableDeviceDao
import com.smart.house.db.repository.DeviceDAO
import com.smart.house.db.repository.HouseDAO
import com.smart.house.db.repository.RoomDAO
import com.smart.house.model.AvailableDevice
import com.smart.house.model.Device
import com.smart.house.model.DeviceType
import com.smart.house.model.EnvType
import com.smart.house.model.House
import com.smart.house.model.Room
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toHouseJson(): House = House(
    id = this[HouseDAO.id],
    name = this[HouseDAO.name],
    userId = this[HouseDAO.userId],
    deleted = this[HouseDAO.deleted],
    createdAt = this[HouseDAO.createdAt],
    updatedAt = this[HouseDAO.updatedAt],
)

fun ResultRow.toRoomJson(): Room = Room(
    id = this[RoomDAO.id],
    name = this[RoomDAO.name]
).also { it.houseId = this[RoomDAO.houseId] }

fun ResultRow.toDeviceJson(): Device = Device(
    id = this[DeviceDAO.id],
    name = this[DeviceDAO.name],
    type = DeviceType.valueOf(this[DeviceDAO.type]),
    state = this[DeviceDAO.state],
    triggerAmount = this[DeviceDAO.triggerAmount],
    availableDeviceId = this[DeviceDAO.availableDeviceId],
    eventType = EnvType.valueOf(this[DeviceDAO.eventType])
).also { it.roomId = this[DeviceDAO.roomId] }

fun ResultRow.toAvailableDevice(): AvailableDevice = AvailableDevice(
    id = this[AvailableDeviceDao.id],
    name = this[AvailableDeviceDao.name],
    description = this[AvailableDeviceDao.description],
    type = DeviceType.valueOf(this[AvailableDeviceDao.type])
)
