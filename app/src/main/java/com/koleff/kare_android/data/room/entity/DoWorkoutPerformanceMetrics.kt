package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.KareDtoExtended
import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import java.util.*

@Entity(
    tableName = "do_workout_performance_metrics",
)
data class DoWorkoutPerformanceMetrics (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val workoutId: Int,
    val date: Date  //to record the exact time the workout was completed
): KareDtoExtended<DoWorkoutPerformanceMetricsDto, List<DoWorkoutExerciseSetDto>> {
    override fun toDto(doWorkoutExerciseSets: List<DoWorkoutExerciseSetDto>): DoWorkoutPerformanceMetricsDto {
        return DoWorkoutPerformanceMetricsDto(
            id = if(id == 0) 0 else id,
            workoutId = workoutId,
            date = date,
            doWorkoutExerciseSets = doWorkoutExerciseSets
        )
    }
}