package com.smart.house.db.service

import com.smart.house.db.repository.HouseRepository
import com.smart.house.model.House
import io.ktor.server.plugins.NotFoundException
import java.util.UUID

class HouseService(
    private val repository: HouseRepository,
) {
    suspend fun getHouse(id: UUID): House = repository.findHouse(id)
        ?: throw NotFoundException("House with id=$id is not found")

    suspend fun saveHouse(house: House): House = repository.saveHouse(house)
}
