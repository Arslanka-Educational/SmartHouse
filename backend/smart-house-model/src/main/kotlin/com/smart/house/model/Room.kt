package com.smart.house.model

import java.util.UUID

data class Room(
    val id: UUID = UUID.randomUUID(),
    val name: String,
) {
    var houseId: UUID = UUID.randomUUID() // need to init from path variable
}
