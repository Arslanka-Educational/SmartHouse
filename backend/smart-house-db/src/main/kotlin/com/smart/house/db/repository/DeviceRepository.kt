package com.smart.house.db.repository

import com.smart.house.db.plugins.DatabaseSingleton.dbQuery
import com.smart.house.db.repository.mapper.toDeviceJson
import com.smart.house.model.Device
import org.jetbrains.exposed.sql.*
import java.util.UUID

object DeviceDAO : Table("rooms_devices") {
    val id = uuid("id")
    val name = varchar("name", 64)
    val roomId = uuid("room_id").references(RoomDAO.id, onDelete = ReferenceOption.CASCADE)
    val type = varchar("type", 64)
    val state = bool("state")
    val triggerAmount = integer("trigger_amount").nullable()
    val availableDeviceId = uuid("available_device_id").references(AvailableDeviceDao.id, onDelete = ReferenceOption.CASCADE)
    val eventType = text("event_type")

    override val primaryKey = PrimaryKey(id)
}

class DeviceRepository {
    suspend fun findDevice(id: UUID): Device? = dbQuery {
        DeviceDAO.select {
            (DeviceDAO.id eq id)
        }.mapNotNull { it.toDeviceJson() }.singleOrNull()
    }

    suspend fun updateDeviceState(deviceId: UUID, state: Boolean): Device {
        dbQuery {
            DeviceDAO.update({ DeviceDAO.id eq deviceId }) {
                it[this.state] = state
            }
        }
        return findDevice(deviceId)!!
    }

    suspend fun findDevicesInHome(id: UUID): List<Device> = dbQuery {
        DeviceDAO.innerJoin(RoomDAO, { roomId }, { RoomDAO.id })
            .innerJoin(HouseDAO, { RoomDAO.houseId }, { HouseDAO.id })
            .select {
                (HouseDAO.id eq id)
            }.mapNotNull { it.toDeviceJson() }
    }

    suspend fun saveDevice(device: Device): Device {
        dbQuery {
            DeviceDAO.insert {
                it[id] = device.id
                it[name] = device.name
                it[roomId] = device.roomId
                it[type] = device.type.toString()
                it[state] = device.state
                it[triggerAmount] = device.triggerAmount
                it[availableDeviceId] = device.availableDeviceId
                it[eventType] = device.eventType.name
            }
        }
        return findDevice(device.id)!!
    }

    suspend fun changeTriggerAmount(deviceId: UUID, amount: Int): Device {
        dbQuery {
            DeviceDAO.update({ DeviceDAO.id eq deviceId }) {
                it[triggerAmount] = amount
            }
        }
        return findDevice(deviceId)!!
    }

    suspend fun changeRoom(deviceId: UUID, roomId: UUID) {
        dbQuery {
            DeviceDAO.update({ DeviceDAO.id eq deviceId }) {
                it[DeviceDAO.roomId] = roomId
            }
        }
    }
}
