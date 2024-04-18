package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.room.entity.DoWorkoutExerciseSet
import com.koleff.kare_android.data.room.entity.DoWorkoutPerformanceMetrics

data class DoWorkoutPerformanceWithSets(
    @Embedded val performanceMetrics: DoWorkoutPerformanceMetrics,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutPerformanceMetricsId"
    )
    val exerciseSets: List<DoWorkoutExerciseSet>
){
    fun toDoWorkoutPerformanceMetricsDto(): DoWorkoutPerformanceMetricsDto{
        return DoWorkoutPerformanceMetricsDto(
            id = performanceMetrics.id,
            workoutId = performanceMetrics.workoutId,
            date = performanceMetrics.date,
            doWorkoutExerciseSets = exerciseSets.map { it.toDoWorkoutExerciseSetDto() }
        )
    }
}