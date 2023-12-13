package com.koleff.kare_android.data.model.dto

import android.os.Parcelable
import com.koleff.kare_android.data.room.entity.Exercise
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseDto(
    @field:Json(name = "id")
    val exerciseId: Int = -1,
    @field:Json(name = "name")
    val name: String = "",
    @field:Json(name = "muscle_group")
    val muscleGroup: MuscleGroup = MuscleGroup.NONE,
    @field:Json(name = "machine")
    val machineType: MachineType = MachineType.NONE,
    @field:Json(name = "snapshot") //TODO: image...
    val snapshot: String = "",
    @field:Json(name = "sets")
    val sets: List<ExerciseSet> = emptyList(),
): Parcelable{
    fun toExercise(): Exercise {
        //TODO: update with set...
        return Exercise(
            exerciseId = exerciseId,
            name = name,
            muscleGroup = muscleGroup,
            machineType = machineType,
            snapshot = snapshot
        )
    }
}
