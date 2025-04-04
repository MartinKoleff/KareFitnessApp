package com.koleff.kare_android.data.model.dto

enum class StatisticScreenType(val id: Int) {
    GENERAL(0),
    EXERCISE(1),
    WORKOUT(2);

    companion object {
        fun fromId(id: Int): StatisticScreenType =
            StatisticScreenType.entries.find { it.id == id } ?: GENERAL
    }
}