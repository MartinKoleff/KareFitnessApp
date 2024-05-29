package com.koleff.kare_android.workout.data

import com.koleff.kare_android.data.room.dao.WorkoutConfigurationDao
import com.koleff.kare_android.data.room.dao.WorkoutConfigurationId
import com.koleff.kare_android.data.room.entity.WorkoutConfiguration
import com.koleff.kare_android.utils.FakeDao

class WorkoutConfigurationDaoFake(
    private val workoutConfigurationChangeListener: WorkoutConfigurationChangeListener
): WorkoutConfigurationDao, FakeDao {

    private val workoutConfigurationDB = mutableListOf<WorkoutConfiguration>()

    override suspend fun insertWorkoutConfiguration(configuration: WorkoutConfiguration): WorkoutConfigurationId {
        workoutConfigurationDB.add(configuration)
        workoutConfigurationChangeListener.onWorkoutConfigurationUpdated(configuration)

        return workoutConfigurationDB.size.toLong()
    }

    override suspend fun getWorkoutConfiguration(workoutId: Int): WorkoutConfiguration? {
        return workoutConfigurationDB.firstOrNull { it.workoutId == workoutId }
    }

    override suspend fun updateWorkoutConfiguration(configuration: WorkoutConfiguration) {
        val index = workoutConfigurationDB.indexOfFirst { it.workoutId == configuration.workoutId }

        //WorkoutConfiguration found
        if (index != -1) {

            //Replace workout configuration
            workoutConfigurationDB[index] = configuration
            workoutConfigurationChangeListener.onWorkoutConfigurationUpdated(configuration)
        } else {

            //Delete invalid workout configuration?
        }
    }

    override suspend fun deleteWorkoutConfiguration(configuration: WorkoutConfiguration) {
        workoutConfigurationDB.remove(configuration)
        workoutConfigurationChangeListener.onWorkoutConfigurationDeleted(configuration.workoutId)
    }

    override suspend fun deleteWorkoutConfiguration(workoutId: Int) {
        workoutConfigurationDB.removeAll {it.workoutId == workoutId }
        workoutConfigurationChangeListener.onWorkoutConfigurationDeleted(workoutId)
    }

    fun getAllWorkoutConfigurations(): List<WorkoutConfiguration> {
        return workoutConfigurationDB
    }

    override fun clearDB(){
        workoutConfigurationDB.clear()
    }
}