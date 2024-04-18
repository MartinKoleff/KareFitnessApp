package com.koleff.kare_android.data.model.dto

import com.koleff.kare_android.data.model.KareEntity
import com.koleff.kare_android.data.room.entity.DoWorkoutPerformanceMetrics
import com.squareup.moshi.Json
import java.util.Date

data class DoWorkoutPerformanceMetricsDto(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "workout_id")
    val workoutId: Int,
    @field:Json(name = "date")
    val date: Date,  //to record the exact time the workout was completed
    @field:Json(name = "do_workout_exercise_sets")
    val doWorkoutExerciseSets: List<DoWorkoutExerciseSetDto>
): KareEntity<DoWorkoutPerformanceMetrics> {
    override fun toEntity(): DoWorkoutPerformanceMetrics {
        return DoWorkoutPerformanceMetrics(
            id = id,
            workoutId = workoutId,
            date = date
        )
    }
}