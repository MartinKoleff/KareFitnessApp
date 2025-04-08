package com.koleff.kare_android.common.manager.data

import com.google.firebase.crashlytics.internal.Logger
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.room.dao.ExerciseSetDao

object WorkoutPerformanceMetricsGenerator {

    fun getAllWorkoutPerformanceMetrics(exerciseSetDao: ExerciseSetDao): List<DoWorkoutPerformanceMetricsDto> {
        val workoutDetails = WorkoutGenerator.getAllWorkoutDetails()
        Logger.getLogger().i("--------------------------------")
        workoutDetails.map { it.exercises }.forEach { exerciseSets ->
            exerciseSets?.forEach { exerciseWithSets ->
                exerciseWithSets.sets.forEach {exerciseSet ->
                    Logger.getLogger().i("[WorkoutPerformanceMetricsGenerator] Exercise Set with id ${exerciseSet.setId} from DB: ${exerciseSetDao.getSetById(exerciseSet.setId)}")
                }
            }
        }
        Logger.getLogger().i("--------------------------------")


        var counter = 1
        val performanceMetrics = workoutDetails.map { currentWorkoutDetails ->
            MockupDataGeneratorV2.generateDoWorkoutPerformanceMetrics(
                id = counter++,
                workout = currentWorkoutDetails.toDto().toWorkout(),
                exercises = currentWorkoutDetails.exercises?.map { it.toDto() } ?: emptyList()
            )
        }

        return performanceMetrics
    }
}