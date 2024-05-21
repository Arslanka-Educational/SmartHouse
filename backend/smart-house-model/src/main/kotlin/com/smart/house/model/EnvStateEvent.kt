package com.smart.house.model

import java.util.UUID

data class EnvStateEvent(val houseId: UUID, val envType: String, val amount: Int)
