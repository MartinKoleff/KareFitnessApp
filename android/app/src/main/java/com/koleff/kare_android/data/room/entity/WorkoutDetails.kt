package com.koleff.kare_android.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto

@Entity(tableName = "workout_details_table")
data class WorkoutDetails(
    @PrimaryKey
    val workoutDetailsId: Int,
    val name: String,
    val description: String,
    val muscleGroup: MuscleGroup,
    val isSelected: Boolean
){
    fun toWorkoutDetailsDto(): WorkoutDetailsDto {
        return WorkoutDetailsDto(
            workoutId = workoutDetailsId,
            name = name,
            description = description,
            muscleGroup = muscleGroup,
            exercises = emptyList(), // You'll fetch and populate this in the DAO
            isSelected = isSelected
        )
    }
}
