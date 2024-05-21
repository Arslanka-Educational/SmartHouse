package com.smart.house.db.plugins

import com.smart.house.db.repository.AvailableDeviceDao
import com.smart.house.db.repository.DeviceDAO
import com.smart.house.db.repository.HouseDAO
import com.smart.house.db.repository.RoomDAO
import io.ktor.server.application.ApplicationEnvironment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {
    fun init(environment: ApplicationEnvironment): Database {
        val driver = environment.config.property("ktor.datasource.driverClassName").getString()
        val url = environment.config.property("ktor.datasource.url").getString()
        val username = environment.config.property("ktor.datasource.username").getString()
        val password = environment.config.property("ktor.datasource.password").getString()

        val database = Database.connect(url = url, driver = driver, user = username, password = password)
        transaction(database) {
            SchemaUtils.create(HouseDAO)
            SchemaUtils.create(AvailableDeviceDao)
            SchemaUtils.create(RoomDAO)
            SchemaUtils.create(DeviceDAO)
        }
        return database
    }

    suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }
}
