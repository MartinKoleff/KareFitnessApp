package com.koleff.kare_android.data

interface KareEntity<T> {

    fun toEntity(): T
}