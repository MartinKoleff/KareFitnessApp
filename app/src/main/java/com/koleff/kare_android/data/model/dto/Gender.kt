package com.koleff.kare_android.data.model.dto

import com.koleff.kare_android.R
import com.koleff.kare_android.common.ExerciseGenerator

enum class Gender(
    val id: Int,
    val text: String,
) {
    MALE(1, "Male"),
    FEMALE(2, "Female"),
    NONE(-1, "None");

    companion object {
        fun fromId(id: Int): Gender =
            entries.find { it.id == id } ?: NONE

        fun getGenderList(): List<Gender> {
            return listOf(MALE, FEMALE)
        }
    }
}

