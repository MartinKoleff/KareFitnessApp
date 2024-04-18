package com.koleff.kare_android.data.model.dto

import android.os.Parcelable
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.model.KareEntity
import com.koleff.kare_android.data.room.entity.Exercise
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseDto(
    @field:Json(name = "exercise_id")
    val exerciseId: Int = -1,
    @field:Json(name = "workout_id")
    val workoutId: Int = -1,
    @field:Json(name = "name")
    val name: String = "",
    @field:Json(name = "muscle_group")
    val muscleGroup: MuscleGroup = MuscleGroup.NONE,
    @field:Json(name = "machine")
    val machineType: MachineType = MachineType.NONE,
    @field:Json(name = "snapshot") //TODO: image...
    val snapshot: String = "",
    @field:Json(name = "sets")
    val sets: List<ExerciseSetDto> = emptyList()
): Parcelable, KareEntity<Exercise> {
    override fun toEntity(): Exercise {
        return Exercise(
            exerciseId = exerciseId,
            workoutId = workoutId,
            name = name,
            muscleGroup = muscleGroup,
            machineType = machineType,
            snapshot = snapshot
        )
    }
}
