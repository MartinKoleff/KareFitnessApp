package com.koleff.kare_android.exercise.data

import android.util.Log
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseDetails
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsWithExercise
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseWithSet
import com.koleff.kare_android.utils.TestLogger

class ExerciseDaoFake(
    private val exerciseSetDao: ExerciseSetDaoFake,
    private val exerciseDetailsDao: ExerciseDetailsDaoFake,
    private val logger: TestLogger
) : ExerciseDao {
    private val exerciseWithSetDB = mutableListOf<ExerciseWithSet>()
    private val exerciseDetailsWithExerciseDB = mutableListOf<ExerciseDetailsWithExercise>()

    private val exerciseDetailsExerciseCrossRefs =
        mutableListOf<ExerciseDetailsExerciseCrossRef>()
    private val exerciseSetCrossRefs =
        mutableListOf<ExerciseSetCrossRef>()

    private val isInternalLogging = false

    companion object {
        private const val TAG = "ExerciseDaoFake"
    }


    override suspend fun insertExercise(exercise: Exercise) {
        val sets = getExerciseSets(exercise)

        //Check for entry with same exerciseId
        if (exerciseWithSetDB.any {
                it.exercise.exerciseId == exercise.exerciseId && it.exercise.workoutId != Constants.CATALOG_EXERCISE_ID
            }
        ) {
            exerciseWithSetDB.removeAll {
                it.exercise.exerciseId == exercise.exerciseId
            }
        }

        //Add new entry
        exerciseWithSetDB.add(
            ExerciseWithSet(exercise = exercise, sets = sets)
        )


        if (isInternalLogging) logger.i(
            TAG,
            "Inserted exercise sets in DB for exercise ${exercise.exerciseId}: $sets"
        )
    }

    private fun updateExerciseDetailsDB(exerciseDetailsId: Int, workoutId: Int) {
        val exercise = this.getExerciseByExerciseAndWorkoutId(exerciseDetailsId, workoutId).exercise
        val exerciseDetails = exerciseDetailsDao.getExerciseDetailsByExerciseAndWorkoutId(
            exerciseDetailsId,
            workoutId
        )

        exerciseDetailsWithExerciseDB.add(
            ExerciseDetailsWithExercise(exerciseDetails = exerciseDetails, exercise = exercise)
        )

        if (isInternalLogging) logger.i(
            TAG,
            "Update exercise details - exercise cross ref in DB: $exercise\n$exerciseDetails"
        )
    }

    private fun getExerciseSets(exercise: Exercise): List<ExerciseSet> {
        val setIndexes = exerciseSetCrossRefs
            .filter { it.exerciseId == exercise.exerciseId && it.workoutId == exercise.workoutId }
            .map { it.setId }

        val sets = mutableListOf<ExerciseSet>()
        for (setIndex in setIndexes) {
            val set = exerciseSetDao.getSetById(setIndex)

            sets.add(set)
        }

        if (isInternalLogging) logger.i(
            TAG,
            "Get exercise sets for exercise with id ${exercise.exerciseId}: $sets"
        )

        return sets
            .distinct()
            .sortedBy { it.number }
    }

    override suspend fun insertAll(exercises: List<Exercise>) {
        exercises.forEach { exercise ->
            insertExercise(exercise)
        }
    }

    override suspend fun insertExerciseDetailsExerciseCrossRef(crossRef: ExerciseDetailsExerciseCrossRef) {
        exerciseDetailsExerciseCrossRefs.add(crossRef)

        //Update DB after new cross ref is added...
        updateExerciseDetailsDB(
            exerciseDetailsId = crossRef.exerciseDetailsId,
            workoutId = crossRef.workoutId
        )
    }

    override suspend fun insertAllExerciseDetailsExerciseCrossRefs(crossRefs: List<ExerciseDetailsExerciseCrossRef>) {
        exerciseDetailsExerciseCrossRefs.addAll(crossRefs)


        //Update DB after new cross ref is added...
        crossRefs.forEach { crossRef ->
            updateExerciseDetailsDB(
                exerciseDetailsId = crossRef.exerciseDetailsId,
                workoutId = crossRef.workoutId
            )
        }
    }

    override suspend fun insertExerciseSetCrossRef(crossRef: ExerciseSetCrossRef) {
        exerciseSetCrossRefs.add(crossRef)

        //Update DB after new cross ref is added...
        try {
            updateExerciseWithSets(
                exerciseId = crossRef.exerciseId,
                workoutId = crossRef.workoutId
            )
        } catch (e: NoSuchElementException) {
            Log.d("ExerciseDaoFake", "No DB entry for exercise")
        }
    }

    override suspend fun insertAllExerciseSetCrossRef(crossRefs: List<ExerciseSetCrossRef>) {
        exerciseSetCrossRefs.addAll(crossRefs)

        //Update DB after new cross ref is added...
        try {
            crossRefs.forEach {
                updateExerciseWithSets(
                    exerciseId = it.exerciseId,
                    workoutId = it.workoutId
                )
            }
        } catch (e: NoSuchElementException) {
            Log.d("ExerciseDaoFake", "No DB entry for exercise")
        }
    }

    private fun updateExerciseWithSets(exerciseId: Int, workoutId: Int) {
        val exerciseWithNoSets = getExerciseByExerciseAndWorkoutId(exerciseId, workoutId)
        val exerciseSets = getExerciseSets(exerciseWithNoSets.exercise)
        val exerciseWithSets = exerciseWithNoSets.copy(
            sets = exerciseSets
        )

        exerciseWithSetDB.remove(exerciseWithNoSets)
        exerciseWithSetDB.add(exerciseWithSets)

        if (isInternalLogging) logger.i(
            TAG,
            "Exercise $exerciseId with sets updated: $exerciseWithSets"
        )
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        val entry = exerciseWithSetDB.filter { it.exercise.exerciseId == exercise.exerciseId }

        //If there are multiple entries -> remove all
        exerciseWithSetDB.removeAll {
            it.exercise.exerciseId == exercise.exerciseId
        }
    }

    override suspend fun deleteExerciseSetCrossRef(crossRef: ExerciseSetCrossRef) {
        exerciseSetCrossRefs.remove(crossRef)
    }

    override suspend fun deleteAllExerciseSetCrossRef(crossRefs: List<ExerciseSetCrossRef>) {
        exerciseSetCrossRefs.removeAll(crossRefs)
    }

    override fun getExerciseByExerciseAndWorkoutId(
        exerciseId: Int,
        workoutId: Int
    ): ExerciseWithSet {
        return exerciseWithSetDB.first { it.exercise.exerciseId == exerciseId && it.exercise.workoutId == workoutId }
    }

    override fun getExercisesOrderedById(muscleGroup: MuscleGroup): List<ExerciseWithSet> {
        return exerciseWithSetDB
            .filter { it.exercise.muscleGroup == muscleGroup }
            .sortedBy { it.exercise.exerciseId }
    }

    override fun getExercisesOrderedById(): List<ExerciseWithSet> {
        return exerciseWithSetDB.sortedBy { it.exercise.exerciseId }
    }

    override fun getAllExercises(): List<ExerciseWithSet> {
        return exerciseWithSetDB
    }

    fun clearDB() {
        exerciseWithSetDB.clear()
        exerciseDetailsExerciseCrossRefs.clear()
        exerciseDetailsExerciseCrossRefs.clear()
    }
}