package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import com.koleff.kare_android.data.room.entity.ExerciseWithSets

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSetCrossRef(crossRef: ExerciseSetCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExerciseSetCrossRef(crossRefs: List<ExerciseSetCrossRef>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExerciseSetCrossRef(crossRef: ExerciseSetCrossRef)

    @Delete
    suspend fun deleteAllExerciseSetCrossRef(crossRefs: List<ExerciseSetCrossRef>)

    @Transaction
    @Query("SELECT * FROM exercise_table WHERE exerciseId = :exerciseId AND workoutId = :workoutId")
    suspend fun getExercise(exerciseId: Int, workoutId: Int): Exercise?

    @Query("SELECT * FROM exercise_set_table WHERE exerciseId = :exerciseId AND workoutId = :workoutId")
    suspend fun getSetsForExercise(exerciseId: Int, workoutId: Int): List<ExerciseSet>

    suspend fun getExerciseWithSets(exerciseId: Int, workoutId: Int): ExerciseWithSets {
        val exercise = getExercise(exerciseId, workoutId)
        val sets = if (exercise != null) getSetsForExercise(exerciseId, workoutId) else emptyList()
        return if (exercise != null) ExerciseWithSets(exercise, sets) else
            throw NoSuchElementException("No exercise found with exerciseId $exerciseId and workoutId $workoutId")
    }

    @Transaction
    @Query("SELECT * FROM exercise_table WHERE muscleGroup = :muscleGroup ORDER BY exerciseId")
    fun getExercisesOrderedById(muscleGroup: MuscleGroup): List<Exercise>

    @Transaction
    @Query("SELECT * FROM exercise_table ORDER BY exerciseId")
    fun getAllExercises(): List<Exercise>

    @Transaction
    @Query("SELECT * FROM exercise_table ORDER BY exerciseId ASC")
    fun getExercisesOrderedById(): List<Exercise>
}