package com.koleff.kare_android.exercise.data

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.ExerciseWithSets
import java.util.UUID

class ExerciseDaoFakeV2 : ExerciseDao, ExerciseSetChangeListener {
    private val exerciseWithSetDB = mutableListOf<ExerciseWithSets>()

    private val isInternalLogging = false

    companion object {
        private const val TAG = "ExerciseDaoFake"
    }

    override suspend fun insertExercise(exercise: Exercise) {

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
            ExerciseWithSets(
                exercise = exercise,
                sets = emptyList() //New exercises have no sets...
            )
        )
    }

    override suspend fun insertAllExercises(exercises: List<Exercise>) {
        exercises.forEach { exercise ->
            insertExercise(exercise)
        }
    }

    override suspend fun updateExercise(exercise: Exercise) {
        val exercisePosition =
            exerciseWithSetDB.indexOfFirst { it.exercise.exerciseId == exercise.exerciseId } //Get position

        //Valid exercise
        if (exercisePosition != -1) {
            val exerciseSetsInDB = exerciseWithSetDB[exercisePosition].sets
            exerciseWithSetDB[exercisePosition] = ExerciseWithSets(
                exercise = exercise,
                sets = exerciseSetsInDB
            )
        }
    }

    override suspend fun deleteExercise(exercise: Exercise) {

        //If there are multiple entries -> remove all
        exerciseWithSetDB.removeAll {
            it.exercise.exerciseId == exercise.exerciseId &&
                    it.exercise.workoutId == exercise.workoutId
        }
    }

    override suspend fun getExercise(exerciseId: Int, workoutId: Int): Exercise? {
        return exerciseWithSetDB.firstOrNull {
            it.exercise.exerciseId == exerciseId &&
                    it.exercise.workoutId == workoutId
        }?.exercise
    }

    override suspend fun getSetsForExercise(exerciseId: Int, workoutId: Int): List<ExerciseSet> {
        return exerciseWithSetDB.firstOrNull {
            it.exercise.exerciseId == exerciseId &&
                    it.exercise.workoutId == workoutId
        }?.sets ?: throw NoSuchElementException("No sets found for exercise with exerciseId $exerciseId and workoutId $workoutId")
    }

    override suspend fun getExerciseWithSets(exerciseId: Int, workoutId: Int): ExerciseWithSets {
        val exercise = getExercise(exerciseId, workoutId)
        val sets = if (exercise != null) getSetsForExercise(exerciseId, workoutId) else emptyList()
        return if (exercise != null) ExerciseWithSets(exercise, sets) else
            throw NoSuchElementException("No exercise found with exerciseId $exerciseId and workoutId $workoutId")
    }

    override fun getCatalogExercises(workoutId: Int, muscleGroup: MuscleGroup): List<Exercise> {
        return exerciseWithSetDB
            .map { it.exercise }
            .filter { it.muscleGroup == muscleGroup && it.workoutId == workoutId }
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

    override fun getAllCatalogExercises(workoutId: Int): List<Exercise> {
        return exerciseWithSetDB
            .map { it.exercise }
            .filter { it.workoutId == workoutId }
            .sortedBy { it.exerciseId }
    }

    fun clearDB() {
        exerciseWithSetDB.clear()
    }

    override fun onSetAdded(exerciseSet: ExerciseSet) {
        val exercisePosition =
            exerciseWithSetDB.indexOfFirst { it.exercise.exerciseId == exerciseSet.exerciseId } //Get position

        //Valid exercise
        if (exercisePosition != -1) {
            val exercise = exerciseWithSetDB[exercisePosition]

            val updatedSets = exercise.sets.toMutableList()
            updatedSets.add(exerciseSet)
            val updatedExercise = exercise.copy(sets = updatedSets)

            exerciseWithSetDB[exercisePosition] = updatedExercise
        }
    }

    override fun onSetUpdated(exerciseSet: ExerciseSet) {
        val exercisePosition =
            exerciseWithSetDB.indexOfFirst { it.exercise.exerciseId == exerciseSet.exerciseId } //Get position

        //Valid exercise
        if (exercisePosition != -1) {
            val exercise = exerciseWithSetDB[exercisePosition]
            val updatedSets = exercise.sets.toMutableList()

            val setPosition =
                updatedSets.indexOfFirst { it.setId == exerciseSet.setId } //Get position

            //Valid set
            if(setPosition != -1){
                updatedSets[setPosition] = exerciseSet
            }
            val updatedExercise = exercise.copy(sets = updatedSets)

            exerciseWithSetDB[exercisePosition] = updatedExercise
        }
    }

    override fun onSetDeleted(exerciseSet: ExerciseSet) {
        val exercisePosition =
            exerciseWithSetDB.indexOfFirst { it.exercise.exerciseId == exerciseSet.exerciseId } //Get position

        //Valid exercise
        if (exercisePosition != -1) {
            val exercise = exerciseWithSetDB[exercisePosition]

            val updatedSets = exercise.sets.toMutableList()
            updatedSets.removeAll { it.setId == exerciseSet.setId }
            val updatedExercise = exercise.copy(sets = updatedSets)

            exerciseWithSetDB[exercisePosition] = updatedExercise
        }
    }
}