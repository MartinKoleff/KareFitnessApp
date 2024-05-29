package com.koleff.kare_android.data.room.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.koleff.kare_android.data.KareDto
import com.koleff.kare_android.data.KareDtoExtended
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto

data class WorkoutDetailsWithExercises(
    @Embedded
    val workoutDetails: WorkoutDetails,

    @Relation(
        parentColumn = "workoutDetailsId",
        entityColumn = "workoutId",
        entity = Exercise::class,
    )
    val exercises: List<ExerciseWithSets>?,

    @Relation(
        parentColumn = "workoutDetailsId",
        entityColumn = "workoutId"
    )
    val configuration: WorkoutConfiguration?,
) : KareDto<WorkoutDetailsDto> {
    val safeExercises: List<ExerciseWithSets>
        get() = exercises ?: emptyList()

    override fun toDto(): WorkoutDetailsDto {
        return WorkoutDetailsDto(
            workoutId = workoutDetails.workoutDetailsId,
            name = workoutDetails.name,
            description = workoutDetails.description,
            muscleGroup = workoutDetails.muscleGroup,
            exercises = (exercises?.map {
                it.toDto()
            } ?: emptyList()),
            isFavorite = workoutDetails.isFavorite,
            configuration = configuration?.toDto()
                ?: WorkoutConfigurationDto(workoutId = workoutDetails.workoutDetailsId) //Default configuration if there is no in DB...
        )
    }
}