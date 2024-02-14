package com.koleff.kare_android.exercise.data

import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseWithSet

class ExerciseDaoFake : ExerciseDao { //TODO: wire cross refs with other DAOs...
    private val exerciseWithSetDB = mutableListOf<ExerciseWithSet>()
    private val exerciseDetailsExerciseCrossRefs = mutableListOf<ExerciseDetailsExerciseCrossRef>()
    private val exerciseSetCrossRefs = mutableListOf<ExerciseSetCrossRef>()

    override suspend fun insertExercise(exercise: Exercise) {
        exerciseWithSetDB.add(
            ExerciseWithSet(exercise = exercise, sets = emptyList())
        )
    }

    override suspend fun insertAll(exercises: List<Exercise>) {
        exercises.forEach { exercise ->
            insertExercise(exercise)
        }
    }

    override suspend fun insertExerciseDetailsExerciseCrossRef(crossRef: ExerciseDetailsExerciseCrossRef) {
        exerciseDetailsExerciseCrossRefs.add(crossRef)
    }

    override suspend fun insertAllExerciseDetailsExerciseCrossRefs(crossRefs: List<ExerciseDetailsExerciseCrossRef>) {
        exerciseDetailsExerciseCrossRefs.addAll(crossRefs)
    }

    override suspend fun insertExerciseSetCrossRef(crossRef: ExerciseSetCrossRef) {
        exerciseSetCrossRefs.add(crossRef)
    }

    override suspend fun insertAllExerciseSetCrossRef(crossRefs: List<ExerciseSetCrossRef>) {
        exerciseSetCrossRefs.addAll(crossRefs)
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

    override fun getExerciseById(exerciseId: Int): ExerciseWithSet {
        return exerciseWithSetDB.first { it.exercise.exerciseId == exerciseId }
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
}