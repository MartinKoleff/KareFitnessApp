package com.koleff.kare_android.exercise.data

import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.entity.ExerciseSet
import java.util.UUID

class ExerciseSetDaoFake : ExerciseSetDao {

    private val exerciseSetDB = mutableListOf<ExerciseSet>()

    override fun getSetById(setId: UUID): ExerciseSet {
        return exerciseSetDB.first { it.setId == setId }
    }

    override suspend fun saveSet(exerciseSet: ExerciseSet) {
        exerciseSetDB.add(exerciseSet)
    }

    override suspend fun insertAllExerciseSets(sets: List<ExerciseSet>) {
        exerciseSetDB.addAll(sets)
    }

    override suspend fun updateSet(exerciseSet: ExerciseSet) {

        //Find exercise set
        val index = exerciseSetDB.indexOf(exerciseSet)

        //Exercise found
        if (index != -1) {
            exerciseSetDB[index] = exerciseSet
        } else {

            //Delete invalid exercise set?
        }
    }

    override suspend fun deleteSet(exerciseSet: ExerciseSet) {
        exerciseSetDB.removeAll { it.setId == exerciseSet.setId }
    }

    fun clearDB(){
        exerciseSetDB.clear()
    }
}