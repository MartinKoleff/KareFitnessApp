package com.koleff.kare_android.workout.data

import com.koleff.kare_android.data.room.dao.WorkoutConfigurationDao
import com.koleff.kare_android.data.room.dao.WorkoutConfigurationId
import com.koleff.kare_android.data.room.entity.WorkoutConfiguration

class WorkoutConfigurationDaoFake: WorkoutConfigurationDao {
    override suspend fun insertWorkoutConfiguration(config: WorkoutConfiguration): WorkoutConfigurationId {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkoutConfiguration(workoutId: Int): WorkoutConfiguration? {
        TODO("Not yet implemented")
    }

    override suspend fun updateWorkoutConfiguration(config: WorkoutConfiguration) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWorkoutConfiguration(config: WorkoutConfiguration) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWorkoutConfiguration(workoutConfigurationId: Int) {
        TODO("Not yet implemented")
    }
}