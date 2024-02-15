package com.koleff.kare_android.exercise.data

import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseWithSet
import com.koleff.kare_android.utils.TestLogger

class ExerciseDaoFake(private val exerciseSetDao: ExerciseSetDaoFake) : ExerciseDao {
    private val exerciseWithSetDB = mutableListOf<ExerciseWithSet>()

    private val exerciseDetailsExerciseCrossRefs =
        mutableListOf<ExerciseDetailsExerciseCrossRef>() //TODO: wire cross refs with other DAOs...
    private val exerciseSetCrossRefs =
        mutableListOf<ExerciseSetCrossRef>()

    private val logger: TestLogger = TestLogger()

    override suspend fun insertExercise(exercise: Exercise) {
        val sets = getExerciseSets(exercise)

        exerciseWithSetDB.add(
            ExerciseWithSet(exercise = exercise, sets = sets)
        )

        logger.i("ExerciseDaoFake-insertExercise", sets.toString())
    }

    private fun getExerciseSets(exercise: Exercise): List<ExerciseSet> {
        val setIndexes = exerciseSetCrossRefs
            .filter { it.exerciseId == exercise.exerciseId }
            .map { it.setId }

        val sets = mutableListOf<ExerciseSet>()
        for (setIndex in setIndexes) {
            val set = exerciseSetDao.getSetById(setIndex)

            sets.add(set)
        }

        logger.i("ExerciseDaoFake-getExerciseSets", sets.toString())

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
    }

    override suspend fun insertAllExerciseDetailsExerciseCrossRefs(crossRefs: List<ExerciseDetailsExerciseCrossRef>) {
        exerciseDetailsExerciseCrossRefs.addAll(crossRefs)
    }

    override suspend fun insertExerciseSetCrossRef(crossRef: ExerciseSetCrossRef) {
        exerciseSetCrossRefs.add(crossRef)
    }

    override suspend fun insertAllExerciseSetCrossRef(crossRefs: List<ExerciseSetCrossRef>) {
        exerciseSetCrossRefs.addAll(crossRefs)

        //Update DB after new cross ref is added...
        val exerciseIndexesToUpdate = crossRefs.map { it.exerciseId }
        exerciseIndexesToUpdate.forEach { updateExerciseWithSets(it) }
    }

    private fun updateExerciseWithSets(exerciseId: Int) {
        val exerciseWithNoSets = getExerciseById(exerciseId)
        val exerciseSets = getExerciseSets(exerciseWithNoSets.exercise)
        val exerciseWithSets = exerciseWithNoSets.copy(
            sets = exerciseSets
        )

        exerciseWithSetDB.remove(exerciseWithNoSets)
        exerciseWithSetDB.add(exerciseWithSets)

        logger.i("ExerciseDaoFake-updateExerciseWithSets", exerciseWithSets.toString())
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

    fun clearDB() {
        exerciseWithSetDB.clear()
        exerciseDetailsExerciseCrossRefs.clear()
        exerciseDetailsExerciseCrossRefs.clear()
    }
}