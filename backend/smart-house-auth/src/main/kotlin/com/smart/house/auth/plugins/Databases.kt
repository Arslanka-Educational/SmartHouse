package com.smart.house.auth.plugins

import com.smart.house.auth.repository.UserDao
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
            SchemaUtils.create(UserDao)
        }
        return database
    }

    suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }
}
