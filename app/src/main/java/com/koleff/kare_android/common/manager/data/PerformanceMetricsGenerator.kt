package com.koleff.kare_android.common.manager.data

import com.koleff.kare_android.common.DateUtils
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto

object PerformanceMetricsGenerator {

    const val TOTAL_WORKOUTS = 3

    fun generatePerformanceMetrics(): List<DoWorkoutPerformanceMetricsDto> {
        val workouts = WorkoutGenerator.getAllWorkouts()
        val workoutList = listOf(
            workouts[0],
            workouts[1],
            workouts[1],
            workouts[2]
        )

        val workoutDetails= WorkoutGenerator.getAllWorkoutDetails()
        val workoutDetailsList = listOf(
            workoutDetails[0],
            workoutDetails[1],
            workoutDetails[1],
            workoutDetails[2]
        )

        val date1 = DateUtils.getYesterdayDate()
        val date2 = DateUtils.getTodayDate()
        val date3 = DateUtils.getRandomDate()
        val dates = listOf(date1, date2, date2, date3)

        val performanceMetricsList = mutableListOf<DoWorkoutPerformanceMetricsDto>()
        repeat(4) { it ->
            performanceMetricsList.add(
                DoWorkoutPerformanceMetricsDto(
                    workout = workoutList[it].toDto(),
                    date = dates[it],
                    doWorkoutExerciseSets = MockupDataGeneratorV2.generateDoWorkoutExerciseSets(
                        performanceMetricsId = it + 1,
                        muscleGroup = workoutList[it].muscleGroup,
                        numberOfExercises = workoutList[it].totalExercises,
                        workoutId = workoutList[it].workoutId,
                        exercises = workoutDetailsList[it].exercises?.map { it.toDto() }
                    )
                )
            )
        }

        return performanceMetricsList
    }
}