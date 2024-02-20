package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.ExerciseGenerator
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExerciseDBManager @Inject constructor(
    private val preferences: Preferences,
    private val exerciseDao: ExerciseDao,
    private val exerciseDetailsDao: ExerciseDetailsDao,
    private val exerciseSetDao: ExerciseSetDao
) {
    private val hasInitializedExerciseTableRoomDB = preferences.hasInitializedExerciseTableRoomDB()

    suspend fun initializeExerciseTableRoomDB() = withContext(Dispatchers.IO) { //TODO: use in testing...
        if (hasInitializedExerciseTableRoomDB) return@withContext

        for (muscleGroup in MuscleGroup.entries) {
            val exercisesList = ExerciseGenerator.loadExercises(muscleGroup)
            val exerciseDetailsList = ExerciseGenerator.loadExerciseDetails(muscleGroup)
            val exerciseDetailsExerciseCrossRef = ExerciseGenerator.loadAllCrossRefs()

            val exerciseSetsCrossRef = loadExerciseSetsCrossRefs(exercisesList)

            exerciseDao.insertAll(exercisesList)
            exerciseDetailsDao.insertAll(exerciseDetailsList)

            exerciseDao.insertAllExerciseDetailsExerciseCrossRefs(exerciseDetailsExerciseCrossRef)
            exerciseDao.insertAllExerciseSetCrossRef(exerciseSetsCrossRef)
        }
        preferences.initializeExerciseTableRoomDB()
    }

    private suspend fun loadExerciseSetsCrossRefs(allExercises: List<Exercise>): List<ExerciseSetCrossRef> {
        val crossRefs: MutableList<ExerciseSetCrossRef> = mutableListOf()

        val exerciseSets: MutableList<ExerciseSet> = mutableListOf()
        var counter: Int = 0
        for (exercise in allExercises) {
            exerciseSets.addAll(ExerciseGenerator.loadExerciseSets()) //Generate new setId with same ExerciseSetDto data...

            crossRefs.add(ExerciseSetCrossRef(exercise.exerciseId, exerciseSets[counter + 0].setId))
            crossRefs.add(ExerciseSetCrossRef(exercise.exerciseId, exerciseSets[counter + 1].setId))
            crossRefs.add(ExerciseSetCrossRef(exercise.exerciseId, exerciseSets[counter + 2].setId))

            counter += 3
        }

        exerciseSetDao.insertAllExerciseSets(exerciseSets) //Insert ExerciseSetDto
        return crossRefs
    }
}