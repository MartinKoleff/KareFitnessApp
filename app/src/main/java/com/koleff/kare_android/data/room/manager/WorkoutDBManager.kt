package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.ExerciseGenerator
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.common.WorkoutGenerator
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import com.koleff.kare_android.data.room.entity.WorkoutDetailsWithExercises
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutDBManager @Inject constructor(
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
            val workoutDetailsList = WorkoutGenerator.getAllWorkoutDetails()
            val workoutDetailsExerciseCrossRefs =
                WorkoutGenerator.getAllWorkoutDetailsExerciseCrossRefs()
            val workoutDetailsWorkoutCrossRefs =
                WorkoutGenerator.getAllWorkoutDetailsWorkoutCrossRefs()

            workoutDao.insertAll(workoutList)
            workoutDetailsDao.insertAllDetails(workoutDetailsList.map(WorkoutDetailsWithExercises::workoutDetails))

            workoutDetailsDao.insertAllWorkoutDetailsExerciseCrossRef(
                workoutDetailsExerciseCrossRefs
            )
            workoutDao.insertAllWorkoutDetailsWorkoutCrossRef(workoutDetailsWorkoutCrossRefs)

            //Exercises
            val exerciseSetsCrossRef = mutableListOf<ExerciseSetCrossRef>()
            val exerciseIdsList = mutableListOf<Int>()
            for (data in workoutDetailsList) {

                //Save all exercises in workout
                val exercises = data.safeExercises
                exerciseDao.insertAll(exercises)

                exerciseIdsList.addAll(
                    exercises.map { it.exerciseId }
                )

                for (exercise in exercises) {

                    //Generate sets
                    val sets =
                        MockupDataGeneratorV2.generateExerciseSetsList(
                            4,
                            exerciseId = exercise.exerciseId,
                            workoutId = exercise.workoutId,
                            enableSetIdGeneration = true
                        )
                    for (set in sets) {

                        //Save set
                        exerciseSetDao.saveSet(set.toEntity())

                        exerciseSetsCrossRef.add(
                            ExerciseSetCrossRef(
                                exerciseId = exercise.exerciseId,
                                workoutId = data.workoutDetails.workoutDetailsId,
                                setId = set.setId!!
                            )
                        )
                    }
                }
            }
            //Exercise - ExerciseSet cross refs
            exerciseDao.insertAllExerciseSetCrossRef(exerciseSetsCrossRef)

            //ExerciseDetails - Exercise cross refs
            val exerciseDetailsExerciseCrossRef =
                ExerciseGenerator.loadAllExerciseDetailsExerciseCrossRefs()
                    .filter { exerciseIdsList.contains(it.exerciseId) }
            exerciseDao.insertAllExerciseDetailsExerciseCrossRefs(exerciseDetailsExerciseCrossRef)

            //Initialization callback
            onDBInitialized()
        }
}