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
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<Exercise>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseDetailsExerciseCrossRef(crossRef: ExerciseDetailsExerciseCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExerciseDetailsExerciseCrossRefs(crossRefs: List<ExerciseDetailsExerciseCrossRef>)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Transaction
    @Query("SELECT * FROM exercise_table WHERE exerciseId = :exerciseId")
    fun getExerciseById(exerciseId: Int): Exercise

    @Transaction
    @Query("SELECT * FROM exercise_table WHERE muscleGroup = :muscleGroup ORDER BY exerciseId")
    fun getExercisesOrderedById(muscleGroup: MuscleGroup): List<Exercise>

    @Transaction
    @Query("SELECT * FROM exercise_table ORDER BY exerciseId ASC")
    fun getExercisesOrderedById(): List<Exercise>
}