package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.koleff.kare_android.data.room.entity.DoWorkoutData

@Dao
interface DoWorkoutDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutData(doWorkoutData: DoWorkoutData): Long

    @Update
    suspend fun updateWorkoutData(doWorkoutData: DoWorkoutData)

    @Delete
    suspend fun deleteWorkoutData(doWorkoutData: DoWorkoutData)

    @Query("SELECT * FROM do_workout_data")
    suspend fun getAllWorkoutData(): List<DoWorkoutData>

    @Query("SELECT * FROM do_workout_data WHERE id = :id")
    suspend fun getWorkoutDataById(id: Int): DoWorkoutData?

    @Query("SELECT * FROM do_workout_data WHERE workoutId = :workoutId ORDER BY timestamp DESC")
    suspend fun getWorkoutDataByWorkoutId(workoutId: Int): List<DoWorkoutData>
}
