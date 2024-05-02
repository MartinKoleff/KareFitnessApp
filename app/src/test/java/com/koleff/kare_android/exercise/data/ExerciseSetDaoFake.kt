package com.koleff.kare_android.exercise.data

import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.entity.ExerciseSet
import java.util.UUID

class ExerciseSetDaoFake(
    private val exerciseSetChangeListener: ExerciseSetChangeListener
) : ExerciseSetDao{

    private val exerciseSetDB = mutableListOf<ExerciseSet>()

    override fun getSetById(setId: UUID): ExerciseSet {
        return exerciseSetDB.first { it.setId == setId }
    }

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet) {
        exerciseSetDB.add(exerciseSet)

        exerciseSetChangeListener.onSetAdded(exerciseSet)
    }

    override suspend fun insertAllExerciseSets(exerciseSets: List<ExerciseSet>) {
        exerciseSetDB.addAll(exerciseSets)

        exerciseSets.forEach { exerciseSet ->
            exerciseSetChangeListener.onSetAdded(exerciseSet)
        }
    }

    override suspend fun updateSet(exerciseSet: ExerciseSet) {

        //Find exercise set
        val index = exerciseSetDB.indexOf(exerciseSet)

        //Exercise found
        if (index != -1) {
            exerciseSetDB[index] = exerciseSet
            exerciseSetChangeListener.onSetUpdated(exerciseSet)
        } else {

            //Delete invalid exercise set?
        }
    }

    override suspend fun deleteSet(exerciseSet: ExerciseSet) {
        exerciseSetDB.removeAll { it.setId == exerciseSet.setId }
        exerciseSetChangeListener.onSetDeleted(exerciseSet)
    }

    override suspend fun deleteSet(setId: UUID) {
        val exerciseSet = exerciseSetDB.firstOrNull { it.setId == setId } ?: return

        exerciseSetDB.removeAll { it.setId == exerciseSet.setId }
        exerciseSetChangeListener.onSetDeleted(exerciseSet)
    }

    fun clearDB() {
        exerciseSetDB.clear()
    }
}