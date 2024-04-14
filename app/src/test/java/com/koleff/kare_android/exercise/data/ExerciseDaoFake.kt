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
import com.koleff.kare_android.data.room.entity.relations.ExerciseWithSets
import com.koleff.kare_android.utils.TestLogger

class ExerciseDaoFake(
    private val exerciseSetDao: ExerciseSetDaoFake,
    private val exerciseDetailsDao: ExerciseDetailsDaoFake,
    private val logger: TestLogger
) : ExerciseDao {
    private val exerciseWithSetDB = mutableListOf<ExerciseWithSets>()
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
            ExerciseWithSets(exercise = exercise, sets = sets)
        )


        if (isInternalLogging) logger.i(
            TAG,
            "Inserted exercise sets in DB for exercise ${exercise.exerciseId}: $sets"
        )
    }

    private suspend fun updateExerciseDetailsDB(exerciseDetailsId: Int, workoutId: Int) {
        val exercise = this.getExerciseWithSets(exerciseDetailsId, workoutId).exercise
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

    private suspend fun updateExerciseWithSets(exerciseId: Int, workoutId: Int) {
        val exerciseWithNoSets = getExerciseWithSets(exerciseId, workoutId)
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
            it.exercise.exerciseId == exercise.exerciseId &&
                    it.exercise.workoutId == exercise.workoutId
        }
    }

    override suspend fun deleteExerciseSetCrossRef(crossRef: ExerciseSetCrossRef) {
        exerciseSetCrossRefs.remove(crossRef)

        updateExerciseSetDB(crossRef)
        exerciseSetDao.deleteSet(crossRef.setId)
    }

    override suspend fun deleteAllExerciseSetCrossRef(crossRefs: List<ExerciseSetCrossRef>) {
        exerciseSetCrossRefs.removeAll(crossRefs)

        crossRefs.forEach { crossRef ->
            updateExerciseSetDB(crossRef)
            exerciseSetDao.deleteSet(crossRef.setId)
        }
    }

    override suspend fun getExercise(exerciseId: Int, workoutId: Int): Exercise? {
        return exerciseWithSetDB.first {
            it.exercise.exerciseId == exerciseId &&
                    it.exercise.workoutId == workoutId
        }.exercise
    }

    override suspend fun getSetsForExercise(exerciseId: Int, workoutId: Int): List<ExerciseSet> {
        return exerciseWithSetDB.first {
            it.exercise.exerciseId == exerciseId &&
                    it.exercise.workoutId == workoutId
        }.sets
    }

    private fun updateExerciseSetDB(crossRef: ExerciseSetCrossRef) {
        val selectedExercise = exerciseWithSetDB.first {
            it.exercise.exerciseId == crossRef.exerciseId &&
                    it.exercise.workoutId == crossRef.workoutId
        }

        val updatedExerciseSets = selectedExercise.sets.toMutableList()
            .filterNot { it.setId == crossRef.setId }

        val updatedExercise = ExerciseWithSets(
            exercise = selectedExercise.exercise,
            sets = updatedExerciseSets
        )

        //Remove exercise entry
        exerciseWithSetDB.removeAll {
            it.exercise.exerciseId == crossRef.exerciseId &&
                    it.exercise.workoutId == crossRef.workoutId
        }

        //Add updated exercise entry
        exerciseWithSetDB.add(updatedExercise)
    }

    override suspend fun getExerciseWithSets(exerciseId: Int, workoutId: Int): ExerciseWithSets {
        val exercise = getExercise(exerciseId, workoutId)
        val sets = if (exercise != null) getSetsForExercise(exerciseId, workoutId) else emptyList()
        return if (exercise != null) ExerciseWithSets(exercise, sets) else
            throw NoSuchElementException("No exercise found with exerciseId $exerciseId and workoutId $workoutId")
    }

    override fun getExercisesOrderedById(muscleGroup: MuscleGroup): List<Exercise> {
        return exerciseWithSetDB
            .filter { it.exercise.muscleGroup == muscleGroup }
            .map { it.exercise }
            .sortedBy { it.exerciseId }
    }

    override fun getExercisesOrderedById(): List<Exercise> {
        return exerciseWithSetDB
            .map { it.exercise }
            .sortedBy { it.exerciseId }
    }

    override fun getAllExercises(): List<Exercise> {
        return exerciseWithSetDB.map { it.exercise }
    }

    fun clearDB() {
        exerciseWithSetDB.clear()
        exerciseDetailsExerciseCrossRefs.clear()
        exerciseDetailsExerciseCrossRefs.clear()
    }
}