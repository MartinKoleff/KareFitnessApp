package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(exercise: ExerciseDto)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(exercises: List<ExerciseDto>)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseDto)

    @Query("SELECT * FROM ExerciseDto WHERE muscleGroup = :muscleGroup ORDER BY exerciseId")
    fun getExercisesOrderedById(muscleGroup: MuscleGroup): List<ExerciseDto>


    @Query("SELECT * FROM ExerciseDto ORDER BY exerciseId ASC")
    fun getExercisesOrderedById(): List<ExerciseDto>

    @Query("SELECT * FROM ExerciseDto WHERE exerciseId = :exerciseId")
    fun getExerciseById(exerciseId: Int): ExerciseDto //TODO: wire with ExerciseDetailsDto

}