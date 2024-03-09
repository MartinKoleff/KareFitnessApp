package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDto

@Entity(tableName = "workout_table")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val workoutId: Int,
    var name: String,
    var muscleGroup: MuscleGroup,
    var snapshot: String,
    var totalExercises: Int,
    var isSelected: Boolean,
) {
    fun toWorkoutDto(): WorkoutDto {
        return WorkoutDto(
            workoutId = workoutId,
            name = name,
            muscleGroup = muscleGroup,
            snapshot = snapshot,
            totalExercises = totalExercises,
            isSelected = isSelected
        )
    }
}