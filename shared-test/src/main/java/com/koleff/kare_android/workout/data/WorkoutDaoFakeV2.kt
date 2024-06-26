package com.koleff.kare_android.workout.data

import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.utils.FakeDao

class WorkoutDaoFakeV2(
    private val exerciseChangeListener: ExerciseChangeListener,
    private val workoutDetailsChangeListener: WorkoutDetailsChangeListener,
    private val workoutConfigurationChangeListener: WorkoutConfigurationChangeListener
) : WorkoutDao, FakeDao {

    private var workoutsDB = mutableListOf<Workout>()

    private var autoIncrementId = 1

    override suspend fun insertWorkout(workout: Workout): Long {
        return if (workout.workoutId == 0) {
            workoutsDB.add(workout.copy(workoutId = autoIncrementId))
            autoIncrementId++.toLong()
        } else {
            workoutsDB.add(workout)
            workout.workoutId.toLong()
        }
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
    }
}