package com.smart.house.db.routing

import java.util.UUID

fun String.toUuid(): UUID = UUID.fromString(this)
