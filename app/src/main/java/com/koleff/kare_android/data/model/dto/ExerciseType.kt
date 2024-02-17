package com.koleff.kare_android.data.model.dto

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson

@Deprecated("Navigation overhaul")
class ExerciseType : NavType<ExerciseDto>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ExerciseDto? {
        return bundle.getParcelable(key)
    }
    override fun parseValue(value: String): ExerciseDto {
        return Gson().fromJson(value, ExerciseDto::class.java)
    }
    override fun put(bundle: Bundle, key: String, value: ExerciseDto) {
        bundle.putParcelable(key, value)
    }
}