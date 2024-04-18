package com.koleff.kare_android.data.model.dto

import com.koleff.kare_android.data.model.KareEntity
import com.koleff.kare_android.data.room.entity.ExerciseDetails
import com.squareup.moshi.Json

data class ExerciseDetailsDto(
    @field:Json(name = "exercise_id")
    val id: Int = -1,
    @field:Json(name = "workout_id")
    val workoutId: Int = -1,
    @field:Json(name = "name")
    val name: String = "",
    @field:Json(name = "description")
    val description: String = "",
    @field:Json(name = "muscle_group")
    val muscleGroup: MuscleGroup = MuscleGroup.NONE,
    @field:Json(name = "machine")
    val machineType: MachineType = MachineType.NONE,
    @field:Json(name = "video_url")
    val videoUrl: String = ""
) : KareEntity<ExerciseDetails> {
    override fun toEntity(): ExerciseDetails {
        return ExerciseDetails(
            exerciseDetailsId = id,
            workoutId = workoutId,
            name = name,
            description = description,
            muscleGroup = muscleGroup,
            machineType = machineType,
            videoUrl = videoUrl
        )
    }
}
