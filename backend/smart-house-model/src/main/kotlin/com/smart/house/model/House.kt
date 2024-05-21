package com.smart.house.model

import java.time.LocalDateTime
import java.util.UUID

data class House(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val userId: String,
    val deleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
