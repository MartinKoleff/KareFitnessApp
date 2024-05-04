package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.common.WorkoutGenerator
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.WorkoutConfigurationDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutDBManagerV2 @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val workoutDetailsDao: WorkoutDetailsDao,
    private val workoutConfigurationDao: WorkoutConfigurationDao,
    private val exerciseDao: ExerciseDao,
    private val exerciseSetDao: ExerciseSetDao,
    private val hasInitializedDB: Boolean
) {

    suspend fun initializeWorkoutTable(onDBInitialized: () -> Unit) =
        withContext(Dispatchers.IO) {
            if (hasInitializedDB) return@withContext

            val workoutList = WorkoutGenerator.getAllWorkouts()
            val workoutDetailsWithExercisesList = WorkoutGenerator.getAllWorkoutDetails()

            //Create Workout
            workoutDao.insertAllWorkouts(workoutList)

            //Create Workout Details
            workoutDetailsDao.insertAllWorkoutDetails(
                workoutDetailsWithExercisesList.map {
                    it.workoutDetails
                }
            )

            //Create Workout Configuration
            workoutDetailsWithExercisesList
                .map { it.toDto() }
                .map { it.configuration }
                .forEach { configuration ->
                workoutConfigurationDao.insertWorkoutConfiguration(configuration.toEntity())
            }

            //Create Exercises
            for (workoutDetailsWithExercises in workoutDetailsWithExercisesList) {

                //Save all exercises in workout
                val exercisesWithSets = workoutDetailsWithExercises.safeExercises
                val exercises = exercisesWithSets.map { it.exercise }
                exerciseDao.insertAllExercises(exercises) //Only catalog exercises have exercise details

                exercisesWithSets
                    .map { it.sets }
                    .forEach { sets ->
                        exerciseSetDao.insertAllExerciseSets(sets)
                    }
            }

            //Initialization callback
            onDBInitialized()
        }
}