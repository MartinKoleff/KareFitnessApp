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
import com.koleff.kare_android.data.room.entity.SetEntity
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseWithSet
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef

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

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExerciseSetCrossRef(crossRef: ExerciseSetCrossRef)

    @Delete
    suspend fun deleteAllExerciseSetCrossRef(crossRefs: List<ExerciseSetCrossRef>)

    @Transaction
    @Query("SELECT * FROM exercise_table WHERE exerciseId = :exerciseId")
    fun getExerciseById(exerciseId: Int): ExerciseWithSet

    @Transaction
    @Query("SELECT * FROM exercise_table WHERE muscleGroup = :muscleGroup ORDER BY exerciseId")
    fun getExercisesOrderedById(muscleGroup: MuscleGroup): List<ExerciseWithSet>

    @Transaction
    @Query("SELECT * FROM exercise_table ORDER BY exerciseId")
    fun getAllExercises(): List<ExerciseWithSet>

    @Transaction
    @Query("SELECT * FROM exercise_table ORDER BY exerciseId ASC")
    fun getExercisesOrderedById(): List<ExerciseWithSet>
}