package com.koleff.kare_android.workout.data

import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.room.dao.WorkoutConfigurationDao
import com.koleff.kare_android.data.room.dao.WorkoutConfigurationId
import com.koleff.kare_android.data.room.entity.WorkoutConfiguration
import com.koleff.kare_android.data.room.entity.WorkoutDetailsWithExercises

class WorkoutConfigurationDaoFake(
    private val workoutDetailsDao: WorkoutDetailsDaoFake
): WorkoutConfigurationDao {

    private val workoutConfigurationDB = mutableListOf<WorkoutConfiguration>()

    override suspend fun insertWorkoutConfiguration(configuration: WorkoutConfiguration): WorkoutConfigurationId {
        workoutConfigurationDB.add(configuration)

        //Update workoutDetailsDao DB...
        workoutDetailsDao.updateWorkoutConfiguration(configuration)

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

            //Update workoutDetailsDao DB...
            workoutDetailsDao.updateWorkoutConfiguration(configuration)
        } else {

            //Delete invalid workout configuration?
        }
    }

    override suspend fun deleteWorkoutConfiguration(configuration: WorkoutConfiguration) {
        workoutConfigurationDB.remove(configuration)
    }

    override suspend fun deleteWorkoutConfiguration(workoutConfigurationId: Int) {
        workoutConfigurationDB.removeAll {it.workoutId == workoutConfigurationId }
    }

    fun clearDB(){
        workoutConfigurationDB.clear()
    }

    fun getAllWorkoutConfigurations(): List<WorkoutConfiguration> {
        return workoutConfigurationDB
    }
}