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

    @Query("DELETE FROM workout_table WHERE workout_id = :workoutId")
    suspend fun deleteWorkout(workoutId: Int)

    @Update
    suspend fun updateWorkout(workout: WorkoutDto)

    @Query("UPDATE workout_table SET isSelected = 1 WHERE workout_id = :workoutId")
    suspend fun selectWorkoutById(workoutId: Int)

    @Query("SELECT * FROM workout_table ORDER BY workout_id")
    fun getWorkoutsOrderedById(): List<WorkoutDto>

    @Query("SELECT * FROM workout_table w JOIN exercise_table e ON exercise_id = workout_id WHERE workout_id = :workoutId")
    fun getWorkoutById(workoutId: Int): WorkoutDetailsDto

    @Query("SELECT * FROM workout_table WHERE isSelected = 1") //true = 1, false = 0
    fun getWorkoutByIsSelected(): WorkoutDto

    @Query("SELECT * FROM workout_table w JOIN exercise_table e ON exercise_id = workout_id WHERE workout_id = :workoutId") //true = 1, false = 0
    fun getWorkoutExercises(workoutId: Int): List<ExerciseDto>
}