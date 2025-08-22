package com.srizan.technonextcodingassessment.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object AppsProtoPrefsSerializer : Serializer<AppsProtoPrefs> {
    override val defaultValue: AppsProtoPrefs
        get() = AppsProtoPrefs.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppsProtoPrefs {
        try {
            return AppsProtoPrefs.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AppsProtoPrefs, output: OutputStream) = t.writeTo(output)
}