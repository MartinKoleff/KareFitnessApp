package com.koleff.kare_android.data.room.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.koleff.kare_android.data.KareDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto

data class WorkoutDetailsWithExercises(
    @Embedded
    val workoutDetails: WorkoutDetails,

    @Relation(
        parentColumn = "workoutDetailsId",
        entityColumn = "workoutId",
        entity = Exercise::class,
    )
    val exercises: List<Exercise>?
) : KareDto<WorkoutDetailsDto> {
    val safeExercises: List<Exercise>
        get() = exercises ?: emptyList()

    override fun toDto(): WorkoutDetailsDto {
        return WorkoutDetailsDto(
            workoutId = workoutDetails.workoutDetailsId,
            name = workoutDetails.name,
            description = workoutDetails.description,
            muscleGroup = workoutDetails.muscleGroup,
            exercises = (exercises?.map {
                it.toDto(sets = emptyList())
            } ?: emptyList()).toMutableList(),
            isSelected = workoutDetails.isSelected
        )
    }
}