package com.koleff.kare_android.data.model

interface KareEntity<T> {

    fun toEntity(): T
}