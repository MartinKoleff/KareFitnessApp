package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.SaveWorkoutDto
import com.koleff.kare_android.data.model.dto.WorkoutDto

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWorkout(workout: SaveWorkoutDto)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(workouts: List<WorkoutDto>)

    @Delete
    @Query("DELETE FROM WorkoutDto WHERE workoutId = :workoutId")
    suspend fun deleteWorkout(workoutId: String)

    @Update
    suspend fun updateWorkout(workout: WorkoutDto)

    @Update
    @Query("UPDATE WorkoutDto SET isSelected = 1 WHERE workoutId = :workoutId")
    suspend fun selectWorkoutById(workoutId: String)

    @Query("SELECT * FROM WorkoutDto ORDER BY workoutId")
    fun getWorkoutsOrderedById(): List<WorkoutDto>

    @Query("SELECT * FROM WorkoutDto WHERE workoutId = :workoutId")
    fun getWorkoutById(workoutId: String): WorkoutDto //TODO: wire with WorkoutDetailsDto

    @Query("SELECT * FROM WorkoutDto WHERE isSelected = 1") //true = 1, false = 0
    fun getWorkoutByIsSelected(): WorkoutDto
}