package com.koleff.kare_android.exercise.data

import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.entity.ExerciseSet
import java.util.UUID

class ExerciseSetDaoFake(
    private val exerciseSetChangeListener: ExerciseSetChangeListener
) : ExerciseSetDao, ExerciseSetChangeListener{

    private val exerciseSetDB = mutableListOf<ExerciseSet>()

    override fun getSetById(setId: UUID): ExerciseSet {
        return exerciseSetDB.firstOrNull { it.setId == setId }
            ?: throw NoSuchElementException("Exercise set with setId $setId not found.")
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
        exerciseSetChangeListener.onSetDeleted(exerciseSet)
        exerciseSetDB.removeAll { it.setId == exerciseSet.setId }
    }

    override suspend fun deleteSet(setId: UUID) {
        val exerciseSet = exerciseSetDB.firstOrNull { it.setId == setId } ?: return
        deleteSet(exerciseSet)
    }

    fun clearDB() {
        exerciseSetDB.clear()
    }

    override suspend fun onSetAdded(exerciseSet: ExerciseSet) {
        insertExerciseSet(exerciseSet)
    }

    override suspend fun onSetUpdated(exerciseSet: ExerciseSet) {
        updateSet(exerciseSet)
    }

    override suspend fun onSetDeleted(exerciseSet: ExerciseSet) {
        deleteSet(exerciseSet)
    }

    override suspend fun onSetsDeleted(exerciseId: Int, workoutId: Int) {
        exerciseSetDB.filter {set ->
            set.exerciseId == exerciseId &&
                    set.workoutId == workoutId
        }.forEach { set ->
            deleteSet(set)
        }
    }
}