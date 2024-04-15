package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.koleff.kare_android.data.room.entity.DoWorkoutExerciseSet
import java.util.UUID

@Dao
interface DoWorkoutExerciseSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(doWorkoutExerciseSet: DoWorkoutExerciseSet): Long

    @Update
    suspend fun updateSet(doWorkoutExerciseSet: DoWorkoutExerciseSet)

    @Delete
    suspend fun deleteSet(doWorkoutExerciseSet: DoWorkoutExerciseSet)

    @Query("SELECT * FROM do_workout_exercise_set WHERE workoutDataId = :workoutDataId")
    suspend fun findSetsByWorkoutData(workoutDataId: Int): List<DoWorkoutExerciseSet>

    @Query("SELECT * FROM do_workout_exercise_set WHERE instanceId = :instanceId")
    suspend fun findSetById(instanceId: UUID): DoWorkoutExerciseSet?

    @Query("SELECT * FROM do_workout_exercise_set WHERE workoutId = :workoutId")
    suspend fun findSetsByWorkoutId(workoutId: Int): List<DoWorkoutExerciseSet>
}