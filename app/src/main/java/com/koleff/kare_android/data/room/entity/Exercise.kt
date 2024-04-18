package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import com.koleff.kare_android.data.KareDtoExtended
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup

@Entity(
    tableName = "exercise_table",
    primaryKeys = ["exerciseId", "workoutId"],  //Composite key
//    foreignKeys = [
//        ForeignKey(
//            entity = Workout::class,
//            parentColumns = ["workoutId"],
//            childColumns = ["workoutId"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
)
data class Exercise(
    val exerciseId: Int,
    val workoutId: Int,
    val name: String,
    val muscleGroup: MuscleGroup,
    val machineType: MachineType,
    val snapshot: String,
): KareDtoExtended<ExerciseDto, List<ExerciseSet>> {
    override fun toDto(sets: List<ExerciseSet>): ExerciseDto {
        return ExerciseDto(
            exerciseId = this.exerciseId,
            workoutId = this.workoutId,
            name = this.name,
            muscleGroup = this.muscleGroup,
            machineType = this.machineType,
            snapshot = this.snapshot,
            sets = sets
                .map { it.toDto() }
                .toList()
        )
    }
}
