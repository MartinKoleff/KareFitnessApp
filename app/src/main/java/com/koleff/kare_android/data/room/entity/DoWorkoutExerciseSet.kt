package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.koleff.kare_android.data.KareDto
import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.koleff.kare_android.data.model.dto.ExerciseTime
import java.util.*

@Entity(
    tableName = "do_workout_exercise_set",
    foreignKeys = [
        ForeignKey(
            entity = DoWorkoutPerformanceMetrics::class,
            parentColumns = ["id"],
            childColumns = ["workoutPerformanceMetricsId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseSet::class,
            parentColumns = ["setId"],
            childColumns = ["templateSetId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DoWorkoutExerciseSet(
    @PrimaryKey(autoGenerate = false)
    val instanceId: UUID = UUID.randomUUID(),
    val workoutPerformanceMetricsId: Int,  //Link to DoWorkoutPerformanceMetrics
    val workoutId: Int,
    val exerciseId: Int,
    val templateSetId: UUID,  //Link to the ExerciseSet -> template
    val reps: Int,
    val weight: Float?,
    val isDone: Boolean,
    val time: ExerciseTime?,
    val date: Date  //to record the exact time the workout was completed
): KareDto<DoWorkoutExerciseSetDto> {
    override fun toDto(): DoWorkoutExerciseSetDto {
        return DoWorkoutExerciseSetDto(
            instanceId = instanceId,
            workoutPerformanceMetricsId = workoutPerformanceMetricsId,
            workoutId = workoutId,
            exerciseId = exerciseId,
            templateSetId = templateSetId,
            reps = reps,
            weight = weight,
            isDone = isDone,
            time = time,
            date = date
        )
    }
}