package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsWithExercises

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workouts: List<Workout>)

    @Query("DELETE FROM workout_table WHERE workoutId = :workoutId")
    suspend fun deleteWorkout(workoutId: Int)

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Query("UPDATE workout_table SET isSelected = 1 WHERE workoutId = :workoutId")
    suspend fun selectWorkoutById(workoutId: Int)

    @Query("SELECT * FROM workout_table ORDER BY workoutId")
    fun getWorkoutsOrderedById(): List<Workout>

    @Query("SELECT * FROM workout_details_table w WHERE workoutDetailsId = :workoutId")
    fun getWorkoutById(workoutId: Int): WorkoutDetailsWithExercises

    @Query("SELECT * FROM workout_table WHERE isSelected = 1") //true = 1, false = 0
    fun getWorkoutByIsSelected(): Workout

//    @Query("SELECT * FROM workout_table w WHERE workoutId = :workoutId") //true = 1, false = 0
//    fun getWorkoutExercises(workoutId: Int): WorkoutWithExercises
}