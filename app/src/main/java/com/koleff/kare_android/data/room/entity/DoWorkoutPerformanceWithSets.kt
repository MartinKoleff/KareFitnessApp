package com.koleff.kare_android.data.room.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.koleff.kare_android.data.KareDto
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto

data class DoWorkoutPerformanceWithSets(
    @Embedded val performanceMetrics: DoWorkoutPerformanceMetrics,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutPerformanceMetricsId"
    )
    val exerciseSets: List<DoWorkoutExerciseSet>
): KareDto<DoWorkoutPerformanceMetricsDto> {
    override fun toDto(): DoWorkoutPerformanceMetricsDto{
        return DoWorkoutPerformanceMetricsDto(
            id = performanceMetrics.id,
            workoutId = performanceMetrics.workoutId,
            date = performanceMetrics.date,
            doWorkoutExerciseSets = exerciseSets.map { it.toDto() }
        )
    }
}