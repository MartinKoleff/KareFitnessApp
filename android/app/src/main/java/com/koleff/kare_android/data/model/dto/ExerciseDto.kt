package com.koleff.kare_android.data.model.dto

import android.os.Parcelable
import com.koleff.kare_android.data.room.entity.Exercise
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseDto(
    @field:Json(name = "id")
    val exerciseId: Int,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "muscle_group")
    val muscleGroup: MuscleGroup,
    @field:Json(name = "machine")
    val machineType: MachineType,
    @field:Json(name = "snapshot") //TODO: image...
    val snapshot: String,
    @field:Json(name = "sets")
    val sets: Int = 0,
    @field:Json(name = "reps")
    val reps: Int = 0,
    @field:Json(name = "weight")
    val weight: Int = 0,
): Parcelable{
    fun toExercise(): Exercise {
        return Exercise(
            exerciseId = exerciseId,
            name = name,
            muscleGroup = muscleGroup,
            machineType = machineType,
            snapshot = snapshot
        )
    }
}
