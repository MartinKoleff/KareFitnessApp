package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.WorkoutGeneratorV2
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
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
    private val exerciseDetailsDao: ExerciseDetailsDao,
    private val exerciseSetDao: ExerciseSetDao,
    private val hasInitializedDB: Boolean
) {

    suspend fun initializeWorkoutTable(onDBInitialized: () -> Unit) =
        withContext(Dispatchers.IO) {
            if (hasInitializedDB) return@withContext

            val workoutDetailsFullData = WorkoutGeneratorV2.getWorkoutFullData()

            //Create Workout
            workoutDao.insertAllWorkouts(workoutDetailsFullData.map { it.workout.toEntity() })

            //Create Workout Details
            workoutDetailsDao.insertAllWorkoutDetails(
                workoutDetailsFullData.map {
                    it.workoutDetails.toEntity()
                }
            )

            //Create Workout Configuration
            workoutDetailsFullData
                .map { it.configuration }
                .forEach { configuration ->
                    workoutConfigurationDao.insertWorkoutConfiguration(configuration.toEntity())
                }

            //Create Exercises
            for (workoutDetailsWithExercises in workoutDetailsFullData) {

                //Save all exercises in workout
                val exercises = workoutDetailsWithExercises.exercises
                val exerciseDetails =
                    workoutDetailsWithExercises.exerciseDetails

                exerciseDao.insertAllExercises(
                    exercises.map { it.toEntity() }
                )
                exerciseDetailsDao.insertAllExerciseDetails(
                    exerciseDetails.map { it.toEntity() }
                )

                exercises.forEach { exercise ->
                    exerciseSetDao.insertAllExerciseSets(
                        exercise.sets.map { set -> set.toEntity() }
                    )
                }
            }

            //Initialization callback
            onDBInitialized()
        }
}