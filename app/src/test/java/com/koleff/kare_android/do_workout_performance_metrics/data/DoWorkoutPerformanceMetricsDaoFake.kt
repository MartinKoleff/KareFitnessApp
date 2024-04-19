package com.koleff.kare_android.do_workout_performance_metrics.data

import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.data.room.entity.DoWorkoutExerciseSet
import com.koleff.kare_android.data.room.entity.DoWorkoutPerformanceMetrics
import com.koleff.kare_android.data.room.entity.DoWorkoutPerformanceWithSets
import java.util.Date

class DoWorkoutPerformanceMetricsDaoFake(
    private val doWorkoutExerciseSetDaoFake: DoWorkoutExerciseSetDaoFake
) : DoWorkoutPerformanceMetricsDao {

    private val workoutPerformanceMetricsDB = mutableListOf<DoWorkoutPerformanceWithSets>()

    override suspend fun insertWorkoutPerformanceMetrics(performanceMetrics: DoWorkoutPerformanceMetrics): Long {
        val newEntry = DoWorkoutPerformanceWithSets(
            performanceMetrics = performanceMetrics,
            exerciseSets = emptyList()
        )
        workoutPerformanceMetricsDB.add(newEntry)
        return workoutPerformanceMetricsDB.size.toLong()  //Mimics auto-generate by returning the size as ID
    }

    override suspend fun updateDoWorkoutPerformanceMetrics(performanceMetrics: DoWorkoutPerformanceMetrics) {
        val index = workoutPerformanceMetricsDB.indexOfFirst { it.performanceMetrics.id == performanceMetrics.id }

        if (index != -1) {
            workoutPerformanceMetricsDB[index] = workoutPerformanceMetricsDB[index].copy(
                performanceMetrics = performanceMetrics
            )
        }
    }

    override suspend fun deleteWorkoutPerformanceMetrics(performanceMetrics: DoWorkoutPerformanceMetrics) {
        workoutPerformanceMetricsDB.removeAll { it.performanceMetrics.id == performanceMetrics.id }
    }

    override suspend fun getWorkoutPerformanceMetricsById(id: Int): DoWorkoutPerformanceWithSets? {
        val dbEntry = workoutPerformanceMetricsDB.find { it.performanceMetrics.id == id }
        val sets = dbEntry?.let { getExerciseSets(it.performanceMetrics.id) }
        return dbEntry?.copy(exerciseSets = sets ?: emptyList())
    }

    private suspend fun getExerciseSets(doWorkoutPerformanceMetricsId: Int): List<DoWorkoutExerciseSet> {
        return doWorkoutExerciseSetDaoFake.findSetsByWorkoutData(doWorkoutPerformanceMetricsId)
    }

    override suspend fun getAllWorkoutPerformanceMetricsById(
        id: Int,
        start: Date,
        end: Date
    ): List<DoWorkoutPerformanceWithSets> {
        return workoutPerformanceMetricsDB.filter {
            it.performanceMetrics.id == id &&
                    !it.performanceMetrics.date.before(start) &&
                    !it.performanceMetrics.date.after(end)
        }
    }

    override suspend fun getAllWorkoutPerformanceMetricsByWorkoutId(
        workoutId: Int,
        start: Date,
        end: Date
    ): List<DoWorkoutPerformanceWithSets> {
        return workoutPerformanceMetricsDB.filter {
            it.performanceMetrics.workoutId == workoutId &&
                    !it.performanceMetrics.date.before(start) &&
                    !it.performanceMetrics.date.after(end)
        }
    }

    override suspend fun getAllWorkoutPerformanceMetricsByWorkoutId(workoutId: Int): List<DoWorkoutPerformanceWithSets> {
        return workoutPerformanceMetricsDB.filter { it.performanceMetrics.workoutId == workoutId }
    }

    override suspend fun getAllWorkoutPerformanceMetrics(): List<DoWorkoutPerformanceWithSets> {
        return workoutPerformanceMetricsDB.toList()
    }

    override suspend fun getAllWorkoutPerformanceMetrics(
        start: Date,
        end: Date
    ): List<DoWorkoutPerformanceWithSets> {
        return workoutPerformanceMetricsDB.filter {
            !it.performanceMetrics.date.before(start) &&
                    !it.performanceMetrics.date.after(end)
        }
    }

    override fun deleteWorkoutPerformanceMetricsById(id: Int) {
        workoutPerformanceMetricsDB.removeAll { it.performanceMetrics.id == id }
    }
}