package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsWorkoutCrossRef

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workouts: List<Workout>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWorkoutDetailsWorkoutCrossRef(crossRefs: List<WorkoutDetailsWorkoutCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutDetailsWorkoutCrossRef(crossRef: WorkoutDetailsWorkoutCrossRef)

    @Query("DELETE FROM workout_table WHERE workoutId = :workoutId")
    suspend fun deleteWorkout(workoutId: Int)

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Transaction
    @Query("UPDATE workout_table SET isSelected = 1 WHERE workoutId = :workoutId")
    suspend fun selectWorkoutById(workoutId: Int)

    @Transaction
    @Query("SELECT * FROM workout_table ORDER BY workoutId")
    fun getWorkoutsOrderedById(): List<Workout>

    @Transaction
    @Query("SELECT * FROM workout_table WHERE isSelected = 1") //true = 1, false = 0
    fun getWorkoutByIsSelected(): Workout

    @Transaction
    @Query("SELECT * FROM workout_table WHERE workoutId = :workoutId") //true = 1, false = 0
    fun getWorkoutById(workoutId: Int): Workout
}