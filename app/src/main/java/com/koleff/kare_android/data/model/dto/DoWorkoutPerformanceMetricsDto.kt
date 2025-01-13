package com.koleff.kare_android.data.model.dto

import com.koleff.kare_android.data.KareEntity
import com.koleff.kare_android.data.room.entity.DoWorkoutPerformanceMetrics
import com.squareup.moshi.Json
import java.util.Date

data class DoWorkoutPerformanceMetricsDto(
    @field:Json(name = "id")
    val id: Int = 0,
    @field:Json(name = "workout")
    val workout: WorkoutDto = WorkoutDto(),
    @field:Json(name = "date")
    val date: Date = Date(),  //to record the exact time the workout was completed
    @field:Json(name = "do_workout_exercise_sets")
    val doWorkoutExerciseSets: List<DoWorkoutExerciseSetDto> = emptyList()
): KareEntity<DoWorkoutPerformanceMetrics> {
    override fun toEntity(): DoWorkoutPerformanceMetrics {
        return DoWorkoutPerformanceMetrics(
            id = id,
            workoutId = workout.workoutId,
            date = date
	)
	}
}