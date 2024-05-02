package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.common.WorkoutGenerator
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutDBManagerV2 @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val workoutDetailsDao: WorkoutDetailsDao,
    private val exerciseDao: ExerciseDao,
    private val exerciseSetDao: ExerciseSetDao,
    private val hasInitializedDB: Boolean
) {

    suspend fun initializeWorkoutTable(onDBInitialized: () -> Unit) =
        withContext(Dispatchers.IO) {
            if (hasInitializedDB) return@withContext

            val workoutList = WorkoutGenerator.getAllWorkouts()
            val workoutDetailsWithExercisesList = WorkoutGenerator.getAllWorkoutDetails()

            workoutDao.insertAllWorkouts(workoutList)
            workoutDetailsDao.insertAllWorkoutDetails(
                workoutDetailsWithExercisesList.map {
                    it.workoutDetails
                }
            )

            //Exercises
            for (workoutDetailsWithExercises in workoutDetailsWithExercisesList) {

                //Save all exercises in workout
                val exercises = workoutDetailsWithExercises.safeExercises
                exerciseDao.insertAllExercises(exercises)

                exercises.forEach { exercise ->

                    //Generate sets
                    val sets =
                        MockupDataGeneratorV2.generateExerciseSetsList(
                            4,
                            exerciseId = exercise.exerciseId,
                            workoutId = exercise.workoutId,
                            enableSetIdGeneration = true
                        )

                    //Save set
                    sets
                        .map { it.toEntity() }
                        .forEach { set ->
                        exerciseSetDao.insertExerciseSet(set)
                    }
                }
            }

            //Initialization callback
            onDBInitialized()
        }
}