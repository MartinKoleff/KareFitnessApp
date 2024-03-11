package com.koleff.kare_android.common

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.UUID


class UUIDJsonAdapter {
    @ToJson
    fun toJson(uuid: UUID): String {
        return uuid.toString()
    }

    @FromJson
    fun fromJson(uuid: String?): UUID {
        return UUID.fromString(uuid)
    }
}