package com.database.callProcessSettings

import com.utils.OffsetTimeSerializer
import kotlinx.serialization.Serializable
import java.time.OffsetTime

@Serializable
data class CallProcessSettingsDto(
    @Serializable(with = OffsetTimeSerializer::class) val timeFrom: OffsetTime,
    @Serializable(with = OffsetTimeSerializer::class) val timeTo: OffsetTime
)