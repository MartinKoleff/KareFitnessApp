package com.koleff.kare_android.data

interface KareDto<T> {

    fun toDto(): T = throw UnsupportedOperationException("Conversion not supported without additional data")
}

interface KareDtoExtended<T, K> {

    fun toDto(data: K): T = throw UnsupportedOperationException("Conversion not supported without additional data")
}