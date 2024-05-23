package com.dacha.core.listener

import com.smart.house.db.repository.DeviceRepository
import com.smart.house.model.DeviceType
import com.smart.house.model.EnvStateEvent
import com.smart.house.model.LogSaveRequest
import kotlinx.coroutines.reactor.awaitSingle
import org.redisson.api.RedissonReactiveClient
import java.time.Instant


const val LOG_EVENT_QUEUE = "logs"
const val USER_LOGIN_STUB = "testovy"

class EnvEventHandler(
    private val redisClient: RedissonReactiveClient,
) {

    private val deviceRepository = DeviceRepository()

    suspend fun handle(event: EnvStateEvent) {
        val devices = deviceRepository.findDevicesInHome(event.houseId)
        for (device in devices) {
            if (device.type == DeviceType.MANUAL && device.eventType.name != event.envType) {
                continue
            }

            val triggerAmount: Int = device.triggerAmount ?: continue

            if ((event.amount >= triggerAmount && !device.state) || (event.amount < triggerAmount && device.state)) {
                val newState = device.state.not()
                deviceRepository.updateDeviceState(device.id, newState)

                redisClient.getTopic(LOG_EVENT_QUEUE).publish(
                    LogSaveRequest(
                        userLogin = USER_LOGIN_STUB,
                        logTime = Instant.now(),
                        houseId = event.houseId,
                        roomId = device.roomId,
                        deviceId = device.id,
                        newState = newState,
                        message = "Changed device state from ${newState.not()} to $newState"
                    )
                ).awaitSingle()
            }
        }
    }
}
