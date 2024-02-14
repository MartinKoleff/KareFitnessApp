package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.ExerciseGenerator
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseDetails
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class ExerciseDBManager @Inject constructor(
    private val preferences: Preferences,
    private val exerciseDao: ExerciseDao,
    private val exerciseDetailsDao: ExerciseDetailsDao,
    private val exerciseSetDao: ExerciseSetDao
) {
    private val hasInitializedExerciseTableRoomDB = preferences.hasInitializedExerciseTableRoomDB()

    suspend fun initializeExerciseTableRoomDB() = withContext(Dispatchers.IO) {
        if (hasInitializedExerciseTableRoomDB) return@withContext

        for (muscleGroup in MuscleGroup.entries) {
            val exercisesList = ExerciseGenerator.loadExercises(muscleGroup)
            val exerciseDetailsList = ExerciseGenerator.loadExerciseDetails(muscleGroup)
            val exerciseDetailsExerciseCrossRef = loadAllCrossRefs()

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

    private fun loadAllCrossRefs(): List<ExerciseDetailsExerciseCrossRef> {
        return listOf(
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 1, exerciseId = 1),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 2, exerciseId = 2),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 3, exerciseId = 3),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 4, exerciseId = 4),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 5, exerciseId = 5),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 6, exerciseId = 6),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 7, exerciseId = 7),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 8, exerciseId = 8),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 9, exerciseId = 9),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 10, exerciseId = 10),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 11, exerciseId = 11),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 12, exerciseId = 12),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 13, exerciseId = 13),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 14, exerciseId = 14),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 15, exerciseId = 15),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 16, exerciseId = 16),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 17, exerciseId = 17),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 18, exerciseId = 18),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 19, exerciseId = 19),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 20, exerciseId = 20),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 21, exerciseId = 21),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 22, exerciseId = 22),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 23, exerciseId = 23),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 24, exerciseId = 24),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 25, exerciseId = 25),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 26, exerciseId = 26),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 27, exerciseId = 27),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 28, exerciseId = 28),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 29, exerciseId = 29),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 30, exerciseId = 30),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 31, exerciseId = 31),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 32, exerciseId = 32),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 33, exerciseId = 33),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 34, exerciseId = 34),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 35, exerciseId = 35),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 36, exerciseId = 36),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 37, exerciseId = 37),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 38, exerciseId = 38),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 39, exerciseId = 39),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 40, exerciseId = 40),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 40, exerciseId = 40),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 41, exerciseId = 41),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 42, exerciseId = 42),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 43, exerciseId = 43),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 44, exerciseId = 44),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 45, exerciseId = 45),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 46, exerciseId = 46),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 47, exerciseId = 47),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 48, exerciseId = 48),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 49, exerciseId = 49),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 50, exerciseId = 50),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 51, exerciseId = 51),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 52, exerciseId = 52),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 53, exerciseId = 53),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 54, exerciseId = 54),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 55, exerciseId = 55),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 56, exerciseId = 56),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 57, exerciseId = 57),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 58, exerciseId = 58),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 59, exerciseId = 59),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 60, exerciseId = 60)
        )
    }
}