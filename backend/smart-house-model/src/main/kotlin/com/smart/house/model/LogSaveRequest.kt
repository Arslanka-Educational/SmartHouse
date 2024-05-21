package com.smart.house.model

import java.time.Instant
import java.util.UUID

data class LogSaveRequest(
    val userLogin: String,
    val logTime: Instant,
    val houseId: UUID,
    val roomId: UUID,
    val deviceId: UUID,
    val newState: Boolean,
    val message: String?,
)
