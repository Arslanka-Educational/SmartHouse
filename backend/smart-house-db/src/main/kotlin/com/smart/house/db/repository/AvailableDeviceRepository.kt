package com.smart.house.db.repository

import org.jetbrains.exposed.sql.Table


object AvailableDeviceDao : Table("available_devices") {
    val id = uuid("id")
    val name = varchar("name", 64)
    val description = varchar("description", 256)
    val type = varchar("type", 64)

    override val primaryKey = PrimaryKey(id)
}

class AvailableDevicesRepository {

}
