package com.koleff.kare_android.data.model.dto

import com.koleff.kare_android.data.KareEntity
import com.koleff.kare_android.data.WorkoutDetailsExtended
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.squareup.moshi.Json

data class WorkoutDetailsDto(
    @field:Json(name = "id")
    val workoutId: Int = 0, //0 -> DB generates new id
    @field:Json(name = "name")
    val name: String = "",
    @field:Json(name = "description")
    val description: String = "",
    @field:Json(name = "muscle_group")
    val muscleGroup: MuscleGroup = MuscleGroup.NONE,
    @field:Json(name = "exercises")
    val exercises: List<ExerciseDto> = mutableListOf(),
    @field:Json(name = "is_favorite")
    val isFavorite: Boolean = false,
    @field:Json(name = "configuration")
    val configuration: WorkoutConfigurationDto = WorkoutConfigurationDto()
): KareEntity<WorkoutDetails>, WorkoutDetailsExtended {
    override fun toEntity(): WorkoutDetails{
        return WorkoutDetails(
            workoutDetailsId = workoutId,
            name = name,
            description = description,
            muscleGroup = muscleGroup,
            isFavorite = isFavorite
        )
    }

    override fun toWorkout(): WorkoutDto {
        return WorkoutDto(
            //TODO: add...
        )
    }
}
