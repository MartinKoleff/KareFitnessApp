package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseDetails
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef

@Dao
interface ExerciseDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseDetails(exercise: ExerciseDetails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<ExerciseDetails>)

    @Delete
    suspend fun deleteExerciseDetails(exercise: ExerciseDetails)

    @Transaction
    @Query("SELECT * FROM exercise_details_table WHERE exerciseDetailsId = :exerciseId")
    fun getExerciseDetailsById(exerciseId: Int): ExerciseDetails
}