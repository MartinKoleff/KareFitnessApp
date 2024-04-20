package com.koleff.kare_android.do_workout_performance_metrics.data

import com.koleff.kare_android.data.room.dao.DoWorkoutExerciseSetDao
import com.koleff.kare_android.data.room.entity.DoWorkoutExerciseSet
import com.koleff.kare_android.data.room.entity.ExerciseSet
import java.util.UUID

class DoWorkoutExerciseSetDaoFake
    (private val doWorkoutPerformanceMetricsDaoFake: DoWorkoutPerformanceMetricsDaoFake): DoWorkoutExerciseSetDao {

    private val exerciseSetDB = mutableListOf<DoWorkoutExerciseSet>()

    override suspend fun insertSet(exerciseSet: DoWorkoutExerciseSet): Long {
        exerciseSetDB.add(exerciseSet)

        //Insert DoWorkoutPerformanceMetrics - DoWorkoutExerciseSet cross ref
        doWorkoutPerformanceMetricsDaoFake.updatePerformanceMetricsAfterSetInsertion(exerciseSet)

        return exerciseSetDB.size.toLong() //Mimics auto-generate by returning the size as ID
    }

    override suspend fun insertAllSets(exerciseSets: List<DoWorkoutExerciseSet>) {
        exerciseSetDB.addAll(exerciseSets)
    }

    override suspend fun updateSet(exerciseSet: DoWorkoutExerciseSet): Int {

        //Find exercise set
        val index = exerciseSetDB.indexOf(exerciseSet)

        //Exercise found
        if (index != -1) {
            exerciseSetDB[index] = exerciseSet
        } else {

            //Delete invalid exercise set?
        }

        return index
    }

    override suspend fun deleteSet(exerciseSet: DoWorkoutExerciseSet) {
        exerciseSetDB.removeAll { it.instanceId == exerciseSet.instanceId }
    }

    override suspend fun findSetByPerformanceMetricsId(workoutPerformanceMetricsId: Int): List<DoWorkoutExerciseSet> {
        return exerciseSetDB.filter { it.workoutPerformanceMetricsId == workoutPerformanceMetricsId }
    }

    override suspend fun findSetById(instanceId: UUID): DoWorkoutExerciseSet? {
        return exerciseSetDB.first { it.instanceId == instanceId }
    }

    override suspend fun findSetsByWorkoutId(workoutId: Int): List<DoWorkoutExerciseSet> {
        return exerciseSetDB.filter { it.workoutId == workoutId }
    }


    fun clearDB() {
        exerciseSetDB.clear()
    }
}