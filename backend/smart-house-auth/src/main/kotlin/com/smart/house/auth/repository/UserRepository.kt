package com.smart.house.auth.repository

import com.smart.house.auth.model.User
import com.smart.house.auth.plugins.DatabaseSingleton.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.mindrot.jbcrypt.BCrypt

object UserDao: Table("user") {
    val id = uuid("id")
    val username = varchar("name", 64).uniqueIndex("username_unique_idx")
    val password = varchar("password", 256)
    val email = varchar("email", 64)

    override val primaryKey = PrimaryKey(id)
}

fun ResultRow.toUser(): User = User(
    id = this[UserDao.id],
    username = this[UserDao.username],
    password = this[UserDao.password],
    email = this[UserDao.password]
)

class UserRepository {
    suspend fun findByUsername(username: String): User? = dbQuery {
        UserDao.select { UserDao.username eq username }.firstNotNullOfOrNull { it.toUser() }
    }

    suspend fun saveUser(user: User) = dbQuery {
        UserDao.insert {
            it[id] = user.id
            it[username] = user.username
            it[password] = BCrypt.hashpw(user.password, BCrypt.gensalt())
            it[email] = user.email
        }
    }
}
