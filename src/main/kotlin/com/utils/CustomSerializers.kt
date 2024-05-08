package com.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.OffsetDateTime
import java.time.OffsetTime

object OffsetDateTimeSerializer : KSerializer<OffsetDateTime> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("OffsetDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: OffsetDateTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): OffsetDateTime =
        OffsetDateTime.parse(decoder.decodeString())

}

object OffsetTimeSerializer : KSerializer<OffsetTime> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("OffsetTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: OffsetTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): OffsetTime =
        OffsetTime.parse(decoder.decodeString())

}

object ExceptionSerializer : KSerializer<Exception> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Exception", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Exception) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Exception =
        Exception(decoder.decodeString())

}