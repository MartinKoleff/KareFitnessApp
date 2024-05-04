package com.koleff.kare_android.data.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Deprecated("The idea was to configure the rest in the workout configuration dialog but this will be done in ExerciseDetailsConfigurator screen...")
@Parcelize
data class ExerciseConfigurationDto(
    val name: String,
    val exerciseId: Int,
    val workoutId: Int,
    var durationTime: ExerciseTime,
    var includeRest: Boolean,
    var restTime: ExerciseTime
): Parcelable