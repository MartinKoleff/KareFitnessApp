package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.KareDtoExtended
import com.koleff.kare_android.data.KareDtoExtended2
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto

@Entity(
    tableName = "workout_details_table",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["workoutId"],
            childColumns = ["workoutDetailsId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutDetails(
    @PrimaryKey
    val workoutDetailsId: Int,
    val name: String,
    val description: String,
    val muscleGroup: MuscleGroup,
    var isFavorite: Boolean
): KareDtoExtended2<WorkoutDetailsDto, MutableList<ExerciseDto>, WorkoutConfigurationDto> {
    override fun toDto(exercises: MutableList<ExerciseDto>, configuration: WorkoutConfigurationDto): WorkoutDetailsDto {
        return WorkoutDetailsDto(
            workoutId = workoutDetailsId,
            name = name,
            description = description,
            muscleGroup = muscleGroup,
            exercises = exercises,
            isFavorite = isFavorite,
            configuration = configuration
        )
    }
}
