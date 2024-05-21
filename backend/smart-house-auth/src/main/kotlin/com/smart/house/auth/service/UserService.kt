package com.smart.house.auth.service

import com.smart.house.auth.model.User
import com.smart.house.auth.repository.UserRepository
import io.ktor.server.plugins.NotFoundException

class UserService(
    private val userRepository: UserRepository
) {
    suspend fun findByUsername(username: String): User =
        userRepository.findByUsername(username) ?: throw NotFoundException("No such user")

    suspend fun saveUser(user: User) = userRepository.saveUser(user)
}
