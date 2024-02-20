package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.WorkoutGenerator
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsWithExercises
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutDBManager @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val workoutDetailsDao: WorkoutDetailsDao,
    private val hasInitializedDB: Boolean
) {

    suspend fun initializeWorkoutTableRoomDB(onDBInitialized: () -> Unit) =
        withContext(Dispatchers.IO) {
            if (hasInitializedDB) return@withContext

            val workoutList = WorkoutGenerator.getAllWorkouts()
            val workoutDetailsList = WorkoutGenerator.getAllWorkoutDetails()
            val workoutDetailsExerciseCrossRefs =
                WorkoutGenerator.getAllWorkoutDetailsExerciseCrossRefs()
            val workoutDetailsWorkoutCrossRefs =
                WorkoutGenerator.getAllWorkoutDetailsWorkoutCrossRefs()

            workoutDao.insertAll(workoutList)
            workoutDetailsDao.insertAllDetails(workoutDetailsList.map(WorkoutDetailsWithExercises::workoutDetails))

            workoutDetailsDao.insertAllWorkoutDetailsExerciseCrossRef(
                workoutDetailsExerciseCrossRefs
            )
            workoutDao.insertAllWorkoutDetailsWorkoutCrossRef(workoutDetailsWorkoutCrossRefs)

            onDBInitialized()
        }
}