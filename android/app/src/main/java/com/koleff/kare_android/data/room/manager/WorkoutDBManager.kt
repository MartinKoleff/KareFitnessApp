package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutDBManager @Inject constructor(
    private val preferences: Preferences
) {
    val hasInitializedWorkoutTableRoomDB = preferences.hasInitializedWorkoutTableRoomDB()

    suspend fun initializeWorkoutTableRoomDB(workoutDao: WorkoutDao) = withContext(Dispatchers.IO) {
        val workouts = getAllWorkouts()
        workoutDao.insertAll(workouts)

        preferences.initializeWorkoutTableRoomDB()
    }

    private fun getAllWorkouts(): List<WorkoutDto> {
        return listOf(
            WorkoutDto(
                workoutId = "Workout1",
                name = "Arnold chest workout",
                muscleGroup = MuscleGroup.CHEST,
                snapshot = "",
                totalExercises = 5,
                isSelected = false
            ),
            WorkoutDto(
                workoutId = "Workout2",
                name = "Chavdo destroy back workout",
                muscleGroup = MuscleGroup.BACK,
                snapshot = "",
                totalExercises = 4,
                isSelected = false
            ),
            WorkoutDto(
                workoutId = "Workout3",
                name = "Blow your arms workout",
                muscleGroup = MuscleGroup.ARMS,
                snapshot = "",
                totalExercises = 6,
                isSelected = true
            )
        )
    }
}