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
    suspend fun insertAllExercises(exercises: List<Exercise>)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertExerciseDetailsExerciseCrossRef(crossRef: ExerciseDetailsExerciseCrossRef)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAllExerciseDetailsExerciseCrossRefs(crossRefs: List<ExerciseDetailsExerciseCrossRef>)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertExerciseSetCrossRef(crossRef: ExerciseSetCrossRef)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAllExerciseSetCrossRef(crossRefs: List<ExerciseSetCrossRef>)
//
//    @Delete
//    suspend fun deleteExerciseSetCrossRef(crossRef: ExerciseSetCrossRef)
//
//    @Delete
//    suspend fun deleteAllExerciseSetCrossRef(crossRefs: List<ExerciseSetCrossRef>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Transaction
    @Query("SELECT * FROM exercise_table WHERE exerciseId = :exerciseId AND workoutId = :workoutId")
    suspend fun getExercise(exerciseId: Int, workoutId: Int): Exercise?

    @Query("SELECT * FROM exercise_set_table WHERE exerciseId = :exerciseId AND workoutId = :workoutId")
    suspend fun getSetsForExercise(exerciseId: Int, workoutId: Int): List<ExerciseSet>

    suspend fun getExerciseWithSets(exerciseId: Int, workoutId: Int): ExerciseWithSets {
        val exercise = getExercise(exerciseId, workoutId)
            ?: throw NoSuchElementException("No exercise found with exerciseId $exerciseId and workoutId $workoutId")
        val sets = try {
            getSetsForExercise(exerciseId, workoutId)
        } catch (e: NoSuchElementException) {
            emptyList()
        }
        return ExerciseWithSets(exercise, sets)
    }

    @Transaction
    @Query("SELECT * FROM exercise_table WHERE workoutId = :workoutId AND muscleGroup = :muscleGroup ORDER BY exerciseId")
    fun getCatalogExercises(
        workoutId: Int,
        muscleGroup: MuscleGroup
    ): List<Exercise>

    @Transaction
    @Query("SELECT * FROM exercise_table ORDER BY exerciseId")
    fun getAllExercises(): List<Exercise>

    @Transaction
    @Query("SELECT * FROM exercise_table WHERE workoutId = :workoutId ORDER BY exerciseId")
    fun getAllCatalogExercises(workoutId: Int): List<Exercise>

    @Transaction
    @Query("SELECT * FROM exercise_table ORDER BY exerciseId ASC")
    fun getExercisesOrderedById(): List<Exercise>
}