package com.smart.house.logs.dao

import com.clickhouse.client.ClickHouseClient
import com.clickhouse.client.ClickHouseNodes
import com.clickhouse.client.ClickHouseProtocol
import com.clickhouse.data.ClickHouseDataStreamFactory
import com.clickhouse.data.ClickHouseFormat
import com.clickhouse.data.format.BinaryStreamUtils
import com.smart.house.model.LogSaveRequest
import kotlinx.coroutines.future.await
import java.util.TimeZone

class LogDao(
    private val clickHouseUrl: String,
) {

    private val clickHouseClient: ClickHouseClient = ClickHouseClient.newInstance(ClickHouseProtocol.HTTP)
    private val clickHouseServer: ClickHouseNodes = ClickHouseNodes.of(clickHouseUrl)

    suspend fun saveLogEntity(entity: LogSaveRequest) {
        val request = clickHouseClient
            .read(clickHouseServer)
            .format(ClickHouseFormat.RowBinary)
            .write()
            .table("logs")

        val stream = ClickHouseDataStreamFactory.getInstance().createPipedOutputStream(request.config)
        BinaryStreamUtils.writeString(stream, entity.userLogin)
        BinaryStreamUtils.writeDateTime(stream, entity.logTime.atZone(TimeZone.getTimeZone("Europe/Moscow").toZoneId()).toLocalDateTime(), TimeZone.getTimeZone("Europe/Moscow"))
        BinaryStreamUtils.writeUuid(stream, entity.houseId)
        BinaryStreamUtils.writeUuid(stream, entity.roomId)
        BinaryStreamUtils.writeUuid(stream, entity.deviceId)
        BinaryStreamUtils.writeBoolean(stream, entity.newState)
        BinaryStreamUtils.writeString(stream, entity.message)

        stream.close()

        request.data(stream.inputStream)
            .execute()
            .await()
    }
}
