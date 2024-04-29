package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.koleff.kare_android.data.room.entity.WorkoutConfiguration

typealias WorkoutConfigurationId = Int

@Dao
interface WorkoutConfigurationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutConfiguration(config: WorkoutConfiguration): WorkoutConfigurationId

    @Query("SELECT * FROM workout_configuration_table WHERE workoutId = :workoutId")
    suspend fun getWorkoutConfiguration(workoutId: Int): WorkoutConfiguration?

    @Update
    suspend fun updateWorkoutConfiguration(config: WorkoutConfiguration)

    @Delete
    suspend fun deleteWorkoutConfiguration(config: WorkoutConfiguration)

    @Query("DELETE FROM workout_configuration_table WHERE id = :workoutConfigurationId")
    suspend fun deleteWorkoutConfiguration(workoutConfigurationId: Int)
}