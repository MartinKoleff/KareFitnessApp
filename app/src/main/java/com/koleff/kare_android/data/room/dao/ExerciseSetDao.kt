package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.koleff.kare_android.data.room.entity.ExerciseSet
import java.util.UUID

@Dao
interface ExerciseSetDao {

    @Query("SELECT * FROM exercise_set_table WHERE setId = :setId")
    fun getSetById(setId: UUID): ExerciseSet

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSet(exerciseSet: ExerciseSet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExerciseSets(sets: List<ExerciseSet>)

    @Update
    suspend fun updateSet(exerciseSet: ExerciseSet)

    @Delete
    suspend fun deleteSet(exerciseSet: ExerciseSet)

    @Query("DELETE FROM exercise_set_table WHERE setId = :setId")
    suspend fun deleteSet(setId: UUID)
}