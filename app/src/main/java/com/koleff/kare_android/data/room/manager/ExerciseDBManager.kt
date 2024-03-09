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
    private val exerciseDao: ExerciseDao,
    private val exerciseDetailsDao: ExerciseDetailsDao,
    private val exerciseSetDao: ExerciseSetDao,
    private val hasInitializedDB: Boolean
) {
    suspend fun initializeExerciseTable(onDBInitialized: () -> Unit) = withContext(Dispatchers.IO) {
        if (hasInitializedDB) return@withContext

        for (muscleGroup in MuscleGroup.getSupportedMuscleGroups()) {
            val exercisesList = ExerciseGenerator.loadExercises(muscleGroup)
            val exerciseDetailsList = ExerciseGenerator.loadExerciseDetails(muscleGroup)

            exerciseDao.insertAll(exercisesList)
            exerciseDetailsDao.insertAll(exerciseDetailsList)

            //Exercise - ExerciseSet cross refs for each exercise
//            for(exercise in exercisesList){
//                val exerciseSets = ExerciseGenerator.loadExerciseSets()
//                exerciseSetDao.insertAllExerciseSets(exerciseSets)
//
//                val exerciseSetsCrossRef = ExerciseGenerator.loadExerciseSetsCrossRefs(exercise, exerciseSets)
//                exerciseDao.insertAllExerciseSetCrossRef(exerciseSetsCrossRef)
//            }
        }

        //ExerciseDetails - Exercise cross refs
        val exerciseDetailsExerciseCrossRef = ExerciseGenerator.loadAllExerciseDetailsExerciseCrossRefs()
        exerciseDao.insertAllExerciseDetailsExerciseCrossRefs(exerciseDetailsExerciseCrossRef)

        onDBInitialized()
    }
}