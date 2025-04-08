package com.koleff.kare_android.data.room.manager

import com.google.firebase.crashlytics.internal.Logger
import com.koleff.kare_android.common.manager.data.WorkoutPerformanceMetricsGenerator
import com.koleff.kare_android.data.room.dao.DoWorkoutExerciseSetDao
import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutPerformanceMetricsDBManager @Inject constructor(
    private val doWorkoutPerformanceMetricsDao: DoWorkoutPerformanceMetricsDao,
    private val doWorkoutExerciseSetDao: DoWorkoutExerciseSetDao,
    private val exerciseSetDao: ExerciseSetDao,
    private val workoutDao: WorkoutDao,
    private val hasInitializedDB: Boolean
) {

    //Pre-requisite -> Workout, exercises and exercise sets are in DB.
    suspend fun initializeWorkoutPerformanceMetricsTable(onDBInitialized: () -> Unit) =
        withContext(Dispatchers.IO) {
            if (hasInitializedDB) return@withContext

            val performanceMetricsList =
                WorkoutPerformanceMetricsGenerator.getAllWorkoutPerformanceMetrics(exerciseSetDao)

            //Create Performance Metrics
            performanceMetricsList.forEach { performanceMetrics ->
                doWorkoutPerformanceMetricsDao.insertWorkoutPerformanceMetrics(performanceMetrics.toEntity())
            }

            //Create Exercise Sets
            Logger.getLogger().i("--------Exercise sets DB--------")
            exerciseSetDao.getAllSets().forEach {
                Logger.getLogger().i("[WorkoutPerformanceMetricsDBManager] Exercise set: $it")
            }
            Logger.getLogger().i("--------------------------------")
            performanceMetricsList
                .map { it.doWorkoutExerciseSets }
                .forEach { exerciseSets ->
                    exerciseSets.forEach {
                        try {
                            Logger.getLogger().i("Do workout Exercise set: $it")
                            Logger.getLogger().i("Exercise set: ${exerciseSetDao.getSetById(it.templateSetId)}")
                            Logger.getLogger().i("Workout: ${workoutDao.getWorkoutById(it.workoutId)}")
                            Logger.getLogger().i("Performance Metrics: ${doWorkoutPerformanceMetricsDao.getWorkoutPerformanceMetricsById(it.workoutPerformanceMetricsId)}")
                            Logger.getLogger().i("--------------------------------")

                            doWorkoutExerciseSetDao.insertSet(it.toEntity())
//                          doWorkoutExerciseSetDao.insertAllSets(
//                              exerciseSets.map { it.toEntity() }
//                           )
                        } catch (e: Exception) {
                            Logger.getLogger().e("SQL Error inserting exercise set.")
                        }
                    }

                }

            //Initialization callback
            onDBInitialized()
        }
}