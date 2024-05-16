//package com.koleff.kare_android.data.room.manager
//
//import com.koleff.kare_android.common.Constants
//import com.koleff.kare_android.common.ExerciseGenerator
//import com.koleff.kare_android.common.preferences.Preferences
//import com.koleff.kare_android.data.model.dto.MuscleGroup
//import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
//import com.koleff.kare_android.data.room.dao.ExerciseDao
//import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
//import com.koleff.kare_android.data.room.dao.ExerciseSetDao
//import com.koleff.kare_android.data.room.dao.WorkoutDao
//import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
//import com.koleff.kare_android.data.room.entity.Exercise
//import com.koleff.kare_android.data.room.entity.ExerciseSet
//import com.koleff.kare_android.data.room.entity.Workout
//import com.koleff.kare_android.data.room.entity.WorkoutDetails
//import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
//import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import javax.inject.Inject
//
//class ExerciseDBManager @Inject constructor(
//    private val exerciseDao: ExerciseDao,
//    private val exerciseDetailsDao: ExerciseDetailsDao,
//    private val exerciseSetDao: ExerciseSetDao,
//    private val workoutDao: WorkoutDao,
//    private val workoutDetailsDao: WorkoutDetailsDao,
//    private val hasInitializedDB: Boolean
//) {
//    suspend fun initializeExerciseTable(hasSets: Boolean = true, onDBInitialized: () -> Unit) =
//        withContext(Dispatchers.IO) {
//            if (hasInitializedDB) return@withContext
//
//            //Catalog workout and workout details placeholders
//            val catalogPlaceholderWorkout =
//                Workout(
//                    workoutId = Constants.CATALOG_EXERCISE_ID,
//                    name = "Catalog",
//                    muscleGroup = MuscleGroup.NONE,
//                    snapshot = "default_snapshot.png",
//                    totalExercises = MuscleGroup.getTotalExercises(MuscleGroup.ALL),
//                    isFavorite = false
//                )
//            workoutDao.insertWorkout(
//                catalogPlaceholderWorkout
//            )
//            val catalogPlaceholderWorkoutDetails =
//                WorkoutDetails(
//                    workoutDetailsId = Constants.CATALOG_EXERCISE_ID,
//                    name = "Catalog",
//                    description = "",
//                    muscleGroup = MuscleGroup.NONE,
//                    isFavorite = false
//                )
//            workoutDetailsDao.insertWorkoutDetails(
//                catalogPlaceholderWorkoutDetails
//            )
//
//            for (muscleGroup in MuscleGroup.getSupportedMuscleGroups()) {
//                val exercisesList = ExerciseGenerator.loadExercises(muscleGroup, false)
//                val exerciseDetailsList = ExerciseGenerator.loadExerciseDetails(muscleGroup, false)
//
//                exerciseDao.insertAll(exercisesList)
//                exerciseDetailsDao.insertAll(exerciseDetailsList)
//
//                //Exercise - ExerciseSet cross refs for each exercise
//                if (hasSets) {
//                    for (exercise in exercisesList) {
//                        val exerciseSets = ExerciseGenerator.loadExerciseSets(
//                            exerciseId = exercise.exerciseId,
//                            workoutId = exercise.workoutId
//                        )
//                        exerciseSetDao.insertAllExerciseSets(exerciseSets)
//
//                        val totalSets = exerciseSets.size
//                        val exerciseSetsCrossRef =
//                            ExerciseGenerator.loadExerciseSetsCrossRefs(
//                                exercise,
//                                exerciseSets,
//                                totalSets
//                            )
//                        exerciseDao.insertAllExerciseSetCrossRef(exerciseSetsCrossRef)
//                    }
//                }
//            }
//
//            //ExerciseDetails - Exercise cross refs
//            val exerciseDetailsExerciseCrossRef =
//                ExerciseGenerator.loadAllExerciseDetailsExerciseCrossRefs()
//            exerciseDao.insertAllExerciseDetailsExerciseCrossRefs(exerciseDetailsExerciseCrossRef)
//
//            onDBInitialized()
//        }
//}