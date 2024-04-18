package com.koleff.kare_android.data.model.dto

import com.koleff.kare_android.data.KareEntity
import com.koleff.kare_android.data.room.entity.DoWorkoutExerciseSet
import com.squareup.moshi.Json
import java.util.Date
import java.util.UUID

data class DoWorkoutExerciseSetDto(
    @field:Json(name = "instance_id")
    val instanceId: UUID = UUID.randomUUID(),
    @field:Json(name = "workout_performance_metrics_id")
    val workoutPerformanceMetricsId: Int,  //Link to DoWorkoutPerformanceMetrics
    @field:Json(name = "workout_id")
    val workoutId: Int,
    @field:Json(name = "exercise_id")
    val exerciseId: Int,
    @field:Json(name = "template_set_id")
    val templateSetId: UUID,  //Link to the ExerciseSet -> template
    @field:Json(name = "reps")
    val reps: Int,
    @field:Json(name = "weight")
    val weight: Float?,
    @field:Json(name = "time")
    val time: ExerciseTime?,
    @field:Json(name = "date")
    val date: Date  //to record the exact time the workout was completed
) : KareEntity<DoWorkoutExerciseSet> {
    override fun toEntity(): DoWorkoutExerciseSet {
        return DoWorkoutExerciseSet(
            instanceId = instanceId,
            workoutPerformanceMetricsId = workoutPerformanceMetricsId,
            workoutId = workoutId,
            exerciseId = exerciseId,
            templateSetId = templateSetId,
            reps = reps,
            weight = weight,
            time = time,
            date = date
        )
    }
}