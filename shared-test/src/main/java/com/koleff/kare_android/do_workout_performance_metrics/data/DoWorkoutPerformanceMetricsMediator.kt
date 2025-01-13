package com.koleff.kare_android.do_workout_performance_metrics.data

import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.room.dao.DoWorkoutExerciseSetDao
import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.data.room.entity.DoWorkoutExerciseSet
import com.koleff.kare_android.data.room.entity.DoWorkoutPerformanceMetrics
import com.koleff.kare_android.data.room.entity.DoWorkoutPerformanceWithSets
import com.koleff.kare_android.utils.FakeDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.workout.data.ExerciseChangeListener
import com.koleff.kare_android.workout.data.WorkoutDetailsChangeListener
import com.koleff.kare_android.workout.data.WorkoutConfigurationChangeListener
import com.koleff.kare_android.utils.TestLogger
import java.util.Date
import java.util.UUID

class DoWorkoutPerformanceMetricsMediator(
    private val exerciseChangeListener: ExerciseChangeListener,
    private val workoutDetailsChangeListener: WorkoutDetailsChangeListener,
    private val workoutConfigurationChangeListener: WorkoutConfigurationChangeListener,
    private val logger: TestLogger
) : DoWorkoutPerformanceMetricsDao, DoWorkoutExerciseSetDao, WorkoutDao, FakeDao {

    private val workoutPerformanceMetricsDB = mutableListOf<DoWorkoutPerformanceWithSets>()
    private val exerciseSetDB = mutableListOf<DoWorkoutExerciseSet>()
    private var workoutsDB = mutableListOf<Workout>()
    private var autoIncrementId = 1

    override suspend fun insertWorkoutPerformanceMetrics(performanceMetrics: DoWorkoutPerformanceMetrics): Long {
        val updatedPerformanceMetrics =
            performanceMetrics.copy(id = workoutPerformanceMetricsDB.size + 1)

        val workout = workoutsDB.firstOrNull { it.workoutId == performanceMetrics.workoutId }
            ?: WorkoutDto().toEntity()
        val newEntry = DoWorkoutPerformanceWithSets(
            performanceMetrics = updatedPerformanceMetrics,
            exerciseSets = emptyList(),
            workout = workout
        )

        workoutPerformanceMetricsDB.add(newEntry)
        return workoutPerformanceMetricsDB.size.toLong()  //Mimics auto-generate by returning the size as ID
    }

    override suspend fun updateDoWorkoutPerformanceMetrics(performanceMetrics: DoWorkoutPerformanceMetrics) {
        val index =
            workoutPerformanceMetricsDB.indexOfFirst { it.performanceMetrics.id == performanceMetrics.id }

        //Entry was found in DB
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
        return findSetByPerformanceMetricsId(doWorkoutPerformanceMetricsId)
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
            .also {
                logger.i("TEST", "${workoutsDB}\n${workoutPerformanceMetricsDB}")
            }
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

    private suspend fun updatePerformanceMetricsAfterSetInsertion(exerciseSet: DoWorkoutExerciseSet) {
        val performanceMetricsWithSets =
            getWorkoutPerformanceMetricsById(exerciseSet.workoutPerformanceMetricsId) ?: return

        val updatedSets = performanceMetricsWithSets.exerciseSets.toMutableList()
        updatedSets.add(exerciseSet)

        val updatedPerformanceMetricsWithSets = performanceMetricsWithSets.copy(
            exerciseSets = updatedSets
        )

        //Update entry
        updateDoWorkoutPerformanceMetricsWithSets(updatedPerformanceMetricsWithSets)
    }

    private fun updateDoWorkoutPerformanceMetricsWithSets(performanceMetricsWithSets: DoWorkoutPerformanceWithSets) {
        val index =
            workoutPerformanceMetricsDB.indexOfFirst { it.performanceMetrics.id == performanceMetricsWithSets.performanceMetrics.id }

        //Entry was found in DB
        if (index != -1) {
            workoutPerformanceMetricsDB[index] = performanceMetricsWithSets
        }
    }

    //DoWorkoutExerciseSetDao

    override suspend fun insertSet(exerciseSet: DoWorkoutExerciseSet): Long {
        exerciseSetDB.add(exerciseSet)

        //Insert DoWorkoutPerformanceMetrics - DoWorkoutExerciseSet cross ref
        updatePerformanceMetricsAfterSetInsertion(exerciseSet)

        return exerciseSetDB.size.toLong() //Mimics auto-generate by returning the size as ID
    }

    override suspend fun insertAllSets(exerciseSets: List<DoWorkoutExerciseSet>) {
//        exerciseSetDB.addAll(exerciseSets)

        //Insert DoWorkoutPerformanceMetrics - DoWorkoutExerciseSet cross refs
        exerciseSets.forEach { exerciseSet ->
            exerciseSetDB.add(exerciseSet)

            updatePerformanceMetricsAfterSetInsertion(exerciseSet)
        }
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

    override suspend fun insertWorkout(workout: Workout): Long {
        return if (workout.workoutId == 0) {
            workoutsDB.add(workout.copy(workoutId = autoIncrementId))
            autoIncrementId++.toLong()
        } else {
            workoutsDB.add(workout)
            addWorkoutToPerformanceMetrics(workout)

            return workout.workoutId.toLong()
        }
    }

    private fun addWorkoutToPerformanceMetrics(workout: Workout) {
        val selectedPerformanceMetrics = workoutPerformanceMetricsDB
            .firstOrNull { it.performanceMetrics.workoutId == workout.workoutId }
        selectedPerformanceMetrics ?: return

        val updatedPerformanceMetrics = selectedPerformanceMetrics.copy(workout = workout)
        val index = workoutPerformanceMetricsDB.indexOfFirst {
            it.performanceMetrics.workoutId == workout.workoutId
        }
        workoutPerformanceMetricsDB[index] = updatedPerformanceMetrics
    }

    override suspend fun insertAllWorkouts(workouts: List<Workout>) {
        workouts.forEach { insertWorkout(it) }
    }

    override suspend fun deleteWorkout(workoutId: Int) {

        //If there are multiple entries -> remove all
        workoutsDB.removeAll { it.workoutId == workoutId }

        //Delete workout details, exercises, sets, workout configuration...
        exerciseChangeListener.onExercisesDeleted(workoutId)
        workoutConfigurationChangeListener.onWorkoutConfigurationDeleted(workoutId)
        workoutDetailsChangeListener.onWorkoutDetailsDeleted(workoutId)
    }

    override suspend fun updateWorkout(workout: Workout) {

        //Find workout
        val index = workoutsDB.indexOfFirst { it.workoutId == workout.workoutId }

        //Workout found
        if (index != -1) {

            //Replace workout
            workoutsDB[index] = workout
        } else {

            //Delete invalid workout?
        }
    }

    override suspend fun favoriteWorkoutById(workoutId: Int) {

        //Find workout
        val index = workoutsDB.indexOfFirst { it.workoutId == workoutId }

        //Workout found
        if (index != -1) {

            //Replace workout
            workoutsDB[index] = workoutsDB[index].copy(isFavorite = true)
        } else {

            //Delete invalid workout?
        }
    }

    override suspend fun unfavoriteWorkoutById(workoutId: Int) {

        //Find workout
        val index = workoutsDB.indexOfFirst { it.workoutId == workoutId }

        //Workout found
        if (index != -1) {

            //Replace workout
            workoutsDB[index] = workoutsDB[index].copy(isFavorite = false)
        } else {

            //Delete invalid workout?
        }
    }

    override fun getWorkoutsOrderedById(): List<Workout> = workoutsDB.sortedBy { it.workoutId }

    override fun getWorkoutByIsFavorite(): List<Workout> = workoutsDB.filter { it.isFavorite }

    override fun getWorkoutById(workoutId: Int): Workout =
        workoutsDB.first { it.workoutId == workoutId }

    override fun clearDB() {
        workoutsDB.clear()

        exerciseSetDB.clear()
        workoutPerformanceMetricsDB.clear()
    }
}
