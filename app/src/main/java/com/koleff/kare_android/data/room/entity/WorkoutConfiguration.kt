package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.koleff.kare_android.data.KareDto
import com.koleff.kare_android.data.KareEntity
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto

@Entity(
    tableName = "workout_configuration_table",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutDetails::class,
            parentColumns = ["workoutDetailsId"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutConfiguration(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val workoutId: Int,
    var cooldownTime: ExerciseTime
): KareDto<WorkoutConfigurationDto>{

    override fun toDto(): WorkoutConfigurationDto {
        return WorkoutConfigurationDto(
            workoutId = workoutId,
            cooldownTime = cooldownTime
        )
    }
}