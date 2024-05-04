package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.ExerciseGenerator
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExerciseDBManagerV2 @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val exerciseDetailsDao: ExerciseDetailsDao,
    private val exerciseSetDao: ExerciseSetDao,
    private val workoutDao: WorkoutDao,
    private val workoutDetailsDao: WorkoutDetailsDao,
    private val hasInitializedDB: Boolean
) {
    suspend fun initializeExerciseTable(hasSets: Boolean = true, onDBInitialized: () -> Unit) =
        withContext(Dispatchers.IO) {
            if (hasInitializedDB) return@withContext

            //Catalog workout and workout details placeholders
            generateCatalogWorkout()

            MuscleGroup.getSupportedMuscleGroups().forEach { muscleGroup ->
                val exercisesList = ExerciseGenerator.loadExercises(muscleGroup, false)
                val exerciseDetailsList = ExerciseGenerator.loadExerciseDetails(muscleGroup, false)

                exerciseDao.insertAllExercises(exercisesList)
                exerciseDetailsDao.insertAllExerciseDetails(exerciseDetailsList)

                if (hasSets) {
                    for (exercise in exercisesList) {
                        val exerciseSets = ExerciseGenerator.loadExerciseSets(
                            exerciseId = exercise.exerciseId,
                            workoutId = exercise.workoutId
                        )
                        exerciseSetDao.insertAllExerciseSets(exerciseSets)
                    }
                }
            }
            onDBInitialized()
        }

    private suspend fun generateCatalogWorkout() {
        val catalogPlaceholderWorkout =
            Workout(
                workoutId = Constants.CATALOG_EXERCISE_ID,
                name = "Catalog",
                muscleGroup = MuscleGroup.NONE,
                snapshot = "default_snapshot.png",
                totalExercises = MuscleGroup.getTotalExercises(MuscleGroup.ALL),
                isSelected = false
            )
        workoutDao.insertWorkout(
            catalogPlaceholderWorkout
        )
        val catalogPlaceholderWorkoutDetails =
            WorkoutDetails(
                workoutDetailsId = Constants.CATALOG_EXERCISE_ID,
                name = "Catalog",
                description = "",
                muscleGroup = MuscleGroup.NONE,
                isSelected = false
            )
        workoutDetailsDao.insertWorkoutDetails(
            catalogPlaceholderWorkoutDetails
        )
    }
}