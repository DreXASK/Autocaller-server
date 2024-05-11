package com.features

import java.time.OffsetDateTime
import java.time.ZoneOffset

fun checkIsFetchDatetimeOfCallTaskInWorkOutdated(dateTime: OffsetDateTime): Boolean {
    val currentTimeUtc = OffsetDateTime.now(ZoneOffset.UTC)
    return currentTimeUtc > dateTime.plusMinutes(5)
}