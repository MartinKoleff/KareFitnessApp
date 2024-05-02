package com.koleff.kare_android.exercise.data

import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.entity.ExerciseDetails

class ExerciseDetailsDaoFake : ExerciseDetailsDao {

    private val exerciseDetailsDB = mutableListOf<ExerciseDetails>()

    override suspend fun insertExerciseDetails(exercise: ExerciseDetails) {
        exerciseDetailsDB.add(exercise)
    }

    override suspend fun insertAllExerciseDetails(exercises: List<ExerciseDetails>) {
        exerciseDetailsDB.addAll(exercises)
    }

    override suspend fun deleteExerciseDetails(exercise: ExerciseDetails) {

        //If there are multiple entries -> remove all
        exerciseDetailsDB.removeAll { it.exerciseDetailsId == exercise.exerciseDetailsId }
    }

    override fun getExerciseDetailsByExerciseAndWorkoutId(
        exerciseId: Int,
        workoutId: Int
    ): ExerciseDetails {
        return exerciseDetailsDB.first { it.exerciseDetailsId == exerciseId && it.workoutId == workoutId }
    }

    fun clearDB() {
        exerciseDetailsDB.clear()
    }
}